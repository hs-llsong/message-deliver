package hs.ontheroadstore.message.deliver.bean;

import java.io.Serializable;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/29.
 */
public class HsAliOnsPushMessage implements Serializable{
    private String key;
    private String tag;
    private String body;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
