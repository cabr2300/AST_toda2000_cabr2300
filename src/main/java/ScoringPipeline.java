import java.util.List;

/**
 * Class for traversing an AST and giving it a binary score
 * based on whether it includes deprecated code or not.
 * @author Carl Broberg
 */
public class ScoringPipeline {

    private final List<DeprecationRule> rules;

    /**
     * Construct the list of {@link DeprecationRule}s used to score an AST.
     */
    public ScoringPipeline() {
        //TODO add more rules
        rules = List.of(
                new DeprecationRule(NodeType.VAR_DECLARATION, "var is deprecated, use let or const"),
                new DeprecationRule(NodeType.FUNCTION_DECLARATION, "prefer arrow functions in modern JS")
        );
    }

    /**
     * Score an AST based on whether it violates deprecation rules or not.
     * @param root is the root {@link ASTNode} of the AST
     * @return 0 of at least one rule has been violated, otherwise 1.
     */
    public int score(ASTNode root) {
        int penalty = countPenalties(root);
        return penalty == 0 ? 1 : 0;
    }

    /**
     * Traverses the AST recursively, accumulating penalties when deprecation rules are violated.
     * @param node is the root of the current subtree being investigated
     * @return the sum of penalties.
     */
    private int countPenalties(ASTNode node) {
        int penalty = 0;
        for(DeprecationRule rule : rules) {
            if(rule.matches(node)) {
                System.out.println(node.getValue() + ": " + rule.getReason());
                penalty += 1;
            }
        }
        for(ASTNode child : node.getChildren()) {
            penalty += countPenalties(child);
        }
        return penalty;
    }
}
