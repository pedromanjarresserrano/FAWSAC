package com.gitlab.pedrioko.spring;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("session")
public class Controllers {

    //@RequestMapping("/{page}")
    public ModelAndView root(@PathVariable String page) {
        ModelAndView model = new ModelAndView(page);
        model.addObject("message", "Dinesh");
        return model;
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

    @GetMapping("/favicon.ico")
    public String favicon() {
        return "images/favicon.ico";
    }




    public class Hello {
        private String greeting;

        public Hello() {
        }

        public Hello(String greeting) {
            this.greeting = greeting;
        }

        public String getGreeting() {
            return greeting;
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
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
