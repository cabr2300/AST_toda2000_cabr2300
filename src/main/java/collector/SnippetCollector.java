package collector;

import config.AppConfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Class for producing the code snippets for evaluation.
 * Connects to the APIs of the LLMs and prompts them with the list of queries.
 * Saves the response snippet data.
 * @author Carl Broberg
 */
public class SnippetCollector {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Formulates a request adjusted to the LLM being prompted.
     * Connects to the API and sends the request.
     * Receives the API response, extracts and sanitizes the code snippet.
     * @param model is the LLM that will be prompted.
     * @param prompt is the query being prompted to the LLM.
     * @return the raw code snippet from the response
     * @throws IOException if the Jackson ObjectMapper fails to serialize the request body to valid JSON
     * @throws IOException if communication with the server fails.
     * @throws InterruptedException if the API request is interrupted.
     */
    public String fetchSnippet(String model, String prompt) throws InterruptedException, IOException {

        // Determine the LLM
        String[] modelArray = model.split("-");

        // Construct the request body
        String requestBody;
        if(Objects.equals(modelArray[0], "gpt")) {
            requestBody = objectMapper.writeValueAsString(
                    objectMapper.createObjectNode()
                            .put("model", model)
                            .put("temperature", 0)
                            .set("messages", objectMapper.createArrayNode()
                                    .add(objectMapper.createObjectNode()
                                            .put("role", "user")
                                            .put("content", prompt)))
            );
        } else if (Objects.equals(modelArray[0], "claude")) {
            requestBody = objectMapper.writeValueAsString(
                    objectMapper.createObjectNode()
                            .put("model", model)
                            .put("temperature", 0)
                            .put("max_tokens", 1024)
                            .set("messages", objectMapper.createArrayNode()
                                    .add(objectMapper.createObjectNode()
                                            .put("role", "user")
                                            .put("content", prompt)))
            );
        } else {
            System.err.println("Unknown LLM model");
            return null;
        }

        // Formulate the API request
        HttpRequest request;
        if(Objects.equals(modelArray[0], "gpt")) {
            request = HttpRequest.newBuilder()
                .uri(URI.create(AppConfig.GPT_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + AppConfig.GPT_API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        } else if (Objects.equals(modelArray[0], "claude")) {
            request = HttpRequest.newBuilder()
                .uri(URI.create(AppConfig.CLAUDE_API_URL))
                .header("Content-Type", "application/json")
                .header("x-api-key", AppConfig.CLAUDE_API_KEY)
                .header("anthropic-version", "2023-06-01")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        } else {
            System.err.println("Unknown LLM model");
            return null;
        }
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode responseJson = objectMapper.readTree(response.body());

        // extract the code
        if(Objects.equals(modelArray[0], "gpt")) {
            return responseJson
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText()
                .replaceAll("```(?:js|javascript)?\\s*", "") // sanitize if needed
                .replaceAll("\\s*```$", "");
        } else if (Objects.equals(modelArray[0], "claude")) {
            return responseJson
                .get("content")
                .get(0)
                .get("text")
                .asText()
                .replaceAll("```(?:js|javascript)?\\s*", "")
                .replaceAll("\\s*```$", "");
        } else { return null;}
    }

    /**
     * Gets the code snippet produced by each prompt and each LLM.
     * Constructs a JSON object holding model, prompt, and code data for each response.
     * Stores the objects in a JSON array.
     * Converts it to a string and writes it to file.
     * @param models is a list of all LLMs to be queried.
     * @param prompts is a list of queries to be presented to each of the LLMs
     * @param outputPath is the file location for the data output.
     * @throws IOException if Jackson deserialization fails.
     * @throws IOException if writing the data to file fails.
     * @throws InterruptedException if {@code fetchSnippet()} fails.
     */
    public void collect(List<String> models, List<String> prompts, String outputPath) throws IOException, InterruptedException {
        ArrayNode results = objectMapper.createArrayNode();
        int id = 1;
        for (String model : models) {
            for (String prompt : prompts) {
                String code = fetchSnippet(model, prompt);
                ObjectNode entry = objectMapper.createObjectNode();
                entry.put("id", id++);
                entry.put("model", model);
                entry.put("prompt", prompt);
                entry.put("code", code);
                results.add(entry);
            }
        }
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(results);
        Files.writeString(Paths.get(outputPath), json);
    }

    //TODO remove and integrate with Initializer
    public static void main(String[] args) throws Exception {
        List<String> models = List.of(AppConfig.GPT_MODEL, AppConfig.CLAUDE_MODEL);

        String promptsJson = Files.readString(Paths.get(AppConfig.PROMPTS_JSON));
        List<String> prompts = new ObjectMapper().readValue(promptsJson,
                new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});

        SnippetCollector collector = new SnippetCollector();
        collector.collect(models, prompts, "data/snippets2.json"); //TODO change to AppConfig file location after testing
    }
}

