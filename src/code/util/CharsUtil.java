package code.util;

// TODO refactor this util class
public class CharsUtil {
    private static final String UNIQUE_OPERATORS = "+-*/%(){}[]=<>^|&!?:$.,;";

    public static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    // TODO maybe refactor
    public static boolean isIdentifierSymbol(char c) {
        return Character.isLetter(c);
    }

    public static boolean isDot(char c) {
        return c == '.';
    }

    public static boolean isWhitespace(char c) {
        return c == ' ';
    }

    public static boolean isTab(char c) {
        return c == '\t';
    }

    public static boolean isOperator(char c) {
        return UNIQUE_OPERATORS.indexOf(c) != -1;
    }

    public static boolean isComment(char c1, char c2) {
        return (c1 == '/' && c2 == '/') || (c1 == '/' && c2 == '*');
    }

    public static boolean isSingleQuote(char c) {
        return c == '\'';
    }

    public static boolean isDoubleQuote(char c) {
        return c == '"';
    }

    public static boolean isBracket(char c) {
        return c == '[' || c == ']';
    }

    // TODO maybe refactor
    public static boolean isArrow(char c1, char c2) {
        return (c1 == '-' || c1 == '=') && c2 == '>';
    }

    public static boolean isCloseBrace(char c) {
        return c == '}';
    }

    // TODO maybe refactor
    public static boolean isEndOfToken(char c) {
        return isWhitespace(c) || isTab(c) || isOperator(c);
    }

    public static boolean isEscapeCharacter(char c) {
        return c == 'b' || c == 't' || c == 'n' || c == 'f' || c == 'r' || c == '\"' || c == '\'' || c == '\\';
    }
}
