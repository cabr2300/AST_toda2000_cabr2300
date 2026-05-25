import * as acorn from "acorn";
import fs from"fs";
import jsx from "acorn-jsx"; // adds Acorn types to handle html inserted in the code
import { tsPlugin } from "acorn-typescript"; // adds TypeScript parsing capability

/**
 * Node.js file that uses Acorn to parse a string of JS code into a JSON AST
 * @type {string} is the JS code.
 */
const Parser = acorn.Parser.extend(tsPlugin(), jsx());
const code = fs.readFileSync(process.stdin.fd, "utf-8");
const ast = Parser.parse(code, { ecmaVersion: "latest", sourceType: "module", locations: true });
console.log(JSON.stringify(ast));