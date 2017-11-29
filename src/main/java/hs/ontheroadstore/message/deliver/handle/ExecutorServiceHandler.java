package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/29.
 */
public interface ExecutorServiceHandler {
    void doWork(int mesageType,String message,JsonCacheHandler cacheHandler);
    void release();
}
