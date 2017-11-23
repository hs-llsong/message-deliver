package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import hs.ontheroadstore.message.deliver.tools.HsHttpClient;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/22.
 */
public class WxAccessTokenHandlerImpl implements WxTokenHandler{
    static final String WEIXIN_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    private String appId;
    private String appSecret;
    private WxAccessToken wxAccessToken;
    private final Logger logger = Logger.getLogger(WxAccessTokenHandlerImpl.class);
    private HsHttpClient hsHttpClient;
    private boolean refreshTokenImmediately = false;
    private volatile boolean stop = false;


    public WxAccessTokenHandlerImpl(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
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
        refreshTokenImmediately = true;
    }

    @Override
    public void run() {
        while (!stop) {
            if(wxAccessToken != null ) {
                while (wxAccessToken.expiresIn>30 && !refreshTokenImmediately) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error("Sleep exception occured.");
                        Thread.yield();
                        continue;
                    }
                    wxAccessToken.expiresIn--;
                }
                if(stop) continue;
            }
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
            responseBody = hsHttpClient.Get(WxAccessTokenHandlerImpl.WEIXIN_TOKEN_URL,params);
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
                logger.error("refreshAccessToken getAccessToken is null");
                return null;
            }
            return acToken;

        } catch (JsonSyntaxException e) {
            logger.error("refreshAccessToken fromJson exception:" + e.getMessage());
            return null;
        }

    }

    class WxAccessToken implements Serializable {
        private String accessToken;
        private int expiresIn = -1;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }
    }
}
