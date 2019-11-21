import models.*;
import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import spark.ModelAndView;
import spark.Spark;

import java.util.HashMap;
import java.util.UUID;


import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();

        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/acebook", null, null).load();
        flyway.migrate();

        Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "acebook", null, null, new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        Model model = new Sql2oModel(sql2o);
        UserModel userModel = new Sql2oModel(sql2o);

        Spark.get("/posts", (req, res) -> {
            HashMap posts = new HashMap();
            posts.put("posts", model.getAllPosts());
            posts.put("comments", model.getAllComments());

            return new ModelAndView(posts, "templates/posts.vtl");
        }, new VelocityTemplateEngine());


        Spark.get("/newpost", (req, res) -> {
            return new ModelAndView(new HashMap<>(),"templates/newPost.vtl");
        }, new VelocityTemplateEngine());

        post("/newpost", (request, response) -> {
            String title;
            String content;
            title = request.queryParams("title");
            content = request.queryParams("content");
            response.redirect("/posts");
            UUID id = model.createPost(title, content);
            return null;
        });

        post("/likepost", (request, response) -> {
            String id;
            id = request.queryParams("id");
            model.addLike(id);
            response.redirect("/posts");
            return null;
        });

        //Sign in methods

        Spark.get("/", (req, res) -> {
            HashMap users = new HashMap();
            return new ModelAndView(users, "templates/sign-in.vtl");
        }, new VelocityTemplateEngine());


        post("/sign-in", (req,res) -> {

            String password = req.queryParams("password");
            String email = req.queryParams("email");

            if(userModel.verifyUser(email, password)) {
                res.redirect("/posts");
            }
            res.redirect("/");
            return null;
        });


        //Sign up methods

        Spark.get("/sign-up", (req, res) -> {
            HashMap users = new HashMap();
            return new ModelAndView(users, "templates/sign-up.vtl");
        }, new VelocityTemplateEngine());

        post("/sign-up", (req,res) -> {
            String first_name = req.queryParams("first_name");
            String last_name = req.queryParams("last_name");
            String password = req.queryParams("password");
            String email = req.queryParams("email");

            if (model.doesEmailExist(email)) {
                res.redirect("/sign-up");
            } else {
                userModel.createUser(first_name, last_name, password, email);
                res.redirect("/posts");
            }
            return null;
        });

        post("/postcomment", (request, response) -> {
           String comment = request.queryParams("comment");
           String post_id = request.queryParams("post_id");

           model.postComment(comment, post_id);

           response.redirect("/posts");
           return null;
        });

        post("/deletecomment", (request, response) -> {
            String comment_id = request.queryParams("comment_id");

            model.deleteComment(comment_id);

            response.redirect("/posts");
            return null;
        });

        post("/deletepost", (request, response) -> {
            String post_id = request.queryParams("post_id");

            model.deletePost(post_id);

            response.redirect("/posts");
            return null;
        });
    };
}
