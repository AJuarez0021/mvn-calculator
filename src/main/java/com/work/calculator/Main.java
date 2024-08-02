package com.work.calculator;

import javax.swing.SwingUtilities;

/**
 *
 * @author linux
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}
