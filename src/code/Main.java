package code;

import code.lexer.Lexer;
import code.token.InvalidToken;
import code.token.ProcessedToken;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer();

        String pathToScalaInputFile = "src/resources/scala/Input.scala";
        try {
            lexer.processTokenInFile(pathToScalaInputFile);

            var processedTokens = lexer.getProcessedTokens();

            System.out.println("--- Tokens ---");
            printPretty(lexer.getTable(), processedTokens);

            System.out.println("\n--- Errors ---");
            printPrettyErrors(lexer.getErrorTokens());

        } catch (IOException e) {
            e.printStackTrace();
        }
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
