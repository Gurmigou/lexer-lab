package code.lexer;

import code.lexer.state.LexerState;
import code.token.InvalidToken;
import code.token.ProcessedToken;
import code.token.TokenType;
import code.util.CharsUtil;
import code.util.CodeLineContainer;
import code.util.LexerCache;
import code.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final FiniteAutomata operatorsAutomata;
    private final FiniteAutomata keywordsAutomata;
    private LexerState lexerState = LexerState.DEFAULT;

    private final List<String> table;
    private final List<ProcessedToken> processedTokens;
    private final List<InvalidToken> errorTokens;

    private final LexerCache lexerCache;

    private CodeLineContainer codeLineContainer;

    public Lexer() {
        this.operatorsAutomata = new FiniteAutomata(TokenType.getOperatorTokens(), TokenType::getOperator);
        this.keywordsAutomata = new FiniteAutomata(TokenType.getKeywordTokens(), TokenType::getName);
        this.table = new ArrayList<>();
        this.processedTokens = new ArrayList<>();
        this.errorTokens = new ArrayList<>();
        this.lexerCache = new LexerCache();
    }

    public void processTokenInFile(String path) throws IOException {
        List<String> fileLines = Files.readAllLines(Path.of(path));

        this.codeLineContainer = new CodeLineContainer();

        for (String line : fileLines) {
            codeLineContainer.setCodeLine(line);
            codeLineContainer.setColumn(0);

            while (!codeLineContainer.isEnded()) {
                processNextToken();
            }

            codeLineContainer.incrementRow();
        }
    }

    private void processNextToken() {
        if (lexerState != LexerState.DEFAULT) {
            processMultipleLines();
            return;
        }

        char cur = codeLineContainer.getNextSymbol();

        if (CharsUtil.isTab(cur))
            addProcessedToken(TokenType.TAB, 1);
        else if (CharsUtil.isWhitespace(cur))
            addProcessedToken(TokenType.WHITESPACE, 1);
        else if ((cur == '-' || cur == '+') && !codeLineContainer.isEnded(1) && CharsUtil.isDigit(codeLineContainer.getNextSymbol(1)))
            processNumber();
        else if (!codeLineContainer.isEnded(1) && CharsUtil.isComment(cur, codeLineContainer.getNextSymbol(1)))
            processComment();
        else if (CharsUtil.isIdentifierSymbol(cur))
            processIdentifier();
        else if (CharsUtil.isSingleQuote(cur))
            processSingleQuote();
        else if (CharsUtil.isDoubleQuote(cur))
            processDoubleQuote();
        else if (CharsUtil.isOperator(cur))
            processOperator();
        else {
            // TODO check next value in debugger
            char next = codeLineContainer.getNextSymbol();
            InvalidToken error = new InvalidToken(codeLineContainer.getRow(), codeLineContainer.getColumn(),
                    InvalidToken.INVALID_TOKEN_MSG, String.valueOf(next));
            this.errorTokens.add(error);

            codeLineContainer.incrementColumn();
        }
    }

    private void processOperator() {
        var tokenPair = operatorsAutomata.findToken(codeLineContainer.getCodeLine(),
                codeLineContainer.getColumn());
        TokenType tokenType = tokenPair.getFirst();
        int tokenEndPos = tokenPair.getSecond();

        if (tokenType == TokenType.INVALID_TOKEN) {
            processError(tokenEndPos - codeLineContainer.getColumn());
        } else {
            addProcessedToken(tokenType, tokenEndPos - codeLineContainer.getColumn());
        }
    }

    private void processSingleQuote() {
        this.lexerState = LexerState.CHAR;
        char next = codeLineContainer.getNextSymbol(1);
        if (next == '\\') {
            // Handling escaped characters
            if (CharsUtil.isEscapeCharacter(next) && !codeLineContainer.isEnded(3) && codeLineContainer.getNextSymbol(3) == '\'') {
                addProcessedToken(TokenType.CharValue, 4);
            } else {
                processError(1);
            }
        } else if (!codeLineContainer.isEnded(2) && codeLineContainer.getNextSymbol(2) == '\'') {
            addProcessedToken(TokenType.CharValue, 3);
        } else {
            processError(1);
        }
    }

    private void processDoubleQuote() {
        this.lexerState = LexerState.STRING;

        char cur;
        int pos = 0;

        while (!codeLineContainer.isEnded(pos + 1)) {
            pos++;
            cur = codeLineContainer.getNextSymbol();
            if (lexerState == LexerState.STRING) {
                if (cur == '\\') {
                    this.lexerState = LexerState.BACK_SLASH;
                } else if (cur == '\"') {
                    addProcessedToken(TokenType.StringValue, pos + 1);
                    return;
                }
            } else if (lexerState == LexerState.BACK_SLASH) {
                this.lexerState = LexerState.STRING;
            }
        }

        // TODO: implement correct handling of error
        processError(codeLineContainer.getColumn());
    }

    private void processIdentifier() {
        int pos = 0;
        char cur;
        while (!codeLineContainer.isEnded(pos + 1)) {
            pos++;
            cur = codeLineContainer.getNextSymbol(pos);
            if (CharsUtil.isEndOfToken(cur)) {
                var tokenPair = keywordsAutomata.findToken(codeLineContainer.getCodeLine(),
                        codeLineContainer.getColumn());
                TokenType tokenType = tokenPair.getFirst();

                // means that the Lexer has found an identifier
                if (tokenType == TokenType.INVALID_TOKEN) {
                    addProcessedToken(TokenType.IDENTIFIER, pos);
                } else {
                    addProcessedToken(tokenType, pos);
                }
                return;
            }
        }
        // TODO: MAY BE TO DO after cycle condition
    }

    private void processNumber() {
        this.lexerState = LexerState.INTEGER;

        char cur = codeLineContainer.getNextSymbol();
        int pos = 0;

        // handling singed number
        if (cur == '-' || cur == '+') {
            cur = codeLineContainer.getNextSymbol(1);
            pos++;
        }

        var firstNumberPart = readNumber(cur, pos);
        cur = firstNumberPart.getFirst();
        pos = firstNumberPart.getSecond();

        if (CharsUtil.isDot(cur)) {
            this.lexerState = LexerState.DOUBLE;
            pos++;
            cur = codeLineContainer.getNextSymbol(pos);
        }

        var secondNumberPart = readNumber(cur, pos);
        cur = secondNumberPart.getFirst();
        pos = secondNumberPart.getSecond();

        if (codeLineContainer.isEnded(pos + 1) || CharsUtil.isEndOfToken(cur)) {
            TokenType tokenType;
            if (lexerState == LexerState.DOUBLE)
                tokenType = TokenType.DoubleValue;
            else
                tokenType = TokenType.IntValue;
            addProcessedToken(tokenType, pos + 1);
        } else {
            processError(pos);
        }
    }

    private Pair<Character, Integer> readNumber(char cur, int pos) {
        while (!codeLineContainer.isEnded(pos + 1) && CharsUtil.isDigit(cur)) {
            pos++;
            cur = codeLineContainer.getNextSymbol(pos);
        }
        return Pair.of(cur, pos);
    }

    private void processMultipleLines() {
        if (lexerState == LexerState.MULTI_LINE_COMMENT) {
            processMultilineComment();
        } else {
            processError(0);
        }
    }

    private void processComment() {
        char next = codeLineContainer.getNextSymbol(1);
        if (next == '/') {
            int tokenLength = codeLineContainer.getCodeLine().length() - codeLineContainer.getColumn();
            addProcessedToken(TokenType.SINGLE_LINE_COMMENT, tokenLength);
        } else {
            processMultilineComment();
        }
    }

    private void processMultilineComment() {
        this.lexerState = LexerState.MULTI_LINE_COMMENT;

        char cur;
        int pos = 0;

        while (!codeLineContainer.isEnded(pos + 1)) {
            pos++;
            cur = codeLineContainer.getNextSymbol(pos);

            if (lexerState == LexerState.MULTI_LINE_COMMENT) {
                if (cur == '*') {
                    this.lexerState = LexerState.MULTI_LINE_COMMENT_PRE_END; // requires also '/' symbol
                }
            } else {
                if (cur == '/') {
                    addProcessedMultilineToken(TokenType.MULTI_LINE_COMMENT, pos + 1);
                    return;
                } else {
                    // TODO maybe required changes
                    this.lexerState = LexerState.MULTI_LINE_COMMENT;
                }
            }
        }

        if (lexerCache.isNonEmpty())
            lexerCache.add("\\n");

        lexerCache.add(codeLineContainer.getCodeLine().substring(codeLineContainer.getColumn(),
                codeLineContainer.getColumn() + pos));
        codeLineContainer.setColumn(codeLineContainer.getColumn() + pos + 1);
        this.lexerState = LexerState.MULTI_LINE_COMMENT;
    }

    private void processError(int pos) {
        this.lexerState = LexerState.ERROR;
        char cur = codeLineContainer.getNextSymbol(pos);
        while (CharsUtil.isEndOfToken(cur) || !codeLineContainer.isEnded(pos + 1)) {
            pos++;
            cur = codeLineContainer.getNextSymbol(pos);
        }

        String invalidToken = codeLineContainer.getCodeLine().substring(codeLineContainer.getColumn(), pos);
        InvalidToken error = new InvalidToken(codeLineContainer.getRow(), codeLineContainer.getColumn(),
                InvalidToken.INVALID_TOKEN_MSG, invalidToken);
        this.errorTokens.add(error);
    }

    private void addProcessedToken(TokenType tokenType, int tokenLength) {
        String tableItem = codeLineContainer.getCodeLine().substring(codeLineContainer.getColumn(),
                codeLineContainer.getColumn() + tokenLength);
        addProcessedTokenHelper(tokenType, tokenLength, tableItem);
    }

    private void addProcessedMultilineToken(TokenType tokenType, int tokenLength) {
        String tableItem = lexerCache.getCache() + codeLineContainer.getCodeLine().substring(codeLineContainer.getColumn(),
                codeLineContainer.getColumn() + tokenLength);
        addProcessedTokenHelper(tokenType, tokenLength, tableItem);
        this.lexerCache.clear();
    }

    private void addProcessedTokenHelper(TokenType tokenType, int tokenLength, String tableItem) {
        ProcessedToken token = new ProcessedToken(codeLineContainer.getRow(), codeLineContainer.getColumn(),
                tokenType, this.table.size());
        this.table.add(tableItem);
        codeLineContainer.setColumn(codeLineContainer.getColumn() + tokenLength);
        this.processedTokens.add(token);
        this.lexerState = LexerState.DEFAULT;
    }

    public List<String> getTable() {
        return table;
    }

    public List<ProcessedToken> getProcessedTokens() {
        return processedTokens;
    }

    public List<InvalidToken> getErrorTokens() {
        return errorTokens;
    }
}

