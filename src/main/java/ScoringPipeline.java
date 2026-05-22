import java.util.List;
import java.util.Map;

/**
 * Class for traversing an AST and giving it a binary score
 * based on whether it includes deprecated code or not.
 * @author Carl Broberg
 */
public class ScoringPipeline {

    private final List<DeprecationRule> rules;

    /**
     * Get the list of {@link DeprecationRule}s used to score an AST.
     */
    public ScoringPipeline() {
        rules = RuleBook.getRules();
    }

    /**
     * Score an AST based on whether it violates deprecation rules or not.
     * @param root is the root {@link ASTNode} of the AST
     * @return 0 of at least one rule has been violated, otherwise 1.
     */
    public int score(ASTNode root, Context ctx) {
        populateContext(root, ctx);
        int penalty = countPenalties(root, ctx);
        return penalty == 0 ? 1 : 0;
    }

    /**
     * Traverses the AST recursively, accumulating penalties when deprecation rules are violated.
     * @param node is the root of the current subtree being investigated
     * @param ctx is the {@link Context} of the AST by which the node is evaluated.
     * @return the sum of penalties.
     */
    private int countPenalties(ASTNode node, Context ctx) {
        int penalty = 0;
        for(DeprecationRule rule : rules) {
            if(rule.matches(node, ctx)) {
                System.out.println(rule.getReason());
                penalty += 1;
            }
        }
        for(ASTNode child : node.getChildren()) {
            penalty += countPenalties(child, ctx);
        }
        return penalty;
    }

    /**
     * Traverses the AST recursively, adding relevant context in order to evaluate {@link DeprecationRule}s
     * @param node is the current {@link ASTNode} to examine.
     * @param ctx is the {@link Context} instance to which data mey be added.
     */
    private void populateContext(ASTNode node, Context ctx) {
        if(node.getType() == NodeType.IMPORT_DECLARATION) {
            Map<String, ImportInfo> imports = node.getImportInfo();
            ctx.imports.putAll(imports);
        }
        if(node.getType() == NodeType.MEMBER_EXPRESSION) {
            ctx.memberExpressions.add(node.getMemberExpression());
        }
        for(ASTNode child : node.getChildren()) {
            populateContext(child, ctx);
        }
    }
}
