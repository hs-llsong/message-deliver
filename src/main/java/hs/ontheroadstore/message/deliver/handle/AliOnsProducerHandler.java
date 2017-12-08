package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/27.
 */
public interface AliOnsProducerHandler {
    boolean send(String message,String topic,String tag,String key);
}
