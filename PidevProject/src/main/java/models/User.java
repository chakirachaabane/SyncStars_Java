package models;

import java.util.Date;

public class User {
    private int id;
    private int cin;
    private String first_name;
    private String last_name;
    private String gender;
    private Date Dob;
    private String roles ;
    private String role ;
    private String email ;
    private String password ;
    private int tel ;
    private String address ;

    //----------------------------------Getters | Setters--------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return Dob;
    }

    public void setDob(Date dob) {
        Dob = dob;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User(int id, int cin, String first_name, String last_name, String gender, Date dob, String roles, String role, String email, String password, int tel, String address) {
        this.id = id;
        this.cin = cin;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.Dob = dob;
        this.roles = roles;
        this.role = role;
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.address = address;
    }

    public User() {
    }

    //----------------------------------To String--------------------------------

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", cin=" + cin +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", gender='" + gender + '\'' +
                ", Dob=" + Dob +
                ", roles='" + roles + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", tel=" + tel +
                ", address='" + address + '\'' +
                '}';
    }
}
