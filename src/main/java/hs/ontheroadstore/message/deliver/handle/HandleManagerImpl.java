package hs.ontheroadstore.message.deliver.handle;

import hs.ontheroadstore.message.deliver.App;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public class HandleManagerImpl implements HandleManager {
    private WxTemplateMessageHandler wxTemplateMessageHandler;
    private WxTokenHandler wxTokenHandler;
    private WxMessageMakeupHandle wxMessageMakeupHandle;
    private JsonCacheHandler jsonCacheHandler;
    private JedisPoolHandler jedisPoolHandler;
    private AliOnsProducerHandler aliOnsProducerHandler;
    private ProducerExecutorServiceHandler executorServiceHandler;
    private AppMessagePushHandler appMessagePushHandler;
    private NoDisturbHandle noDisturbHandle;
    @Override
    public AliOnsProducerHandler getAliOnsProducerHandler() {
        return this.aliOnsProducerHandler;
    }

    @Override
    public void registerAliOnsProducerHandler(AliOnsProducerHandler handler) {
        this.aliOnsProducerHandler = handler;
    }

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

    @Override
    public JedisPoolHandler getJedisPoolHandler() {
        return this.jedisPoolHandler;
    }

    @Override
    public void registerJedisPoolHandler(JedisPoolHandler handler) {
        this.jedisPoolHandler = handler;
    }


    @Override
    public ProducerExecutorServiceHandler getExecutorServiceHandler() {
        return executorServiceHandler;
    }

    @Override
    public void registerExecutorServiceHandler(ProducerExecutorServiceHandler handler) {
        this.executorServiceHandler = handler;
    }

    @Override
    public AppMessagePushHandler getAppMessagePushHandler() {
        return appMessagePushHandler;
    }

    @Override
    public void registerAppMessagePushHandler(AppMessagePushHandler appMessagePushHandler) {
        this.appMessagePushHandler = appMessagePushHandler;
    }

    @Override
    public NoDisturbHandle getNoDisturbHandler() {
        return this.noDisturbHandle;
    }

    @Override
    public void registerNoDisturbHandle(NoDisturbHandle handle) {
        this.noDisturbHandle = handle;
    }
}
