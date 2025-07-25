import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String input;
        input="2+1*3";
        String postfix = ShuntingYard(input);
        System.out.println("El postfix: ");
        System.out.println(postfix);
        System.out.println("El infix: ");

    }

    public static String ShuntingYard(String input) {
        Stack<Character> stack = new Stack<Character>();
        List<Character> chars = new ArrayList<Character>();
        String postfix;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if(c==' '){
                continue;
            }
            if(Character.isLetterOrDigit(c)){
                chars.add(c);
            }else if (c=='('){
                stack.push(c);
            }else if(c==')'){
                while(!stack.isEmpty()&&stack.peek()!='('){
                    chars.add(stack.pop());
                }
                if(!stack.isEmpty()){
                    stack.pop();
                }else{
                    System.out.println("Hay parentesis extras, revise el infix");
                }
            }else if(isOperator(c)){
                while (!stack.isEmpty() && stack.peek() != '(' && precedence(stack.peek()) >= precedence(c)){
                    chars.add(stack.pop());
                }
                stack.push(c);
            }
        }
        while(!stack.isEmpty()){
            if(stack.peek()=='('||stack.peek()==')'){
                System.out.println("Parentesis extras, revise el infix");
            }
            chars.add(stack.pop());
        }
        StringBuilder builder = new StringBuilder();
        for(Character c : chars){
            builder.append(c);
        }
        postfix=builder.toString();
        return postfix;
    }

    //Outfix a infix, tu
    public String Outfix(String input) {
        return input;
    }

    private static boolean isOperator(char c){
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }
}