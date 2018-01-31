package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.aliyuncs.AcsRequest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.push.model.v20160801.PushMessageToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushRequest;
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
    AcsRequest makeRequest(BaseMobileMessage baseMobileMessage, int pushType) {
        if (pushType == AppPropertyKeyConst.PUSH_TYPE_MESSAGE){
            PushMessageToAndroidRequest androidRequest = new PushMessageToAndroidRequest();
            androidRequest.setProtocol(ProtocolType.HTTPS);
            androidRequest.setMethod(MethodType.POST);
            androidRequest.setAppKey(getAppkey());
            androidRequest.setTarget(baseMobileMessage.getTarget());
            androidRequest.setTargetValue(baseMobileMessage.getTargetValue());
            androidRequest.setTitle(baseMobileMessage.getTitle());
            androidRequest.setBody(baseMobileMessage.getBody());
            return androidRequest;
        } else if(pushType == AppPropertyKeyConst.PUSH_TYPE_NOTICE) {
            //android default is notice message
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
        } else  {
            PushRequest pushRequest = new PushRequest();
            pushRequest.setProtocol(ProtocolType.HTTPS);
            pushRequest.setAppKey(getAppkey());
            pushRequest.setTarget(baseMobileMessage.getTarget());
            pushRequest.setTargetValue(baseMobileMessage.getTargetValue());
            pushRequest.setPushType("NOTICE");
            pushRequest.setTitle(baseMobileMessage.getTitle());
            pushRequest.setBody(baseMobileMessage.getBody());
            pushRequest.setDeviceType("ANDROID");
            if(!StringUtil.isNullOrEmpty(baseMobileMessage.getAndroidOpenType())) {
                pushRequest.setAndroidOpenType(baseMobileMessage.getAndroidOpenType());
                switch (baseMobileMessage.getAndroidOpenType()) {
                    case AppPropertyKeyConst.ANDROID_OPEN_TYPE_ACTIVITY:
                        pushRequest.setAndroidActivity(baseMobileMessage.getAndroidOpenTarget());
                        break;
                    case AppPropertyKeyConst.ANDROID_OPEN_TYPE_APPLICATION:
                        break;
                    case AppPropertyKeyConst.ANDROID_OPEN_TYPE_NONE:
                        break;
                    case AppPropertyKeyConst.ANDROID_OPEN_TYPE_URL:
                        pushRequest.setAndroidOpenUrl(baseMobileMessage.getAndroidOpenTarget());
                        break;
                }
            }
            pushRequest.setAndroidExtParameters(baseMobileMessage.getExtParameters());
            return pushRequest;
        }

    }
}
