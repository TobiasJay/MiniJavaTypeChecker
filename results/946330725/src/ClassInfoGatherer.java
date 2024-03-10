import syntaxtree.*;
import visitor.*;
import java.util.*;

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

public class ClassInfoGatherer extends GJDepthFirst<Object, Object> {
    public boolean distinct(String... names) {
        Set<String> set = new HashSet<>();
        for (String name : names) {
            if (!set.add(name)) {
                return false;
            }
        }
        return true;
    }

    /*
    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
    public Object visit(ClassDeclaration n, Object arg) {
        ArgObj argu = (ArgObj) arg;

        argu.parentNode = n;
        String ClassName = n.f1.f0.tokenImage;
        argu.className = n.f1.f0.tokenImage;
        argu.scope = argu.scope + "->" + ClassName;
        argu.classes.put(ClassName, new Stack<Node>());
        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        return _ret;
    }
    */

    /**
     * f0 -> MainClass()
     * f1 -> ( ClassDeclaration() )*
     * f2 -> <EOF>
     * 
     */
    public Object visit(Goal n, Object arg) {
        ArgObj argu = (ArgObj) arg;
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

        String className = n.f1.f0.tokenImage;
        argu.className = className;
        argu.scopePath.add(0, className);
        
        argu.typeEnv.put(className, new HashMap<>());

        argu.classes.add(className);
        argu.fields.put(className, new HashMap<>());
        argu.methods.put(className, new HashMap<>());
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

        // Making sure there aren't any duplicate classses
        if (argu.typeEnv.containsKey(argu.scopePath.get(0))) {
            System.out.print("Type error\n");
            System.exit(0);
        }
        argu.typeEnv.put(n.f1.f0.tokenImage, new HashMap<>());


        String ClassName = n.f1.f0.tokenImage;
        argu.className = n.f1.f0.tokenImage;

        argu.classes.add(ClassName);
        argu.fields.put(ClassName, new HashMap<>());
        argu.methods.put(ClassName, new HashMap<>());
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


    // ==================== Not used? ====================


    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public Object visit(VarDeclaration n, Object arg) {
        // populate type environment
        ArgObj argu = (ArgObj) arg;

        argu.typeEnv.get(argu.scopePath.get(0)).put(n.f1.f0.tokenImage, (String) n.f0.accept(this, argu));

        // Only set below line if the parent node is a class declaration
        
        if (!argu.scope.equals("Meth")) {
            argu.fields.get(argu.className).put(n.f1.f0.tokenImage, n.f0.f0.choice.getClass().getSimpleName());
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

        argu.scope = "Meth";
        // Make sure this method is new. Reference methods with their classes
        // WHat class is this in?
        String className = null;
        for (String c : argu.classes) {
            if (argu.scopePath.get(0).equals(c)) {
                className = c;
                break;
            }
        }
        String fullMethName= ""+className +"-"+ n.f2.f0.tokenImage;
        // If we already have a method with this name, throw an error
        if (argu.methods.get(className).containsKey(n.f2.f0.tokenImage)) {
            System.out.print("Type error\n");

            System.exit(0);
        }
        argu.typeEnv.put(fullMethName, new HashMap<>());
        
        String type = n.f1.f0.choice.getClass().getSimpleName();
        // if the type is a class
        if (type.equals("Identifier")) {

            type = (String) n.f1.f0.accept(this, argu);
        }

        argu.methods.get(argu.className).put(n.f2.f0.tokenImage, type);

        argu.scopePath.add(0, fullMethName);
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
        argu.scopePath.remove(0);
        return _ret;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public Object visit(FormalParameterList n, Object argu) {
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
        // populate type environment
        ArgObj argu = (ArgObj) arg;
        // Check if a class is being declared
        Boolean isClass = false;
        for (String c : argu.classes) {
            if (n.f0.accept(this, argu).equals(c)) {
                // This is a class object 
                isClass = true;
                argu.typeEnv.get(argu.scopePath.get(0)).put(n.f1.f0.tokenImage, (String) n.f0.accept(this, argu));
            }
        }

        if (!isClass) {
            argu.typeEnv.get(argu.scopePath.get(0)).put(n.f1.f0.tokenImage, n.f0.f0.choice.getClass().getSimpleName());
        }
        // Return the name and its type as a string with both of them in it
        String type = null;
        if (isClass) {
            type = (String) n.f0.accept(this, argu);
        } else {
            type = n.f0.f0.choice.getClass().getSimpleName();
        }
        
        String _ret = type;

        // What method are we in?
        // iterate through scopePath to find the closest method
        for (String s : argu.scopePath) {
            if (s.contains("-")) {
                // This is the method
                if (!argu.methArgs.containsKey(s)) {
                    argu.methArgs.put(s, new HashMap<>());
                }
                //System.out.println("Putting "+ n.f1.f0.tokenImage + " in " + s + " with type " + type);
                // Instead place a count of certain types of arguments
                // aka 5 ints, 3 booleans, 2 classes
                // Map<String, Integer> where the string is the type and the integer is the count
                
                argu.methArgs.get(s).put(type, argu.methArgs.get(s).getOrDefault(type, 0) + 1);
                break;
            }
        }

        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public String visit(FormalParameterRest n, Object argu) {
        String _ret = null;
        n.f0.accept(this, argu);
        _ret = (String) n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public String visit(Type n, Object argu) {
        String _ret = null;
        _ret = (String) n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public Object visit(ArrayType n, Object argu) {
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
        
        Object _ret = "BooleanType";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
     */
    public Object visit(IntegerType n, Object argu) {
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
        Object _ret = null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    public Object visit(Block n, Object arg) {
        ArgObj argu = (ArgObj) arg;
        

        // Need to add a unique identifier to this while loop
        // loop until a unique identifier is found
        boolean condition = true;
        int i = 0;
        while (condition) {
            if (argu.scopePath.contains("block" + i)) {
                i++;
            } else {
                condition = false;
                // add to front of list 
                argu.scopePath.add(0, "block" + i);
                argu.typeEnv.put("block" + i, new HashMap<>());

            }
        }
        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        argu.scopePath.remove(0);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public Object visit(AssignmentStatement n, Object arg) {
        Object _ret = null;

        ArgObj argu = (ArgObj) arg;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
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
    public String visit(Expression n, Object argu) {
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
        String _ret = null;
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
    public Object visit(CompareExpression n, Object argu) {
        Object _ret = null;
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
    public Object visit(PlusExpression n, Object argu) {
        Object _ret = null;
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
    public Object visit(MinusExpression n, Object argu) {
        Object _ret = null;
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
    public Object visit(TimesExpression n, Object argu) {
        Object _ret = null;
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
    public Object visit(ArrayLookup n, Object argu) {
        Object _ret = null;
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
    public Object visit(ArrayLength n, Object argu) {
        Object _ret = null;
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
    public Object visit(MessageSend n, Object argu) {
        Object _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public ArrayList<String> visit(ExpressionList n, Object argu) {
        String exp = (String) n.f0.accept(this, argu);
        ArrayList<String> array = new ArrayList<>();
        array.add(exp);
        ArrayList<String> rest = (ArrayList<String>) n.f1.accept(this, argu);
        if (rest != null) {
            array.addAll(rest);
        }
        return array;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public String visit(ExpressionRest n, Object argu) {
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
    public String visit(PrimaryExpression n, Object argu) {
        String _ret = null;
        _ret = (String) n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String visit(IntegerLiteral n, Object argu) {
        String _ret = "int";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "true"
     */
    public String visit(TrueLiteral n, Object argu) {
        String _ret = "bool";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "false"
     */
    public String visit(FalseLiteral n, Object argu) {
        String _ret = "bool";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Identifier n, Object argu) {
        String _ret = null;
        _ret = n.f0.tokenImage;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "this"
     */
    public String visit(ThisExpression n, Object arg) {
        String _ret = null;
        // : C (The class that we are in now)
        ArgObj argu = (ArgObj) arg;
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
    public String visit(ArrayAllocationExpression n, Object argu) {
        String _ret = "int[]";
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
    public String visit(AllocationExpression n, Object arg) {
        ArgObj argu = (ArgObj) arg;

        String _ret = null;
        n.f0.accept(this, argu);
        _ret = (String) n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        // System.exit(0);
        return _ret;
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public String visit(NotExpression n, Object argu) {
        String _ret = "bool"; // Not expressions have to be booleans
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
