package tn.esprit.services;

import tn.esprit.models.User;

public interface IUserService  {
    void addUser(User u);
    void modifyUser(User u);
    User getUserData(String email);

}
