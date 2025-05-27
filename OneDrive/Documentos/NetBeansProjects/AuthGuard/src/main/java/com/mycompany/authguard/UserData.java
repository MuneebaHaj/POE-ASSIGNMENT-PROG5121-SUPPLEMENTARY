package com.mycompany.authguard;

import java.util.HashMap;

public class UserData {
    private static HashMap<String, String> users = new HashMap<>();
    private static HashMap<String, String> phoneNumbers = new HashMap<>();

    static {
        users.put("admin", "Password123!");
        phoneNumbers.put("admin", "0712345678");
    }

    public static boolean validateLogin(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public static boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]{5,12}$");
    }

    public static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,20}$");
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone.matches("^0[678]\\d{8}$");
    }

    public static void registerUser(String username, String password, String phoneNumber) {
        users.put(username, password);
        phoneNumbers.put(username, phoneNumber);
    }

    public static String getUserDetails(String username) {
        return "Username: " + username + "\nPhone: " + phoneNumbers.get(username);
    }
}