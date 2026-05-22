import * as acorn from "acorn";
import fs from"fs";
import jsx from "acorn-jsx";

/**
 * Node.js file that uses Acorn to parse a string of JS code into a JSON AST
 * @type {string} is the JS code.
 */

const Parser = acorn.Parser.extend(jsx());
const code = fs.readFileSync(process.stdin.fd, "utf-8");
const ast = Parser.parse(code, { ecmaVersion: 2020, sourceType: "module" });
console.log(JSON.stringify(ast));