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
import hs.ontheroadstore.message.deliver.handle.NoDisturbHandle;
import hs.ontheroadstore.message.deliver.handle.WxTokenAutoRefreshHandler;
import hs.ontheroadstore.message.deliver.handle.WxTokenHandler;
import org.apache.log4j.Logger;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */

public class DeliverListener implements MessageListener{
    private App app;
    private final Logger logger = Logger.getLogger(DeliverListener.class);
    private String topic;
    public DeliverListener(final App app,final String topic) {
        this.app = app;
        this.topic = topic;
    }
    @Override
    public Action consume(Message message, ConsumeContext context) {
        String msgData = new String(message.getBody());

        //TODO 记录消息发送总数，每日发送总数，成功数，失败数，重试数
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
            case AppPropertyKeyConst.MSG_TAG_NEW_WEIXIN:
                return doNewWxTemplateMessage(msgData);
            default:
                return doWxTemplateMessage(msgData);
        }
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
                .send(message,deviceType,AppPropertyKeyConst.PUSH_TYPE_ADVANCE);

        if(result == false) return Action.ReconsumeLater;
        return Action.CommitMessage;
    }
    private Action doWxAppMessage(String message) {
        logger.info("doWxAppMessage:" + message);
        return Action.CommitMessage;
    }

    private boolean checkEnv() {
        if(app.getHandleManager() == null) {
            logger.error("Handler manager is null.");
            return false;
        }
        if (app.getHandleManager().getWxTemplateMessageHandler()== null) {
            logger.error("WxTemplateMessageHandler is null.");
            return false;
        }
        if (app.getHandleManager().getWxTokenHandler()== null) {
            logger.error("WxTokenHandler is null.");
            return false;
        }
        if (app.getHandleManager().getWxMessageMakeupHandler() == null) {
            logger.error("WxMessageMakeupHandle is null.");
            return false;
        }
        return true;
    }
    private Action doNewWxTemplateMessage(String message) {
        if(!checkEnv()) return Action.ReconsumeLater;
        String accessToken = app.getHandleManager().getWxTokenHandler().getAccessToken();
        if (StringUtil.isNullOrEmpty(accessToken)) {
            logger.error("WxAccessToken is null.");
            return Action.ReconsumeLater;
        }
        WxTemplateMessageResponse wxTemplateMessageResponse =
                app.getHandleManager().getWxTemplateMessageHandler().send(message,accessToken);
        if (wxTemplateMessageResponse == null) {
            logger.info("Send template message response null,consume later.");
            return Action.ReconsumeLater;
        }
        if (wxTemplateMessageResponse.getErrcode()!=0) {
            logger.error("Error code:" + wxTemplateMessageResponse.getErrcode());
            switch (wxTemplateMessageResponse.getErrcode()) {
                case 43004:
                    //{"errcode":43004,"errmsg":"require subscribe hint: [zgtDpA0256ge29]"}
                    doNoDisturbeAction(message);
                    break;
                case 43019:
                    //{"errcode":43019,"errmsg":"require remove blacklist hint: [lU_240184ge30]"}
                    doNoDisturbeAction(message);
                    break;
                case 40037:
                    logger.error("Consume later.Send message failed." + wxTemplateMessageResponse.getErrmsg());
                    return Action.ReconsumeLater;
                case 40001:
                    WxTokenHandler handler = app.getHandleManager().getWxTokenHandler();
                    if(handler instanceof WxTokenAutoRefreshHandler) {
                        ((WxTokenAutoRefreshHandler) handler).refreshToken();
                    }
                    return Action.ReconsumeLater;
                case 40003: //invalid openid hint
                    //{"errcode":40003,"errmsg":"invalid openid hint: [iozOma0197ge30]"}
                    break;
                default:
                    break;
            }
        }
        return Action.CommitMessage;
    }
    private void doNoDisturbeAction(String message){
        NoDisturbHandle noDisturbHandle = app.getHandleManager().getNoDisturbHandler();
        if(noDisturbHandle == null) return;
        WeixinMessageTemplate weixinMessageTemplate = new Gson().fromJson(message,WeixinMessageTemplate.class);
        if(weixinMessageTemplate == null) return;
        if(StringUtil.isNullOrEmpty(weixinMessageTemplate.getToUser())) return;
        noDisturbHandle.pacify(weixinMessageTemplate.getToUser());
    }

    private Action doWxTemplateMessage(String message) {

        if(!checkEnv()) return Action.ReconsumeLater;
        String accessToken = app.getHandleManager().getWxTokenHandler().getAccessToken();
        if (StringUtil.isNullOrEmpty(accessToken)) {
            logger.error("WxAccessToken is null.");
            return Action.ReconsumeLater;
        }
        boolean isPushMessage = false;
        WeixinMessageTemplate weixinMessageTemplate;
        try {
            WxTemplateMessage wxTemplateMessage = new Gson().fromJson(message, WxTemplateMessage.class);
            if(wxTemplateMessage == null) {
                logger.error("WxTemplateMessage is null.message style error!" + message);
                return Action.CommitMessage;
            }
            if (StringUtil.isNullOrEmpty(wxTemplateMessage.getToUser())) {
                logger.error("To user openid is null." + message);
                return Action.CommitMessage;
            }
            if(wxTemplateMessage.getStyleName().equals(AppPropertyKeyConst.STYLE_UPDATE)) {
                isPushMessage = true;
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
                    //{"errcode":43004,"errmsg":"require subscribe hint: [zgtDpA0256ge29]"}
                    if (isPushMessage) {
                        NoDisturbHandle noDisturbHandle = app.getHandleManager().getNoDisturbHandler();
                        if (noDisturbHandle != null) noDisturbHandle.pacify(weixinMessageTemplate.getToUser());
                    }
                    break;
                case 43019:
                    //{"errcode":43019,"errmsg":"require remove blacklist hint: [lU_240184ge30]"}

                    NoDisturbHandle noDisturbHandle = app.getHandleManager().getNoDisturbHandler();
                    if (noDisturbHandle != null) noDisturbHandle.pacify(weixinMessageTemplate.getToUser());

                    break;
                case 40037:
                    logger.error("Reconsume later.Send message failed." + wxTemplateMessageResponse.getErrmsg());
                    return Action.ReconsumeLater;
                case 40001:
                    WxTokenHandler handler = app.getHandleManager().getWxTokenHandler();
                    if(handler instanceof WxTokenAutoRefreshHandler) {
                        ((WxTokenAutoRefreshHandler) handler).refreshToken();
                    }
                    return Action.ReconsumeLater;
                case 40003: //invalid openid hint
                    //{"errcode":40003,"errmsg":"invalid openid hint: [iozOma0197ge30]"}
                    break;
                default:
                    break;
            }
        }
        return Action.CommitMessage;
    }
}
