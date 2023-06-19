package com.example.movie.common.redis;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Repository
public class RedisDAO {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisDAO(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> void addListValues(String key, List<T> data, Duration duration) throws JsonProcessingException {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        List<String> serializedList = new ArrayList<>(data.size());

        for (T datum : data) {
            String serializedDatum = objectMapper.writeValueAsString(datum);
            serializedList.add(serializedDatum);
        }

        listOperations.rightPushAll(key, serializedList.toArray(new String[0]));
        redisTemplate.expire(key, duration);
    }

    public <T> List<T> getListValues(String key, Class<T> elementType) throws JsonProcessingException {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        List<String> serializedData = listOperations.range(key, 0, -1);

        if (serializedData != null && !serializedData.isEmpty()) {
            List<T> deserializedData = new ArrayList<>();

            for (String serializedDatum : serializedData) {
                T deserializedDatum = objectMapper.readValue(serializedDatum, elementType);
                deserializedData.add(deserializedDatum);
            }

            return deserializedData;
        }

        throw new NoSuchElementException("키와 대응하는 값이 없습니다");
    }

    public void setValues(String key, Object data, Duration duration) throws JsonProcessingException {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String serializedData = objectMapper.writeValueAsString(data); // 객체를 JSON 문자열로 직렬화
        values.set(key, serializedData, duration);
    }

    public <T> T getValues(String key, Class<T> valueType) throws JsonProcessingException {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String serializedData = values.get(key);
        if (serializedData != null) {
            return objectMapper.readValue(serializedData, valueType); // JSON 문자열을 객체로 역직렬화
        }
        throw new NoSuchElementException("키와 대응하는 값이 없습니다");
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void flushAll() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushAll();
    }
}
