package hs.ontheroadstore.message.deliver.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/23.
 */
public class WeixinMessageTemplate implements Serializable {
    @SerializedName("touser") private String toUser;
    @SerializedName("template_id") private String templateId;
    private String url;
    //private String topColor;
    @SerializedName("miniprogram") MiniProgram miniProgram;
    private Map<String,ObjData> data = new HashMap<>();
    class MiniProgram {
        String appid;
        String pagepath;
        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPagepath() {
            return pagepath;
        }

        public void setPagepath(String pagepath) {
            this.pagepath = pagepath;
        }
    }
    class ObjData {
        private String value;
        private String color;
        public ObjData(String value, String color) {
            this.value = value;
            this.color = color;
        }
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public void addElement(String name,String value,String color) {
        this.data.put(name,new ObjData(value,color));
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, ObjData> getData() {
        return data;
    }

    public void setData(Map<String, ObjData> data) {
        this.data = data;
    }
}
