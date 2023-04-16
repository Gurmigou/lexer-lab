package code.token;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum TokenType {
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
    BY("by"),

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
    LAZY("lazy"),

    // Types
    Byte("Byte"),
    Short("Short"),
    Int("Int"),
    Long("Long"),
    Float("Float"),
    Double("Double"),
    Boolean("Boolean"),
    Char("Char"),
    String("String"),
    Unit("Unit"),
    Null("Null"),
    Any("Any"),
    AnyRef("AnyRef"),
    BigInt("BigInt"),
    BigDecimal("BigDecimal"),

    // Values
    ByteValue("Byte value"),
    ShortValue("Short value"),
    IntValue("Integer value"),
    DoubleValue("Double value"),
    StringValue("String value"),
    CharValue("Character value"),

    // --------------------------------------------

    // Math operators
    PLUS("plus", "+"),
    MINUS("minus", "-"),
    MULTIPLY("multiply", "*"),
    DIVIDE("divide", "/"),
    MODULO("modulo", "%"),

    // Math + logical operators
    EQUAL_TO("equalTo", "=="),
    NOT_EQUAL_TO("notEqualTo", "!="),
    GREATER_THAN("greaterThan", ">"),
    LESS_THAN("lessThan", "<"),
    GREATER_THAN_OR_EQUAL_TO("greaterThanOrEqualTo", ">="),
    LESS_THAN_OR_EQUAL_TO("lessThanOrEqualTo", "<="),

    // Boolean operators
    AND("and", "&&"),
    OR("or", "||"),
    NOT("not", "!"),

    // Assignment operators
    ASSIGN("assign", "="),
    PLUS_ASSIGN("plusAssign", "+="),
    MINUS_ASSIGN("minusAssign", "-="),
    MULTIPLY_ASSIGN("multiplyAssign", "*="),
    DIVIDE_ASSIGN("divideAssign", "/="),
    MODULO_ASSIGN("moduloAssign", "%="),

    // Bitwise operators
    BITWISE_AND("bitwiseAnd", "&"),
    BITWISE_OR("bitwiseOr", "|"),
    BITWISE_XOR("bitwiseXor", "^"),
    BITWISE_NOT("bitwiseNot", "~"),
    LEFT_SHIFT("leftShift", ">>"),
    RIGHT_SHIFT("rightShift", "<<"),
    UNSIGNED_RIGHT_SHIFT("unsignedRightShift", ">>>"),
    LEFT_SHIFT_ASSIGN("leftShiftAssign", "<<="),
    RIGHT_SHIFT_ASSIGN("rightShiftAssign", ">>="),
    BITWISE_AND_ASSIGN("bitwiseAndAssign", "&="),
    BITWISE_XOR_ASSIGN("bitwiseXorAssign", "^="),
    BITWISE_OR_ASSIGN("bitwiseOrAssign", "|="),
    UNSIGNED_RIGHT_SHIFT_ASSIGN("unsignedRightShiftAssign", ">>>="),

    // Type
    TYPE_COLON_OPERATOR("typeColonOperator", ":"),
    UPPER_TYPE_BOUND("upperTypeBound", "<:"),
    LOWER_TYPE_BOUND("lowerTypeBound", ">:"),
    VIEW_BOUND("viewBound", "<%"),

    // Other
    LEFT_ARROW("leftArrow", "<-"),
    RIGHT_ARROW("rightArrow", "->"),
    LEFT_EQUAL_ARROW("leftEqualArrow", "=>"),
    TWO_COLON("twoColon", "::"),
    PLUS_COLON("plusColon", "+:"),
    PLUS_PLUS("plusPlus", "++"),
    COLON_PLUS_PLUS("colonPlusPlus", ":++"),
    DOT("dot", "."),
    TWO_DOTS_RANGE("twoDotsRange", ".."),
    COMMA("comma", ","),
    SEMICOLON("semicolon", ";"),
    OPEN_PARENTHESIS("openParenthesis", "("),
    CLOSE_PARENTHESIS("closeParenthesis", ")"),
    OPEN_BRACKET("openBracket", "["),
    CLOSE_BRACKET("closeBracket", "]"),
    OPEN_BRACE("openBrace", "{"),
    CLOSE_BRACE("closeBrace", "}"),
    S_INTERPOLATOR("sInterpolator", "s"),

    // Format other
    SINGLE_LINE_COMMENT("singleLineComment"),
    MULTI_LINE_COMMENT("multiLineComment"),
    WHITESPACE("whitespace"),
    TAB("tab"),

    // Identifier
    IDENTIFIER("Identifier"),

    // Error
    INVALID_TOKEN("invalidToken");

    private static final Set<TokenType> operatorTokens;
    private static final Set<TokenType> keywordTokens;

    static {
        // Initialize operator tokens
        operatorTokens = Arrays.stream(TokenType.values())
                .filter(type -> type.ordinal() >= PLUS.ordinal() && type.ordinal() <= S_INTERPOLATOR.ordinal())
                .collect(Collectors.toSet());

        // Initialize keyword tokens
        keywordTokens = Arrays.stream(TokenType.values())
                .filter(type -> type.ordinal() < PLUS.ordinal())
                .collect(Collectors.toSet());
    }

    private final String name;
    private String operator;

    TokenType(String name) {
        this.name = name;
    }

    TokenType(String name, String operator) {
        this.name = name;
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public String getOperator() {
        return operator;
    }

    public static Set<TokenType> getOperatorTokens() {
        return operatorTokens;
    }

    public static Set<TokenType> getKeywordTokens() {
        return keywordTokens;
    }

    @Override
    public String toString() {
        String token = "Token: " + this.name();
        if (operatorTokens.contains(this)) {
            return token + " (" + this.operator + ")  | ";
        } else {
            return token + " (" + this.name + ")  | ";
        }
    }
}
