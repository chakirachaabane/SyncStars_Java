package com.example.pidevproject;

import Services.QuestionService;
import Services.ResponseService;
import Services.UserService;
import models.Question;
import models.Response;
import models.User;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        UserService US = new UserService();
        QuestionService QS = new QuestionService();
        ResponseService RS = new ResponseService();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dob = dateFormat.parse("10/03/2001");
            User U1 = new User(1, 32165412, "ahmed", "ahmed1", "man", dob, "[\"ROLE_CLIENT\"]", "Client", "ahmed@gmail.com", "azdkozd256314",32132112,"adasdasdasda");

            // Use U1 as needed

            /*try{
                US.add(U1);
                System.out.println("New Client ajouté"+U1.getFirst_name());
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }*/

            //-------------------New Question --------------------

            /*try{
                QS.add(new Question("aziz2",5,"aziz2","aziz.jpg"));
                System.out.println("New question is Created !");
            }catch(SQLException e){
                System.err.println(e.getMessage());
            }*/

            //--------------------Update Question--------------------------------

            /*try {
                QS.update(new Question(23,"aziz5",5,"aziz5","aziz.png",0,0));
            }
            catch (SQLException e){
                System.err.println(e.getMessage());
            }*/

            //--------------------Delete Question--------------------------------

            /*try {
                QS.delete(24);
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }*/

            //-------------------- get all questions--------------------------------

            /*try {
                System.out.println(QS.getAll());
            }catch(SQLException e){
                System.err.println(e.getMessage());
            }*/

            //--------------------- Get Question By Id ------------------------------

            /*try {
                System.out.println(QS.getById(23));
            }catch(SQLException e){
                System.err.println(e.getMessage());
            }*/

            //----------------------add a Like ----------------------------------

            /*try{
                QS.addLike(23);
                System.out.println("Like Added");
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }*/

            //--------------------- add a Dislike --------------------------------

            /*try{
                QS.dislike(23);
                System.out.println("DisLike Added");
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }*/

            //--------------------- New Response ------------------------------

            /*try {
                RS.add(new Response("Hey222",5,23));
                System.out.println("New Response Added");
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }*/

            //------------------------- Update response ----------------------------

            /*try{
                RS.update(new Response(6,"aazeazeae",5,23));
                System.out.println("Response Updated");
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }*/

            //-------------------------- Delete Response ---------------------------

            /*try{
                RS.delete(6);
                System.out.println("Response delete");
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }*/

            //------------------------ List all responses --------------------------

            /*try{
                System.out.println(RS.getAll());
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }*/

            //----------------------- Get Response By Id ------------------------------

            /*try{
                System.out.println(RS.getById(8));
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }*/

            //-------------------------------------------------------------------------
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parse exception
        }
    }
}