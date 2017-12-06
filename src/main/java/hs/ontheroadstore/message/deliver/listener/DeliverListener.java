package hs.ontheroadstore.message.deliver.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import hs.ontheroadstore.message.deliver.App;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import hs.ontheroadstore.message.deliver.bean.WeixinMessageTemplate;
import hs.ontheroadstore.message.deliver.bean.WxTemplateMessage;
import hs.ontheroadstore.message.deliver.bean.WxTemplateMessageResponse;
import org.apache.log4j.Logger;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */

public class DeliverListener implements MessageListener{
    private App app;
    private final Logger logger = Logger.getLogger(DeliverListener.class);
    public DeliverListener(final App app) {
        this.app = app;
    }
    @Override
    public Action consume(Message message, ConsumeContext context) {
        String msgData = new String(message.getBody());
        logger.debug("consume message:" + msgData);
        if (StringUtil.isNullOrEmpty(msgData)) {
            logger.error("Empty message:" + new Gson().toJson(message));
            return Action.CommitMessage;
        }
        String tag = message.getTag();
        if (StringUtil.isNullOrEmpty(tag)) {
            logger.error("Not tag message.give up:" + msgData);
            return Action.CommitMessage;
        }
        switch (tag) {
            case AppPropertyKeyConst.MSG_TAG_WEIXIN_TEMPLATE:
                return doWxTemplateMessage(msgData);
            case AppPropertyKeyConst.MSG_TAG_IOS:
                return doIosMessage(msgData,message.getKey());
            case AppPropertyKeyConst.MSG_TAG_android:
                return doAndroidMessage(msgData,message.getKey());
            case AppPropertyKeyConst.MSG_TAG_WX_APP:
                return doWxAppMessage(msgData);
            default:
                logger.info("other message tag:" + tag);
                break;
        }
        return Action.CommitMessage;
    }

    /**
     *
     * @param message
     * @param key  push type. notice or message
     * @return
     */
    private Action doIosMessage(String message,String key) {
        return doMobileNoticeMessage(message,AppPropertyKeyConst.DEVICE_IOS);
    }
    private Action doAndroidMessage(String message,String key) {
        return doMobileNoticeMessage(message,AppPropertyKeyConst.DEVICE_ANDROID);
    }
    private Action doMobileNoticeMessage (String message,int deviceType) {
        boolean result = app.getHandleManager()
                .getAppMessagePushHandler()
                .send(message,deviceType,AppPropertyKeyConst.PUSH_TYPE_NOTICE);
        logger.debug("doMobileNoticeMessage:" + message + ",result =" + result);
        if(result == false) return Action.ReconsumeLater;
        return Action.CommitMessage;
    }
    private Action doWxAppMessage(String message) {
        logger.info("doWxAppMessage:" + message);
        return Action.CommitMessage;
    }
    private Action doWxTemplateMessage(String message) {

        if(app.getHandleManager() == null) {
            logger.error("Handler manager is null.");
            return Action.ReconsumeLater;
        }
        if (app.getHandleManager().getWxTemplateMessageHandler()== null) {
            logger.error("WxTemplateMessageHandler is null.");
            return Action.ReconsumeLater;
        }
        if (app.getHandleManager().getWxTokenHandler()== null) {
            logger.error("WxTokenHandler is null.");
            return Action.ReconsumeLater;
        }
        if (app.getHandleManager().getWxMessageMakeupHandler() == null) {
            logger.error("WxMessageMakeupHandle is null.");
            return Action.ReconsumeLater;
        }
        String accessToken = app.getHandleManager().getWxTokenHandler().getAccessToken();
        if (StringUtil.isNullOrEmpty(accessToken)) {
            logger.error("WxAccessToken is null.");
            return Action.ReconsumeLater;
        }

        WeixinMessageTemplate weixinMessageTemplate;
        try {
            WxTemplateMessage wxTemplateMessage = new Gson().fromJson(message, WxTemplateMessage.class);
            if(wxTemplateMessage == null) {
                logger.error("WxTemplateMessage is null.message style error!" + message);
                return Action.CommitMessage;
            }
            weixinMessageTemplate = app.getHandleManager().getWxMessageMakeupHandler().disguise(wxTemplateMessage);

        } catch (JsonSyntaxException e) {
            logger.error(e.getMessage() + ",message:" + message);
            return Action.CommitMessage;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Action.CommitMessage;
        }
        if (weixinMessageTemplate == null) {
            logger.error("WeixinMessageTemplate is null.");
            return Action.CommitMessage;
        }


        WxTemplateMessageResponse wxTemplateMessageResponse =
                    app.getHandleManager().getWxTemplateMessageHandler().send(weixinMessageTemplate,accessToken);
        if (wxTemplateMessageResponse == null) {
            logger.info("Send weixin template message response null,Reconsume later");
            return Action.ReconsumeLater;
        }
        if (wxTemplateMessageResponse.getErrcode()!=0) {
            logger.error("Error code:" + wxTemplateMessageResponse.getErrcode());
            switch (wxTemplateMessageResponse.getErrcode()) {
                case 43004:
                    break;
                case 43019:
                    break;
                case 40037:
                    logger.error("Reconsume later.Send message failed." + wxTemplateMessageResponse.getErrmsg());
                    return Action.ReconsumeLater;
                case 40001:
                    //app.getHandleManager().getWxTokenHandler().refreshToken();
                    logger.error("Reconsume later.Send message 40001 error." + wxTemplateMessageResponse.getErrmsg());
                    return Action.ReconsumeLater;
                case 40003: //invalid openid hint
                    break;
                default:
                    break;
            }
        }
        return Action.CommitMessage;
    }
}
