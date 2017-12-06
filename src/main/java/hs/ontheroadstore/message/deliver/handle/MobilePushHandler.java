package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.push.model.v20160801.*;
import org.apache.log4j.Logger;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/5.
 */
public abstract class MobilePushHandler {
    private IAcsClient client;
    private long appkey;
    private final Logger logger = Logger.getLogger(MobilePushHandler.class);
    public MobilePushHandler(IAcsClient client,long appkey) {
        this.client = client;
        this.appkey = appkey;
    }
    protected long getAppkey() {
        return this.appkey;
    }
    public boolean send(String message,int pushType) {
        AcsRequest request = makeRequest(message,pushType);
        if (request == null) {
            logger.error("Build request return null.");
            return false;
        }
        try {
            AcsResponse response = client.getAcsResponse(request);
            if(response == null) {
                logger.error("Push message to mobile device error response is null");
                return false;
            }
            String messageId = null;
            if (response instanceof PushMessageToAndroidResponse) {
                messageId = ((PushMessageToAndroidResponse)response).getMessageId();
            } else if(response instanceof PushMessageToiOSResponse) {
                messageId = ((PushMessageToiOSResponse)response).getMessageId();
            } else if(response instanceof PushNoticeToAndroidResponse) {
                messageId = ((PushNoticeToAndroidResponse)response).getMessageId();
            } else if(response instanceof PushNoticeToiOSResponse) {
                messageId = ((PushNoticeToiOSResponse)response).getMessageId();
            } else if(response instanceof PushResponse) {
                messageId = ((PushResponse)response).getMessageId();
            }
            if(StringUtil.isNullOrEmpty(messageId)) return false;
            logger.debug("send message to acs success.message id:" + messageId);
            return true;
        }catch (Exception e) {
            logger.error("Push message to mobile device error:" + e.getMessage());
            return false;
        }
    }

    abstract AcsRequest makeRequest(String message,int pushType);

}
