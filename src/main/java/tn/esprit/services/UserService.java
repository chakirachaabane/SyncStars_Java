package tn.esprit.services;


import at.favre.lib.crypto.bcrypt.BCrypt;
import tn.esprit.models.User;
import tn.esprit.utils.MyDatabase;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserService {

    static Connection conn = MyDatabase.getInstance().getConnection();
    PreparedStatement pst;


    public UserService() {
    }

    public void addUser(User u){

        String sql = "insert into user (cin,first_name,last_name,gender,dob,roles,role,email,password,tel,address,image) values (? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1,u.getCin());
            pst.setString(2,u.getFirstname());
            pst.setString(3,u.getLastname());
            pst.setString(4,u.getGender());
            pst.setDate(5,Date.valueOf(u.getBirth_date()));
            if (u.getRole().equals("Client")) {
                pst.setString(6, "[\"ROLE_CLIENT\"]");
                pst.setString(7, "Client");
            }
            else {
                pst.setString(6, "[\"ROLE_ADMIN\"]");
                pst.setString(7, "Admin");
            }
            pst.setString(8,u.getEmail());
            pst.setString(9,u.getPwd());
            pst.setInt(10,u.getPhone_number());
            pst.setString(11,u.getAddress());
            pst.setString(12,u.getImage());
            pst.executeUpdate();
            System.out.println("success!!");

        } catch (SQLException ex) {
            System.err.println("error!!");
        }
    }

    public static ArrayList<User> displayUsers() {

        ArrayList<User> list = new ArrayList<>();
        ResultSet res;

        try {
            Statement statement= conn.createStatement();
            res= statement.executeQuery("SELECT * FROM user");
            while (res.next()) {
                LocalDate D = res.getDate(6).toLocalDate();
                User data = new User(
                        res.getInt(1),
                        res.getInt(2),
                        res.getString(3),
                        res.getString(4),
                        res.getString(5),
                        D,
                        res.getString(7),
                        res.getString(8),
                        res.getString(9),
                        res.getString(10),
                        res.getInt(11),
                        res.getString(12),
                        res.getString(13)
                );
                list.add(data);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void deleteUser(int id) {

        Statement statement;

        try {
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM user WHERE id =" + id);

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public void modifyUser(User u){

        String sql = "update user set cin = ?, first_name = ?, last_name = ?, gender = ?, dob = ?, tel=?, address=?, image=? where id =?";

        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1,u.getCin());
            pst.setString(2,u.getFirstname());
            pst.setString(3,u.getLastname());
            pst.setString(4,u.getGender());
            pst.setDate(5,Date.valueOf(u.getBirth_date()));
            pst.setInt(6,u.getPhone_number());
            pst.setString(7,u.getAddress());
            pst.setString(8,u.getImage());
            pst.setInt(9,u.getId());
            pst.executeUpdate();
            System.out.println("success!!");

        } catch (SQLException ex) {
            System.err.println("error!!");

        }

    }

    public User getUserData(String email) throws NoSuchAlgorithmException {
        User data = null;
        ResultSet res = null;
        String sql = "SELECT id,cin,first_name,last_name,gender,dob,roles,role,email,password,tel,address,image FROM user WHERE email=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            res = stmt.executeQuery();
            if (res.next()) {

                    LocalDate D = res.getDate(6).toLocalDate();
                    data = new User(
                            res.getInt(1),
                            res.getInt(2),
                            res.getString(3),
                            res.getString(4),
                            res.getString(5),
                            D,
                            res.getString(7),
                            res.getString(8),
                            res.getString(9),
                            res.getString(10),
                            res.getInt(11),
                            res.getString(12),
                            res.getString(13)
                    );

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    public static String hashPassword(String password) {
        String hashedPassword = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(6, password.toCharArray());
        return hashedPassword;
    }
}

