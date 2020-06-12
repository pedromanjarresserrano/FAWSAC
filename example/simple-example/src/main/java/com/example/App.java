package com.example;


import com.gitlab.pedrioko.config.ZKCEApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@ZKCEApplication
@EntityScan
public class App {

    public static void main(String[] args) {
        new SpringApplicationBuilder(App.class).run();
    }
}
