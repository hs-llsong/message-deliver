package hs.ontheroadstore.message.deliver;

import hs.ontheroadstore.message.deliver.handle.HandleManager;

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
}
