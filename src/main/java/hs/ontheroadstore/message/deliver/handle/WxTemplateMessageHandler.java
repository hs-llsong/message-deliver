package hs.ontheroadstore.message.deliver.handle;

import hs.ontheroadstore.message.deliver.bean.WeixinMessageTemplate;
import hs.ontheroadstore.message.deliver.bean.WxTemplateMessageResponse;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public interface WxTemplateMessageHandler {
    WxTemplateMessageResponse send(WeixinMessageTemplate message, String accessToken);
    WxTemplateMessageResponse send(String message,String accessToken);
}
