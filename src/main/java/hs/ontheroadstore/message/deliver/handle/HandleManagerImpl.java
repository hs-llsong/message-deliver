package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public class HandleManagerImpl implements HandleManager {
    private WxTemplateMessageHandler wxTemplateMessageHandler;
    @Override
    public WxTemplateMessageHandler getWxTemplateMessageHandler() {
        return this.wxTemplateMessageHandler;
    }

    @Override
    public void registerWxTemplateMessageHandler(WxTemplateMessageHandler handler) {
        this.wxTemplateMessageHandler = handler;
    }

    @Override
    public WxTokenHandler getWxTokenHandler() {
        return null;
    }

    @Override
    public void registerWxTokenHandler(WxTokenHandler handler) {

    }
}
