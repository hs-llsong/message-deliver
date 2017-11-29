package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/29.
 */
public class RedisLooper implements Runnable{
    private JsonCacheHandler cacheHandler;
    private HandleManager handleManager;
    private boolean stop = false;
    private int messageType;
    public RedisLooper(final HandleManager handleManager,String cacheKey,int messageType) {
        this.handleManager = handleManager;
        this.cacheHandler  = new RedisCacheHandlerImpl(handleManager.getJedisPoolHandler(),cacheKey);
        this.messageType = messageType;
    }

    public void terminate() {
        stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            String message;
            message = cacheHandler.poll();
            if (StringUtil.isNullOrEmpty(message)) {
                try{
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.yield();
                }
                continue;
            }
            handleManager.getExecutorServiceHandler().doWork(messageType,message,cacheHandler);
            Thread.yield();
        }
    }
}
