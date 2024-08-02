package com.work.calculator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author linux
 */
public class Calculator extends JFrame implements ActionListener {

    private final JTextField display;
    private final JPanel panel;
    private final String[] buttons = {
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "0", ".", "=", "+",
        "C", "(", ")"
    };
    private final JButton[] buttonObjects = new JButton[buttons.length];

    public Calculator() {
        setTitle("Calculadora");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);

        // Crear el display
        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 18));
        add(display, BorderLayout.NORTH);

        // Crear el panel de botones
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4));
        for (int i = 0; i < buttons.length; i++) {
            buttonObjects[i] = new JButton(buttons[i]);
            buttonObjects[i].setFont(new Font("Arial", Font.BOLD, 18));
            buttonObjects[i].addActionListener(this);
            panel.add(buttonObjects[i]);
        }

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command.charAt(0)) {
            case '=' -> {
                try {
                    display.setText(evaluate(display.getText()));
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                    display.setText("Error");
                }
            }
            case 'C' ->
                display.setText("");
            default ->
                display.setText(display.getText() + command);
        }
    }

    private String evaluate(String expression) {
        return Double.toString(evaluateExpression(expression));
    }

    private double evaluateExpression(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ') {
                continue;
            }
            if ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.'
                    || (tokens[i] == '-' && (i == 0 || tokens[i - 1] == '(' || isOperator(tokens[i - 1])))) {
                StringBuilder sbuf = new StringBuilder();
                if (tokens[i] == '-' && (i == 0 || tokens[i - 1] == '(' || isOperator(tokens[i - 1]))) {
                    sbuf.append('-');
                    i++;
                }
                while (i < tokens.length && ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.')) {
                    sbuf.append(tokens[i++]);
                }
                values.push(Double.valueOf(sbuf.toString()));
                i--;
            } else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            } else if (isOperator(tokens[i])) {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(tokens[i]);
            }
        }

        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return !((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'));
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+' -> {
                return a + b;
            }
            case '-' -> {
                return a - b;
            }
            case '*' -> {
                return a * b;
            }
            case '/' -> {
                if (b == 0) {
                    throw new UnsupportedOperationException("No se puede dividir por cero");
                }
                return a / b;
            }
        }
        return 0;
    }
}
