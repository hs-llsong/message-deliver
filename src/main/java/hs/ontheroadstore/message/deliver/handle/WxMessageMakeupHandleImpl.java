package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import hs.ontheroadstore.message.deliver.bean.WeixinMessageTemplate;
import hs.ontheroadstore.message.deliver.bean.WxMessageStyle;
import hs.ontheroadstore.message.deliver.bean.WxTemplateMessage;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/23.
 */
public class WxMessageMakeupHandleImpl implements WxMessageMakeupHandle{
    private Map<String,WxMessageStyle> messageStyleMap = new HashMap<>();
    private Properties prop;
    private final Logger logger = Logger.getLogger(WxMessageMakeupHandleImpl.class);
    public WxMessageMakeupHandleImpl(final Properties prop) {
        this.prop = prop;
    }

    private WxMessageStyle getMessageStyle(String styleName) {
        if(!this.messageStyleMap.isEmpty()) {
            if(this.messageStyleMap.containsKey(styleName)) return messageStyleMap.get(styleName);
        }
        if(this.prop.isEmpty()) {
            return null;
        }
        String upcaseStyleName = styleName.toUpperCase();
        String color = this.prop.getProperty(AppPropertyKeyConst.COLOR_KEY + upcaseStyleName);
        String templateId = this.prop.getProperty(AppPropertyKeyConst.TEMPLATEID_KEY + upcaseStyleName);
        String jumpUrl = this.prop.getProperty(AppPropertyKeyConst.JUMPURL_KEY + upcaseStyleName);
        WxMessageStyle messageStyle = new WxMessageStyle();
        messageStyle.setTemplateId(templateId);
        messageStyle.setColor(color);
        messageStyle.setJumpUrl(jumpUrl);
        this.messageStyleMap.put(styleName,messageStyle);
        return messageStyle;
    }
    @Override
    public WeixinMessageTemplate disguise(WxTemplateMessage message) {
        if (StringUtil.isNullOrEmpty(message.getToUser())) {
            logger.error("Message to user is empty.");
            return null;
        }
        if (StringUtil.isNullOrEmpty(message.getStyleName())) {
            logger.error("Message style name is empty.");
            return null;
        }
        WxMessageStyle messageStyle = this.getMessageStyle(message.getStyleName());
        if (messageStyle == null) {
            logger.error("Message style configure not found.");
            return null;
        }
        if(message.getMsgData()==null || message.getMsgData().isEmpty()) {
            logger.error("Message data body is null.");
            return null;
        }
        WeixinMessageTemplate messageTemplate = new WeixinMessageTemplate();
        messageTemplate.setToUser(message.getToUser());
        messageTemplate.setTemplateId(messageStyle.getTemplateId());
        messageTemplate.setTopColor(messageStyle.getColor());
        if (StringUtil.isNullOrEmpty(message.getJumpUrl())) {
           messageTemplate.setUrl(messageStyle.getJumpUrl());
        } else {
            messageTemplate.setUrl(message.getJumpUrl());
        }
        Iterator<Map<String,String>> msgIt = message.getMsgData().iterator();
        while (msgIt.hasNext()) {
            Map<String,String> el = msgIt.next();
            if (el.get("name") == null) {
                continue;
            }
            messageTemplate.addElement(el.get("name"),el.get("value"),messageStyle.getColor());
        }
        return messageTemplate;
    }
}
