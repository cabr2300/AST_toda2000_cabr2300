import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Class for parsing JavaScript code into an Abstract Search Tree.
 * @author Carl Broberg
 * @author Tobias Danielsson
 */
public class AcornParser {

    /**
     * Turn a JS source string into a JSON AST string.
     * @param jsSource is the JS source code
     * @return a string containing the AST JSON
     * @throws URISyntaxException if the path cannot be converted to URI
     * @throws IOException if the subprocess fails
     * @throws InterruptedException if the subprocess is interrupted
     */
    public String parse(String jsSource) throws URISyntaxException, IOException, InterruptedException {
        // Get the path to the node.js script
        String scriptPath = Paths.get(AcornParser.class
                .getClassLoader()
                .getResource("parse.js")
                .toURI())
                .toString();

        // Create node.js subprocess
        ProcessBuilder pb = new ProcessBuilder("node", scriptPath);
        Process process = pb.start();

        // Write jsSource to the process stdin
        try(OutputStream stdin = process.getOutputStream()){
            stdin.write(jsSource.getBytes(StandardCharsets.UTF_8));
        }

        // Read the json AST back from the stdout
        String json = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();
        if (exitCode != 0) return null;
        return json;
    }
}
