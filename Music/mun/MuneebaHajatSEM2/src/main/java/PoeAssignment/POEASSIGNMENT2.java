package PoeAssignment;

import javax.swing.JOptionPane;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// Part 3 Arrays POE

public class POEASSIGNMENT2 {
//max mssg
    public static final int MAX_MESSAGES = 1000;

    public static Message[] sentMessages = new Message[MAX_MESSAGES];
    public static Message[] disregardedMessages = new Message[MAX_MESSAGES];
    public static Message[] storedMessages = new Message[MAX_MESSAGES];
    public static String[] messageHashes = new String[MAX_MESSAGES];
    public static String[] messageIDs = new String[MAX_MESSAGES];

    public static int sentCount = 0;
    public static int disregardedCount = 0;
    public static int storedCount = 0;
    public static int hashCount = 0;
    public static int idCount = 0;

    public static String username;
    public static String password;
    public static String cellNumber;
    public static String firstName;
    public static String lastName;
    public static String messageHash;
    public static String cellRegex = "^\\+27\\d{9}$";
    static int sentMessageCount;
    static int sentMessagesCount;
    static int messageHashCount;

    public static void main(String[] args) {
        Login loginClass = new Login();
        loadStoredMessagesFromFile();

        // users name nd last name
        firstName = JOptionPane.showInputDialog("Enter your first name:");
        lastName = JOptionPane.showInputDialog("Enter your last name:");

        // make sure to keep asking for username till its correct format 
        while (true) {
            username = JOptionPane.showInputDialog("Create a username:");
            if (loginClass.checkUserName()) {
                JOptionPane.showMessageDialog(null, "Username successfully captured.");
                break;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Username is not correctly formatted.\nIt must contain an underscore (_) and be no more than five characters.");
            }
        }

        // password must be correctly formatted, or else should keep retrtying
        while (true) {
            password = JOptionPane.showInputDialog("Create a password:");
            if (loginClass.checkPasswordComplexity(password)) {
                JOptionPane.showMessageDialog(null, "Password successfully captured.");
                break;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Password is not correctly formatted.\nIt must contain at least 8 characters, a capital letter, a number, and a special character.");
            }
        }

        // Cell number check, keep retrying
        while (true) {
            cellNumber = JOptionPane.showInputDialog("Enter your cell phone number (e.g., +27789709652):");
            if (loginClass.checkCellPhoneNumber()) {
                JOptionPane.showMessageDialog(null, "Cell number successfully captured.");
                break;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Cell number is not correctly formatted.\nIt must start with '+' and be followed by exactly 11 digits (e.g., +27789709652).");
            }
        }

        // registration complete
        JOptionPane.showMessageDialog(null, loginClass.registerUser());

        //login begins here
        boolean loginSuccess = false;

        while (!loginSuccess) {
            String loginUsername = JOptionPane.showInputDialog("Login - Enter your username:");
            String loginPassword = JOptionPane.showInputDialog("Login - Enter your password:");

            loginSuccess = loginClass.loginUser(loginUsername, loginPassword);
            String loginMessage = loginClass.returnLoginStatus(loginSuccess);
            JOptionPane.showMessageDialog(null, loginMessage);
        }
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat!");

        //  successful login
       MenuHandler.handleMenu();
    }

    public static String generateMessageID() {
          long min = 1000000000L; // minimum 10 digit number
    long max = 9999999999L; // maximum 10 digit number
    long randomNum = min + (long)(Math.random() * (max - min + 1));
    return String.valueOf(randomNum);
    }

    private static void loadStoredMessagesFromFile() {
        String filePath = "storedMessages.json";

        try {
            File file = new File(filePath);

            if (file.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                JSONArray storedMessagesJson = new JSONArray(content);

                for (int i = 0; i < storedMessagesJson.length(); i++) {
                    JSONObject obj = storedMessagesJson.getJSONObject(i);
                    String messageID = obj.getString("messageID");
                    String recipient = obj.getString("recipient");
                    String messageText = obj.getString("messageText");
                    String status = obj.getString("status");

                    Message msg = new Message(messageID, recipient, messageText, status);
                    storedMessages[storedCount++] = msg;
                }
            } else {
                JOptionPane.showMessageDialog(null, "No stored messages found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading stored messages file: " + e.getMessage());
        }
    }

    private static String generateHashFromMessage(Message msg) {
        //message hash again
        String firstTwoDigits = msg.getMessageID().substring(0, 2);
        String firstWord = msg.getMessageText().split(" ")[0];
        String[] words = msg.getMessageText().split(" ");
        String lastWord = words[words.length - 1];
        int messageNumber = 1; // hash message 

        return (firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }
    
}

// NEW CHANGES THAT HAVE BEEN MADE:
// i have seperated the cluttered code, so that the main becomes shorter and easier to read. 
//made sure all tests are working
//(OpenAI, 2025)
//OpenAI. (2025). ChatGPT (June 2025 version) [Large language model]. https://chat.openai.com/


//(Microsoft Copilot,2025)
// promts : How do I validate a phone number in Java using regex?
 //How do I hash a string in Java for display?
// Best practice for storing messages in an array in Java
//BETTER coding practice

