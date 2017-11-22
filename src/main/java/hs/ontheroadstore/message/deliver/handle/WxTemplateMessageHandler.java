package hs.ontheroadstore.message.deliver.handle;

import hs.ontheroadstore.message.deliver.bean.WxTemplateMessageResponse;
import hs.ontheroadstore.message.deliver.bean.WxTemplateMessage;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public interface WxTemplateMessageHandler {
    WxTemplateMessageResponse send(WxTemplateMessage message, String accessToken);
}
