package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/5.
 */
public interface AppMessagePushHandler {
    boolean send(String message,int deviceType,int pushType);
}
