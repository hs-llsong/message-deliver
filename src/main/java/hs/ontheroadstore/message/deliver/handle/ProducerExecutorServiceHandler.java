package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/29.
 */
public interface ProducerExecutorServiceHandler {
    void doWork(String topic,String message,JsonCacheHandler cacheHandler);
    void release();
}
