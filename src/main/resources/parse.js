import * as acorn from "acorn";
import fs from"fs";
import jsx from "acorn-jsx"; // adds Acorn types to handle html inserted in the code

/**
 * Node.js file that uses Acorn to parse a string of JS code into a JSON AST
 * @type {string} is the JS code.
 */
const Parser = acorn.Parser.extend(jsx());
const code = fs.readFileSync(process.stdin.fd, "utf-8");
// Use sourceType: "script" to parse snippets containing WithStatement, or sourceType: "module" to parse imports
const ast = Parser.parse(code, { ecmaVersion: "latest", sourceType: "module", locations: true, allowWith: true });
console.log(JSON.stringify(ast, (key, value) =>
    typeof value === 'bigint' ? null : value)); // replace any bigint with null so JSON can stringify it