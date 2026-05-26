package evaluator;

import mapper.ASTNode;

/**
 * Functional interface representing a possible condition that needs to be
 * fulfilled in order for an Acorn type to be considered as deprecated.
 * @author Carl Broberg
 */
@FunctionalInterface
public interface NodeCondition {
    boolean test(ASTNode node, Context ctx);
}
