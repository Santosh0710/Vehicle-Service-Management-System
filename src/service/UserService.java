package service;

import dao.UserDAO;
import exception.InvalidInputException;
import model.User;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) throws Exception {
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty()) {
            throw new InvalidInputException("Username or Password cannot be empty");
        }

        User user = userDAO.login(username, password);

        if (user == null) {
            throw new InvalidInputException("Invalid Username or Password");
        }

        return user;
    }
}