package hs.ontheroadstore.message.deliver.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/23.
 */
public class WeixinMessageTemplate implements Serializable {

    private String toUser;
    private String templateId;
    private String url;
    private String topColor;
    private List<Element> datas = new ArrayList<>();

    class Element {
        private String name;
        private String value;
        private String color;

        public Element(String name, String value, String color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
        this.datas.add(new Element(name,value,color));
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

    public String getTopColor() {
        return topColor;
    }

    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }

    public List<Element> getDatas() {
        return datas;
    }

    public void setDatas(List<Element> datas) {
        this.datas = datas;
    }
}
