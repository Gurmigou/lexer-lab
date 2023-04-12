package code;

import code.lexer.Lexer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer();

        String pathToScalaInputFile = "src/resources/scala/Input.scala";
        try {
            lexer.processTokenInFile(pathToScalaInputFile);

            var processedTokens = lexer.getProcessedTokens();
            processedTokens.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
