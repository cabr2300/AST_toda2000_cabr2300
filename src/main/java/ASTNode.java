import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

/**
 * Class representing a node in the AST
 * @author Carl Broberg
 */
public class ASTNode {

    private final NodeType type;
    private final List<ASTNode> children;
    private final JsonNode raw;
    private final NodeType parentType;
    
    /**
     * Construct the AST node.
     * @param type the syntax category of the node.
     * @param raw the JsonNode containing the raw data
     */
    public ASTNode(NodeType type, JsonNode raw, NodeType parentType) {
        this.type = type;
        this.children = new ArrayList<>();
        this.raw = raw;
        this.parentType = parentType;
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
     * Get the type of the node's parent
     * @return the {@link NodeType} of the parent
     */
    public NodeType getParentType() { return parentType; }

    /**
     * Get the node's children
     * @return a {@code List} of {@link ASTNode}s
     */
    public List<ASTNode> getChildren() {
        return children;
    }

    /**
     * Needed for CallExpression Acorn type
     * @return the name of the callee, if applicable
     */
    public String getCalleeName() {
        if(raw == null) return null;
        JsonNode callee = raw.get("callee");
        if(callee == null) return null;
        if("Identifier".equals(callee.get("type").asText())) {
            return callee.get("name").asText();
        }
        if("MemberExpression".equals(callee.get("type").asText())) {
            String object = callee.get("object").get("name").asText();
            String property = callee.get("property").get("name").asText();
            return object + "." + property;
        }
        return null;
    }

    /**
     * Get the callee object of a MemberExpression
     * For instance: The property of the expression {@code myObject.myMethod()} is {@code myObject}.
     * @return the object as a String
     */
    public String getCalleeObject() {
        JsonNode callee = getCallee();
        if(callee == null) {
            return null;
        }
        JsonNode object = callee.get("object");
        if(object == null) return null;
        return object.get("name").asText();
    }

    /**
     * Get the callee property name of a MemberExpression or callee name of a CallExpression
     * For instance: The property of the expression {@code myObject.myMethod()} is {@code myMethod}.
     * @return the property as a String
     */
    public String getCalleeProperty() {
        JsonNode callee = getCallee();
        if(callee == null) {
            return null;
        }
        JsonNode property = callee.get("property");
        if(property == null) {
            return callee.get("name").asText();
        }
        return property.get("name").asText();
    }

    /**
     * Needed for MemberExpression Acorn type
     * @return the callee and property names joined by a period, if applicable
     */
    public String getMemberExpression() {
        JsonNode callee = getCallee();
        if(callee == null) {
            JsonNode object = raw.get("object").get("name");
            if(object != null) {
                String property = raw.get("property").get("name").asText();
                return object.asText() + "." + property;
            } else {
                return null;
            }
        }
        String object = callee.get("object").get("name").asText();
        String property = callee.get("property").get("name").asText();
        return object + "." + property;
    }

    /**
     * Get the callee of the current {@link ASTNode} if one exists
     * @return the callee JsonNode, or {@code null}
     */
    private JsonNode getCallee() {
        if(raw == null) return null;
        return raw.get("callee");
    }

    /**
     * Needed for {@code ImportDeclaration} Acorn type
     * @return the name of the source, if applicable
     */
    public String getImportSource() {
        if(raw == null) return null;
        JsonNode source = raw.get("source");
        if(source != null) {
            return source.get("value").asText();
        }
        return null;
    }

    /**
     * Get the {@code kind} of the node.
     * Used to determine which key word a certain Acorn type consists of
     * @return the Acorn {@code kind} as a string.
     */
    public String getKind() {
        if(raw == null || !raw.has("kind")) return null;
        return raw.get("kind").asText();
    }

    /**
     * Get the {@code name} of the node.
     * @return the Acorn {@code name} as a string.
     */
    public String getName() {
        if(raw == null || !raw.has("name")) return null;
        return raw.get("name").asText();
    }

    /**
     * Checks whether a call expression argument is definitely a string.
     * @param index is the zero-based argument position.
     * @return whether the argument is a string literal or static template string.
     */
    public boolean hasStringArgument(int index) {
        if(raw == null || index < 0) return false;
        JsonNode arguments = raw.get("arguments");
        if(arguments == null || !arguments.isArray() || arguments.size() <= index) return false;

        JsonNode argument = arguments.get(index);
        JsonNode type = argument.get("type");
        if(type == null) return false;

        if("Literal".equals(type.asText())) {
            JsonNode value = argument.get("value");
            return value != null && value.isTextual();
        }
        if("TemplateLiteral".equals(type.asText())) {
            JsonNode expressions = argument.get("expressions");
            return expressions != null && expressions.isArray() && expressions.size() == 0;
        }
        return false;
    }

    /**
     * Extracts the source value of the import module and the local name of the import.
     * Creates an {@link ImportInfo } for each import from the source.
     * The method is called on nodes of the {@code ImportDeclaration} type, which wraps
     * {@code ImportDefaultSpecifier} (e.g. import "myLib" from myLib),
     * {@code ImportNamespaceSpecifier} (e.g. import "myMethod" from myLib), and
     * {@code ImportSpecifier} (e.g. import "myMethod" as "theMethod" from myLib),
     * thus covering a variety of import practices.
     * @return a Map of String keys and {@link ImportInfo } values, each pair representing an import from one common source module.
     */
    public Map<String, ImportInfo> getImportInfo() {
        Map<String, ImportInfo> imports = new HashMap<>();
        if(raw == null) return imports;
        JsonNode source = raw.get("source");
        if(source == null || !source.has("value")) {
            return imports;
        }
        String sourceValue = source.get("value").asText();
        JsonNode specifiers = raw.get("specifiers");
        if(specifiers == null || !specifiers.isArray()) {
            return imports;
        }
        for(JsonNode spec : specifiers) {
            JsonNode local = spec.get("local");
            if(local != null && local.has("name")) {
                JsonNode imported = spec.get("imported");
                ImportInfo importInfo;
                if(imported != null && imported.has("name")) {
                    importInfo = new ImportInfo(sourceValue, imported.get("name").asText());
                } else {
                    importInfo = new ImportInfo(sourceValue, local.get("name").asText());
                }
                imports.put(local.get("name").asText(), importInfo);
            }
        }
        return imports;
    }
}
