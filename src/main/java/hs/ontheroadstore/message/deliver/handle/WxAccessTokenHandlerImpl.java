package hs.ontheroadstore.message.deliver.handle;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/12/6.
 */
public class WxAccessTokenHandlerImpl implements WxTokenHandler {
    private JedisPoolHandler jedisPoolHandler;
    private String redisTokenKey;
    private final Logger logger = Logger.getLogger(WxAccessTokenHandlerImpl.class);
    public WxAccessTokenHandlerImpl(JedisPoolHandler jedisPoolHandler, String redisTokenKey) {
        this.jedisPoolHandler = jedisPoolHandler;
        this.redisTokenKey = redisTokenKey;
    }

    @Override
    public String getAccessToken() {
        Jedis jedis = jedisPoolHandler.getResource();
        if (jedis != null) {
            try {
                String result = jedis.get(redisTokenKey);
                return result;
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }
        return null;
    }
}
