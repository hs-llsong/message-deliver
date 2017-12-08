package hs.ontheroadstore.message.deliver.handle;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;


/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/24.
 */
public class RedisCacheHandlerImpl implements JsonCacheHandler{
    private JedisPoolHandler jedisPoolHandler;
    private String cacheKey;
    private static final Logger logger = Logger.getLogger(RedisCacheHandlerImpl.class);
    public RedisCacheHandlerImpl(JedisPoolHandler poolHandler,String cacheKey) {
        this.jedisPoolHandler = poolHandler;
        this.cacheKey = cacheKey;
    }

    @Override
    public String poll() {
        Jedis jedis = jedisPoolHandler.getResource();
        if (jedis == null){
            logger.error("Get jedis null from pool.");
            return null;
        }
        try {
            String result = jedis.lpop(cacheKey);
            jedis.close();
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
    @Override
    public boolean add(String jsonMessage) {
        Jedis jedis = jedisPoolHandler.getResource();
        if (jedis == null) return false;
        try {
            long result = jedis.rpush(cacheKey,jsonMessage);
            jedis.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

}
