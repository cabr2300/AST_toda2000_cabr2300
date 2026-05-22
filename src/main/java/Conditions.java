import java.util.Objects;

/**
 * Class establishing the conditions under which {@link DeprecationRule}s may be valid.
 * The different type of condition depends on what type of code element is deprecated.
 * Each static method implements the logic of how to determine deprecation under a certain condition.
 * @author Carl Broberg
 */
public final class Conditions {

    /**
     * Avoid instantiation of static class
     */
    private Conditions() {}

    /**
     * The conditions to confirm whether a given method of a given import module is used in an {@link ASTNode}.
     * @param moduleName is the name of an imported module binding.
     * @param methodName is the name of a method of that module.
     * @return the {@link NodeCondition} that determines whether the condition is true or false.
     */
    public static NodeCondition deprecatedImportedMethod(String moduleName, String methodName) {
        return (node, ctx) -> {
            String object = node.getCalleeObject();
            String property = node.getCalleeProperty();
            if(object == null) { // import existSync from "fs" || import existSync as exist fom "fs"
                ImportInfo info = ctx.imports.get(property);
                return info != null
                        && info.sourceModule().equals(moduleName)
                        && info.importedName().equals(methodName);
            } // import fs from "fs"
            ImportInfo info = ctx.imports.get(object);
            return info != null
                    && property.equals(methodName)
                    && info.sourceModule().equals(moduleName);
        };
    }

    /**
     * The conditions to confirm whether a given imported method is called with a string first argument.
     * @param moduleName is the name of an imported module binding.
     * @param methodName is the name of a method of that module.
     * @return the {@link NodeCondition} that determines whether the condition is true or false.
     */
    public static NodeCondition deprecatedImportedMethodWithStringArgument(String moduleName, String methodName) {
        NodeCondition importedMethod = deprecatedImportedMethod(moduleName, methodName);
        return (node, ctx) -> importedMethod.test(node, ctx) && node.hasStringArgument(0);
    }

    /**
     * The conditions to confirm whether a given import module is used in an {@link ASTNode}.
     * @param moduleName is the name of an imported module binding.
     * @return the {@link NodeCondition} that determines whether the condition is true or false.
     */
    public static NodeCondition deprecatedModule(String moduleName) {
        return (node, ctx) -> moduleName.equals(node.getImportSource());
    }

    /**
     * The conditions to confirm whether a given method of the standard library is used in an {@link ASTNode}.
     * @param methodName is the name of a method.
     * @return the {@link NodeCondition} that determines whether the condition is true or false.
     */
    public static NodeCondition deprecatedStandardMethod(String methodName) {
        return (node, ctx) -> methodName.equals(node.getCalleeName());
    }

    /**
     * Overloaded method to confirm whether a given method of the standard library is used in an {@link ASTNode}.
     * Used if the method may be used with or without its object.
     * @param methodName is the name of a method.
     * @param objectMethodName is the alternate name of the method or object+method.
     * @return the {@link NodeCondition} that determines whether the condition is true or false.
     */
    public static NodeCondition deprecatedStandardMethod(String methodName, String objectMethodName) {
        return (node, ctx) -> methodName.equals(node.getCalleeName()) || objectMethodName.equals(node.getCalleeName());
    }

    /**
     * The condition to confirm whether the node is of a specific Acorn {@code kind}.
     * @param kind is the {@code kind} attribute of some nodes.
     * @return whether the provided kind is a match.
     */
    public static NodeCondition deprecatedKind(String kind) {
        return (node, ctx) -> kind.equals((node.getKind()));
    }

    /**
     * The condition to confirm whether the node is a specific method of a specific class.
     * @param className is the name of the Class within which the method is.
     * @param methodName is the name of the method.
     * @return whether the provided params match.
     */
    public static NodeCondition deprecatedLifecycleMethod(String className, String methodName) {
        return (node, ctx) -> node.getName().equals(methodName) && ctx.memberExpressions.contains(className);
    }
}
