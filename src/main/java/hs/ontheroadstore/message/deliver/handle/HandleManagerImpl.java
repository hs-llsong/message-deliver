package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public class HandleManagerImpl implements HandleManager {
    private WxTemplateMessageHandler wxTemplateMessageHandler;
    private WxTokenHandler wxTokenHandler;
    private WxMessageMakeupHandle wxMessageMakeupHandle;
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
        return this.wxTokenHandler;
    }

    @Override
    public void registerWxTokenHandler(WxTokenHandler handler) {
        this.wxTokenHandler = handler;
    }

    @Override
    public void registerWxMessageMakeupHandler(WxMessageMakeupHandle handle) {
        this.wxMessageMakeupHandle = handle;
    }

    @Override
    public WxMessageMakeupHandle getWxMessageMakeupHandler() {
        return this.wxMessageMakeupHandle;
    }
}
