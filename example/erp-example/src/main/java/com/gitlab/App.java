package com.gitlab;

import com.gitlab.pedrioko.config.ZKCEApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@ZKCEApplication
public class App {

    public static void main(String[] args) {
        new SpringApplicationBuilder(App.class).run();
    }

}


