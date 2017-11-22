package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hs.ontheroadstore.message.deliver.bean.WxTemplateMessage;
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

    @Override
    public WxTemplateMessageResponse send(WxTemplateMessage message, String accessToken) {
        HsHttpClient hsHttpClient = new HsHttpClient();
        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(message);
        String responseBody;
        try {
            responseBody = hsHttpClient.postJson(WEIXIN_TEMPLATE_MESSAGE_URL + accessToken,jsonBody);
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
        return gson.fromJson(responseBody,WxTemplateMessageResponse.class);
    }
}
