package com.crooks;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {
    HashMap<String, User> userHash = new HashMap<>();


    public static void main(String[] args) {

        Spark.init();
        Spark.get(
                "/",
                (request, response) ->{
                    HashMap m = new HashMap();
                    return new ModelAndView(m, "login.html");
                },
                new MustacheTemplateEngine()
        );


    }
}
