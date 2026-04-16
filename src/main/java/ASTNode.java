import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a node in the AST
 * @author Carl Broberg
 */
public class ASTNode {

    private final NodeType type;
    private final String value;
    private final List<ASTNode> children;

    /**
     * Construct the AST node.
     * @param type the syntax category of the node.
     * @param value the source code fragment the node represents
     */
    public ASTNode(NodeType type, String value) {
        this.type = type;
        this.value = value;
        this.children = new ArrayList<>();
    }

    /**
     * Add a child node to the node
     * @param child is another AST node
     */
    public void addChild(ASTNode child) {
        children.add(child);
    }

    /**
     * Get the node type
     * @return the {@link NodeType} of the node
     */
    public NodeType getType() {
        return type;
    }

    /**
     * Get the value of the node
     * @return the String containing the code fragment
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the node's children
     * @return a {@code List} of {@link ASTNode}s
     */
    public List<ASTNode> getChildren() {
        return children;
    }
}
