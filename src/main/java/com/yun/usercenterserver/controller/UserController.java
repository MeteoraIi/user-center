package com.yun.usercenterserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yun.usercenterserver.common.BaseResponse;
import com.yun.usercenterserver.common.ErrorCode;
import com.yun.usercenterserver.exception.BusinessException;
import com.yun.usercenterserver.model.domain.User;
import com.yun.usercenterserver.model.request.UserLoginRequest;
import com.yun.usercenterserver.model.request.UserRegisterRequest;
import com.yun.usercenterserver.service.UserService;
import com.yun.usercenterserver.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yun.usercenterserver.constant.UserConstant.ADMIN_ROLE;
import static com.yun.usercenterserver.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author yun
 */
@RestController
// 将返回值序列化为 JSON 并写入响应体，标记这里是一个横切关注点 （AOP）
// 用于构建基于 RESTful 风格的 Web 服务，默认返回前端的值为JSON
@RequestMapping("/user")
// 将 HTTP 请求映射到对应的处理方法上，类似 go 中的路由机制
// 标注在类上：定义了该控制器类中所有请求处理方法的公共路径前缀。
// 例如，意味着这个类里的方法处理的请求 URL 都会以/user开头。
// 标注在方法上：进一步细化请求映射，指定具体的请求路径、请求方法（GET、POST 等）、请求参数等条件，
// 只有符合这些条件的请求才会被映射到该方法进行处理。
@Slf4j
public class UserController {

    // controller 层倾向于对参数本身的校验，不涉及业务逻辑本身（越少越好）
    @Resource
    private UserService userService;

    @PostMapping("/register")
    // 仅匹配 Post 请求，发送给后端的信息
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // @RequestBody 将 HTTP 请求体中的 JSON 数据自动转换为 Java 对象，绑定到方法参数上
        // JSON → Java 类的转换过程就是反序列化，并且这个过程主要由 Spring MVC 框架 + Jackson 库 共同完成
        if (userRegisterRequest == null) {
            // return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            // 遇到异常不会再往下执行了，转而由全局异常处理器接手并返回给前端，面向AOP
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "请求数据体为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        // 提前检测是否为 null，减少进入业务层的次数
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "有参数为空");
        }
        // 返回前端，由于采用 @RestController，以 JSON 格式返回前端
        // todo 注册失败友好的提示
        Long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        // 不管返回的是啥，转换成 JSON 后，只要前后端字段名一致，就可以通过 JSON 中的键名获取数据
        // 由于这里多封装了一层data，前端也要改相应的结构
        return ResultUtils.sucess(result);
    }

    @PostMapping("/login")
    // 由于要设置 session 的字段，所以需要将参数 HttpServletRequest request 传进后端
    // 请求体中的数据传入后端，是前端干的 => UserLoginRequest userLoginRequest
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // HttpServletRequest request 怎么接收这个参数？ => 由 MVC 框架直接注入
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "请求数据体为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "有数据为空");
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.sucess(result);
    }

    @PostMapping("logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "HTTP报文为空");
        }
        int result = userService.userLogout(request);
        return ResultUtils.sucess(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User)userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        // 如果用户的信息频繁变化，那么返回当前用户就最好是从数据库实时取出
        long id = currentUser.getId();
        // todo校验用户是否合法
        User result = userService.getSafetyUser(userService.getById(id));
        return ResultUtils.sucess(result);
    }

    @GetMapping("/search")
    // 在 controller 层就访问了数据库?
    // GET 请求：参数只能放在URL 的查询字符串中（即?key=value&key2=value2），无法携带请求体
    // 当前端通过GET 请求传递 username 时，参数会被拼接在 URL 的查询字符串中（?后面的部分），
    // 此时后端 username 参数的值就是从 URL 中解析而来
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        // 鉴权，仅管理员可查
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(username)) {
            queryWrapper.like("username", username);
        }
        // 这里如果 qureyWrapper 没加上任何条件，也就是说 sql 语句没有 where
        // 会查出数据库中所有记录
        List<User> userList = userService.list(queryWrapper);
        // 将列表转换为 Stream
        // map 方法：对 Stream 中的每个 User 对象执行转换操作。
        // 将 Stream 元素收集回 List。
        List<User> result =
                userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.sucess(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        // 鉴权，仅管理员可删
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id为空");
        }
        // mybatis-plus 的 IService 接口也实现了对数据库表的一些操作，这里直接调用
        boolean result = userService.removeById(id);
        return ResultUtils.sucess(result);
    }

    /**
     *
     * @param request 查看 http 中 session 的信息，查看用户是否是管理员权限
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 鉴权，仅管理员可查
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj; // 类型转换
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }

}
