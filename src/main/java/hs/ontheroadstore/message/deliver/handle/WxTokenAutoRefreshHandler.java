package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/6.
 */
public interface WxTokenAutoRefreshHandler extends Runnable,WxTokenHandler{
    void refreshToken();
    void terminate();
}
