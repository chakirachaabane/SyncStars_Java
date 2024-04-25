package models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Field cannot be blank")
    @Size(min = 1, message = "Field must have at least 1 character")
    private String Title;
    @NotNull
    private int UserId;
    @NotBlank(message = "Field cannot be blank")
    @Size(min = 1, message = "Field must have at least 1 character")
    private String Content;
    @Column(nullable = true)
    private String Attachment;
    private LocalDateTime D_Post= LocalDateTime.now();
    private int Nbr_Likes = 0;
    private int Nbr_DisLikes = 0;
    private List<Response> responses; // List to hold responses


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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }

    public LocalDateTime getD_Post() {
        return D_Post;
    }

    public void setD_Post(LocalDateTime d_Post) {
        D_Post = d_Post;
    }

    public int getNbr_Likes() {
        return Nbr_Likes;
    }

    public void setNbr_Likes(int nbr_Likes) {
        Nbr_Likes = nbr_Likes;
    }

    public int getNbr_DisLikes() {
        return Nbr_DisLikes;
    }

    public void setNbr_DisLikes(int nbr_DisLikes) {
        Nbr_DisLikes = nbr_DisLikes;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void addResponse(Response response) {
        responses.add(response);
    }

    public Question(String title, int userId, String content, String attachment) {
        this.Title = title;
        this.UserId = userId;
        this.Content = content;
        this.Attachment = attachment;
        this.responses = new ArrayList<>();
    }

    public Question() {
    }
    //----------------------------------To String--------------------------------


    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", Title='" + Title + '\'' +
                ", UserId=" + UserId +
                ", Content='" + Content + '\'' +
                ", Attachment='" + Attachment + '\'' +
                ", D_Post=" + D_Post +
                ", Nbr_Likes=" + Nbr_Likes +
                ", Nbr_DisLikes=" + Nbr_DisLikes +
                '}';
    }
}
