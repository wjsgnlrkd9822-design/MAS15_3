    package com.aloha.project.controller;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;


    @Controller
    public class HomeController {
        

        @GetMapping("/admin/ad_main")
        public String admin() {
            return "admin/ad_main";
        }
        
    }
    