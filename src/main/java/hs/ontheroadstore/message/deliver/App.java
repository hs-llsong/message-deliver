package hs.ontheroadstore.message.deliver;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import hs.ontheroadstore.message.deliver.bean.OnsPropertyKeyConst;
import hs.ontheroadstore.message.deliver.handle.*;
import hs.ontheroadstore.message.deliver.listener.DeliverListener;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public class App {
    private HandleManager handleManager;
    private final static Logger logger = Logger.getLogger(App.class);
    public HandleManager getHandleManager() {
        return handleManager;
    }

    public void setHandleManager(HandleManager handleManager) {
        this.handleManager = handleManager;
    }
    public static void main( String[] args ){
        App app = new App();
        Properties prop = app.loadPropertis();
        if(prop == null) {
            System.out.println("Load properties failed.");
            System.exit(0);
        }
        String appid,appSecret;
        appid = prop.getProperty(AppPropertyKeyConst.WX_APPID);
        appSecret = prop.getProperty(AppPropertyKeyConst.WX_APP_SECRET);
        if(StringUtil.isNullOrEmpty(appid) || StringUtil.isNullOrEmpty(appSecret)) {
            System.out.println("Weixin appid or secret not set.");
            System.exit(0);
        }
        String consumeTopics;
        consumeTopics = prop.getProperty(AppPropertyKeyConst.CONSUME_TOPICS_KEY); //多个topic以,号隔开
        if (StringUtil.isNullOrEmpty(consumeTopics)) {
            System.out.println("ConsumeTopics is null");
            System.exit(0);
        }

        String[] topics = consumeTopics.split(",");
        List<String> channelLists = new ArrayList<>();
        for (String topic: topics
             ) {
            String propKey = topic.toUpperCase() + AppPropertyKeyConst.CHANNEL_NAME_SUFFIX;
            String channelName = prop.getProperty(propKey);
            if(StringUtil.isNullOrEmpty(channelName)) continue;
            channelLists.add(channelName);
        }

        String redis_host,redis_auth,redis_sport,redis_token_key;
        int redis_port = 6379;
        redis_host = prop.getProperty(AppPropertyKeyConst.REDIS_HOST_KEY);
        redis_auth = prop.getProperty(AppPropertyKeyConst.REDIS_AUTH);
        redis_sport = prop.getProperty(AppPropertyKeyConst.REDIS_PORT_KEY);
        redis_token_key = prop.getProperty(AppPropertyKeyConst.REDIS_TOKEN_KEY);

        if (StringUtil.isNullOrEmpty(redis_host)) {
            System.out.println("Redis host must be set.");
            System.exit(0);
        }
        if (!StringUtil.isNullOrEmpty(redis_sport)) {
            redis_port = Integer.valueOf(redis_sport);
        }
        int threadPoolSize = 5;
        String poolSize = prop.getProperty(AppPropertyKeyConst.EXECUTOR_SERVICE_POOL_SIZE);
        if(!StringUtil.isNullOrEmpty(poolSize)) {
            threadPoolSize = Integer.valueOf(poolSize);
        }
        JedisPoolHandler jedisPoolHandler = new JedisPoolHandler(redis_host,redis_port,redis_auth,3000);
        app.setHandleManager(new HandleManagerImpl());
        app.getHandleManager().registerJedisPoolHandler(jedisPoolHandler);
        app.getHandleManager().registerAliOnsProducerHandler(new AliOnsProducerHandlerImpl(prop));
        app.getHandleManager().registerWxTemplateMessageHandler(new WxTemplateMessageHandlerImpl());
        app.getHandleManager().registerWxMessageMakeupHandler(new WxMessageMakeupHandleImpl(prop));
        app.getHandleManager().registerExecutorServiceHandler(new ProducerExecutorServiceHandlerImpl(app.getHandleManager(),threadPoolSize));
        app.getHandleManager().registerAppMessagePushHandler(new AliAcsMessagePushHandlerImpl(prop));
        app.getHandleManager().registerNoDisturbHandle(new HsWeixinNoDisturbHandleImpl(prop));
        boolean wxAccessTokenReadonly = false;
        String isWxAccessTokenReadonly = prop.getProperty(AppPropertyKeyConst.WX_ACCESS_TOKEN_READONLY_KEY);
        if (!StringUtil.isNullOrEmpty(isWxAccessTokenReadonly)) {
            if(isWxAccessTokenReadonly.toUpperCase().equals("TRUE")) {
                wxAccessTokenReadonly = true;
            }
        }
        if (wxAccessTokenReadonly) {
            WxTokenHandler tokenHandler = new WxAccessTokenHandlerImpl(jedisPoolHandler,redis_token_key);
            app.getHandleManager().registerWxTokenHandler(tokenHandler);
        } else {
            WxTokenAutoRefreshHandler tokenHandler = new WxAccessTokenAutoRefreshHandlerImpl(appid,appSecret,jedisPoolHandler,redis_token_key);
            Thread t = new Thread(tokenHandler);
            t.start();
            logger.info("WxTokenAutoRefreshHandler service started");
            app.getHandleManager().registerWxTokenHandler(tokenHandler);
        }

        boolean loadingRedisListener = true;
        String isRedisCacheListening = prop.getProperty(AppPropertyKeyConst.REDIS_CACHE_LISTEN_KEY);
        if (!StringUtil.isNullOrEmpty(isRedisCacheListening)) {
            //default loading Redis pool
            if(isRedisCacheListening.toUpperCase().equals("FALSE")){
                loadingRedisListener = false;
            }
        }
        String heishiRedisKey = prop.getProperty(AppPropertyKeyConst.REDIS_HEISHI_MESSAGE_CACHE_KEY);
        String heishiRedisProcuderTopic = prop.getProperty(AppPropertyKeyConst.REDIS_HEISHI_MESSAGE_PRODUCER_TOPIC_KEY);
        if (StringUtil.isNullOrEmpty(heishiRedisKey)) {
            logger.info("Listener redis key is null. ");
            loadingRedisListener = false;
        }
        if (StringUtil.isNullOrEmpty(heishiRedisProcuderTopic)) {
            logger.info("Listener redis cache producer topic is null.");
            loadingRedisListener = false;
        }
        if (loadingRedisListener) {
            RedisLooper redisHeishiLooper = new RedisLooper(app.getHandleManager(),heishiRedisKey,heishiRedisProcuderTopic);
            Thread redisThread = new Thread(redisHeishiLooper);
            redisThread.start();
            logger.info("Heishi redis looper started. producer topic:" + heishiRedisProcuderTopic + ",cache key:" + heishiRedisKey);
        } else {
            logger.info("Listener redis is disabled.");
        }

        for (String channelName:channelLists) {
            Consumer Consumer = app.createConsumer(prop,channelName);
            if (Consumer == null) {
                logger.error("Consumer " + channelName + " create failed.");
            }
            Consumer.start();
            logger.info( channelName + " consumer service started.");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("System exit.");
            }
        });

    }

    private Consumer createConsumer(final Properties properties,String channelName) {
        Properties prop = this.getChannelProperties(properties,channelName);
        if(prop.isEmpty()) {
            logger.error("Can't load properites for "+channelName);
            return null;
        }
        Consumer consumer;
        try {
            consumer = ONSFactory.createConsumer(prop);
            String topic = prop.getProperty(OnsPropertyKeyConst.Topic);
            if (StringUtil.isNullOrEmpty(topic)) {
                logger.error("create consumer failed.because topic is null.");
                consumer.shutdown();
                return null;
            }
            String subExpression = prop.getProperty(OnsPropertyKeyConst.ConsumerTag);
            if(StringUtil.isNullOrEmpty(subExpression)) {
                subExpression = "*";
            }
            consumer.subscribe(topic, subExpression, new DeliverListener(this,topic));
        } catch (ONSClientException e) {
            logger.error(e.getMessage());
            return null;
        }
        return consumer;
    }

    private Properties getChannelProperties(Properties propAll,String channelName) {
        Properties prop = new Properties();
        Field[] fields = OnsPropertyKeyConst.class.getFields();
        for (Field field:fields) {
            String key = channelName + field.getName();
            if (propAll.containsKey(key)) {
                prop.put(field.getName(), propAll.getProperty(key));
            }
        }
        return prop;
    }

    public Properties loadPropertis() {
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            return null;
        }
        return prop;
    }
}
