package com.mycompany.authguard;

import java.util.HashMap;

public class DatabaseHandler {
    private static HashMap<String, String> usersDatabase = new HashMap<>();
    private static HashMap<String, String> messagesDatabase = new HashMap<>();

    public static void saveUser(String username, String password) {
        usersDatabase.put(username, password);
    }

    public static void saveMessage(String username, String message) {
        messagesDatabase.put(username, message);
    }

    public static String retrieveMessage(String username) {
        return messagesDatabase.getOrDefault(username, "No messages found.");
    }
}