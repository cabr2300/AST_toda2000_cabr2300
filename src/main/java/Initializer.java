import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfig;

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
                List<Snippet> snippets = objectMapper.readValue(json, new TypeReference<List<Snippet>>() {});
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
                    System.out.println("Snippet " + snippet.id + " modernity score: " + score + "/1");
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
}
