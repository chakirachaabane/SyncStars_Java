package tn.esprit.models;

import tn.esprit.services.UserService;

public class Data {
    static UserService userService = new UserService();

    public static User user;
    public static final String username = "alignvibe2024@gmail.com";
    public static final String password = "mccaoobddhibigca";
    public static String currentUserMail = userService.getEmailsFromFile();
}
