package code.lexer.state;

public enum LexerState {
    DEFAULT,
    ERROR,
    MULTI_LINE_COMMENT,
    MULTI_LINE_COMMENT_PRE_END,
    PARAMETER,
    DOUBLE,
    CHAR,
    STRING,
    BACK_SLASH,
    EXPRESSION,
    INTERPOLATION,
}
