package models;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import lombok.Data;
@Data
public class Post {
    private UUID post_id;
    public String title;
    private String content;
    private Timestamp time;
    private Integer likes;

    public Post(UUID post_id, String title, String content, Timestamp time, Integer likes) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.likes = likes;
    }

}
