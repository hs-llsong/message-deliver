package hs.ontheroadstore.message.deliver;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import hs.ontheroadstore.message.deliver.bean.OnsPropertyKeyConst;
import hs.ontheroadstore.message.deliver.handle.HandleManager;
import hs.ontheroadstore.message.deliver.listener.DeliverListener;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public class App {
    private HandleManager handleManager;

    public HandleManager getHandleManager() {
        return handleManager;
    }

    public void setHandleManager(HandleManager handleManager) {
        this.handleManager = handleManager;
    }
    public static void main( String[] args ){

    }
    private Consumer createConsumer(Properties properties,String channelName,String topic,String subExpression) {
        Properties prop = this.getChannelProperties(properties,channelName);
        Consumer consumer = ONSFactory.createConsumer(prop);
        consumer.subscribe(topic,subExpression,new DeliverListener(this));
        return consumer;
    }

    private Properties getChannelProperties(Properties propAll,String channelName) {
        Properties prop = new Properties();
        Field[] fields = OnsPropertyKeyConst.class.getFields();
        for(Field field:fields) {
            String key = channelName + field.getName();
            if (propAll.containsKey(key)) {
                prop.put(field.getName(), propAll.getProperty(key));
            }
        }
        return prop;
    }
}
