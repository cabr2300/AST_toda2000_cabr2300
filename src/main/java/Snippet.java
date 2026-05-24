import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a code snipped created by an LLM, and associated metadata
 * @author Carl Broberg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Snippet {
    public int id;
    public String model;
    public String prompt;
    public String code;
}
