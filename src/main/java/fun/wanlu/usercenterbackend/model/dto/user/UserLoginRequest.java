package fun.wanlu.usercenterbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 8434791290840873837L;

    private String userAccount;

    private String userPassword;
}
