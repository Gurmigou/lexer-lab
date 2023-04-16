package code.lexer.state;

public enum LexerState {
    DEFAULT,
    ERROR,
    SINGLE_LINE_COMMENT,
    MULTI_LINE_COMMENT,
    MULTI_LINE_COMMENT_PRE_END,
    PARAMETER,
    INTEGER,
    DOUBLE,
    CHAR,
    STRING,
    BACK_SLASH,
    IDENTIFIER
}
