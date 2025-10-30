package com.example.embabeldemo;

import com.embabel.agent.config.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAgents(loggingTheme = LoggingThemes.STAR_WARS, localModels = LocalModels.OLLAMA)
public class EmbabelDemoApplication {

    static void main() {
        SpringApplication.run(EmbabelDemoApplication.class);
    }

}
