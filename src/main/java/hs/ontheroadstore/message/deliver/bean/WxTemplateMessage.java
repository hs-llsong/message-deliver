package hs.ontheroadstore.message.deliver.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/21.
 */
public class WxTemplateMessage implements Serializable {
    private String toUser;
    private String styleName;
    private String jumpUrl;
    private List<Map<String,String>> msgData;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public List<Map<String, String>> getMsgData() {
        return msgData;
    }

    public void setMsgData(List<Map<String, String>> msgData) {
        this.msgData = msgData;
    }
}
