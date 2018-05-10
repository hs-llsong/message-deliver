package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import org.apache.log4j.Logger;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/29.
 */
public class RedisLooper implements Runnable{
    private JsonCacheHandler cacheHandler;
    private HandleManager handleManager;
    private boolean stop = false;
    private String topic;
    private final Logger logger = Logger.getLogger(RedisLooper.class);
    public RedisLooper(final HandleManager handleManager,String cacheKey,String topic) {
        this.handleManager = handleManager;
        this.cacheHandler  = new RedisCacheHandlerImpl(handleManager.getJedisPoolHandler(),cacheKey);
        this.topic = topic;
    }

    public void terminate() {
        stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            String message;
            message = cacheHandler.poll();
            logger.debug("received message: " + message);
            if (StringUtil.isNullOrEmpty(message)) {
                try{
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.yield();
                }
                continue;
            }
            handleManager.getExecutorServiceHandler().doWork(topic,message,cacheHandler);
            Thread.yield();
        }
    }
}
