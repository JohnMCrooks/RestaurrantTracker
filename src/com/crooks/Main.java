package com.crooks;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {
    static HashMap<String, User> userHash = new HashMap<>();


    public static void main(String[] args) {

        //this is a  comment

        Spark.init();
        Spark.get(
                "/",
                (request, response) ->{
                    Session session = request.session();
                    String username = session.attribute("username");
                    HashMap m = new HashMap();

                    if(username==null){
                        return new ModelAndView(m, "login.html");
                    }else{
                        User user = userHash.get(username);  //Gotta pull the User object out of the hash to get to the restaurant variable
                        m.put("restaurants",user.restaurants);
                        m.put("name", user.name);
                        return new ModelAndView(m, "home.html");
                    }

                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                (request, response) -> {
                    String name = request.queryParams("username");
                    String pass = request.queryParams("password");
                    if(name==null ||pass==null){
                        throw new Exception("Name or Pass not sent through");
                    }

                    User user = userHash.get(name);
                    if (user==null){
                        user = new User(name,pass);
                        userHash.put(name,user);
                    }else if (!pass.equals(user.password)){
                        throw new Exception("Wrong Password");
                    }

                    Session session = request.session();
                    session.attribute("username",name);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/create-restaurant",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if(username==null){
                        throw new Exception("Not Logged in");
                    }

                    String name = request.queryParams("name");
                    String location = request.queryParams("location");
                    String comment = request.queryParams("comment");
                    int rating = Integer.valueOf(request.queryParams("rating"));

                    if (name==null||location==null||comment==null){
                        throw new Exception("Incomplete form!!");
                    }

                    User user = userHash.get(username);
                    if (user==null){
                        throw new Exception("User Doesn't Exist");
                    }
                    Restaurant r1 = new Restaurant(name,location,comment,rating);
                    user.restaurants.add(r1);   //adds restaurant to User Object

                    response.redirect("/");
                    return"";

                }
        );

        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return"";
                }
        );

        Spark.post(
                "/delete-restaurant",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if(username==null){
                        throw new Exception("Not Logged in");
                    }
                    int id = Integer.valueOf(request.queryParams("id"));
                    User user = userHash.get(username);
                    if(id<= 0 || id-1 >= user.restaurants.size()){
                        throw new Exception("Invalid number!!");
                    }
                    user.restaurants.remove(id-1);

                    response.redirect("/");
                    return "";

                }
        );


    }
}
