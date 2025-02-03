package bll;

import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

/**
 * ExpressionEvaluator class evaluates mathematical expressions.
 * Uses a stack-based approach to directly evaluate infix expressions.
 */
public class ExpressionEvaluator {
    private final Map<Character, Integer> precedence; // Stores operator precedence levels

    /**
     * Constructor initializes operator precedence.
     */
    public ExpressionEvaluator() {
        precedence = new HashMap<>();
        precedence.put('+', 1); // Addition and Subtraction (Lowest precedence)
        precedence.put('-', 1);
        precedence.put('*', 2); // Multiplication and Division (Higher precedence)
        precedence.put('/', 2);
        precedence.put('^', 3); // Exponentiation (Highest precedence)
    }

    /**
     * Evaluates an infix mathematical expression.
     * @param expression The mathematical expression as a string.
     * @return The computed result as a double.
     */
    public double evaluate(String expression) {
        Stack<Double> operands = new Stack<>(); // Stack for numbers (operands)
        Stack<Character> operators = new Stack<>(); // Stack for operators
        int i = 0;
        
        while (i < expression.length()) {
            char c = expression.charAt(i);

            // If it's a number, parse and push onto operand stack
            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i++));
                }
                operands.push(Double.parseDouble(num.toString()));
                continue; // Skip incrementing `i` since it's already updated
            }

            // Handle opening parenthesis
            if (c == '(') {
                operators.push(c);
            }
            // Handle closing parenthesis
            else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') { // Apply operations until matching '(' (peek is for looking without removing)
                    applyOperation(operands, operators.pop());
                }
                if (operators.isEmpty() || operators.pop() != '(') { 
                    throw new IllegalArgumentException("Mismatched parentheses"); // Ensure parentheses are balanced
                }
            }
            // Handle operators and manage precedence
            else if (precedence.containsKey(c)) {
                /**
                 * Precedence Handling:
                 * - While the operator stack is not empty and the operator at the top has
                 *   higher or equal precedence than the current operator, apply the operation.
                 * - This ensures that operations with higher precedence (e.g., * and /)
                 *   are executed before lower precedence ones (e.g., + and -).
                 */
                while (!operators.isEmpty() && precedence.getOrDefault(operators.peek(), 0) >= precedence.get(c)) {
                    applyOperation(operands, operators.pop());
                }
                operators.push(c);
            } else {
                throw new IllegalArgumentException("Invalid character in expression: " + c);
            }
            i++; // Move to next character
        }

        // Process remaining operators in the stack
        while (!operators.isEmpty()) {
            applyOperation(operands, operators.pop());
        }

        if (operands.size() != 1) {
            throw new IllegalArgumentException("Invalid expression format");
        }

        return operands.pop();
    }

    /**
     * Applies an operator to the top two operands from the stack.
     * @param operands Stack of operand values.
     * @param operator The operator to apply.
     */
    private void applyOperation(Stack<Double> operands, char operator) {
        if (operands.size() < 2) {
            throw new IllegalArgumentException("Invalid expression");
        }
        double b = operands.pop();
        double a = operands.pop();

        switch (operator) {
            case '+': operands.push(a + b); break;
            case '-': operands.push(a - b); break;
            case '*': operands.push(a * b); break;
            case '/': 
                if (b == 0) throw new ArithmeticException("Cannot divide by zero");
                operands.push(a / b);
                break;
            case '^': operands.push(Math.pow(a, b)); break;
            default: throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
}
