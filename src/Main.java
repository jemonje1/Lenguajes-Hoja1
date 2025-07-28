import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        //Lectura de archivos
        try {
            List<String> lines = Files.readAllLines(Paths.get("entrada.txt"));
            for(String line : lines) {
                StringBuilder sb = new StringBuilder();
                sb.append(line);
                String infix = sb.toString();
                String postfix = ShuntingYard(infix);
                System.out.println("Entrada: " + infix+"\n");
                System.out.println(postfix);
                double result = evaluatePostfix(postfix,0,new Stack<>());
                System.out.println("Expresin evaluada: "+result+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static double precedence(char c) {
        return switch (c) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^'->3;
            default -> 0;
        };
    }

    private static double evaluatePostfix(String tokens, int index, Stack<Double> stack) {
        if (index >= tokens.length()) {
            return stack.pop();
        }
        char token = tokens.charAt(index);
        if (isOperator(token)) {
            if (stack.size() < 2) {
                return stack.pop();
            }
            double val2 = stack.pop();
            double val1 = stack.pop();
            double result = applyOperation(token, val1, val2);
            stack.push(result);
        } else {
            stack.push((double) Integer.parseInt(String.valueOf(token)));
        }
        return evaluatePostfix(tokens, index + 1, stack);
    }

    private static double applyOperation(char op, double val1, double val2) {
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
                return (double)Math.pow(val1, val2);
            default:
                return 0;
        }
    }
}