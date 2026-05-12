import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class for carrying import module information between nodes in the AST.
 * Used for identifying imported methods
 * @author Carl Broberg
 */
public class Context {
    Map<String, ImportInfo> imports = new HashMap<>();
}