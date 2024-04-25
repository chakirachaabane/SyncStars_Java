package Services;

import models.User;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    private Connection connection;

    public UserService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(User user) throws SQLException {
        String sql = "INSERT INTO user (cin, first_name, last_name, gender, dob,roles,role,email,password,tel,address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getCin());
        preparedStatement.setString(2, user.getFirst_name());
        preparedStatement.setString(3, user.getLast_name());
        preparedStatement.setString(4, user.getGender());
        preparedStatement.setString(6, user.getRoles());
        preparedStatement.setString(7, user.getRole());
        preparedStatement.setDate(5, new java.sql.Date(user.getDob().getTime())); // Convert java.util.Date to java.sql.Date
        preparedStatement.setString(8, user.getEmail());
        preparedStatement.setString(9, user.getPassword());
        preparedStatement.setInt(10, user.getTel());
        preparedStatement.setString(11, user.getAddress());
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "update user set cin = ?, first_name = ?, last_name = ?, gender = ?, Dob = ?, email = ?, tel = ?, address = ?, password = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(2, user.getCin());
        preparedStatement.setString(3, user.getFirst_name());
        preparedStatement.setString(4, user.getLast_name());
        preparedStatement.setString(5, user.getGender());
        preparedStatement.setDate(6, new java.sql.Date(user.getDob().getTime())); // Convert java.util.Date to java.sql.Date
        preparedStatement.setString(7, user.getEmail());
        preparedStatement.setInt(9, user.getTel());
        preparedStatement.setString(9, user.getAddress());
        preparedStatement.setString(8, user.getPassword());
        preparedStatement.setInt(1, user.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from user where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<User> getAll() throws SQLException {
        String sql = "select * from user";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setCin(rs.getInt("Cin"));
            u.setFirst_name(rs.getString("first_name"));
            u.setLast_name(rs.getString("last_name"));
            u.setGender(rs.getString("gender"));
            u.setDob(rs.getDate("gender"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("password"));
            u.setTel(rs.getInt("Tel"));
            u.setAddress(rs.getString("address"));

            users.add(u);
        }
        return users;
    }

    @Override
    public User getById(int idUser) throws SQLException {
        String sql = "SELECT * FROM `user` WHERE `id` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idUser);
        ResultSet resultSet = preparedStatement.executeQuery();

        User user = null; // Initialize user to null

        if (resultSet.next()) {
            user = new User();
            user.setId(resultSet.getInt("id"));
            user.setCin(resultSet.getInt("Cin"));
            user.setFirst_name(resultSet.getString("first_name"));
            user.setLast_name(resultSet.getString("last_name"));
            user.setGender(resultSet.getString("gender"));
            user.setDob(resultSet.getDate("dob")); // Fixed the column name
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setTel(resultSet.getInt("Tel")); // Fixed the column name
            user.setAddress(resultSet.getString("address"));
        } else {
            // Handle the case when no matching record is found
            return null;
        }

        return user; // Return the user object
    }

}