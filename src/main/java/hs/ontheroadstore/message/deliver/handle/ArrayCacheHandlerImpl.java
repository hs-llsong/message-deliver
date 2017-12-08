package hs.ontheroadstore.message.deliver.handle;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/24.
 */
public class ArrayCacheHandlerImpl implements JsonCacheHandler{
    private Queue<String> queue = new LinkedList<>();
    @Override
    public String poll() {
        return queue.poll();
    }

    @Override
    public boolean add(String jsonMessage) {
        return queue.add(jsonMessage);
    }
}
