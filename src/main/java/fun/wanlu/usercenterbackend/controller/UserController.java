package fun.wanlu.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.wanlu.usercenterbackend.common.BaseResponse;
import fun.wanlu.usercenterbackend.common.ErrorCode;
import fun.wanlu.usercenterbackend.exception.BusinessException;
import fun.wanlu.usercenterbackend.model.dto.user.UserDeleteRequest;
import fun.wanlu.usercenterbackend.model.dto.user.UserLoginRequest;
import fun.wanlu.usercenterbackend.model.dto.user.UserRegisterRequest;
import fun.wanlu.usercenterbackend.model.entity.User;
import fun.wanlu.usercenterbackend.model.vo.UserVo;
import fun.wanlu.usercenterbackend.service.UserService;
import fun.wanlu.usercenterbackend.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    /**
     * 注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 登录
     *
     * @param userLoginRequest
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserVo> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo userVo = userService.userLogin(userAccount, userPassword, httpServletRequest);
        return ResultUtils.success(userVo);
    }

    /**
     * 退出登录
     *
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success(userService.userLogout(httpServletRequest));
    }

    /**
     * 查询当前用户
     *
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<UserVo> GetCurrent(HttpServletRequest httpServletRequest) {
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        UserVo currentUserVo = (UserVo) userObj;
        if (currentUserVo == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUserVo.getId();
        User user = userService.getById(userId);
        UserVo safetyUserVo = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUserVo);
    }

    /**
     * 查询用户
     *
     * @param userName
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<UserVo>> searchUsers(String userName, HttpServletRequest httpServletRequest) {
        if (!isAdmin(httpServletRequest)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("userName", userName);
        }
        List<User> userList = userService.list(queryWrapper);

        List<UserVo> userVoList = userList.stream().map(
                user -> userService.getSafetyUser(user)
        ).collect(Collectors.toList());
        return ResultUtils.success(userVoList);
    }

    /**
     * 删除用户
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsersById(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest httpServletRequest) {
        Long id = userDeleteRequest.getId();
        if (!isAdmin(httpServletRequest)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean userRemove = userService.removeById(id);
        return ResultUtils.success(userRemove);
    }

    /**
     * 判断是否为管理员
     *
     * @param httpServletRequest
     * @return
     */
    private boolean isAdmin(HttpServletRequest httpServletRequest) {
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        UserVo userVo = (UserVo) userObj;
        if (userVo == null || userVo.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }
}
