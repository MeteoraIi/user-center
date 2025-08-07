package com.yun.usercenterserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@SpringBootTest
class UserCenterServerApplicationTests {

    @Test
    /**
     * 1.拼接字符串：将 "abcd" 和 "myPassword" 拼接为 "abcdmyPassword"。
     * 2.转换为字节数组：使用平台默认编码（如 UTF-8）将字符串转为 byte[]。
     * 3.计算 MD5：调用 DigestUtils.md5DigestAsHex() 生成 32 位十六进制字符串。
     */
    void testDigest() throws NoSuchAlgorithmException {
        String newPassword = DigestUtils.md5DigestAsHex(("abcd" + "myPassword").getBytes());
        System.out.println("newPassword:" + newPassword);
    }

    @Test
    void contextLoads() {

    }

}
