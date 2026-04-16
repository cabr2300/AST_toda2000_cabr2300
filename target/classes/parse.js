const acorn = require("acorn");
const fs = require("fs");

const code = fs.readFileSync(process.stdin.fd, "utf-8");
const ast = acorn.parse(code, { ecmaVersion: 2020 });
console.log(JSON.stringify(ast));