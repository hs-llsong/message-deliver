package hs.ontheroadstore.message.deliver.handle;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.push.model.v20160801.PushMessageToiOSRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToiOSRequest;
import com.google.gson.Gson;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import hs.ontheroadstore.message.deliver.bean.BaseMobileMessage;
import hs.ontheroadstore.message.deliver.bean.OldIosPushMessage;
import org.apache.log4j.Logger;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/5.
 */
public class IosPushHandler extends MobilePushHandler{
    private final Logger logger = Logger.getLogger(IosPushHandler.class);
    private String apnsEnv;
    public IosPushHandler(IAcsClient client,long appkey,String apnsEvn) {
        super(client,appkey);
        this.apnsEnv = apnsEvn;
    }

    @Override
    AcsRequest makeRequest(String message, int pushType) {
        try {
            /*
            //Test using old message
            BaseMobileMessage baseMobileMessage = null;
            OldIosPushMessage oldIosPushMessage = new Gson().fromJson(message,OldIosPushMessage.class);
            if(oldIosPushMessage == null) {
                logger.error("parse message to json return null:"+ message);
                return null;
            } else {
                baseMobileMessage = new BaseMobileMessage();
                baseMobileMessage.setExtParameters(oldIosPushMessage.getiOSExtParameters());
                baseMobileMessage.setBody(oldIosPushMessage.getSummary());
                baseMobileMessage.setTitle(oldIosPushMessage.getTitle());
                baseMobileMessage.setTarget("DEVICE");
                baseMobileMessage.setTargetValue(oldIosPushMessage.getDeviceId());
            }*/
            BaseMobileMessage baseMobileMessage = new Gson().fromJson(message, BaseMobileMessage.class);
            if (baseMobileMessage == null) {
                logger.error("parse message to json return null:"+ message);
                return null;
            }
            if (pushType == AppPropertyKeyConst.PUSH_TYPE_NOTICE) {
                PushNoticeToiOSRequest iosRequest = new PushNoticeToiOSRequest();
                iosRequest.setProtocol(ProtocolType.HTTPS);
                iosRequest.setMethod(MethodType.POST);
                iosRequest.setApnsEnv(this.apnsEnv);
                iosRequest.setAppKey(getAppkey());
                iosRequest.setTarget(baseMobileMessage.getTarget());
                iosRequest.setTargetValue(baseMobileMessage.getTargetValue());
                iosRequest.setBody(baseMobileMessage.getBody());
                iosRequest.setExtParameters(baseMobileMessage.getExtParameters());
                return iosRequest;
            } else {
                PushMessageToiOSRequest iosRequest = new PushMessageToiOSRequest();
                iosRequest.setProtocol(ProtocolType.HTTPS);
                iosRequest.setMethod(MethodType.POST);
                iosRequest.setAppKey(getAppkey());
                iosRequest.setTarget(baseMobileMessage.getTarget());
                iosRequest.setTargetValue(baseMobileMessage.getTargetValue());
                iosRequest.setTitle(baseMobileMessage.getTitle());
                iosRequest.setBody(baseMobileMessage.getBody());
                return iosRequest;
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

}
