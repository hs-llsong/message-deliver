package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public interface HandleManager {
    WxTemplateMessageHandler getWxTemplateMessageHandler();
    WxTokenHandler getWxTokenHandler();
    WxMessageMakeupHandle getWxMessageMakeupHandler();
    JedisPoolHandler getJedisPoolHandler();
    //JsonCacheHandler getJsonCacheHandler();
    AliOnsProducerHandler getAliOnsProducerHandler();
    ProducerExecutorServiceHandler getExecutorServiceHandler();
    AppMessagePushHandler getAppMessagePushHandler();
    NoDisturbHandle getNoDisturbHandler();
    void registerWxTemplateMessageHandler(WxTemplateMessageHandler handler);
    void registerWxTokenHandler(WxTokenHandler handler);
    void registerWxMessageMakeupHandler(WxMessageMakeupHandle handle);
    void registerJedisPoolHandler(JedisPoolHandler handler);
    //void registerJsonCacheHandler(JsonCacheHandler handler);
    void registerAliOnsProducerHandler(AliOnsProducerHandler handler);
    void registerExecutorServiceHandler(ProducerExecutorServiceHandler handler);
    void registerAppMessagePushHandler(AppMessagePushHandler handler);
    void registerNoDisturbHandle(NoDisturbHandle handle);
}
