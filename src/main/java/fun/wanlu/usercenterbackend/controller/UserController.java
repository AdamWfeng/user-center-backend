package fun.wanlu.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.wanlu.usercenterbackend.common.BaseResponse;
import fun.wanlu.usercenterbackend.common.ErrorCode;
import fun.wanlu.usercenterbackend.exception.BusinessException;
import fun.wanlu.usercenterbackend.utils.ResultUtils;
import fun.wanlu.usercenterbackend.model.entity.User;
import fun.wanlu.usercenterbackend.model.dto.user.UserLoginRequest;
import fun.wanlu.usercenterbackend.model.dto.user.UserRegisterRequest;
import fun.wanlu.usercenterbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fun.wanlu.usercenterbackend.constant.UserConstant.ADMIN_ROLE;
import static fun.wanlu.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            return ResultUtils.success(null);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.success(null);
        }
        User user = userService.userLogin(userAccount, userPassword, httpServletRequest);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return ResultUtils.success(0);
        }
        return ResultUtils.success(userService.userLogout(httpServletRequest));
    }

    @GetMapping("/current")
    public BaseResponse<User> GetCurrent(HttpServletRequest httpServletRequest) {
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        long userId = currentUser.getId();
        User user = userService.getById(userId);
        return ResultUtils.success(user);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest httpServletRequest) {
        if (!isAdmin(httpServletRequest)) {
            return ResultUtils.success(new ArrayList<>());
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("userName", userName);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> collect = userList.stream().map(
                user -> userService.getSafetyUser(user)
        ).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id, HttpServletRequest httpServletRequest) {
        if (!isAdmin(httpServletRequest)) {
            return ResultUtils.success(false);
        }
        if (id <= 0) {
            return ResultUtils.success(false);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    private boolean isAdmin(HttpServletRequest httpServletRequest) {
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }
}
