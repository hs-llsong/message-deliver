package hs.ontheroadstore.message.deliver.handle;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import hs.ontheroadstore.message.deliver.bean.OnsPropertyKeyConst;

import java.util.Properties;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/5.
 */
public class AliAcsMessagePushHandlerImpl implements AppMessagePushHandler{
    private IAcsClient acsClient;
    private IClientProfile profile;
    public AliAcsMessagePushHandlerImpl(final Properties prop) {
        this.profile = DefaultProfile.getProfile(
                prop.getProperty(AppPropertyKeyConst.AMS_REGION_KEY),
                prop.getProperty(OnsPropertyKeyConst.AccessKey),
                prop.getProperty(OnsPropertyKeyConst.SecretKey)
        );
        this.acsClient = new DefaultAcsClient(this.profile);
    }

    @Override
    public boolean send(String message, int deviceType, int pushType) {
        if(deviceType == AppPropertyKeyConst.DEVICE_ANDROID) return new AndroidPushHandler(acsClient).send(message,pushType);
        if(deviceType == AppPropertyKeyConst.DEVICE_IOS) return new IosPushHandler(acsClient).send(message,pushType);
        return false;
    }

}
