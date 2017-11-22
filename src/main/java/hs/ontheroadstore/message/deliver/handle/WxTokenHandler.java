package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/22.
 */
public interface WxTokenHandler {
    String getAccessToken();
    void refreshToken();
}
