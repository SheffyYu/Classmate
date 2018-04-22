package com.example.sheffy.classmate;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import application.MyApplication;
import bean.UserBean;
import circleimageview.CircleImageView;
import circleimageview.ImageUtils;
import constant.ServerUrl;
import http.HttpCallback;
import http.HttpUtils;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View view;
    private CircleImageView civ_favicon_my;
    private TextView txv_user_name_my,txv_change_favicon,txv_change_password,
                     txv_feedback,txv_about,txv_version;
    private MyApplication myApp;
    private String userName;
    private UserBean userBean,postUserBean;
    private  Uri uritempFile;
    private String faviconPath;

    //修改头像功能的数据
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;


    //数据操作，数据初始化
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取用户名
        myApp=(MyApplication)getActivity().getApplication();
        userName=myApp.getUserName();
        faviconPath=myApp.getFaviconPath();
    }

    //ui操作
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_my, container, false);

        //初始化组件
        initView();
        //设置用户名
        txv_user_name_my.setText(userName);
        //设置头像
        if(faviconPath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(faviconPath);
            Log.i("MyFragment","setImageToView:"+bitmap);
            civ_favicon_my.setImageBitmap(bitmap);
        }

        return view;
    }

    //点击操作
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View viewDialog=getLayoutInflater(savedInstanceState).inflate(R.layout.layout_dialog_edit,null);

        //修改头像
        txv_change_favicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog(v);
            }
        });

        //修改密码
        txv_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("修改密码")//设置对话框的标题
                        .setView(viewDialog)
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //获取edit内容
                                EditText et_change_password=(EditText)viewDialog.findViewById(R.id.et_change_password);
                                EditText et_password_again=(EditText)viewDialog.findViewById(R.id.et_change_password_again);

                                //修改密码
                                changePassword(et_change_password.getText().toString(),et_password_again.getText().toString());
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        //反馈意见
        txv_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"click",Toast.LENGTH_SHORT).show();
            }
        });

        //关于我们
        txv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("关于我们")//设置对话框的标题
                        .setMessage("学院：计算机科学与技术学院\n班级：软件zy1402\n指导老师：库少平\n学生：余诗慧")//设置对话框的内容
                        //设置对话框的按钮
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
    }

    //初始化组件
    protected void initView(){
        civ_favicon_my=(CircleImageView)view.findViewById(R.id.user_favicon_my);
        txv_user_name_my=(TextView)view.findViewById(R.id.txv_user_name_my);
        txv_change_favicon=(TextView)view.findViewById(R.id.txv_change_favicon);
        txv_change_password=(TextView)view.findViewById(R.id.txv_change_password);
        txv_about=(TextView)view.findViewById(R.id.txv_about);
        txv_feedback=(TextView)view.findViewById(R.id.txv_feedback);
        txv_version=(TextView)view.findViewById(R.id.txv_version);
    }

    //修改密码
    protected void changePassword(String password,String password2){
        //判断两个密码是否相同
        if(!password.equals(password2)){
            Toast.makeText(getActivity(),"密码不相同",Toast.LENGTH_SHORT).show();
        }
        else {
            //联网修改密码
            userBean=new UserBean();
            userBean.setUserId(userName);
            userBean.setPassword(password);

            updatePassword();
        }
    }

    //联网修改密码
    protected void updatePassword(){
        //转换成 Json 文本
        Gson gson = new Gson();
        String json =  gson.toJson(userBean);
        Log.i("json", "updatePassword: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(ServerUrl.UPDATE_PASSWORD,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                        //跳转到目录
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"提交失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示修改头像的对话框
     */
    public void showChoosePicDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        takePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }

    //***********************************************************************************************
    //照相机功能存在问题，照片可以保存到存储路径里，但却无法跳转到剪切部分，直接回到activity，显示操作失败
    //主要是小米手机，其他手机暂时还未发现问题
    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(getContext(), permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment
                .getExternalStorageDirectory(), "image.jpg");
        Log.e("takePicture: ", file.getPath() );
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(getActivity(), "com.example.sheffy.classmate.fileProvider", file);
            tempUri = FileProvider.getUriForFile(getActivity(), "com.example.sheffy.classmate.fileProvider", file);
            Log.e("takePicture: ", tempUri.getPath() );
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }
//**************************************************************************************************

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        Log.i("onActivityResult: ",uritempFile.getPath());
                        Bitmap bitmap = BitmapFactory.decodeFile(uritempFile.getPath());
                        Log.i("MyFragment","setImageToView:"+bitmap);
                        bitmap = ImageUtils.toRoundBitmap(bitmap); // 这个时候的图片已经被处理成圆形的了
                        civ_favicon_my.setImageBitmap(bitmap);
                        myApp.setFaviconPath(uritempFile.getPath());
                        uploadPic(bitmap);
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 60);
        intent.putExtra("outputY", 60);
        uritempFile = Uri.parse("file://" + "/" + myApp.getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }


    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String imagePath = ImageUtils.savePhoto(bitmap, myApp.getPath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // imagePath上传
            Log.d("MyFragment","imagePath:"+imagePath);
            postUserBean=new UserBean();
            postUserBean.setUserId(userName);
            postUserBean.setFavicon(imagePath);

            //转换成 Json 文本
            Gson gson = new Gson();
            String json =  gson.toJson(postUserBean);
            Log.i("json", "postUser: "+json);

            // 提交 json 文本到服务器
            new HttpUtils().postData(ServerUrl.UPDATE_FAVICON,json,new HttpCallback(){
                @Override
                public void onSuccess(Object data) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"上传头像成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(String message) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"注册失败,网络错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(getActivity(), "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }


    //********************************以下为自动生成函数*************************************************
    public MyFragment() {
        // Required empty public constructor
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
