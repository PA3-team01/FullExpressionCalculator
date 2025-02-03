package main;
import java.util.Scanner;

import bll.ExpressionEvaluator;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        System.out.println("Console Calculator (supports +, -, *, /, ^, and parentheses)");
        System.out.println("Type 'exit' to quit.");
        
        while (true) {
            System.out.print("Enter an expression: ");
            String input = scanner.nextLine().replaceAll("\\s", ""); // Remove spaces

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting calculator...");
                break;
            }

            try {
                double result = evaluator.evaluate(input);
                System.out.println("Result: " + result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
	}

}
