package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import hs.ontheroadstore.message.deliver.bean.OnsPropertyKeyConst;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/5.
 */
public class AliAcsMessagePushHandlerImpl implements AppMessagePushHandler{
    private IAcsClient acsClient;
    private IClientProfile profile;
    private String apnsEnv;
    private long appkey = 0L;
    private final Logger logger = Logger.getLogger(AliAcsMessagePushHandlerImpl.class);
    public AliAcsMessagePushHandlerImpl(final Properties prop) {
        this.profile = DefaultProfile.getProfile(
                prop.getProperty(AppPropertyKeyConst.AMS_REGION_KEY),
                prop.getProperty(OnsPropertyKeyConst.AccessKey),
                prop.getProperty(OnsPropertyKeyConst.SecretKey)
        );
        this.acsClient = new DefaultAcsClient(this.profile);
        this.apnsEnv = prop.getProperty(AppPropertyKeyConst.AMS_APNSENV_KEY);
        if (StringUtil.isNullOrEmpty(apnsEnv)) {
            logger.error("APNS ENV is empty.");
        }
        String appkey = prop.getProperty(AppPropertyKeyConst.AMS_APPKEY_KEY);
        if(StringUtil.isNullOrEmpty(appkey)) {
            logger.error("Ali acs appkey is empty");
        } else {
            this.appkey = Long.valueOf(appkey);
        }
    }

    @Override
    public boolean send(String message, int deviceType, int pushType) {
        if(deviceType == AppPropertyKeyConst.DEVICE_ANDROID) return new AndroidPushHandler(acsClient,appkey).send(message,pushType);
        if(deviceType == AppPropertyKeyConst.DEVICE_IOS) return new IosPushHandler(acsClient,appkey,apnsEnv).send(message,pushType);
        return false;
    }

}
