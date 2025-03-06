package com.rpc.example.constant;

/**
 * @author 周贵龙
 * @version 1.0.0
 * @ClassName ReqType.java
 * @Description TODO
 * @createTime 2022年04月23日 20:27:00
 */
public enum SerialType {

    JSON_SERIAL((byte) 1),
    JAVA_SERIAL((byte) 2);


    private byte code;

    SerialType(byte code) {
        this.code = code;
    }

    public byte code() {
        return this.code;
    }
}
