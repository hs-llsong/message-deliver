package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import hs.ontheroadstore.message.deliver.bean.WeixinMessageTemplate;
import hs.ontheroadstore.message.deliver.bean.WxTemplateMessageResponse;
import hs.ontheroadstore.message.deliver.tools.HsHttpClient;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public class WxTemplateMessageHandlerImpl implements WxTemplateMessageHandler {
    static final String WEIXIN_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    private final Logger logger = Logger.getLogger(WxTemplateMessageHandlerImpl.class);
    private HsHttpClient hsHttpClient = new HsHttpClient();
    @Override
    public WxTemplateMessageResponse send(WeixinMessageTemplate message, String accessToken) {
        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(message);
        return send(jsonBody,accessToken);
    }

    @Override
    public WxTemplateMessageResponse send(String message, String accessToken) {
        logger.debug("Request json body:" + message);
        String responseBody;
        try {
            responseBody = hsHttpClient.postJson(WEIXIN_TEMPLATE_MESSAGE_URL + accessToken,message);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        if (StringUtil.isNullOrEmpty(responseBody)) {
            logger.error("Weixin template send response null");
            return null;
        }
        logger.debug("Response body:" + responseBody);
        WxTemplateMessageResponse response;
        try {
            response = new Gson().fromJson(responseBody,WxTemplateMessageResponse.class);
        } catch (JsonSyntaxException e) {
            logger.error(e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        return response;
    }
}
