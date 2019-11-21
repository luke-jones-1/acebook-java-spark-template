package models;

import models.Model;
import models.UserModel;
import models.Post;
import models.Sql2oModel;
import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oModelTest {

    Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "acebook_test",
            null, null, new PostgresQuirks() {
        {
            // make sure we use default UUID converter.
            converters.put(UUID.class, new UUIDConverter());
        }
    });

    UUID id = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");
    UUID comment_id = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cc");

    Connection conn = sql2o.open();
    Model model = new Sql2oModel(sql2o);
    UserModel userModel= new Sql2oModel(sql2o);

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    @BeforeAll
    static void setUpClass() {
        BasicConfigurator.configure();
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/acebook_test", null, null).load();
        flyway.migrate();

    }

    @BeforeEach
    void setUp() {
        conn.createQuery("insert into posts(post_id, title, content, time, likes) VALUES (:post_id, :title, :content, :time, 0)")
                .addParameter("post_id", id)
                .addParameter("title", "example title")
                .addParameter("content", "example content")
                .addParameter("time", timestamp)
                .executeUpdate();
    }

    @AfterEach
    void tearDown() {
        conn.createQuery("TRUNCATE TABLE comments, posts, users")
        .executeUpdate();
    }

    @Test
    void createPost() {
        conn.createQuery("TRUNCATE TABLE comments, posts, users")
                .executeUpdate();
        model.createPost("Hello guys", "good morning im having a swell day");
        List<Post> posts = model.getAllPosts();
        assertEquals(posts.get(0).title, "Hello guys");
    }

    @org.junit.jupiter.api.Test
    void getAllPosts() {
        List<Post> posts = new ArrayList<Post>();
        posts.add(new Post(id, "example title", "example content", timestamp, 0));
        assertEquals(model.getAllPosts(), posts);
    }

    @org.junit.jupiter.api.Test
    void addComment() {
        conn.createQuery("insert into comments(comment_id, post_id, comment) VALUES (:comment_id, :post_id, 'Looking good')")
                .addParameter("comment_id", comment_id)
                .addParameter("post_id", id)
                .executeUpdate();
        String comments = model.gettingComments(id);
        assertEquals( comments, "[Looking good]");
    }

    @org.junit.jupiter.api.Test
    void addLike() {
        model.addLike(id.toString());
        List<Integer> likes = conn.createQuery("select likes from posts where post_id =:id")
                .addParameter("id", id.toString())
                .executeAndFetch(Integer.class);
        assertEquals(likes.get(0), 1);
    }
  
    @org.junit.jupiter.api.Test
    void deletePost() {
        model.deletePost(id.toString());
        List<String> post_id = conn.createQuery("select post_id from posts where post_id =:id")
                .addParameter("id", id.toString())
                .executeAndFetch(String.class);
        assertTrue(post_id.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void deleteComment() {
        model.postComment("This is a comment", id.toString());
        List<Comment> comments = model.getAllComments();
        model.deleteComment(comments.get(0).comment_id.toString());
        assertTrue(model.getAllComments().isEmpty());
    }

    @org.junit.jupiter.api.Test
    void verifyUser(){

        userModel.createUser("Example", "name","password","name@name.com");
        List<User> user = new ArrayList<>();
        user.add(new User( id , "Example", "name", "name@name.com", "password"));
        assertTrue(userModel.verifyUser("name@name.com", "password"));
    }

    @org.junit.jupiter.api.Test
    void createUser(){
        assertEquals(userModel.createUser("Example", "name","password","name@name.com").toString(), userModel.getUserID("name@name.com"));
    }

    @org.junit.jupiter.api.Test
    void doesUserAlreadyExistYes(){
        userModel.createUser("Example", "lastname", "password", "test@gmail.com");
        assertTrue(model.doesEmailExist("test@gmail.com"));
    }

    @org.junit.jupiter.api.Test
    void doesUserAlreadyExistNo(){
        assertFalse(model.doesEmailExist("test@gmail.com"));
    }
}