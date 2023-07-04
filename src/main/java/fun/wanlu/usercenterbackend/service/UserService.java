package fun.wanlu.usercenterbackend.service;

import fun.wanlu.usercenterbackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.wanlu.usercenterbackend.model.vo.UserVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author Adam
* @description 针对表【user】的数据库操作Service
* @createDate 2023-07-01 15:20:35
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @return
     */
    UserVo userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 注销
     * @return
     */
    Integer userLogout(HttpServletRequest httpServletRequest);

    /**
     * 脱敏
     * @param originUser
     * @return
     */
    UserVo getSafetyUser(User originUser);

}
