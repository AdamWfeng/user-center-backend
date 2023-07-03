package fun.wanlu.usercenterbackend.service;

import fun.wanlu.usercenterbackend.mapper.UserMapper;
import fun.wanlu.usercenterbackend.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUserName("adam");
        user.setUserAccount("adam");
        user.setAvatarUrl("https://wanlu.fun");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("1234");
        user.setEmail("@qq.com ");

        boolean save = userService.save(user);
        System.out.println(user.getId());
        assertTrue(save);

    }

    @Test
    void userRegister() {
        String userAccount = "adamtest";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        System.out.println(result);
        assertEquals(-1, result);
    }

    @Test
    void userLogin() {

    }
}