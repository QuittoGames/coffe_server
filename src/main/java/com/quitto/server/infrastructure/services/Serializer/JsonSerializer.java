package com.quitto.server.infrastructure.services.Serializer;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.domain.interfaces.Serializer.Serializer;

@Service
public class JsonSerializer implements Serializer {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
