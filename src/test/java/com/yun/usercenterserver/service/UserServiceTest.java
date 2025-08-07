package com.yun.usercenterserver.service;
import java.util.Date;

import com.yun.usercenterserver.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;



/**
 * 用户服务测试
 *
 * @author yun
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;


    @Test
    public void testAddUser() {
        User user = new User();

        user.setUsername("testyun");
        user.setUserAccount("123456");
        user.setAvatarURL("D:/图片/1347891.jpeg");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123");
        user.setEmail("123");

        /**
         * 启用 mapUnderscoreToCamelCase 后，MyBatis 会自动将下划线命名的列名转换为驼峰命名的属性名，
         * 数据库 → Java：数据库列 user_account → 实体类属性 userAccount；
         * Java → 数据库：实体类属性 userAccount → SQL 中的列 user_account。
         */

        boolean result = userService.save(user);
        System.out.println("userId:" + user.getId());

        Assertions.assertTrue(result);

    }

    @Test
    void userRegister() {
        // 测试按照原来的逻辑走，要能走到每一个分支
        String userAccount = "wuyun";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "1";
        long result = userService.userRegister(userAccount,  userPassword, checkPassword, planetCode);
        Assertions.assertEquals(result, -1);

        userAccount = "yun";
        result = userService.userRegister(userAccount,  userPassword, checkPassword, planetCode);
        Assertions.assertEquals(result, -1);

        userAccount = "wuyun";
        userPassword = "123456";
        result = userService.userRegister(userAccount,  userPassword, checkPassword, planetCode);
        Assertions.assertEquals(result, -1);

        userAccount = "wu yun";
        userPassword = "12345678";
        result = userService.userRegister(userAccount,  userPassword, checkPassword, planetCode);
        Assertions.assertEquals(result, -1);

        checkPassword = "123456789";
        result = userService.userRegister(userAccount,  userPassword, checkPassword, planetCode);
        Assertions.assertEquals(result, -1);

        // 重复
        userAccount = "testyun";
        userPassword = "123456789";
        result = userService.userRegister(userAccount,  userPassword, checkPassword, planetCode);
        Assertions.assertEquals(result, -1);

        userAccount = "wuyun";
        result = userService.userRegister(userAccount,  userPassword, checkPassword, planetCode);
        Assertions.assertTrue(result > 0);
    }
}