package com.mycompany.authguard;

import javax.swing.*;
import java.util.HashMap;

public class UserAuth {
    private static HashMap<String, String> users = new HashMap<>();
    private static HashMap<String, String> phoneNumbers = new HashMap<>();

    static {
        users.put("admin", "Password123!");
        phoneNumbers.put("admin", "0712345678");
    }

    public static String validateLogin(String username, String password) {
        if (!users.containsKey(username)) {
            return "Error: Username does not exist!";
        }
        if (!users.get(username).equals(password)) {
            return "Error: Incorrect password!";
        }
        return "Login Successful!";
    }

    public static void registerUser(String username, String password, String phoneNumber) {
        if (!PhoneValidation.validate(phoneNumber)) {
            JOptionPane.showMessageDialog(null, "Invalid phone number format.");
            return;
        }
        users.put(username, password);
        phoneNumbers.put(username, phoneNumber);
    }

    public static void showUserOptions(String username) {
        while (true) {
            String[] options = {"View Details", "Edit Phone Number", "Send Message", "View Messages", "Delete Account", "Logout"};
            int choice = JOptionPane.showOptionDialog(null, "Welcome, " + username + "!", "User Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    JOptionPane.showMessageDialog(null, "Username: " + username + "\nPhone: " + phoneNumbers.get(username));
                    break;
                case 1:
                    String newPhone = JOptionPane.showInputDialog("Enter new phone number:");
                    if (PhoneValidation.validate(newPhone)) {
                        phoneNumbers.put(username, newPhone);
                        JOptionPane.showMessageDialog(null, "Phone number updated!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid phone number format.");
                    }
                    break;
                case 2:
                    String message = JOptionPane.showInputDialog("Enter your secure message:");
                    MessageManager.storeMessage(username, message);
                    JOptionPane.showMessageDialog(null, "Message stored securely!");
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, MessageManager.getMessage(username));
                    break;
                case 4:
                    users.remove(username);
                    phoneNumbers.remove(username);
                    JOptionPane.showMessageDialog(null, "Account deleted.");
                    return;
                case 5:
                    return;
                default:
                    break;
            }
        }
    }
}