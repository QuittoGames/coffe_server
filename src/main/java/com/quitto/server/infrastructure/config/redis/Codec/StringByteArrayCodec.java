package com.quitto.server.infrastructure.config.redis.Codec;

import java.nio.ByteBuffer;

import org.springframework.stereotype.Component;

import com.quitto.server.infrastructure.interfaces.Codec.RedisArryCodec;

import io.lettuce.core.codec.StringCodec;

@Component
public class StringByteArrayCodec implements RedisArryCodec {

    private final StringCodec stringCodec = StringCodec.UTF8;

    public String decodeKey(ByteBuffer bytes) {
        return stringCodec.decodeKey(bytes);
    }

    public byte[] decodeValue(ByteBuffer bytes) {
        byte[] result = new byte[bytes.remaining()];
        bytes.get(result);
        return result;
    }

    public ByteBuffer encodeKey(String key) {
        return stringCodec.encodeKey(key);
    }

    public ByteBuffer encodeValue(byte[] value) {
        return ByteBuffer.wrap(value);
    }
}
