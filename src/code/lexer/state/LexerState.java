package code.lexer.state;

public enum LexerState {
    DEFAULT,
    ERROR,
    SINGLE_LINE_COMMENT,
    MULTI_LINE_COMMENT,
    INTEGER,
    FLOAT,
    CHAR,
    STRING,
    IDENTIFIER
}
