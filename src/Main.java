import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String input;
        input="3+4*2+5";
        String postfix = ShuntingYard(input);
        System.out.println("El postfix: ");
        System.out.println(postfix);
        System.out.println("Evaluacion");
        Stack<Integer> stack = new Stack<>();
        int evaluate = evaluatePostfix(postfix,0, stack);
        System.out.println(evaluate);

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

    private static boolean isOperator(char c){
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static int precedence(char c) {
        return switch (c) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^'->3;
            default -> 0;
        };
    }


    private static int evaluatePostfix(String tokens, int index, Stack<Integer> stack) {
        if (index >= tokens.length()) {
            return stack.pop();
        }
        char token = tokens.charAt(index);
        if (isOperator(token)) {
            if (stack.size() < 2) {
                return stack.pop();
            }
            int val2 = stack.pop();
            int val1 = stack.pop();
            int result = applyOperation(token, val1, val2);
            stack.push(result);
        } else {
            stack.push(Integer.parseInt(String.valueOf(token)));
        }
        return evaluatePostfix(tokens, index + 1, stack);
    }

    private static int applyOperation(char op, int val1, int val2) {
        switch(op) {
            case '+':
                return val1 + val2;
            case '-':
                return val1 - val2;
            case '*':
                return val1 * val2;
            case '/':
                return val1 / val2;
            case '^':
                return val1 ^ val2;
            default:
                return 0;
        }
    }
}