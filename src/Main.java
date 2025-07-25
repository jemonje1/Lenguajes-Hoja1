import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String input;
        input="2+1*(8-7)";
        ShuntingYard(input);
    }

    public static void ShuntingYard(String input) {
        Stack<Character> stack = new Stack<Character>();
        List<Character> chars = new ArrayList<Character>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if(c==' '){
                continue;
            }
            if(Character.isLetterOrDigit(c)){
                chars.add(c);
            } else if (c=='('){

            }
        }
    }

    // Infix a Outfix, yo
    //Outfix a infix, tu
    public String Outfix(String input) {

        return input;
    }

    public static boolean isOperator(char c){
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

}