# Embabel Demo
We will experiment with [Embabel](https://github.com/embabel)

- [Embabel Agent Examples Repo](https://github.com/embabel/embabel-agent-examples/tree/main)
- [Agentic AI Agents with Embabel](https://www.youtube.com/watch?v=kpeYvKha5oE)

> _Note:_ there's no intermediate assignment commits for this demo. Go to the "solution" commit for the result.

## Assignment
### MassageFinderAgent
Following the massage chatbot app we just built ([repo](https://github.com/JornR94/SpringAI)),
we will build a similar solution using Embabel.

Use Embabel to build a MassageFinderAgent. The agent should:
1. Find all relevant massage therapists based on a user's query (e.g., find all therapists who can do back massages if
   the user asks for a back massage)
    - You can re-use the MassageTherapist and MassageTherapistRepository classes from the previous project.
    - The best practice would be to somehow pass all therapists through embedding. For this demo, you can just
      pass the therapists in String format to the LLM in this action.
2. Return the best matching therapist
    - For the evaluation you can use the same default LLM as used in the other action from step 1, for demo purposes

### Testing
Once you have a working agent, write a test class that asserts the prompt generation that's passed on to the LLM. See
[Agentic AI Agents with Embabel](https://www.youtube.com/watch?v=kpeYvKha5oE) for some examples and inspiration.

