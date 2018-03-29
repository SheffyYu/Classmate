package http;

/**
 * Created by Sheffy on 2018/3/27.
 */

public interface HttpCallback {
    // 当成功调用
    void onSuccess(Object data);

    void onFailure(String message);
}
