package com.mycompany.authguard;

import java.util.HashMap;

public class MessageManager {
    private static HashMap<String, String> messages = new HashMap<>();
    private static HashMap<String, Boolean> sentFlags = new HashMap<>();
    private static HashMap<String, Boolean> receivedFlags = new HashMap<>();
    private static HashMap<String, Boolean> readFlags = new HashMap<>();

    public static void storeMessage(String username, String message) {
        messages.put(username, message);
        sentFlags.put(username, true);
        receivedFlags.put(username, false);
        readFlags.put(username, false);
    }

    public static String getMessage(String username) {
        if (!messages.containsKey(username)) {
            return "No messages found.";
        }
        receivedFlags.put(username, true);
        readFlags.put(username, true);

        return "Message: " + messages.get(username) + "\nSent: " + sentFlags.get(username)
                + "\nReceived: " + receivedFlags.get(username) + "\nRead: " + readFlags.get(username);
    }
}