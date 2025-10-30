package com.example.embabeldemo;

import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.testing.unit.FakeOperationContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MassageAgentTest {

    @Test
    void promptMustContainUserInputAndAllTherapists_andNoToolsAreExposed() {
        // given
        var t1 = new MassageTherapist(1L, "Alice", "Deep tissue specialist", "neck, back");
        var t2 = new MassageTherapist(2L, "Bram", "Sports massage", "shoulder, hamstrings");
        var repo = mock(MassageTherapistRepository.class);
        when(repo.findAll()).thenReturn(List.of(t1, t2));

        var agent = new MassageAgent(repo);
        var ctx = new FakeOperationContext();

        String userQuery = "neck pain after cycling";
        var expected = new MassageTherapists(List.of(t1), userQuery);
        ctx.expectResponse(expected);

        // when
        var out = agent.fetchRelevantTherapists(new UserInput(userQuery), ctx);

        // then
        assertEquals(expected.therapists(), out.therapists(), "Should return the fake LLM-selected therapists");
        assertEquals(expected.originalUserInput(), out.originalUserInput());

        var invocation = ctx.getLlmInvocations().getFirst();
        var prompt = invocation.getPrompt();
        var toolGroups = invocation.getInteraction().getToolGroups();

        assertTrue(prompt.contains(userQuery), "Prompt should include the original user query");
        assertTrue(prompt.contains(String.valueOf(t1.getId())), "Prompt should list therapist 1");
        assertTrue(prompt.contains(t1.getName()), "Prompt should include therapist 1 name");
        assertTrue(prompt.contains(t1.getDescription()), "Prompt should include therapist 1 description");
        assertTrue(prompt.contains(t1.getSpecialties()), "Prompt should include therapist 1 specialties");

        assertTrue(prompt.contains(String.valueOf(t2.getId())), "Prompt should list therapist 2");
        assertTrue(prompt.contains(t2.getName()), "Prompt should include therapist 2 name");
        assertTrue(prompt.contains(t2.getDescription()), "Prompt should include therapist 2 description");
        assertTrue(prompt.contains(t2.getSpecialties()), "Prompt should include therapist 2 specialties");

        assertTrue(toolGroups.isEmpty(), "No tool groups should be exposed for this createObject call");

        // and the repo should have been queried
        verify(repo, atLeastOnce()).findAll();
    }

    @Test
    void promptMustContainCandidatesAndOriginalInput_andNoToolsAreExposed() {
        // given
        var t1 = new MassageTherapist(1L, "Alice", "Deep tissue specialist", "neck, back");
        var t2 = new MassageTherapist(2L, "Bram", "Sports massage", "shoulder, hamstrings");
        var input = new MassageTherapists(
                List.of(t1, t2),
                "tight shoulders before marathon"
        );

        var agent = new MassageAgent(mock(MassageTherapistRepository.class));
        var ctx = new FakeOperationContext();

        // Stub selected therapist
        var expectedChosen = t2;
        ctx.expectResponse(expectedChosen);

        // when
        var chosen = agent.findMostRelevantTherapist(input, ctx);

        // then
        assertEquals(expectedChosen.getId(), chosen.getId());
        assertEquals(expectedChosen.getName(), chosen.getName());

        // then (LLM orchestration / prompt verification)
        var invocation = ctx.getLlmInvocations().getFirst();
        var prompt = invocation.getPrompt();
        var toolGroups = invocation.getInteraction().getToolGroups();

        // Prompt should enumerate candidates and include the user’s original query
        assertTrue(prompt.contains("Possible massage therapists"), "Prompt should list the candidates section");
        assertTrue(prompt.contains("User query"), "Prompt should include the user query section");
        assertTrue(prompt.contains(input.originalUserInput()), "Prompt should embed the original user input");

        // Each candidate should be visible in the prompt (the code uses Arrays.toString(..))
        assertTrue(prompt.contains(t1.getName()), "Prompt should include candidate t1");
        assertTrue(prompt.contains(t1.getDescription()), "Prompt should include candidate t1 description");
        assertTrue(prompt.contains(t1.getSpecialties()), "Prompt should include candidate t1 specialties");

        assertTrue(prompt.contains(t2.getName()), "Prompt should include candidate t2");
        assertTrue(prompt.contains(t2.getDescription()), "Prompt should include candidate t2 description");
        assertTrue(prompt.contains(t2.getSpecialties()), "Prompt should include candidate t2 specialties");

        assertTrue(toolGroups.isEmpty(), "No tool groups should be exposed for this createObject call");

        // If your FakeOperationContext exposes model info, you can also assert the model selection:
        // assertEquals("mistral:latest", invocation.getInteraction().getModelId());
    }
}
