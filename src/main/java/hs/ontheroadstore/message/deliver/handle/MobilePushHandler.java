package hs.ontheroadstore.message.deliver.handle;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.push.model.v20160801.PushMessageToAndroidResponse;
import com.aliyuncs.push.model.v20160801.PushMessageToiOSResponse;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import org.apache.log4j.Logger;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/5.
 */
public abstract class MobilePushHandler {
    private IAcsClient client;
    private final Logger logger = Logger.getLogger(MobilePushHandler.class);
    public MobilePushHandler(IAcsClient client) {
        this.client = client;
    }
    public boolean send(String message,int pushType) {
        AcsRequest request = makeRequest(message,pushType);
        try {
            AcsResponse response = client.getAcsResponse(request);
            if(response == null) {
                logger.error("Push message to mobile device error response is null");
                return false;
            }
            //TODO
            if(response instanceof PushMessageToAndroidResponse) {

            } else if(response instanceof PushMessageToiOSResponse) {

            } else if(response instanceof PushResponse) {

            }

        }catch (Exception e) {
            logger.error("Push message to mobile device error:" + e.getMessage());
            return false;
        }
        return true;
    }

    abstract AcsRequest makeRequest(String message,int pushType);

}
