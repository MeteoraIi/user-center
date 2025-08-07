package com.yun.usercenterserver.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体，接收请求体中的JSON
 * Json.userRegisterRequest -> Class.userRegisterRequest
 * @author yun
 */

// 接收前端发来的 JSON
// Serializable 是 Java 提供的一个标记接口，用于指示某个类的对象可以被序列化，方便传输
@Data
// lombok 的注解，自动生成 getter、setter、toString、equals、hashCode 方法以及无参构造函数
public class UserRegisterRequest implements Serializable {

    // Serializable 是一个标记接口，唯一作用是给 JVM 一个 “标记”，告诉 JVM：“这个类的对象可以被序列化”，
    // 它内部没有定义任何方法，因此实现类无需重写任何方法，
    // 当一个类实现了 Serializable 接口，对象在被序列化时，
    // Java 虚拟机会根据类的结构信息（如字段类型、数量，方法等）
    // 自动生成一个 serialVersionUID 值（如果类中没有显式定义的话），并将其与对象的字节流一起保存。
    // 而在反序列化时，JVM 会再次根据当前类的结构信息生成一个 serialVersionUID，
    // 然后将这个新生成的值与字节流中保存的 serialVersionUID 进行比较。
    // 版本控制，识别 类 的版本
    private static final long serialVersionUID = 1L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
