package hs.ontheroadstore.message.deliver.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import hs.ontheroadstore.message.deliver.App;
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
        if (StringUtil.isNullOrEmpty(msgData)) {
            logger.error("Empty message:" + new Gson().toJson(message));
            return Action.CommitMessage;
        }
        return doWxTemplateMessage(msgData);
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
        String accessToken = app.getHandleManager().getWxTokenHandler().getAccessToken();
        if (StringUtil.isNullOrEmpty(accessToken)) {
            logger.error("WxAccessToken is null.");
            return Action.ReconsumeLater;
        }
        try {
            WxTemplateMessage wxTemplateMessage = new Gson().fromJson(message,WxTemplateMessage.class);
            WxTemplateMessageResponse wxTemplateMessageResponse =
                    app.getHandleManager().getWxTemplateMessageHandler().send(wxTemplateMessage,accessToken);
            if (wxTemplateMessageResponse == null) {
                logger.info("Reconsume later");
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
                        app.getHandleManager().getWxTokenHandler().refreshToken();
                        logger.error("Reconsume later.Send message 40001 error." + wxTemplateMessageResponse.getErrmsg());
                        return Action.ReconsumeLater;
                    default:
                        break;
                }
            }
            return Action.CommitMessage;
        } catch (JsonSyntaxException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage() + ",Reconsume later");
            return Action.ReconsumeLater;
        }
        return Action.CommitMessage;
    }
}
