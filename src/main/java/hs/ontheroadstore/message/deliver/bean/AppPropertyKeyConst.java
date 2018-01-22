package hs.ontheroadstore.message.deliver.bean;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/23.
 */
public class AppPropertyKeyConst {
    public static final String WX_APPID = "WeixinAppId";
    public static final String WX_APP_SECRET = "WeixinAppSecret";
    public static final String COLOR_KEY = "COLOR_";
    public static final String TEMPLATEID_KEY = "TEMPLATE_ID_";
    public static final String JUMPURL_KEY = "JUMP_URL_";
    public final static String REDIS_HOST_KEY = "RedisHost";
    public final static String REDIS_PORT_KEY = "RedisPort";
    public final static String REDIS_AUTH = "RedisAuth";
    public final static String WX_ACCESS_TOKEN_READONLY_KEY = "WxAccessTokenReadOnly";
    public final static String REDIS_TOKEN_KEY = "RedisTokenKey";
    public final static String REDIS_TICKET_KEY = "RedisTicketKey";
    public final static String REDIS_CACHE_LISTEN_KEY = "RedisCacheListening";
    public final static String REDIS_HEISHI_MESSAGE_CACHE_KEY = "RedisHsMessageCacheKey";
    public final static String REDIS_HEISHI_MESSAGE_PRODUCER_TOPIC_KEY = "RedisHsMessageProducerTopic";
    public final static String EXECUTOR_SERVICE_POOL_SIZE = "ExecutorServicePoolSize";
    public final static String CONSUME_TOPICS_KEY = "ConsumeTopics";
    public final static int MESSAGE_TYPE_HEISHI_ALIONS_PUSH = 1;
    public final static String CHANNEL_NAME_SUFFIX = "_CHANNEL_NAME";
    public final static String DEFAULT_ALIONS_TAG = "heishi";
    public final static String MSG_TAG_WEIXIN_TEMPLATE = "heishi";
    public final static String MSG_TAG_IOS = "iosapp";
    public final static String MSG_TAG_android = "androidapp";
    public final static String MSG_TAG_WX_APP = "weixinapp"; //微信小程序
    public final static String MSG_TAG_NEW_WEIXIN = "newwx";
    public final static String AMS_REGION_KEY = "AMSRegion";
    public final static String AMS_APPKEY_KEY =  "AMSAppkey";
    public final static String AMS_APNSENV_KEY = "AMSApnsEnv";
    public final static String STYLE_UPDATE = "UPDATE";
    public final static int DEVICE_IOS = 1;
    public final static int DEVICE_ANDROID = 2;
    public final static int PUSH_TYPE_NOTICE = 1;
    public final static int PUSH_TYPE_MESSAGE = 2;
    public final static String DB_HOST_KEY = "DbHost";
    public final static String DB_NAME_KEY = "DbName";
    public final static String DB_USER_KEY = "DbUser";
    public final static String DB_PASSWORD_KEY = "DbPassword";

}
