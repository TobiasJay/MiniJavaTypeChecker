import syntaxtree.Node;
import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;


public class ArgObj {
    public String scope;
    public HashMap<String, HashMap<String, String>> methods; // maps classes to list of their methods
    public HashMap<String, HashMap<String, String>> fields; // maps classes to a mapping of their fields to their types
    public ArrayList<String> classes; // list of all classes
    public String parentName;
    public String className;
    public ArrayList<String> localVars;
    public ArrayList<String> scopePath;
    public HashMap<String, HashMap<String, String>> typeEnv; // maps scopes to a mapping of their variables to their types
    public HashMap<String, HashMap<String, Integer>> methArgs; // maps methods to a mapping of their arguments to their types

    public ArgObj() {
        this.scope = "";
        this.classes = new ArrayList<>();
        this.methods = new HashMap<>();
        this.fields = new HashMap<>();
        this.parentName = "";
        this.className = "";
        this.localVars = new ArrayList<>();
        this.scopePath = new ArrayList<>();
        this.typeEnv = new HashMap<>();
        this.methArgs = new HashMap<>();
    }

}
