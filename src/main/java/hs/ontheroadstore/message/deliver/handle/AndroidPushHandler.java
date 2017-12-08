package hs.ontheroadstore.message.deliver.handle;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.push.model.v20160801.PushMessageToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidRequest;
import com.google.gson.Gson;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import hs.ontheroadstore.message.deliver.bean.BaseMobileMessage;
import org.apache.log4j.Logger;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/5.
 */
public class AndroidPushHandler extends MobilePushHandler{
    private final Logger logger = Logger.getLogger(AndroidPushHandler.class);
    public AndroidPushHandler(IAcsClient client,long appkey) {
        super(client,appkey);
    }

    @Override
    AcsRequest makeRequest(String message, int pushType) {

        BaseMobileMessage baseMobileMessage = null;
        try {
            baseMobileMessage = new Gson().fromJson(message, BaseMobileMessage.class);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        if (baseMobileMessage == null) {
            logger.error("parse message to json return null:"+ message);
            return null;
        }
        if (pushType == AppPropertyKeyConst.PUSH_TYPE_NOTICE) {
            PushNoticeToAndroidRequest androidRequest = new PushNoticeToAndroidRequest();
            androidRequest.setProtocol(ProtocolType.HTTPS);
            androidRequest.setMethod(MethodType.POST);
            androidRequest.setAppKey(getAppkey());
            androidRequest.setTarget(baseMobileMessage.getTarget());
            androidRequest.setTargetValue(baseMobileMessage.getTargetValue());
            androidRequest.setTitle(baseMobileMessage.getTitle());
            androidRequest.setBody(baseMobileMessage.getBody());
            androidRequest.setExtParameters(baseMobileMessage.getExtParameters());
            return androidRequest;
        } else {
            PushMessageToAndroidRequest androidRequest = new PushMessageToAndroidRequest();
            androidRequest.setProtocol(ProtocolType.HTTPS);
            androidRequest.setMethod(MethodType.POST);
            androidRequest.setAppKey(getAppkey());
            androidRequest.setTarget(baseMobileMessage.getTarget());
            androidRequest.setTargetValue(baseMobileMessage.getTargetValue());
            androidRequest.setTitle(baseMobileMessage.getTitle());
            androidRequest.setBody(baseMobileMessage.getBody());
            return androidRequest;
        }

    }
}
