/**
 * Represents a rule for code deprecation.
 * @author Carl Broberg
 */
public class DeprecationRule {

    private final NodeType pattern;
    private final String reason;

    /**
     * Construct the rule specifying the associated pattern and reason.
     * @param pattern is a {@link NodeType} specifying a code pattern.
     * @param reason is a string explaining why the pattern is deprecated.
     */
    public DeprecationRule(NodeType pattern, String reason) {
        this.pattern = pattern;
        this.reason = reason;
    }

    /**
     * Check if a node matches the pattern of this rule
     * @param node is an {@link ASTNode} that has a {@link NodeType}
     * @return whether the {@link NodeType} matches the pattern
     */
    public boolean matches(ASTNode node) {
        return node.getType() == this.pattern;
    }

    /**
     * Get the reason for deprecation.
     * @return the reason string.
     */
    public String getReason() {
        return this.reason;
    }
}
