package com.aloha.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.aloha.project.dto.Trainer;
import com.aloha.project.service.TrainerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ServiceController {

    private final TrainerService trainerService;
    @GetMapping("/service")
    public String service(Model model) throws Exception {
        List<Trainer> trainer = trainerService.list();
        model.addAttribute("trainer", trainer);
        return "service/service";
    }

}
