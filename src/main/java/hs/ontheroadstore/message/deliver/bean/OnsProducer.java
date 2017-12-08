package hs.ontheroadstore.message.deliver.bean;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/27.
 */
public class OnsProducer {
    private Properties prop;
    private Producer producer;
    private String topic;
    private final Logger logger = Logger.getLogger(OnsProducer.class);
    public OnsProducer(Properties prop,String topic) {
        this.prop = prop;
        this.topic = topic;
        this.init();
    }

    public void init() {
        if (producer != null) {
            producer.shutdown();
            producer = null;
        }
        try {
            producer = ONSFactory.createProducer(prop);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
        producer.start();
    }

    public boolean send(String message, String tag, String key){
        if(producer == null) {
            logger.error("producer is null.");
            return false;
        }
        Message msg = new Message();
        msg.setTopic(this.topic);
        msg.setTag(tag);
        msg.setBody(message.getBytes());
        if (!key.isEmpty()){
            msg.setKey(key);
        }
        try {
            producer.send(msg);
            return true;
        } catch(Exception e) {
            logger.error("send message:" + message + " failed." + e.getMessage());
            return false;
        }
    }
}
