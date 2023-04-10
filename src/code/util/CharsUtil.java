package code.util;

// TODO refactor this util class
public class CharsUtil {
    public static boolean isDigit(char c) {
        return ('0' <= c && c <= '9');
    }

    public static boolean isFirstIdentifierSymbol(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || c == '_';
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

    public static boolean isPunctuation(char c) {
        String punctuation = "";
        return punctuation.indexOf(c) != -1;
    }

    public static boolean isOperator(char c) {
        String operators = "+-*/%^|&=<>!?:$.,;[]{}()";
        return operators.indexOf(c) != -1;
    }

    public static boolean isComment(char c1, char c2) {
        return c2 == '-' && (c1 == '-' || c1 == '{');
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

    public static boolean isArrow(char c1, char c2) {
        return (c1 == '-' || c1 == '=') && c2 == '>';
    }

    public static boolean isCloseBrace(char c) {
        return c == '}';
    }

    public static boolean isEndOfToken(char c) {
        return isWhitespace(c) || isTab(c) || isPunctuation(c) || isOperator(c);
    }
}
