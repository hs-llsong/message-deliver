package hs.ontheroadstore.message.deliver.tools;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/22.
 */
public class DateTimeTools {
    static String getNowTimeStamp() {
        long time = System.currentTimeMillis();
        return String.valueOf(time/1000);
    }
}
