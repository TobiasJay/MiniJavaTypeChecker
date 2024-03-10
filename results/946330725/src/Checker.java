import syntaxtree.*;
import visitor.*;
import java.util.*;
    /*
     * Implement a type checker using visitor pattern
     * Feel free to rename or make any change in file
     */

    // The output of this homework should be one of:
    // * Type error
    // * Program type checked successfully
    // Print the message to STDOUT
    // DO NOT THROW EXCEPTIONS FOR ERRORS OR PRINT TO STDERR

// Again, feel free to rename or make any change to this file
// This is mainly given as a reference for you to get started

/**
 * [ClassInfoGatherer] is responsible for the first traversal of the syntax tree:
 * - visit each class including the main class to find their names
 * - check whether the class names in the input program are distinct
 * In addition, you may want to gather more information and implement some helper functions
 * for the Checker.
 * For example:
 * - distinct() checks if the passed identifiers are pair-wise distinct
 * - fields() returns a set of fields declared in a given class
 * - methodType() returns the type of methods given names
 * */

// Depending on your design of the visitor, you may want to change the argument and return type
// They are set to `Object` right now

public class Checker extends GJDepthFirst<Object, Object> {
    boolean diag = false;

    public boolean distinct(String... names) {
        Set<String> set = new HashSet<>();
        for (String name : names) {
            if (!set.add(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * f0 -> MainClass()
     * f1 -> ( ClassDeclaration() )*
     * f2 -> <EOF>
     */
    public Object visit(Goal n, Object argu) {
        if (diag) {
            System.out.println("Entering Goal");
        }
        // Check the class names are distinct
        

        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    public Object visit(MainClass n, Object arg) {
        ArgObj argu = (ArgObj) arg;

        if (diag) {
            System.out.println("Entering MainClass");
        }
        // Check that the main class id
        argu.scopePath.add(0, n.f1.f0.tokenImage);


        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        n.f13.accept(this, argu);
        argu.scopePath.add(0, "main");
        n.f14.accept(this, argu);
        n.f15.accept(this, argu);
        n.f16.accept(this, argu);
        argu.scopePath.remove(0);

        n.f17.accept(this, argu);
        argu.scopePath.remove(0);

        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public Object visit(ClassDeclaration n, Object arg) {
        ArgObj argu = (ArgObj) arg;
        argu.scopePath.add(0, n.f1.f0.tokenImage);


        if (diag) {
            System.out.println("Entering ClassDeclaration");
        }
        String ClassName = n.f1.f0.tokenImage;
        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        argu.scopePath.remove(0);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public Object visit(VarDeclaration n, Object arg) {
        ArgObj argu = (ArgObj) arg;
        if (diag) {
            System.out.println("Entering VarDeclaration");
        }
        

        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    public Object visit(MethodDeclaration n, Object arg) {
        ArgObj argu = (ArgObj) arg;
        // Check the return type of the method
        // Check that the expression is of the same type as the return type
        if (diag) {
            System.out.println("Entering MethodDeclaration");
        }

        String className = null;
        for (String c : argu.classes) {
            if (argu.scopePath.get(0).equals(c)) {
                className = c;
                break;
            }
        }
        String fullMethName= ""+className +"-"+ n.f2.f0.tokenImage;    

        argu.scopePath.add(0, fullMethName);

        Object _ret = null;
        n.f0.accept(this, argu);
        String returnType = (String) n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        // Make sure f4 formal parameters have distinct names
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        String returnExp = (String) n.f10.accept(this, argu);
        if (!returnType.equals(returnExp)) {
            System.out.print("Type error\n");
            System.exit(0);
        }
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        argu.scopePath.remove(0);
        return _ret;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public Object visit(FormalParameterList n, Object argu) {
        if (diag) {
            System.out.println("Entering FormalParameterList");
        }

        ArrayList<String> names = new ArrayList<>();
        
        String FullName = (String) n.f0.accept(this, argu);
        String justName = FullName.substring(FullName.indexOf(" ") + 1);
        names.add(justName);
        for (int j = 0; j < n.f1.size(); j++) {

            String typeAndName = (String) n.f1.elementAt(j).accept(this, argu);
            String Name = typeAndName.substring(typeAndName.indexOf(" ") + 1);
            names.add(Name);
        }
        if (!distinct(names.toArray(new String[0]))) {
            System.out.print("Type error\n");
            System.exit(0);
        }
        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public String visit(FormalParameter n, Object arg) {
        if (diag) {
            System.out.println("Entering FormalParameter");
        }
        ArgObj argu = (ArgObj) arg;
        // If type is a class, check that the class exists and make the id of the name of that class



        String _ret = n.f0.accept(this,argu)+" "+n.f1.f0.tokenImage;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        //System.out.println(_ret);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public String visit(FormalParameterRest n, Object argu) {
        if (diag) {
            System.out.println("Entering FormalParameterRest");
        }
        String _ret = ""+n.f1.accept(this, argu);

        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public Object visit(Type n, Object argu) {
        if (diag) {
            System.out.println("Entering Type");
        }
        Object _ret = (String) n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public Object visit(ArrayType n, Object argu) {
        if (diag) {
            System.out.println("Entering ArrayType");
        }
        Object _ret = "ArrayType";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "boolean"
     */
    public Object visit(BooleanType n, Object argu) {
        if (diag) {
            System.out.println("Entering BooleanType");
        }
        Object _ret = "BooleanType";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
     */
    public Object visit(IntegerType n, Object argu) {
        if (diag) {
            System.out.println("Entering IntegerType");
        }
        Object _ret = "IntegerType";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Block()
     *       | AssignmentStatement()
     *       | ArrayAssignmentStatement()
     *       | IfStatement()
     *       | WhileStatement()
     *       | PrintStatement()
     */
    public Object visit(Statement n, Object argu) {
        if (diag) {
            System.out.println("Entering Statement");
        }
        Object _ret = null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    public Object visit(Block n, Object argu) {
        // Check that all statements are valid ?? idk about this one blocks are confusing!
        if (diag) {
            System.out.println("Entering Block");
        }
        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public Object visit(AssignmentStatement n, Object arg) {
        if (diag) {
            System.out.println("Entering AssignmentStatement");
        }
        String _ret = null;
        ArgObj argu = (ArgObj) arg;
        // Check that the identifier is in the type environment, and find its type
        // Check that the expression is of the same type as the identifier
        // This does this! hooray!
        // Why is exp v_Age type instead of int?
        String id = (String) n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String exp = (String) n.f2.accept(this, argu);
        //System.out.println("id: " + id + " exp: " + exp);
        if (!id.equals(exp)) {
            System.out.print("Type error\n");
            System.exit(0);
        } 

        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
    public Object visit(ArrayAssignmentStatement n, Object argu) {
        // Check that the 1st identifier is an array
        // Check that the 2nd expression is an integer
        // Check that the 3rd expression is an integer
        if (diag) {
            System.out.println("Entering ArrayAssignmentStatement");
        }

        if (!n.f0.accept(this, argu).equals("ArrayType")) {
            System.out.print("Type error\n");
            System.exit(0);
        } else if (!n.f2.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        } else if (!n.f5.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
    public Object visit(IfStatement n, Object argu) {
        if (diag) {
            System.out.println("IfStatement");
        }
        // Check that the expression is a boolean
        if (!n.f2.accept(this, argu).equals("BooleanType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public Object visit(WhileStatement n, Object arg) {
        ArgObj argu = (ArgObj) arg;
        // Check that the expression is a boolean
        // Check that the statement is a block ?? idk about this one
        if (diag) {
            System.out.println("WhileStatement");
        }
        if (!n.f2.accept(this, argu).equals("BooleanType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public Object visit(PrintStatement n, Object argu) {

        if (diag) {
            System.out.println("PrintStatement");
        }
        // Check that the expression is an integer
        if (!n.f2.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }
        

        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> AndExpression()
     *       | CompareExpression()
     *       | PlusExpression()
     *       | MinusExpression()
     *       | TimesExpression()
     *       | ArrayLookup()
     *       | ArrayLength()
     *       | MessageSend()
     *       | PrimaryExpression()
     */
    public String visit(Expression n, Object arg) {
        ArgObj argu = (ArgObj) arg;
        String _ret = null;
        _ret = (String) n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public String visit(AndExpression n, Object argu) {
        // Check that both primary expressions are booleans
        if (diag) {
            System.out.println("AndExpression");
        }
        if (!n.f0.accept(this, argu).equals("BooleanType")) {
            System.out.print("Type error\n");
            System.exit(0);
        } else if (!n.f2.accept(this, argu).equals("BooleanType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        String _ret = "BooleanType";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public String visit(CompareExpression n, Object argu) {
        // Check that both primary expressions are integers
        if (diag) {
            System.out.println("   CompareExpression");
        }
        if (!n.f0.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        } else if (!n.f2.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        String _ret = "BooleanType";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public String visit(PlusExpression n, Object argu) {
        // Check that both primary expressions are integers
        if (diag) {
            System.out.println("PlusExpression");
        }
        if (!n.f0.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        } else if (!n.f2.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        String _ret = "IntegerType";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public String visit(MinusExpression n, Object argu) {
        // Check that both primary expressions are integers
        if (diag) {
            System.out.println("MinusExpression");
        }
        if (!n.f0.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        } else if (!n.f2.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        String _ret = "IntegerType";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public String visit(TimesExpression n, Object argu) {
        if (diag) {
            System.out.println("TimesExpression");
        }
        // Check that both primary expressions are integers
        if (!n.f0.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        } else if (!n.f2.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        String _ret = "IntegerType";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public String visit(ArrayLookup n, Object argu) {
        // Check that the 1st primary expression is an array
        // Check that the 2nd primary expression is an integer
        if (diag) {
            System.out.println("ArrayLookup");
        }
        if (!n.f0.accept(this, argu).equals("ArrayType")) {
            System.out.print("Type error\n");
            System.exit(0);
        } else if (!n.f2.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        // Are there string arrays?

        String _ret = "IntegerType";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public String visit(ArrayLength n, Object argu) {
        // Check that the primary expression is an array
        if (diag) {
            System.out.println("ArrayLength");
        }
        if (!n.f0.accept(this, argu).equals("ArrayType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }
        String _ret = "IntegerType";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    public String visit(MessageSend n, Object arg) {
        // trying to run a method i think
        if (diag) {
            System.out.println("MessageSend");
        }
        ArgObj argu = (ArgObj) arg;
        String className = null;
        String _ret = null;
        // Check if Primary expression is a class
        for (String s : argu.classes) {
            if (n.f0.f0.accept(this, argu).equals(s)) {
                className = s;
                // now _ret is set to the class type
                break;
            }
        }

        // Check if the identifier is a method of that class
        String methName = (String) n.f2.accept(this, argu);
        // Check the return type of that method and return that type
        _ret = argu.methods.get(className).get(methName);

        // May also need to check the arguments of the method
        // TODO: Check the arguments of the method
        // check the names to be distinct and the types correct and the number of them correct
        // look at expression list for the arguments f4
        // check that the arguments are the same as the method declaration

        /// make expression list return a list of types

        n.f1.accept(this, argu);
        n.f2.accept(this, argu);

        n.f3.accept(this, argu);

        ArrayList<String> expList = (ArrayList<String>) n.f4.accept(this, argu);
        // compare the list of types to the other dictionary
        // get counts of each type in the list
        // compare the counts to the counts in the other dictionary
        HashMap<String, Integer> expMap = new HashMap<>();
        if (expList != null) {
            for (String s : expList) {
                if (expMap.get(s) == null) {
                    expMap.put(s, 1);
                } else {
                    expMap.put(s, expMap.get(s) + 1);
                }
            }

            argu.methArgs.get(className + "-" + methName).forEach((k, v) -> {
                if (expMap.get(k) == null || expMap.get(k) != v) {
                    System.out.print("Type error\n");
                    System.exit(0);
                }
            });    
        } else if (argu.methArgs.get(className + "-" + methName) != null) {
            System.out.print("Type error\n");
            System.exit(0);
        }



        n.f5.accept(this, argu);

        return _ret;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public ArrayList<String> visit(ExpressionList n, Object argu) {
        if (diag) {
            System.out.println("ExpressionList");
        }

        // check the names are distinct

        ArrayList<String> array = new ArrayList<>();
        array.add((String) n.f0.accept(this, argu));
        for (int j = 0; j < n.f1.size(); j++) {
            array.add((String) n.f1.elementAt(j).accept(this, argu));
        }

        return array;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public String visit(ExpressionRest n, Object argu) {
        if (diag) {
            System.out.println("ExpressionRest");
        }
        String _ret = null;
        n.f0.accept(this, argu);
        _ret = (String) n.f1.accept(this, argu);

        return _ret;
    }

    /**
     * f0 -> IntegerLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | NotExpression()
     *       | BracketExpression()
     */
    public String visit(PrimaryExpression n, Object arg) {
        if (diag) {
            System.out.println("PrimaryExpression");
        }

        ArgObj argu = (ArgObj) arg;
        String className = null;
        String _ret = null;
        // Check if Primary expression is a class
        for (String s : argu.classes) {
            if (n.f0.accept(this, argu).equals(s)) {
                className = s;
                // now _ret is set to the class type
                break;
            }
        }

        _ret = (String) n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String visit(IntegerLiteral n, Object argu) {
        if (diag) {
            System.out.println("IntegerLiteral");
        }
        String _ret = "IntegerType";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "true"
     */
    public String visit(TrueLiteral n, Object argu) {
        if (diag) {
            System.out.println("TrueLiteral");
        }
        String _ret = "BooleanType";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "false"
     */
    public String visit(FalseLiteral n, Object argu) {
        if (diag) {
            System.out.println("FalseLiteral");
        }
        String _ret = "BooleanType";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Identifier n, Object arg) {
        ArgObj argu = (ArgObj) arg;
        if (diag) {
            System.out.println("id: " + n.f0.tokenImage);
        }
        // Return based on the type of the identifier in the type environment
        String _ret = null;
        // Check if the identifier is a field
        // Instead check the type environment to see if the id is in there

        // Instead check the type environment to see if the id is in there
        int i = 0;
        while (_ret == null && i < argu.scopePath.size()) {
            if (argu.typeEnv.get(argu.scopePath.get(i)) != null) {
                if (argu.typeEnv.get(argu.scopePath.get(i)).get(n.f0.tokenImage) != null) {
                    _ret = argu.typeEnv.get(argu.scopePath.get(i)).get(n.f0.tokenImage);
                }
            }
            i++;
        }

        // check if the identifier is a class
        // TODO: Potentially make it so the return value is not the method name but the class type (aka class name)

        if (_ret == null) {
            _ret = n.f0.tokenImage;
        }
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "this"
     */
    public String visit(ThisExpression n, Object argu) {
        String _ret = null;
        if (diag) {
            System.out.println("ThisExpression");
        }
        // Check if we are in a class
        for (String s : ((ArgObj) argu).scopePath) {
            if (((ArgObj) argu).classes.contains(s)) {
                _ret = s;
                return _ret;
            }
        }

        
        // : C (The class that we are in now)
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public String visit(ArrayAllocationExpression n, Object arg) {
        ArgObj argu = (ArgObj) arg;    
        if (diag) {
            System.out.println("ArrayAllocationExpression");
        }
        // Check if the expression is an integer
        if (!n.f3.accept(this, argu).equals("IntegerType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        String _ret = "ArrayType";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public String visit(AllocationExpression n, Object argu) {
        // Nothing to check
        if (diag) {
            System.out.println("AllocationExpression");
        }
        String _ret = null;
        n.f0.accept(this, argu);
        _ret = (String) n.f1.accept(this, argu); // returns the name of the identifier given
        // NEW OBJECT
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public String visit(NotExpression n, Object argu) {
        // Check if the expression is a boolean
        if (diag) {
            System.out.println("NotExpression");
        }
        if (!n.f1.accept(this, argu).equals("BooleanType")) {
            System.out.print("Type error\n");
            System.exit(0);
        }

        String _ret = "BooleanType"; // Not expressions have to be booleans
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;    
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public String visit(BracketExpression n, Object argu) {
        String _ret = null;
        n.f0.accept(this, argu);
        _ret = (String) n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }
}
