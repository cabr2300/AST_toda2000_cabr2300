/**
 * Represents a rule for code deprecation.
 * @author Carl Broberg
 */
public class DeprecationRule {

    private final NodeType pattern;
    private final String reason;
    private final NodeCondition condition;

    /**
     * Construct the rule specifying the associated pattern and reason.
     * @param pattern is a {@link NodeType} specifying a code pattern.
     * @param reason is a string explaining why the pattern is deprecated.
     */
    public DeprecationRule(NodeType pattern, String reason) {
        this.pattern = pattern;
        this.reason = reason;
        this.condition = (n, c) -> true;
    }

    /**
     * Overloaded class constructor including a custom NodeCondition.
     * @param pattern is a {@link NodeType} specifying a code pattern.
     * @param reason is a string explaining why the pattern is deprecated.
     * @param condition is a special condition that must be fulfilled for the deprecation rule to be valid.
     */
    public DeprecationRule(NodeType pattern, NodeCondition condition, String reason) {
        this.pattern = pattern;
        this.reason = reason;
        this.condition = condition;
    }

    /**
     * Check if a node matches the pattern of this rule
     * @param node is an {@link ASTNode} that has a {@link NodeType}
     * @param ctx is the {@link Context} of the AST under which the match should be considered.
     * @return whether the {@link NodeType} matches the pattern
     */
    public boolean matches(ASTNode node, Context ctx) {
        return node.getType() == pattern && condition.test(node, ctx);
    }

    /**
     * Get the reason for deprecation.
     * @return the reason string.
     */
    public String getReason() {
        return this.reason;
    }
}
