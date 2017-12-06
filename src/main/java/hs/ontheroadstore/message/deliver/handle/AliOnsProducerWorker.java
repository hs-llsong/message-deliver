package hs.ontheroadstore.message.deliver.handle;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import hs.ontheroadstore.message.deliver.bean.HsAliOnsPushMessage;
import org.apache.log4j.Logger;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/28.
 */
public class AliOnsProducerWorker implements Runnable{
    private HandleManager handleManager;
    private JsonCacheHandler jsonCacheHandler;
    private String message;
    private String topic;
    private final Logger logger = Logger.getLogger(AliOnsProducerWorker.class);
    public AliOnsProducerWorker(final HandleManager handleManager,JsonCacheHandler jsonCacheHandler,String topic,String message) {
        this.handleManager = handleManager;
        this.jsonCacheHandler = jsonCacheHandler;
        this.message = message;
        this.topic = topic;
    }

    @Override

    public void run() {
        Gson gson = new Gson();
        HsAliOnsPushMessage hsAliOnsPushMessage = null;
        try {
            hsAliOnsPushMessage = gson.fromJson(message, HsAliOnsPushMessage.class);
        } catch (JsonSyntaxException e) {
            logger.error(e.getMessage()+ ",message push back to redis.");
            jsonCacheHandler.add(message);
            return;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
        if (hsAliOnsPushMessage == null) {
            logger.error("be push object is null.");
            return;
        }
        String tag = hsAliOnsPushMessage.getTag();

        if(StringUtil.isNullOrEmpty(tag)) {
            tag = AppPropertyKeyConst.DEFAULT_ALIONS_TAG;
        }
        boolean result = handleManager.getAliOnsProducerHandler().send(hsAliOnsPushMessage.getBody(),topic
                ,tag,hsAliOnsPushMessage.getKey());
        if (!result) {
            logger.error("Send message failed,push message back to redis.message:" + message);
            jsonCacheHandler.add(message);
        }
    }
}
