package hs.ontheroadstore.message.deliver.bean;

import java.io.Serializable;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/23.
 */
public class WxMessageStyle implements Serializable{
    private String color;
    private String jumpUrl;
    private String templateId;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
