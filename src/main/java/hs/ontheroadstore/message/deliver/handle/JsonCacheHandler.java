package hs.ontheroadstore.message.deliver.handle;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/24.
 */
public interface JsonCacheHandler {
    String poll();
    boolean add(String jsonMessage);
}
