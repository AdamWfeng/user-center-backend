package fun.wanlu.usercenterbackend.model.dto.user;

import lombok.Getter;

import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Getter
public class UserDeleteRequest implements Serializable {

    private static final long serialVersionUID = 8434791290840873837L;

    private Long id;
}
