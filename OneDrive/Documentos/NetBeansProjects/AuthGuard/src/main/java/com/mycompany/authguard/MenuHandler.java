package com.mycompany.authguard;

import javax.swing.*;

public class MenuHandler {
    public static void login() {
        String username = JOptionPane.showInputDialog("Enter Username:");
        String password = JOptionPane.showInputDialog("Enter Password:");

        String result = UserAuth.validateLogin(username, password);
        JOptionPane.showMessageDialog(null, result);

        if (result.equals("Login Successful!")) {
            UserAuth.showUserOptions(username);
        }
    }

    public static void register() {
        String username = JOptionPane.showInputDialog("Enter Username:");
        String password = JOptionPane.showInputDialog("Enter Password:");
        String phoneNumber = JOptionPane.showInputDialog("Enter South African Phone Number:");

        if (!PhoneValidation.validate(phoneNumber)) {
            JOptionPane.showMessageDialog(null, "Invalid phone number format. Please enter a valid South African number.");
            return;
        }

        UserAuth.registerUser(username, password, phoneNumber);
        JOptionPane.showMessageDialog(null, "Registration Successful!");
    }
}