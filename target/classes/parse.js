import * as acorn from "acorn";
import fs from"fs";
import jsx from "acorn-jsx"; // adds Acorn types to handle html inserted in the code

/**
 * Node.js file that uses Acorn to parse a string of JS code into a JSON AST
 * @type {string} is the JS code.
 */
const Parser = acorn.Parser.extend(jsx());
const code = fs.readFileSync(process.stdin.fd, "utf-8");

/**
 * Tries to parse the code as module first, and if it fails, try to parse as script.
 * Only the "module" sourceType can parse code that contains imports.
 * Only the "script" sourceType can parse code that is forbidden in strict mode.
 * @param code is a code snippet to parse.
 * @returns {*} an Acorn AST object
 */
function parseWithFallback(code) {
  try {
    return Parser.parse(code, {
      ecmaVersion: "latest",
      sourceType: "script",
      locations: true,
      allowWith: true,
    });
  } catch (e) {
    return Parser.parse(code, {
      ecmaVersion: "latest",
      sourceType: "module",
      locations: true,
    });
  }
}
const ast = parseWithFallback(code);
console.log(JSON.stringify(ast, (key, value) =>
    typeof value === 'bigint' ? null : value)); // replace any bigint with null so JSON can stringify it