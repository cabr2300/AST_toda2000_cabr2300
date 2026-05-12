import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        try {
            String json = Files.readString(Paths.get("data/snippets.json"));

            // Map for LLMs and their scores
            Map<String, List<Integer>> models = new HashMap<>();

            ObjectMapper objectMapper = new ObjectMapper();
            List<Snippet> snippets = objectMapper.readValue(json, new TypeReference<List<Snippet>>() {});
            for(Snippet snippet : snippets) {
                String parsed = parser.parse(snippet.code);
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
                System.out.println(key + " average score: " + String.format("%.2f", average));
            });

        } catch(IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
