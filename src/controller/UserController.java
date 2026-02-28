package controller;

import model.User;
import service.UserService;

public class UserController {

    private UserService service = new UserService();

    public User login(String username, String password) throws Exception {
        return service.login(username, password);
    }
}