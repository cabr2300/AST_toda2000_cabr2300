package evaluator;

import mapper.NodeType;

import java.util.List;

/**
 * Utility class holding and providing {@link DeprecationRule}s
 * @author Carl Broberg
 * @author Tobias Danielsson
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
            new DeprecationRule(NodeType.WITH_STATEMENT,"With statement considered bad practice"),

            // -- Browser / Web Platform --
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedStandardMethod("escape"),
                    "escape() is deprecated, use encodeURIComponent() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedStandardMethod("unescape"),
                    "unescape() is deprecated, use decodeURIComponent() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedStandardMethod("document.write"),
                    "document.write() is deprecated"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedStandardMethod("document.createEvent"),
                    "document.createEvent() is deprecated, use new Event() or new CustomEvent() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedStandardMethod("document.execCommand"),
                    "document.execCommand() is deprecated, use the Clipboard API instead"),

            
            // -- Node.js -- Modules
            new DeprecationRule(NodeType.IMPORT_DECLARATION,
                    Conditions.deprecatedModule("punycode"),
                    "punycode is deprecated (DEP0040), use a userland alternative"),

            new DeprecationRule(NodeType.IMPORT_DECLARATION,
                    Conditions.deprecatedModule("domain"),
                    "domain is deprecated (DEP0032)"),


            // -- Node.js -- Util
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "isArray"),
                    "util.isArray() is deprecated (DEP0044), use Array.isArray() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "isBoolean"),
                    "util.isBoolean() is deprecated (DEP0045), use typeof x === 'boolean' instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "isBuffer"),
                    "util.isBuffer() is deprecated (DEP0046), use Buffer.isBuffer() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "isDate"),
                    "util.isDate() is deprecated (DEP0047), use x instanceof Date instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "isError"),
                    "util.isError() is deprecated (DEP0048), use Error.isError() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "isFunction"),
                    "util.isFunction() is deprecated (DEP0049), use typeof x === 'function' instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "isNull"),
                    "util.isNull() is deprecated (DEP0050), use x === null instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "isNumber"),
                    "util.isNumber() is deprecated (DEP0052), use typeof x === 'number' instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "isString"),
                    "util.isString() is deprecated (DEP0056), use typeof x === 'string' instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "isUndefined"),
                    "util.isUndefined() is deprecated (DEP0058), use x === undefined instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "log"),
                    "util.log() is deprecated (DEP0059), use console.log() with a timestamp instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("util", "_extend"),
                    "util._extend() is deprecated (DEP0060), use Object.assign() instead"),
            // -- Node.js -- Buffer
            new DeprecationRule(NodeType.NEW_EXPRESSION,
                    Conditions.deprecatedStandardMethod("Buffer"),
                    "new Buffer() is deprecated (DEP0005), use Buffer.from() or Buffer.alloc() instead"),
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedStandardMethod("Buffer"),
                    "Buffer() is deprecated (DEP0005), use Buffer.from() or Buffer.alloc() instead"),

            // -- Node.js -- URL
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("url", "parse"),
                    "url.parse() is deprecated, use new URL() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethodWithStringArgument("url", "format"),
                    "url.format(string) is deprecated, use new URL() instead"),

            // -- Node.js -- File System
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("fs", "exists"),
                    "fs.exists() is deprecated (DEP0034), use fs.access() or fs.stat() instead"),

            // -- Node.js -- Crypto
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("crypto", "createCipher"),
                    "crypto.createCipher() is deprecated (DEP0106), use crypto.createCipheriv() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("crypto", "createDecipher"),
                    "crypto.createDecipher() is deprecated (DEP0106), use crypto.createDecipheriv() instead"),

            // -- Node.js -- OS
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("os", "tmpDir"),
                    "os.tmpDir() is deprecated (DEP0022), use os.tmpdir() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("os", "getNetworkInterfaces"),
                    "os.getNetworkInterfaces() is deprecated (DEP0023), use os.networkInterfaces() instead"),

            // -- Node.js -- TLS
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("tls", "createSecurePair"),
                    "tls.createSecurePair() is deprecated (DEP0064), use tls.TLSSocket instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("tls", "parseCertString"),
                    "tls.parseCertString() is deprecated (DEP0076)"),

            // -- TC39 -- arguments.callee()
            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedStandardMethod("arguments.callee"),
                    "arguments.callee is deprecated, use named function expressions instead"),

            // -- React -- componentWillMount()
            new DeprecationRule(NodeType.IDENTIFIER,
                    Conditions.deprecatedLifecycleMethod("React.Component", "componentWillMount"),
                    "componentWillMount is deprecated, use componentDidMount instead"),

            // -- React -- componentWillReceiveProps()
            new DeprecationRule(NodeType.IDENTIFIER,
                    Conditions.deprecatedLifecycleMethod("React.Component", "componentWillReceiveProps"),
                    "componentWillReceiveProps is deprecated, use getDerivedStateFromProps instead"),

            // -- React -- componentWillUpdate()
            new DeprecationRule(NodeType.IDENTIFIER,
                    Conditions.deprecatedLifecycleMethod("React.Component", "componentWillUpdate"),
                    "componentWillUpdate is deprecated, use getSnapshotBeforeUpdate instead"),

            /* ---------------------------------------------------------------------
               Below: Not formally deprecated but not considered modern practice
            --------------------------------------------------------------------- */
            new DeprecationRule(NodeType.VARIABLE_DECLARATION,
                    Conditions.deprecatedKind("var"),
                    "var is deprecated, use let or const"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedStandardMethod("eval", "window.eval"),
                    "eval() is a security risk and should be avoided"),

            new DeprecationRule(NodeType.IMPORT_DECLARATION,
                    Conditions.deprecatedModule("left-pad"),
                    "left-pad is deprecated"),

            new DeprecationRule(NodeType.FUNCTION_EXPRESSION,
                    Conditions.deprecatedUnlessParentIs(NodeType.METHOD_DEFINITION),
                    "prefer arrow functions in modern JS"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("fs", "existsSync"),
                    "fs.existsSync is considered bad practice"),

            new DeprecationRule(NodeType.IMPORT_DECLARATION,
                    Conditions.deprecatedModule("querystring"),
                    "querystring module is deprecated, use URLSearchParams instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("react","createClass"),
                    "React.createClass() is deprecated, use ES6 class or create-react-class instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("react-dom", "render"),
                    "ReactDOM.render() is deprecated, use createRoot().render() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("react-dom", "hydrate"),
                    "ReactDOM.hydrate() is deprecated, use hydrateRoot() instead"),

            new DeprecationRule(NodeType.CALL_EXPRESSION,
                    Conditions.deprecatedImportedMethod("react-dom", "unmountComponentAtNode"),
                    "ReactDOM.unmountComponentAtNode() is deprecated, use root.unmount() instead")

                /*
            new evaluator.DeprecationRule(mapper.NodeType.CALL_EXPRESSION,
                    evaluator.Conditions.deprecatedStandardMethod("require"),
                    "Modern JS prefers imports from ES Modules") */
        );
    }
}
