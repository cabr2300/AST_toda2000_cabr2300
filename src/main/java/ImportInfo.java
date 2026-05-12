/**
 * Record for keeping track of by what name an imported module is used in the code.
 * Used to keep track of aliases and wildcard imports.
 * @param sourceModule is the name of the source module.
 * @param importedName is the name by which it is used, which may be the real name or an alias.
 * @author Carl Broberg
 */
public record ImportInfo(String sourceModule, String importedName) {
}
