import java.util.List;

/**
 * Utility class holding and providing {@link DeprecationRule}s
 * @author Carl Broberg
 */
public final class RuleBook {

    /**
     * Avoid instantiation of static class
     */
    private RuleBook() {}

    /**
     * Get the rules by which the code snippets will be evaluated.
     * @return a list of {@link DeprecationRule}s
     */
    public static List<DeprecationRule> getRules() {
        return List.of(
            new DeprecationRule(NodeType.VARIABLE_DECLARATION, Conditions.deprecatedKind("var"),"var is deprecated, use let or const"),
            new DeprecationRule(NodeType.FUNCTION_DECLARATION, "prefer arrow functions in modern JS"),
            new DeprecationRule(NodeType.WITH_STATEMENT,"With statement considered bad practice"),
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedStandardMethod("eval", "window.eval"),
                    "Avoid using eval()"),
            new DeprecationRule(NodeType.IMPORT_DECLARATION,
                    Conditions.deprecatedModule("left-pad"),
                    "left-pad is deprecated"),
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("fs", "existsSync"),
                    "fs.existsSync is considered bad practice"
            )
        );
    }
}
