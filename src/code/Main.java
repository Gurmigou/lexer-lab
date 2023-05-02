package code;

import code.lexer.Lexer;
import code.token.InvalidToken;
import code.token.ProcessedToken;
import code.token.TokenType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private static final String INPUT_FILE_PATH = "src/resources/scala/Input.scala";
    private static final String OUTPUT_FILE_PATH = "src/resources/scala/Output.txt";

    // Attention! Use file Input.scala in resources/scala folder as input file. Write any Scala code you want in it.
    // The output will be written in Output.txt file in the same folder.
    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        try {
            lexer.processTokenInFile(INPUT_FILE_PATH);

            // consoleWriter(lexer);
            tokensTableFileWriter(lexer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void tokensTableFileWriter(Lexer lexer) {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE_PATH)) {
            Formatter formatter = new Formatter();

            writer.write("--------------------------------------- Tokens ---------------------------------------\n");
            formatter.format("%-37s%-14s\t\t\t\t\t%s\n", "Token", "(row | column)", "Value");
            writer.write(formatter.toString());
            writer.write("--------------------------------------------------------------------------------------\n");

            for (int index = 0; index < lexer.getProcessedTokens().size(); index++) {
                ProcessedToken token = lexer.getProcessedTokens().get(index);

                // Ignore whitespace tokens in the Tokens table. They are displayed in the Symbol table
                if (token.getTokenType() == TokenType.WHITESPACE)
                    continue;

                formatter = new Formatter();
                formatter.format("%-37s%-14s\t\t\t\t%s\n",
                        token.getTokenType().name(),
                        String.format("\t%d | %d", token.getRow(), token.getColumn()),
                        lexer.getTable().get(index));

                writer.write(formatter.toString());
            }

            formatter = new Formatter();
            writer.write("\n\n--------------------------------------- Errors ---------------------------------------\n");
            formatter.format("%-37s%-14s\t\t\t\t\t%s\n", "Invalid symbol", "(row | column)", "Error Message");
            writer.write(formatter.toString());
            writer.write("--------------------------------------------------------------------------------------\n");

            for (int index = 0; index < lexer.getErrorTokens().size(); index++) {
                InvalidToken token = lexer.getErrorTokens().get(index);

                formatter = new Formatter();
                formatter.format("%-37s%-14s\t\t\t\t%s\n",
                        token.getInvalidSymbol(),
                        String.format("\t%d | %d", token.getRow(), token.getColumn()),
                        token.getErrorMessage());

                writer.write(formatter.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void consoleWriter(Lexer lexer) {
        System.out.println("--- Tokens ---");
        printPretty(lexer.getTable(), lexer.getProcessedTokens());

        System.out.println("\n--- Errors ---");
        printPrettyErrors(lexer.getErrorTokens());
    }

    private static void printPretty(List<String> lexerTable, List<ProcessedToken> processedTokens) {
        if (processedTokens.isEmpty())
            return;

        var groupedList = processedTokens.stream()
                .collect(Collectors.groupingBy(ProcessedToken::getRow))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .toList();

        for (var entry : groupedList) {
            StringBuilder stringBuilder = new StringBuilder((entry.getKey()) + ": ");

            for (var token : entry.getValue()) {
                stringBuilder
                        .append(token.getTokenType().name())
                        .append('[')
                        .append(lexerTable.get(token.getSymbolTableIndex()))
                        .append("], ");
            }

            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            System.out.println(stringBuilder);
        }
    }

    private static void printPrettyErrors(List<InvalidToken> invalidTokens) {
        if (invalidTokens.isEmpty())
            return;

        StringBuilder stringBuilder = new StringBuilder();

        for (var error : invalidTokens) {
            stringBuilder
                    .append(error.toString())
                    .append(": ")
                    .append(error.getFullErrorMessage())
                    .append('\n');
        }

        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        System.out.println(stringBuilder);
    }
}
