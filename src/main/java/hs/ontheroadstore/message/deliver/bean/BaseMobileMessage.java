package hs.ontheroadstore.message.deliver.bean;

import java.io.Serializable;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/6.
 */
public class BaseMobileMessage implements Serializable{
    private String target;
    private String targetValue;
    private String title;
    private String body;
    private String androidOpenType;
    private String androidOpenTarget;
    private String extParameters;

    public String getAndroidOpenType() {
        return androidOpenType;
    }

    public void setAndroidOpenType(String androidOpenType) {
        this.androidOpenType = androidOpenType;
    }

    public String getAndroidOpenTarget() {
        return androidOpenTarget;
    }

    public void setAndroidOpenTarget(String androidOpenTarget) {
        this.androidOpenTarget = androidOpenTarget;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtParameters() {
        return extParameters;
    }

    public void setExtParameters(String extParameters) {
        this.extParameters = extParameters;
    }

}
