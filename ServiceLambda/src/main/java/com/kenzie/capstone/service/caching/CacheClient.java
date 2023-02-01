package com.kenzie.capstone.service.caching;

import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CacheClient {
    private Jedis jedis;

    public void setValue(String key, int seconds, String value) {
        this.jedis = DaggerServiceComponent.create().provideJedis();
        checkNullKey(key);
        jedis.setex(key, seconds, value);
        jedis.close();
    }

    public Optional<String> getValue(String key) {
        this.jedis = DaggerServiceComponent.create().provideJedis();
        checkNullKey(key);
        final Optional<String> value = Optional.ofNullable(jedis.get(key));
        jedis.close();
        return value;
    }

    public Optional<List<String>> getAll() {
        this.jedis = DaggerServiceComponent.create().provideJedis();
        final Set<String> keys = jedis.keys("*");

        try {
            final List<String> values = jedis.mget(keys.toArray(new String[keys.size()]));
            jedis.close();
            return Optional.ofNullable(values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jedis.close();
        return Optional.empty();
    }

    public boolean invalidate(String key) {
        this.jedis = DaggerServiceComponent.create().provideJedis();
        checkNullKey(key);
        boolean bool = jedis.del(key) > 0;
        jedis.close();
        return bool;
    }

    private void checkNullKey(String key){
        if (key == null){
            throw new IllegalArgumentException();
        }
    }
    // Put your Cache Client Here

    // Since Jedis is being used multithreaded, you MUST get a new Jedis instances and close it inside every method.
    // Do NOT use a single instance across multiple of these methods

    // Use Jedis in each method by doing the following:
    // Jedis cache = DaggerServiceComponent.create().provideJedis();
    // ... use the cache
    // cache.close();

    // Remember to check for null keys!
}
