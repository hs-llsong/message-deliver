package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/22.
 */
public interface WxTokenHandler extends Runnable{
    String getAccessToken();
    void refreshToken();
    void terminate();
}
