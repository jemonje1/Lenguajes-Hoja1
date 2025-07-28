import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.nio.file.Paths;

//Sophia Corea - 1185324
//Javier Monje - 1260524
public class Main {
    public static void main(String[] args) {
        //Lectura de archivos
        try {
            int count = 1;
            List<String> lines = Files.readAllLines(Paths.get("expresiones.txt"));
            for(String line : lines) {
                StringBuilder sb = new StringBuilder();
                sb.append(line);
                String infix = sb.toString();
                if (!isValidExpression(infix)) {
                    System.out.println("---------------------------------");
                    System.out.println(count+". Expresión inválida: " + infix);
                    System.out.println("---------------------------------"+"\n");
                } else {
                    String postfix = ShuntingYard(infix);
                    System.out.println("---------------------------------");
                    System.out.println(count+". Infix: " + infix);
                    System.out.println("Postfix: " + postfix);
                    double result = evaluatePostfix(postfix);
                    System.out.println("Expresion evaluada: " + result);
                    System.out.println("---------------------------------"+"\n");
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String ShuntingYard(String input) {
        //Stack de operadores
        Stack<String> stack = new Stack<>();
        //Lista que almacena el resultado en postfix
        List<String> output = new ArrayList<>();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            // Saltar espacios
            if (c == ' ') {
                i++;
                continue;
            }
            // Si es un dígito, extraer el número completo
            if (Character.isDigit(c)) {
                StringBuilder number = new StringBuilder();
                while (i < input.length() && Character.isDigit(input.charAt(i))) {
                    number.append(input.charAt(i));
                    i++;
                }
                output.add(number.toString());
                continue; // No incrementar i aquí porque ya se hizo en el while
            }
            //si es un parentesis lo guarda en stack
            if (c == '(') {
                stack.push(String.valueOf(c));
            }else if (c == ')') {
                //Agrega operadores al postfix hasta encontrar un parentesis de apertura
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                if (!stack.isEmpty()) {
                    stack.pop(); // Remover el '('
                } else {
                    System.out.println("Hay paréntesis extras, revise el infix");
                }
            }else if (isOperator(c)) {
                //Verifica la jerarquia de los operadores
                while (!stack.isEmpty() && !stack.peek().equals("(") &&
                        precedence(stack.peek().charAt(0)) >= precedence(c)) {
                    output.add(stack.pop());
                }
                stack.push(String.valueOf(c));
            }
            i++;
        }
        //verifica equilibrio de parentesis
        while (!stack.isEmpty()) {
            if (stack.peek().equals("(") || stack.peek().equals(")")) {
                System.out.println("Paréntesis extras, revise el infix");
            }
            output.add(stack.pop());
        }
        //Genera la salida de postfix concatenando los tokens con espacios
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < output.size(); j++) {
            result.append(output.get(j));
            if (j < output.size() - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    //Validación que simplifica las condiciones del codigo por si se encuentra un operador
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    /*Este metodo se encarga de recibir un caracter en el metodo ShuntingYard, cuando obtiene el caracter este solamente puede ser un operador
     * el metodo retorna un digito el cual representara la prioridad segun el orden de las operaciones*/
    private static double precedence(char c) {
        return switch (c) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }

    /*validacion, el metodo recibe el postfix a evaluar como string, separa los tokens y los evalua
     * usando un stack para manejar operandos y operadores*/
    private static double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            };
            if (isOperator(token.charAt(0)) && token.length() == 1) {
                if (stack.size() < 2) {
                    System.out.println("Expresión inválida: operadores insuficientes");
                    return 0;
                }
                /*Si el token sí es un operador, y se tienen suficientes elementos
                 * se realiza la operacion entre los dos ultimos digitos y el operador*/
                double val2 = stack.pop();
                double val1 = stack.pop();
                double result = applyOperation(token.charAt(0), val1, val2);
                //Se guarda el resultado en el stack
                stack.push(result);
            } else {
                //Si no es operador, simplemente se guarda el numero completo
                try {
                    double number = Double.parseDouble(token);
                    stack.push(number);
                } catch (NumberFormatException e) {
                    System.out.println("Token inválido: " + token);
                    return 0;
                }
            }
        }
        return stack.isEmpty() ? 0 : stack.pop();
    }

    //Metodo para realizar operaciones
    private static double applyOperation(char op, double val1, double val2) {
        switch(op) {
            case '+':
                return val1 + val2;
            case '-':
                return val1 - val2;
            case '*':
                return val1 * val2;
            case '/':
                if (val2 == 0) {
                    System.out.println("Error: División por cero");
                    return 0;
                }
                return val1 / val2;
            case '^':
                return Math.pow(val1, val2);
            default:
                return 0;
        }
    }

    //Para validar nuestras entradas a partir de nuestro Regex  ^\s*(\(*\s*\d+\s*\)*\s*([\+\-\*\/\^]\s*\(*\s*\d+\s*\)*\s*)*)\s*$
    public static boolean isValidExpression(String expr) {
        // Elimina espacios
        expr = expr.replaceAll("\\s+", "");
        // Verifica que solo tenga caracteres válidos (números de múltiples dígitos)
        if (!expr.matches("[\\d+\\-*/^()]+")) {
            return false;
        }
        // No debe iniciar ni terminar con operador
        if (expr.matches("^[+*/^].*") || expr.matches(".*[+\\-*/^]$")) {
            return false;
        }
        // No operadores duplicados
        if (expr.matches(".*[+\\-*/^]{2,}.*")) {
            return false;
        }
        // No número seguido directamente de paréntesis de apertura
        if (expr.matches(".*\\d\\(.*")) {
            return false;
        }
        // No paréntesis de cierre seguido de número
        if (expr.matches(".*\\)\\d.*")) {
            return false;
        }
        // No paréntesis vacíos
        if (expr.contains("()")) {
            return false;
        }
        // Balance de paréntesis
        int count = 0;
        for (char c : expr.toCharArray()) {
            if (c == '(') count++;
            else if (c == ')') {
                count--;
                if (count < 0) return false;
            }
        }
        return count == 0;
    }
}