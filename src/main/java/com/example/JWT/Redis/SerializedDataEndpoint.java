package com.example.JWT.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Endpoint(id = "serialized-data")
public class SerializedDataEndpoint {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @ReadOperation
    public Object retrieveSerializedData() {
        String redisKey = "7"; // Replace with the actual Redis key
        Object serializedData = redisTemplate.opsForValue().get(redisKey);

        // Deserialize the data if it's Serializable
        if (serializedData instanceof Serializable) {
            return serializedData;
        } else {
            return "Data is not serializable or not found.";
        }
    }
}
