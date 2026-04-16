const acorn = require("acorn");
const fs = require("fs");

/**
 * Node.js file that uses Acorn to parse a string of JS code into a JSON AST
 * @type {string} is the JS code.
 */
const code = fs.readFileSync(process.stdin.fd, "utf-8");
const ast = acorn.parse(code, { ecmaVersion: 2020 });
console.log(JSON.stringify(ast));