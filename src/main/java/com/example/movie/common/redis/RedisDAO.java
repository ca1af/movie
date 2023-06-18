package com.example.movie.common.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Objects;

@Repository
public class RedisDAO {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisDAO(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void setValues(String key, Object data, Duration duration) throws JsonProcessingException {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String serializedData = objectMapper.writeValueAsString(data); // 객체를 JSON 문자열로 직렬화
        values.set(key, serializedData, duration);
    }

    public <T> T getValues(String key, TypeReference<T> typeReference) throws JsonProcessingException {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String serializedData = values.get(key);
        if (serializedData != null) {
            return objectMapper.readValue(serializedData, typeReference); // JSON 문자열을 객체로 역직렬화
        }
        return null;
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void flushAll() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushAll();
    }
}
