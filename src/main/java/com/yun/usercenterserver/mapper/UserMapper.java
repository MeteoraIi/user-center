package com.yun.usercenterserver.mapper;

import com.yun.usercenterserver.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author kabuto
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-07-27 16:01:45
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {
    // extends BaseMapper 继承之后，自动实现数据库的 CRUD，这是由 MyBatis-plus 实现的

}




