package hs.ontheroadstore.message.deliver;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import hs.ontheroadstore.message.deliver.bean.OnsPropertyKeyConst;
import hs.ontheroadstore.message.deliver.handle.*;
import hs.ontheroadstore.message.deliver.listener.DeliverListener;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public class App {
    private HandleManager handleManager;
    private final static Logger logger = Logger.getLogger(App.class);
    public HandleManager getHandleManager() {
        return handleManager;
    }

    public void setHandleManager(HandleManager handleManager) {
        this.handleManager = handleManager;
    }
    public static void main( String[] args ){
        App app = new App();
        Properties prop = app.loadPropertis();
        if(prop == null) {
            System.out.println("Load properties failed.");
            System.exit(0);
        }
        String appid,appSecret;
        appid = prop.getProperty(AppPropertyKeyConst.WX_APPID);
        appSecret = prop.getProperty(AppPropertyKeyConst.WX_APP_SECRET);
        if(StringUtil.isNullOrEmpty(appid) || StringUtil.isNullOrEmpty(appSecret)) {
            System.out.println("Weixin appid or secret not set.");
            System.exit(0);
        }
        app.setHandleManager(new HandleManagerImpl());
        app.getHandleManager().registerWxTemplateMessageHandler(new WxTemplateMessageHandlerImpl());
        app.getHandleManager().registerWxMessageMakeupHandler(new WxMessageMakeupHandleImpl(prop));
        WxTokenHandler tokenHandler = new WxAccessTokenHandlerImpl(appid,appSecret);
        Thread t = new Thread(tokenHandler);
        t.start();
        logger.info("WxAccessTokenHandler service started");
        app.getHandleManager().registerWxTokenHandler(tokenHandler);
        Consumer NotificationTestConsumer = app.createConsumer(prop,"NotificationTest","heishi");
        if (NotificationTestConsumer == null) {
            tokenHandler.terminate();
            try {
                System.out.println("Wait for thread exit.");
                t.join();
                System.out.println("Consumer create failed.game over.");
                System.exit(0);
            } catch (InterruptedException e) {

            }
        }
        NotificationTestConsumer.start();
        logger.info("Notification consumer service started");
    }

    private Consumer createConsumer(final Properties properties,String channelName,String subExpression) {
        Properties prop = this.getChannelProperties(properties,channelName);
        if(prop.isEmpty()) {
            logger.error("Can't load properites for "+channelName);
            return null;
        }
        Consumer consumer;
        try {
            consumer = ONSFactory.createConsumer(prop);
            consumer.subscribe(prop.getProperty(OnsPropertyKeyConst.Topic), subExpression, new DeliverListener(this));
        } catch (ONSClientException e) {
            logger.error(e.getMessage());
            return null;
        }
        return consumer;
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

    public Properties loadPropertis() {
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            return null;
        }
        return prop;
    }
}
