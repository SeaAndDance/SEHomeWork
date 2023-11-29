// prettier.config.js
module.exports = {
    arrowParens: "avoid",
    bracketSpacing: true,
    endOfLine: "crlf",
    semi: true,
    printWidth: 120,
    trailingComma: "none",
    overrides: [
        {
            files: ["*.css", "*.ts"],
            options: {
                tabWidth: 4,
            }
        }
    ]
};
