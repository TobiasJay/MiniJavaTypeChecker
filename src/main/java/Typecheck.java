import java.io.InputStream;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

import syntaxtree.*;
import visitor.Visitor;



public class Typecheck {
    public static void main (String [] args) throws ParseException {
        try {
            boolean testing = false;
            InputStream in = System.in;
    
            if (testing) {
                String filePath = "testcases/hw2/Basic-error.java";
                FileInputStream fis = new FileInputStream(filePath);
                in = fis;
            }
    
            new MiniJavaParser(in);
            Node root = MiniJavaParser.Goal();

            ArgObj arg = new ArgObj();
            // Construct a new visitor to traverse the syntax tree
            ClassInfoGatherer gatherer = new ClassInfoGatherer();
            // All we need this for is the classes and their methods

            // Start the first traversal with an argument
            root.accept(gatherer, arg);

            // Construct a new visitor to traverse the syntax tree
            Checker checker = new Checker();
            // Start the second traversal with an argument
            root.accept(checker, arg);

            System.out.print("Program type checked successfully\n");
    
            // The output of this homework should be one of:
            // * Type error
            // * Program type checked successfully
            // DO NOT THROW EXCEPTIONS FOR ERRORS
            if (testing) {
                ((FileInputStream) in).close();
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
