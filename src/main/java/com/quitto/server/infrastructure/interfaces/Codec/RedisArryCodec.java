package com.quitto.server.infrastructure.interfaces.Codec;

import io.lettuce.core.codec.RedisCodec;

import java.nio.ByteBuffer;

public interface RedisArryCodec extends RedisCodec<String, byte[]>{
    public String decodeKey(ByteBuffer bytes);
    public byte[] decodeValue(ByteBuffer bytes);
    public ByteBuffer encodeKey(String key);
    public ByteBuffer encodeValue(byte[] value);

}
