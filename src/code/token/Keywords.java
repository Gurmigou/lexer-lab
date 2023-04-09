package code.token;

public enum Keywords {
    // Control Structures and Flow Control
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    DO("do"),
    FOR("for"),
    YIELD("yield"),
    RETURN("return"),
    THROW("throw"),
    TRY("try"),
    CATCH("catch"),
    FINALLY("finally"),
    UNTIL("until"),
    TO("to"),

    // Pattern Matching
    CASE("case"),
    MATCH("match"),

    // Types and Type-related Keywords
    ABSTRACT("abstract"),
    CLASS("class"),
    TRAIT("trait"),
    TYPE("type"),
    SEALED("sealed"),
    EXTENDS("extends"),
    WITH("with"),
    FINAL("final"),

    // Access Modifiers
    PRIVATE("private"),
    PROTECTED("protected"),

    // Method and Value Definitions
    DEF("def"),
    VAL("val"),
    VAR("var"),

    // Object-related Keywords
    OBJECT("object"),
    NEW("new"),
    THIS("this"),
    SUPER("super"),

    // Boolean Literals
    TRUE("true"),
    FALSE("false"),

    // Special Value for Null References
    NULL("null"),

    // Imports and Packages
    IMPORT("import"),
    PACKAGE("package"),

    // Overriding and Implementing Members
    OVERRIDE("override"),
    IMPLICIT("implicit"),

    // Existential Types
    FOR_SOME("forSome"),

    // Lazy Initialization
    LAZY("lazy");

    private final String name;

    Keywords(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
