package com.crooks;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {
    static HashMap<String, User> userHash = new HashMap<>();


    public static void main(String[] args) {

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
                    }else if (!name.equals(user.name)){
                        throw new Exception("Wrong Password");
                    }

                    Session session = request.session();
                    session.attribute("username",name);

                    response.redirect("/");
                    return "";
                }
        );


    }
}
