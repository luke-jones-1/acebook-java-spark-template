package models;


import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createPost(String title, String content);
    List getAllPosts();
    void addLike(String id);
    String gettingComments(UUID post_id);
    String gettingPost(String title);
    void postComment(String comment, String post_id);
    List getAllComments();
    void deleteComment(String comment_id);
    void deletePost(String post_id);
    boolean doesEmailExist(String email);
}


