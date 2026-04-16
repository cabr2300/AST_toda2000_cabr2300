/**
 * Represents the categories of syntax patterns that the AST scoring pipeline recognizes.
 * A pattern may or may not be associated with a deprecation rule.
 * @author Carl Broberg
 */
public enum NodeType {

    //TODO add all enum constants
    /**
     * A variable declaration using the {@code var} keyword
     */
    VAR_DECLARATION,

    /**
     * A function declaration using the {@code function} keyword
     */
    FUNCTION_DECLARATION,

    /**
     * An arrow function using the {@code =>} symbol
     */
    ARROW_FUNCTION,

    /**
     * A variable declaration using the {@code let} keyword
     */
    LET_DECLARATION,

    /**
     * A variable declaration using the {@code const} keyword
     */
    CONST_DECLARATION
}
