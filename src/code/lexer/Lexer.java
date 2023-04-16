package code.lexer;

import code.lexer.state.LexerState;
import code.token.InvalidToken;
import code.token.ProcessedToken;
import code.token.TokenType;
import code.util.CodeLineContainer;
import code.util.LexerCache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static code.util.CharsUtil.*;

public class Lexer {
    private final FiniteAutomata operatorsAutomata;
    private final FiniteAutomata keywordsAutomata;
    private LexerState lexerState = LexerState.DEFAULT;

    private final List<String> table;
    private final List<ProcessedToken> processedTokens;
    private final List<InvalidToken> errorTokens;

    private final LexerCache lexerCache;

    private CodeLineContainer container;

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

        this.container = new CodeLineContainer();

        for (String line : fileLines) {
            container.setCodeLine(line);
            container.setColumn(0);

            while (!container.isEnded()) {
                processNextToken();
            }

            container.incrementRow();
        }
    }

    private void processNextToken() {
        if (lexerState != LexerState.DEFAULT && lexerState != LexerState.PARAMETER) {
            processMultipleLines();
            return;
        }

        char cur = container.getShifted();

        if (isTab(cur))
            addProcessedToken(TokenType.TAB, 1);
        else if (isWhitespace(cur))
            addProcessedToken(TokenType.WHITESPACE, 1);
        else if (isDigit(cur) || ((cur == '-' || cur == '+') && !container.isEnded(1) && isDigit(container.getShifted(1))))
            processNumber();
        else if (!container.isEnded(1) && isComment(cur, container.getShifted(1)))
            processComment();
        else if (isIdentifierSymbol(cur))
            processIdentifier();
        else if (isSingleQuote(cur))
            processSingleQuote();
        else if (isDoubleQuote(cur))
            processDoubleQuote();
        else if (isOperator(cur))
            processOperator();
        else {
            // TODO check next value in debugger
            char next = container.getShifted();
            InvalidToken error = new InvalidToken(container.getRow(), container.getColumn(),
                    "", String.valueOf(next));
            this.errorTokens.add(error);

            container.incrementColumn();
        }
    }

    private void processOperator() {
        var tokenPair = operatorsAutomata.findToken(container.getCodeLine(),
                container.getColumn());
        TokenType tokenType = tokenPair.getFirst();
        int tokenEndPos = tokenPair.getSecond();

        if (tokenType == TokenType.OPEN_PARENTHESIS)
            this.lexerState = LexerState.PARAMETER;
        else if (this.lexerState == LexerState.PARAMETER && tokenType == TokenType.CLOSE_PARENTHESIS)
            this.lexerState = LexerState.DEFAULT;

        if (tokenType == TokenType.INVALID_TOKEN) {
            processError(tokenEndPos - container.getColumn());
        } else {
            addProcessedToken(tokenType, tokenEndPos - container.getColumn());
        }
    }

    private void processSingleQuote() {
        this.lexerState = LexerState.CHAR;
        char next = container.getShifted(1);
        if (next == '\\') {
            // Handling escaped characters
            if (isEscapeCharacter(next) && !container.isEnded(3) && container.getShifted(3) == '\'') {
                addProcessedToken(TokenType.CharValue, 4);
            } else {
                processError(1);
            }
        } else if (!container.isEnded(2) && container.getShifted(2) == '\'') {
            addProcessedToken(TokenType.CharValue, 3);
        } else {
            processError(1);
        }
    }

    private void processDoubleQuote() {
        this.lexerState = LexerState.STRING;

        char cur;
        int pos = 0;

        while (!container.isEnded(pos + 1)) {
            pos++;
            cur = container.getShifted(pos);
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

        processError(0, "Missing ending double quote.");
    }

    private void processIdentifier() {
        int pos = 0;
        char cur;
        while (!container.isEnded(pos + 1)) {
            pos++;
            cur = container.getShifted(pos);
            if (isEndOfToken(cur)) {
                var tokenPair = keywordsAutomata.findToken(container.getCodeLine(),
                        container.getColumn());
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

        TokenType tokenType = keywordsAutomata.findToken(container.getCodeLine(), container.getColumn()).getFirst();
        pos++;
        if (tokenType == TokenType.INVALID_TOKEN)
            addProcessedToken(TokenType.IDENTIFIER, pos);
        else
            addProcessedToken(tokenType, pos);
    }

    private void processNumber() {
        int pos = 0;

        // handling signed number
        if (container.getShifted() == '-' || container.getShifted() == '+')
            pos++;

        while (!container.isEnded(pos) && isDigit(container.getShifted(pos))) {
            pos++;
        }

        if (!container.isEnded(pos) && isDot(container.getShifted(pos))) {
            this.lexerState = LexerState.DOUBLE;
            pos++;

            while (!container.isEnded(pos) && isDigit(container.getShifted(pos))) {
                pos++;
            }
        } else if (lexerState != LexerState.PARAMETER && !container.isEnded(pos) && isComma(container.getShifted(pos))) {
            processError(container.getColumn(), container.getColumn() + pos + 1,
                    "Invalid float value format.");
            return;
        }

        if (container.isEnded(pos) || isEndOfToken(container.getShifted(pos))) {
            TokenType tokenType;
            if (lexerState == LexerState.DOUBLE)
                tokenType = TokenType.DoubleValue;
            else
                tokenType = TokenType.IntValue;
            addProcessedToken(tokenType, pos);
        } else {
            processError(pos);
        }
    }

    private void processMultipleLines() {
        if (lexerState == LexerState.MULTI_LINE_COMMENT) {
            processMultilineComment();
        } else {
            processError(0);
        }
    }

    private void processComment() {
        char next = container.getShifted(1);
        if (next == '/') {
            int tokenLength = container.getCodeLine().length() - container.getColumn();
            addProcessedToken(TokenType.SINGLE_LINE_COMMENT, tokenLength);
        } else {
            processMultilineComment();
        }
    }

    private void processMultilineComment() {
        this.lexerState = LexerState.MULTI_LINE_COMMENT;

        char cur;
        int pos = 0;

        while (!container.isEnded(pos + 1)) {
            pos++;
            cur = container.getShifted(pos);

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

        lexerCache.add(container.getCodeLine().substring(container.getColumn(),
                container.getColumn() + pos));
        container.setColumn(container.getColumn() + pos + 1);
        this.lexerState = LexerState.MULTI_LINE_COMMENT;
    }

    private void processError(int pos, String... msg) {
        this.lexerState = LexerState.ERROR;
        char cur = container.getShifted(pos);
        while (isEndOfToken(cur) || !container.isEnded(pos + 1)) {
            pos++;
            cur = container.getShifted(pos);
        }

        String invalidToken = container.getCodeLine().substring(container.getColumn(), container.getColumn() + pos + 1);
        InvalidToken error = new InvalidToken(container.getRow(), container.getColumn(),
                (msg != null && msg.length > 0) ? msg[0] : "", invalidToken);
        this.errorTokens.add(error);
        processErrorCommon(container.getColumn() + pos + 1);
    }

    private void processError(int start, int end, String msg) {
        this.lexerState = LexerState.ERROR;
        String invalidToken = container.getCodeLine().substring(start, end);

        InvalidToken error = new InvalidToken(container.getRow(), container.getColumn(),
                msg, invalidToken);
        this.errorTokens.add(error);
        processErrorCommon(end);
    }

    private void processErrorCommon(int start) {
        this.container.setColumn(start);
        this.lexerState = LexerState.DEFAULT;
    }

    private void addProcessedToken(TokenType tokenType, int tokenLength) {
        String tableItem = container.getCodeLine().substring(container.getColumn(),
                container.getColumn() + tokenLength);
        addProcessedTokenHelper(tokenType, tokenLength, tableItem);
    }

    private void addProcessedMultilineToken(TokenType tokenType, int tokenLength) {
        String tableItem = lexerCache.getCache() + container.getCodeLine().substring(container.getColumn(),
                container.getColumn() + tokenLength);
        addProcessedTokenHelper(tokenType, tokenLength, tableItem);
        this.lexerCache.clear();
    }

    private void addProcessedTokenHelper(TokenType tokenType, int tokenLength, String tableItem) {
        ProcessedToken token = new ProcessedToken(container.getRow(), container.getColumn(),
                tokenType, this.table.size());
        this.table.add(tableItem);
        container.setColumn(container.getColumn() + tokenLength);
        this.processedTokens.add(token);

        if (this.lexerState != LexerState.PARAMETER)
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

