package com.example.embabeldemo;

import com.embabel.agent.api.common.autonomy.Autonomy;
import com.embabel.agent.api.common.autonomy.ProcessExecutionException;
import com.embabel.agent.core.ProcessOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MassageService {

    private final Autonomy autonomy;

    public String askQuestion(String question) {
        try {
            var output = autonomy
                    .chooseAndRunAgent(question, ProcessOptions.getDEFAULT())
                    .getOutput();
            IO.println("MassageBookingService returning answer: %s".formatted(output));
            return switch (output) {
                case MassageTherapist therapist -> therapist.toString();
                default -> "No answer found";
            };
        } catch (ProcessExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
