package com.example.embabeldemo;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;

import java.util.Arrays;

@Agent(
        name = "massage-agent",
        description = "Agent for finding the most relevant massage therapist based on a user's query"
)
public record MassageAgent(MassageTherapistRepository massageTherapistRepository) {

    @Action
    MassageTherapists fetchRelevantTherapists(UserInput userInput, OperationContext context) {
        // Note: better to use RAG for massage therapists, but for demo purposes we create a quick string with all therapists:
        String[] allTherapists = massageTherapistRepository.findAll().stream()
                .map(mt -> "id: %d, name: %s, description: %s, specialties: %s"
                        .formatted(mt.getId(), mt.getName(), mt.getDescription(), mt.getSpecialties()))
                .toArray(String[]::new);
        String prompt = ("Fetch all relevant massage therapists that are fit to help the customer with the following massage" +
                "input query: '%s'.\nAll therapists to choose from: %s").formatted(userInput, Arrays.toString(allTherapists));
        MassageTherapists therapists = context.ai()
                .withDefaultLlm()
                .createObject(prompt, MassageTherapists.class);
        IO.println("MassageBookingAgent returning from fetchRelevantTherapists: %s".formatted(Arrays.toString(therapists.therapists().toArray())));
        return therapists;
    }

    @Action
    @AchievesGoal(description = "Massage therapist selected out of all therapist options")
    MassageTherapist findMostRelevantTherapist(MassageTherapists massageTherapists, OperationContext context) {
        String prompt = """
                Select the best possible massage therapist for the given user query.
                
                Possible massage therapists: %s
                User query: %s
                """.formatted(Arrays.toString(massageTherapists.therapists().toArray()), massageTherapists.originalUserInput());

        return context.ai()
                .withLlm("mistral:latest")
                .createObject(prompt, MassageTherapist.class);
    }
}

