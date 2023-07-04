package fun.wanlu.usercenterbackend.model.dto.user;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Getter
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3704776697191296575L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
