package com.example;

import java.util.HashMap;
import java.util.Map;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.util.FileUtil;

public class JavalinHtmlFormsExampleApp {

    private static final Map<String, String> reservations = new HashMap<>() {{
        put("saturday", "No reservation");
        put("sunday", "No reservation");
    }};

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        });

        app.post("/make-reservation", ctx -> {
            reservations.put(ctx.formParam("day"), ctx.formParam("time"));
            String text = new description().createDescription(ctx.formParam("day"), ctx.formParam("time"));

            System.out.println("Text: " + text);
            if (text != null) {
                ctx.html(text);
            } else {
                ctx.html("No text provided");
            }
        });

        app.get("/check-reservation", ctx -> {
            ctx.html(reservations.get(ctx.queryParam("day")));
        });

        app.post("/upload-example", ctx -> {
            ctx.uploadedFiles("files").forEach(file -> {
                FileUtil.streamToFile(file.content(), "upload/" + file.filename());
            });
            ctx.html("Upload successful");
        });

        // Use the PORT environment variable set by Azure
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7000"));
        app.start(port);
    }
}
