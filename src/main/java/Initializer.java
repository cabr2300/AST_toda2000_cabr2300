import collector.SnippetCollector;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfig;

import mapper.ASTNode;
import evaluator.Context;
import evaluator.ScoringPipeline;
import mapper.TypeMapper;
import parser.AcornParser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class that initializes the scoring pipeline of the AST
 * @author Carl Broberg
 */
public class Initializer {

    /**
     * Instantiates the classes used for running the program.
     */
    public Initializer() {

        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("""
                    
                    Press key to continue:
                    C - collect snippets
                    S - score collected snippets
                    Or press any other key to exit.""");
            String input = scanner.nextLine().toLowerCase();
            if(input.equals("c")) {
                try {
                    fetchSnippets();
                } catch (Exception e) {
                    System.out.println("ERROR: Snippet collection failed");
                }
            } else if(input.equals("s")) {
                parseAndScore();
            } else {
                break;
            }
        }
    }

    private void parseAndScore() {
        AcornParser parser = new AcornParser();
        TypeMapper typeMapper = new TypeMapper();
        ScoringPipeline pipeline = new ScoringPipeline();

        List<String> results = new ArrayList<>();
        List<String> snippetFiles = List.of(AppConfig.SNIPPETS_BASELINE_JSON, AppConfig.SNIPPETS_RECENCY_JSON);
        for(String snippetFile : snippetFiles) {
            try {
                String category = Objects.equals(snippetFile, AppConfig.SNIPPETS_BASELINE_JSON) ?
                        "Baseline results:" : "Recency conditioned results";
                results.add("\n" + category + "\n");
                String json = Files.readString(Paths.get(snippetFile));

                // Map for LLMs and their scores
                Map<String, List<Integer>> models = new HashMap<>();

                ObjectMapper objectMapper = new ObjectMapper();
                List<Snippet> snippets = objectMapper.readValue(json, new TypeReference<>() {});
                for(Snippet snippet : snippets) {
                    String parsed = parser.parse(snippet.code);
                    if (parsed == null) {
                        System.out.println("Snippet " + snippet.id + " could not be parsed, skipping");
                        System.out.println();
                        continue;
                    }
                    ASTNode root = typeMapper.buildTree(parsed);
                    Context ctx = new Context();
                    int score = pipeline.score(root, ctx);
                    System.out.println(snippet.model + "-- Snippet " + snippet.id + " modernity score: " + score + "/1");
                    System.out.println();
                    // Add current model as key if it has not been added yet
                    models.computeIfAbsent(snippet.model, k -> new ArrayList<>()).add(score);
                }
                models.forEach((key, value) -> {
                    System.out.println();
                    double average = value.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(1.0);
                    results.add(key + " average score: " + String.format("%.2f", average));
                });

            } catch(IOException | InterruptedException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        for(String result : results) {
            System.out.println(result);
        }
    }

    private void fetchSnippets() throws Exception {
        List<String> promptFiles = List.of(AppConfig.PROMPTS_BASELINE_JSON, AppConfig.PROMPTS_RECENCY_JSON);
        for(String promptFile : promptFiles) {
            String snippetFile = Objects.equals(promptFile, AppConfig.PROMPTS_BASELINE_JSON) ?
                    AppConfig.SNIPPETS_BASELINE_JSON : AppConfig.SNIPPETS_RECENCY_JSON;
            String promptsJson = Files.readString(Paths.get(promptFile));
            List<String> prompts = new ObjectMapper().readValue(promptsJson,
                    new com.fasterxml.jackson.core.type.TypeReference<>() {});
            SnippetCollector collector = new SnippetCollector();
            System.out.println("Collecting snippets for: " + snippetFile);
            collector.collect(AppConfig.MODELS, prompts, snippetFile);
        }
    }
}
