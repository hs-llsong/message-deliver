package hs.ontheroadstore.message.deliver.handle;

import hs.ontheroadstore.message.deliver.bean.WxTemplateMessage;
import hs.ontheroadstore.message.deliver.bean.WxTemplateMessageResponse;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public class WxTemplateMessageHandlerImpl implements WxTemplateMessageHandler {
    static final String WEIXIN_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    @Override
    public WxTemplateMessageResponse send(WxTemplateMessage message, String accessToken) {
        return null;
    }
}
