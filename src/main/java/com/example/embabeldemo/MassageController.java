package com.example.embabeldemo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/massage")
public class MassageController {

    private final MassageService massageService;

    @GetMapping
    public String askQuestion(@RequestParam String question) {
        IO.println("MassageController GET with question: %s".formatted(question));
        return massageService.askQuestion(question);
    }
}
