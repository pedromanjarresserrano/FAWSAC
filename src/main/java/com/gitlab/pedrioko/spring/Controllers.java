package com.gitlab.pedrioko.spring;

import com.gitlab.pedrioko.core.view.api.ContentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("session")
public class Controllers {

    @GetMapping("/")
    public String root() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/recovery")
    public String recovery() {
        return "recovery";
    }


    public class Hello {
        private String greeting;

        public Hello() {
        }

        public Hello(String greeting) {
            this.greeting = greeting;
        }

        public String getGreeting() {
            return this.greeting;
        }

        public void setGreeting(String greeting) {
            this.greeting = greeting;
        }
    }


    public class User {
        private String name;

        public User() {
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
