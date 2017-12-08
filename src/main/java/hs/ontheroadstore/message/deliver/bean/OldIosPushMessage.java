package hs.ontheroadstore.message.deliver.bean;

import java.io.Serializable;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/6.
 */
public class OldIosPushMessage implements Serializable{
    int type;
    String title;
    String iOStitle;
    String body;
    String summary;
    String deviceId;
    String iOSExtParameters;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getiOStitle() {
        return iOStitle;
    }

    public void setiOStitle(String iOStitle) {
        this.iOStitle = iOStitle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getiOSExtParameters() {
        return iOSExtParameters;
    }

    public void setiOSExtParameters(String iOSExtParameters) {
        this.iOSExtParameters = iOSExtParameters;
    }
}
