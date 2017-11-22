package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public interface HandleManager {
    WxTemplateMessageHandler getWxTemplateMessageHandler();
    void registerWxTemplateMessageHandler(WxTemplateMessageHandler handler);
    WxTokenHandler getWxTokenHandler();
    void registerWxTokenHandler(WxTokenHandler handler);
}
