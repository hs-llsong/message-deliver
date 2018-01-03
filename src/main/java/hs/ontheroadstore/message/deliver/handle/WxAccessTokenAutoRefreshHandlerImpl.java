package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import hs.ontheroadstore.message.deliver.tools.HsHttpClient;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/22.
 */
public class WxAccessTokenAutoRefreshHandlerImpl implements WxTokenAutoRefreshHandler{
    static final String WEIXIN_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    private String appId;
    private String appSecret;
    private WxAccessToken wxAccessToken;
    private final Logger logger = Logger.getLogger(WxAccessTokenAutoRefreshHandlerImpl.class);
    private HsHttpClient hsHttpClient;
    private boolean refreshTokenImmediately = false;
    private int lastRefreshTime = 0;
    private final int refreshFrozeTime = 5*60;  // 5 minutes
    private volatile boolean stop = false;
    private JedisPoolHandler jedisPoolHandler;
    private String redisTokenKey;
    public WxAccessTokenAutoRefreshHandlerImpl(String appId, String appSecret, JedisPoolHandler jedisPoolHandler, String redisTokenKey) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.jedisPoolHandler = jedisPoolHandler;
        this.redisTokenKey = redisTokenKey;
    }

    @Override
    public void terminate() {
        stop = true;
        refreshTokenImmediately = true;
    }

    @Override
    public String getAccessToken() {
        if(wxAccessToken == null) return null;
        return wxAccessToken.getAccessToken();
    }

    @Override
    public void refreshToken() {
        int curTime = (int)(System.currentTimeMillis()/1000);
        if(this.lastRefreshTime>0) {
            if(curTime-this.lastRefreshTime<this.refreshFrozeTime) return;
        }
        refreshTokenImmediately = true;
    }

    @Override
    public void run() {
        while (!stop) {
            if(wxAccessToken != null ) {
                while (wxAccessToken.getExpiresIn()>30 && !refreshTokenImmediately) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error("Sleep exception occured.");
                        Thread.yield();
                        continue;
                    }
                    wxAccessToken.decExpires();
                }
                if(stop) continue;
            }
            this.lastRefreshTime = (int)(System.currentTimeMillis()/1000);
            wxAccessToken = refreshAccessToken();
            if(refreshTokenImmediately) refreshTokenImmediately=false;
            if (wxAccessToken == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("Sleep exception occured.");
                    Thread.yield();
                    continue;
                }
            }
            Jedis jedis = jedisPoolHandler.getResource();
            if (jedis!=null) {
                jedis.setex(redisTokenKey,wxAccessToken.expiresIn,wxAccessToken.accessToken);
                jedis.close();
                logger.info("Update accessToken in redis done!");
            }
        }
    }
    private WxAccessToken refreshAccessToken() {
        if(StringUtil.isNullOrEmpty(appId)||StringUtil.isNullOrEmpty(appSecret)) {
            logger.error("AppId or AppSecret is null");
            return null;
        }
        Map<String,String> params = new HashMap<>();
        params.put("appid",appId);
        params.put("secret",appSecret);
        if(hsHttpClient == null) {
            hsHttpClient = new HsHttpClient();
        }
        String responseBody;
        try {
            responseBody = hsHttpClient.Get(WxAccessTokenAutoRefreshHandlerImpl.WEIXIN_TOKEN_URL,params);
        } catch (IOException e) {
            logger.error("refreshAccessToken exception:" + e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("refreshAccessToken exception:" + e.getMessage());
            return null;
        }
        if(StringUtil.isNullOrEmpty(responseBody)) {
            logger.error("refreshAccessToken response null");
            return null;
        }
        Gson gson = new GsonBuilder().create();
        try {
            WxAccessToken acToken = gson.fromJson(responseBody,WxAccessToken.class);
            if(StringUtil.isNullOrEmpty(acToken.getAccessToken())) {
                logger.error("refreshAccessToken getAccessToken is null.response body:" + responseBody);
                return null;
            }
            logger.info("refresh access token success!");
            return acToken;

        } catch (JsonSyntaxException e) {
            logger.error("refreshAccessToken fromJson exception:" + e.getMessage());
            return null;
        }

    }

    class WxAccessToken implements Serializable {
        @SerializedName("access_token") private String accessToken;
        @SerializedName("expires_in") private int expiresIn = -1;
        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public void decExpires(){ this.expiresIn--;}

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }
    }
}
