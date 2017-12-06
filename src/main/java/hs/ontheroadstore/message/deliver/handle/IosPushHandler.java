package hs.ontheroadstore.message.deliver.handle;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.IAcsClient;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/5.
 */
public class IosPushHandler extends MobilePushHandler{

    public IosPushHandler(IAcsClient client) {
        super(client);
    }

    @Override
    AcsRequest makeRequest(String message, int pushType) {
        return null;
    }
}
