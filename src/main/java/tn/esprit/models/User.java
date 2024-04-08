package tn.esprit.models;


import java.time.LocalDate;


public class User {

    String firstname,lastname,email,pwd,address,gender;
    String role,roles;
    int id,cin,phone_number;
    LocalDate birth_date;
    String image;


    public User() {

    }

    public User(int cin, String firstname, String lastname, String email, String pwd, String address, int phone_number, LocalDate birth_date, String gender, String role, String image) {

        this.cin = cin;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.pwd = pwd;
        this.address = address;
        this.phone_number = phone_number;
        this.birth_date = birth_date;
        this.gender = gender;
        this.role = role;
        this.image = image;
    }

    public User(int id,int cin,String firstname, String lastname,String gender,LocalDate birth_date, String roles,  String role,  String email, String pwd,int phone_number, String address,  String image) {
        this.id = id;
        this.cin = cin;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.birth_date = birth_date;
        this.roles=roles;
        this.role=role;
        this.email = email;
        this.pwd = pwd;
        this.phone_number = phone_number;
        this.address = address;
        this.image = image;
    }

    public User(int id,int cin,String firstName, String lastName,LocalDate birth_date, String address,int phone_number, String gender,String image){
        this.id=id;
        this.cin = cin;
        this.firstname = firstName;
        this.lastname = lastName;
        this.birth_date=birth_date;
        this.address = address;
        this.phone_number = phone_number;
        this.gender = gender;
        this.image = image;
    }



    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

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

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "users{" + "firstname=" + firstname + ", lastname=" + lastname + ", email=" + email + ", pwd=" + pwd + ", address=" + address + ", gender=" + gender + ", role=" + role + ", id=" + id + ", cin=" + cin + ", phone_number=" + phone_number + ", birth_date=" + birth_date +" ,image=" + image  + '}';
    }
}
