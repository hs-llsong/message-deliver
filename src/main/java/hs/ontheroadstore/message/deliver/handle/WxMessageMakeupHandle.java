package hs.ontheroadstore.message.deliver.handle;

import hs.ontheroadstore.message.deliver.bean.WeixinMessageTemplate;
import hs.ontheroadstore.message.deliver.bean.WxTemplateMessage;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/23.
 */
public interface WxMessageMakeupHandle {
    WeixinMessageTemplate disguise(WxTemplateMessage message);
}
