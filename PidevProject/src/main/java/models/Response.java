package models;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @NotBlank(message = "Field cannot be blank")
    @Size(min = 1, message = "Field must have at least 1 character")    private String Content;
    @NotNull
    private int UserId ;
    @NotNull
    private int QuestionId ;
    //----------------------------------Getters | Setters--------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Response(String content, int userId, int questionId) {
        this.Content = content;
        this.UserId = userId;
        this.QuestionId = questionId;
    }

    public Response(String content) {
        this.Content = content;
    }


    public Response() {
    }

    //----------------------------------To String--------------------------------


    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", Content='" + Content + '\'' +
                ", UserId=" + UserId +
                ", QuestionId=" + QuestionId +
                '}';
    }
}
