package com.rpc.example.serial;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName SerializerManager.java
 * @Description 序列化管理 类似工厂策略
 * @createTime 2022年04月23日 20:44:00
 */
public class SerializerManager {

    private final static ConcurrentHashMap<Byte, ISerializer> serializer = new ConcurrentHashMap<>();

    static {
        ISerializer json = new JsonSerializer();
        ISerializer java = new JavaSerializer();
        serializer.put(json.getType(), json);
        serializer.put(java.getType(), java);
    }

    public static ISerializer getSerializer(byte key) {
        ISerializer iSerializer = serializer.get(key);
        if (serializer == null) {
            return new JavaSerializer();
        }
        return iSerializer;
    }
}
