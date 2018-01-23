package hs.ontheroadstore.message.deliver.handle;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.push.model.v20160801.PushMessageToiOSRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToiOSRequest;
import com.aliyuncs.push.model.v20160801.PushRequest;
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
    AcsRequest makeRequest(BaseMobileMessage baseMobileMessage,int pushType) {
        try {
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
            } else if(pushType == AppPropertyKeyConst.PUSH_TYPE_MESSAGE){
                PushMessageToiOSRequest iosRequest = new PushMessageToiOSRequest();
                iosRequest.setProtocol(ProtocolType.HTTPS);
                iosRequest.setMethod(MethodType.POST);
                iosRequest.setAppKey(getAppkey());
                iosRequest.setTarget(baseMobileMessage.getTarget());
                iosRequest.setTargetValue(baseMobileMessage.getTargetValue());
                iosRequest.setTitle(baseMobileMessage.getTitle());
                iosRequest.setBody(baseMobileMessage.getBody());
                return iosRequest;
            } else {
                PushRequest pushRequest = new PushRequest();
                pushRequest.setProtocol(ProtocolType.HTTPS);
                pushRequest.setAppKey(getAppkey());
                pushRequest.setTarget(baseMobileMessage.getTarget());
                pushRequest.setTargetValue(baseMobileMessage.getTargetValue());
                pushRequest.setPushType("NOTICE");
                pushRequest.setTitle(baseMobileMessage.getTitle());
                pushRequest.setBody(baseMobileMessage.getBody());

                pushRequest.setIOSBadgeAutoIncrement(true);
                pushRequest.setIOSApnsEnv(this.apnsEnv);
                pushRequest.setIOSExtParameters(baseMobileMessage.getExtParameters());
                pushRequest.setDeviceType("iOS");
                return pushRequest;
                //pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
                //pushRequest.setIOSRemindBody("iOSRemindBody");//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

}
