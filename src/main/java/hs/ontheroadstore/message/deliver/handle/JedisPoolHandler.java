package hs.ontheroadstore.message.deliver.handle;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/24.
 */
public class JedisPoolHandler {
    private JedisPool jedisPool;
    private String host;
    private int port;
    private String auth;
    private int timeout;
    private final int MAX_IDLE = 5;
    private final int MAX_TOTAL = 18;
    private final Logger logger = Logger.getLogger(JedisPoolHandler.class);
    public JedisPoolHandler(String host,int port,String auth,int timeout) {
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.timeout = timeout;
        createPool();
    }

    public void createPool() {
        jedisPool = null;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(MAX_IDLE);
        config.setMaxTotal(MAX_TOTAL);
        jedisPool = new JedisPool(config,host,port,timeout,auth);
    }

    public void close() {
        try {
            jedisPool.close();
        } catch (JedisException e) {
            logger.error(e.getMessage());
        }
    }

    public Jedis getResource() {
        try {
            Jedis jedis = jedisPool.getResource();
            return jedis;
        } catch (JedisConnectionException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

}
