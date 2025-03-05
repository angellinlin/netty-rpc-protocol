package com.rpc.example;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName MainTest.java
 * @Description TODO
 * @createTime 2022年04月23日 22:04:00
 */
public class MainTest {

    public static void main(String[] args) {
        RpcClientProxy rcp = new RpcClientProxy();
        IUserService userService = rcp.clientProxy(IUserService.class, "127.0.0.1", 8081);
        System.out.println(userService.saveUser("cc "));
    }
}
