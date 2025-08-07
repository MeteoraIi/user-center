package com.yun.usercenterserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.usercenterserver.common.ErrorCode;
import com.yun.usercenterserver.exception.BusinessException;
import com.yun.usercenterserver.model.domain.User;
import com.yun.usercenterserver.service.UserService;
import com.yun.usercenterserver.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yun.usercenterserver.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 *
* @author kabuto
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-07-27 16:01:45
*/
// 这个注解是 IOC 机制 ↓
// ** 开发者只需要编写类和方法（定义 “做什么”），而对象的创建、依赖关系的维护等 “怎么实例化” 的逻辑，全部交给 Spring 容器来完成
@Service
@Slf4j
// 它通过自动生成 SLF4J（Simple Logging Facade for Java）日志对象，
// 让开发者无需手动编写获取日志记录器的样板代码，只需添加注解，无需手动声明日志记录器。
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    // 访问数据库有两种方法，用mapper接口，用service实例
    // Service 层的方法确实是对 Mapper 层方法的封装与扩展
    // 注入mapper，用mapper来访问数据库
    // mapper访问适合自定义方法
    @Resource
    private UserMapper originUser;

    /**
     *  盐值，混淆密码
     */
    private static final String SALT = "yun";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1.校验
        // 利用 apache.commons.lang3.StringUtils 判断多个字符串是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "有参数为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号位数小于4");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码位数小于8");
        }
        // 校验火葬场编号
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "火葬编号大于5");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        // 将模式串编译为 Pattern 对象
        // .matcher 创建匹配器(userAccount)，应用在 userAccount 上
        // 在 userAccount 中查找任意位置是否存在与正则表达式匹配的字符
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不允许异常符号");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }
        // 账户不能重复
        /* 使用 MyBatis-Plus 的 QueryWrapper 构建查询条件，泛型指定查询的实体类为 User，对应数据库中的 user 表
         * eq 为 WHERE userAccount =
         * this.count(queryWrapper) 等价于执行 SQL SELECT COUNT(*) FROM user WHERE user_account = 'xxx'
         * this 代指当前 Service 类的实例，常是实现了 IService<User> 接口的 Service 类 UserServiceImpl
         * MyBatis-Plus 的 IService 接口提供了 count(Wrapper<T> queryWrapper) 方法，用于统计符合条件的记录数
         * */
        QueryWrapper<User> queryWrapperAccount = new QueryWrapper<>();
        queryWrapperAccount.eq("userAccount", userAccount);
        // long count = this.count(queryWrapper); 统计符合条件的记录数
        long count1 = originUser.selectCount(queryWrapperAccount);
        if (count1 > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户重复");
        }
        // 火葬场编号不能重复
        QueryWrapper<User> queryWrapperPlanetCode = new QueryWrapper<>();
        queryWrapperPlanetCode.eq("planetCode", planetCode);
        long count2 = this.count(queryWrapperPlanetCode);
        if (count2 > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "火葬编号重复");
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3，插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        // 提前防止拆箱错误
        if (!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据保存失败");
        }

        return user.getId(); // 返回的是Long包装类，方法返回值是long，如果返回的是null，会发生拆箱错误。
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // HttpServletRequest 用于封装客户端（通常是浏览器）发送给服务器的 HTTP 请求信息，可以看作就是 HTTP 报文
        // 该业务逻辑需要修改 HTTP 报文头部，所以需要这个参数
        // 1.校验，节省一次数据库查询
        log.info("当前JVM file.encoding: {}", System.getProperty("file.encoding"));
        log.info("当前JVM sun.jnu.encoding: {}", System.getProperty("sun.jnu.encoding"));
        log.info("曹寺你" + request.getSession().toString());
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "有参数为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户小于4");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于4");
        }
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户包含非法字符");
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3.查询用户是存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        // 查出来一条数据
        User user = originUser.selectOne(queryWrapper);
        // 没注册？ 密码错误？ 账户错误？
        if (user == null){
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NO_MATCH_ERROR, "账户密码不匹配");
        }
        // 4.用户脱敏
        User safetyUser = getSafetyUser(user);
        // 3.记录用户的登录态
        // 当用户首次访问服务器时，服务器会创建一个唯一的 Session 对象（包含一个唯一的 sessionId），
        // 并通过 Cookie 把 sessionId 返回给浏览器，
        // .getSession()获取当前会话，setAttribute(USER_LOGIN_STATE, safetyUser)，设置用户登陆信息，里面是个Map
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态，操作 Session 就像操作 map
        // 将会话中的登录态移除
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarURL(originUser.getAvatarURL());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        return  safetyUser;
    }
}




