package com.mycompany.authguard;

import javax.swing.*;

public class AuthGuard {
    public static void main(String[] args) {
        while (true) {
            String[] options = {"Login", "Register", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Welcome to AuthGuard!", "Main Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    MenuHandler.login();
                    break;
                case 1:
                    MenuHandler.register();
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Exiting...");
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }
}