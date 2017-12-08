package hs.ontheroadstore.message.deliver.handle;

import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import hs.ontheroadstore.message.deliver.bean.OnsProducer;
import hs.ontheroadstore.message.deliver.bean.OnsPropertyKeyConst;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/27.
 */
public class AliOnsProducerHandlerImpl implements AliOnsProducerHandler {
    private Map<String,OnsProducer> producerMap = new HashMap<>();
    private Properties propAll;
    private final Logger logger = Logger.getLogger(AliOnsProducerHandlerImpl.class);
    public AliOnsProducerHandlerImpl(final Properties prop) {
        this.propAll = prop;
    }

    private OnsProducer createProducer(String topic) {
        String channelKey = topic.toUpperCase() + AppPropertyKeyConst.CHANNEL_NAME_SUFFIX;
        if(!propAll.containsKey(channelKey)) {
            logger.error("Channel name not found. for topic:" + topic);
            return null;
        }
        String channelName = propAll.getProperty(channelKey);
        Properties prop = this.getChannelProperties(propAll,channelName);
        if(prop.isEmpty()) {
            logger.error("Properties for channel " + channelName + " is empty.");
            return null;
        }
        OnsProducer onsProducer = new OnsProducer(prop,topic);
        producerMap.put(topic,onsProducer);
        return onsProducer;

    }

    private Properties getChannelProperties(Properties propAll,String channelName) {
        Properties prop = new Properties();
        Field[] fields = OnsPropertyKeyConst.class.getFields();
        for (Field field:fields) {
            String key = channelName + field.getName();
            if (propAll.containsKey(key)) {
                prop.put(field.getName(), propAll.getProperty(key));
            }
        }
        return prop;
    }

    @Override
    public boolean send(String message, String topic, String tag, String key) {
        OnsProducer onsProducer;
        if(producerMap.containsKey(topic)) {
            onsProducer = producerMap.get(topic);
        } else {
            onsProducer = this.createProducer(topic);
            if(onsProducer == null) {
                logger.error("createProducer for " + topic + " failed.");
                return false;
            }
        }
        logger.debug("to call OnsProducer do send message tag:" + tag + ",topic:" + topic);
        return onsProducer.send(message,tag,key);
    }
}
