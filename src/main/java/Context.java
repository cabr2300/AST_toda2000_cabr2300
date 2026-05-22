import java.util.*;

/**
 * Class for carrying import module information between nodes in the AST.
 * Used for identifying imported methods
 * @author Carl Broberg
 */
public class Context {
    Map<String, ImportInfo> imports = new HashMap<>();
    List<String> memberExpressions = new ArrayList<>();
}