package http;

import android.support.test.espresso.core.deps.guava.reflect.TypeToken;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import bean.BookBean;
import constant.ServerUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sheffy on 2018/4/7.
 */

public class BookHttpUtils {
    // 做网络请求使用的对象
    private static final OkHttpClient client = new OkHttpClient();

    //获取同学录列表
    public void getAllBookByUserId(String userId,final HttpCallback callback){
        Type t = new TypeToken<List<BookBean>>(){}.getType();
        getData(ServerUrl.GET_BOOK_BY_USERID + userId,callback,t);
    }


    public void getData(String url,final HttpCallback callback,final Type type){
        // 创建请求对象
        final Request request = new Request.Builder()
                .get()
                .tag(this)
                .url(url)
                .build();

        //开启线程.执行网络操作
        new Thread(){
            public void run() {
                Response response = null;
                try {
                    // 执行网络操作
                    response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        // 获取响应文本
                        String json = response.body().string();

                        Log.i("OkHttp","打印GET响应的数据：" + json);

                        //json --> java对象
                        Gson gson = new Gson();
                        Object data = gson.fromJson(json,type);

                        // json 处理工具
                        callback.onSuccess(data);
                    } else {
                        String message ="Unexpected code " + response;

                        callback.onFailure(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

}
