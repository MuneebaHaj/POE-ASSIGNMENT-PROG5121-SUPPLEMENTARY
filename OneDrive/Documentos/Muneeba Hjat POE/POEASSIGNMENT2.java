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
        while (true) {
            String option = JOptionPane.showInputDialog(
                    "Please choose an option:\n" +
                    "1. Send Message\n" +
                    "2. Show recently sent messages\n" +
                    "3. Show total messages sent\n" +
                    
                    "5. Show stored messages\n" +
                    "6. Show all message hashes\n" +
                    "7. Show all message IDs\n" +
                    "8. Quit\n" +
                    "9. Show sender and recipient of all sent messages\n" +
                    "10. Show the longest sent message\n" +
                    "11. Search for message by ID\n" +
                    "12. Search for all messages sent to a particular recipient\n" +
                    "13. Delete a message using the message hash\n" +
                    "14. Display full report of all sent messages"
            );

            switch (option) {
                case "1":
                    int totalMessages = 0;
                    int messageCount = 1;
                    int sentMsgCount = 0;

                    try {
                        totalMessages = Integer.parseInt(
                            JOptionPane.showInputDialog("How many messages would you like to send?")
                        );
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid number. Returning to main menu.");
                        break;
                    }

                    for (int i = 0; i < totalMessages; i++) {
                        String recipient;
                        while (true) {
                            recipient = JOptionPane.showInputDialog("Enter recipient's cell number (e.g., +27781234567):");
                            if (recipient != null && recipient.matches(cellRegex)) {
                                break;
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "Invalid recipient number. It must start with '+27' and be followed by exactly 9 digits.");
                            }
                        }

                        String messageID = generateMessageID();
                        messageIDs[idCount++] = messageID;  // Store the generated message ID

                        if (!Message.checkMessageID(messageID)) {
                            JOptionPane.showMessageDialog(null, "Generated Message ID is too long. Skipping this message.");
                            continue;
                        }

                        int currentMessageNumber = messageCount++;

                        String message = JOptionPane.showInputDialog("Enter your message (max 250 characters):");

                        if (message.length() > 250) {
                            JOptionPane.showMessageDialog(null, "Message is too long. Try again.");
                            i--;
                            continue;
                        }

                        String[] words = message.trim().split(" ");
                        String firstWord = words[0];
                        String lastWord = words[words.length - 1];
                        String firstTwoDigits = messageID.substring(0, 2);

                        messageHash = (firstTwoDigits + ":" + currentMessageNumber + ":" + firstWord + lastWord).toUpperCase();
                        messageHashes[hashCount++] = messageHash;  // generated hash is stored

                        JOptionPane.showMessageDialog(null, "Generated Hash: " + messageHash);

                        String action = JOptionPane.showInputDialog(
                            "What would you like to do with this message?\n" +
                            "1. Send Message\n" +
                            "2. Disregard Message\n" +
                            "3. Store Message to send later"
                        );

                        String status = "";

                        if ("1".equals(action)) {
                            status = "Sent";
                            sentMsgCount++;
                            JOptionPane.showMessageDialog(null, "Message sent!");

                            // Add to sentMessages
                            Message msgObj = new Message(messageID, recipient, message, status);
                            sentMessages[sentCount++] = msgObj;

                            

                            JOptionPane.showMessageDialog(null,
                                "Message sent successfully!\n\n" +
                                "Message ID: " + messageID + "\n" +
                                "Message Hash: " + messageHash + "\n" +
                                "Recipient: " + recipient + "\n" +
                                "Message: " + message
                            );

                        } else if ("2".equals(action)) {
                            status = "Disregarded";
                            JOptionPane.showMessageDialog(null, "Message disregarded.");
                            Message msgObj = new Message(messageID, recipient, message, status);
                            disregardedMessages[disregardedCount++] = msgObj;

                        } else if ("3".equals(action)) {
                            status = "Stored";
                            JOptionPane.showMessageDialog(null, "Message stored for later.");

                            Message msgObj = new Message(messageID, recipient, message, status);
                            Message.storeMessage(msgObj);  // Keep your JSON writing method here
                            storedMessages[storedCount++] = msgObj;

                            JSONObject json = new JSONObject();

                            json.put("messageID", messageID);
                            json.put("recipient", recipient);
                            json.put("messageText", message);
                            json.put("status", status);

                            JSONArray messagesArray;
                            String filePath = "storedMessages.json";

                            try {
                                File file = new File(filePath);

                                if (file.exists()) {
                                    String content = new String(Files.readAllBytes(Paths.get(filePath)));
                                    messagesArray = new JSONArray(content);
                                } else {
                                    messagesArray = new JSONArray();
                                }

                                messagesArray.put(json);

                                FileWriter writer = new FileWriter(filePath);
                                writer.write(messagesArray.toString(4));
                                writer.close();

                                JOptionPane.showMessageDialog(null, "Message saved in storedMessages.json.");
                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(null, "Error writing JSON file: " + e.getMessage());
                            }
                        } else {
                            status = "Invalid";
                            JOptionPane.showMessageDialog(null, "Invalid option. Skipping message.");
                        }
                    }

                    JOptionPane.showMessageDialog(null,
                        "Finished capturing " + totalMessages + " message(s).\n" +
                        "Messages successfully sent: " + sentMsgCount);
                    break;

                case "2":
                    if (sentCount == 0) {
                        JOptionPane.showMessageDialog(null, "No sent messages to display.");
                    } else {
                        StringBuilder output = new StringBuilder("Recently Sent Messages:\n\n");
                        for (int i = 0; i < sentCount; i++) {
                            Message m = sentMessages[i];
                            output.append("Message ID: ").append(m.getMessageID()).append("\n")
                                  .append("Recipient: ").append(m.getRecipient()).append("\n")
                                  .append("Message: ").append(m.getMessageText()).append("\n")
                                  .append("Status: ").append(m.getStatus()).append("\n\n");
                        }
                        JOptionPane.showMessageDialog(null, output.toString());
                    }
                    break;

                case "3":
                    JOptionPane.showMessageDialog(null, "Total messages sent: " + sentCount);
                    break;

             

                case "5":
                    if (storedCount == 0) {
                        JOptionPane.showMessageDialog(null, "No stored messages yet.");
                    } else {
                        StringBuilder sOutput = new StringBuilder("Stored Messages:\n\n");
                        for (int i = 0; i < storedCount; i++) {
                            Message m = storedMessages[i];
                            sOutput.append("Message ID: ").append(m.getMessageID()).append("\n")
                                   .append("Recipient: ").append(m.getRecipient()).append("\n")
                                   .append("Message: ").append(m.getMessageText()).append("\n")
                                   .append("Status: ").append(m.getStatus()).append("\n\n");
                        }
                        JOptionPane.showMessageDialog(null, sOutput.toString());
                    }
                    break;

                case "6":
                    if (hashCount == 0) {
                        JOptionPane.showMessageDialog(null, "No message hashes generated yet.");
                    } else {
                        StringBuilder hOutput = new StringBuilder("Message Hashes:\n\n");
                        for (int i = 0; i < hashCount; i++) {
                            hOutput.append(messageHashes[i]).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, hOutput.toString());
                    }
                    break;

                case "7":
                    if (idCount == 0) {
                        JOptionPane.showMessageDialog(null, "No message IDs generated yet.");
                    } else {
                        StringBuilder idOutput = new StringBuilder("Message IDs:\n\n");
                        for (int i = 0; i < idCount; i++) {
                            idOutput.append(messageIDs[i]).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, idOutput.toString());
                    }
                    break;

                case "8":
                    JOptionPane.showMessageDialog(null, "Quitting program. Goodbye!");
                    System.exit(0);
                    break;

                case "9":
                    if (sentCount == 0) {
                        JOptionPane.showMessageDialog(null, "No sent messages yet.");
                    } else {
                        StringBuilder senderRecipientOutput = new StringBuilder("Sender and Recipient of Sent Messages:\n\n");
                        for (int i = 0; i < sentCount; i++) {
                            Message m = sentMessages[i];
                            senderRecipientOutput.append("Sender: ").append(firstName).append(" ").append(lastName).append("\n")
                                                 .append("Recipient: ").append(m.getRecipient()).append("\n\n");
                        }
                        JOptionPane.showMessageDialog(null, senderRecipientOutput.toString());
                    }
                    break;

                case "10":
                    if (sentCount == 0) {
                        JOptionPane.showMessageDialog(null, "No sent messages yet.");
                    } else {
                        int longestIndex = 0;
                        int maxLength = 0;
                        for (int i = 0; i < sentCount; i++) {
                            if (sentMessages[i].getMessageText().length() > maxLength) {
                                maxLength = sentMessages[i].getMessageText().length();
                                longestIndex = i;
                            }
                        }
                        Message longestMsg = sentMessages[longestIndex];
                        JOptionPane.showMessageDialog(null, "Longest sent message:\n\n" + longestMsg.getMessageText());
                    }
                    break;

                case "11":
                    String searchID = JOptionPane.showInputDialog("Enter the message ID to search for:");
                    boolean foundID = false;
                    for (int i = 0; i < sentCount; i++) {
                        if (sentMessages[i].getMessageID().equalsIgnoreCase(searchID)) {
                            Message m = sentMessages[i];
                            JOptionPane.showMessageDialog(null,
                                "Message found:\n\n" +
                                "ID: " + m.getMessageID() + "\n" +
                                "Recipient: " + m.getRecipient() + "\n" +
                                "Message: " + m.getMessageText() + "\n" +
                                "Status: " + m.getStatus());
                            foundID = true;
                            break;
                        }
                    }
                    if (!foundID) {
                        JOptionPane.showMessageDialog(null, "No message found with that ID.");
                    }
                    break;

                case "12":
                    String recipientSearch = JOptionPane.showInputDialog("Enter the recipient number to search for:");
                    StringBuilder foundMessages = new StringBuilder("Messages sent to " + recipientSearch + ":\n\n");
                    boolean foundRecipient = false;
                    for (int i = 0; i < sentCount; i++) {
                        if (sentMessages[i].getRecipient().equals(recipientSearch)) {
                            Message m = sentMessages[i];
                            foundMessages.append("ID: ").append(m.getMessageID()).append("\n")
                                         .append("Message: ").append(m.getMessageText()).append("\n\n");
                            foundRecipient = true;
                        }
                    }
                    if (foundRecipient) {
                        JOptionPane.showMessageDialog(null, foundMessages.toString());
                    } else {
                        JOptionPane.showMessageDialog(null, "No messages found for that recipient.");
                    }
                    break;

                case "13":
                    String hashToDelete = JOptionPane.showInputDialog("Enter the message hash to delete:");
                    boolean deleted = false;

                    // will search in the stores mssgs
                    for (int i = 0; i < storedCount; i++) {
                        String candidateHash = generateHashFromMessage(storedMessages[i]);
                        if (candidateHash.equalsIgnoreCase(hashToDelete)) {
                            
                            for (int j = i; j < storedCount - 1; j++) {
                                storedMessages[j] = storedMessages[j + 1];
                            }
                            storedMessages[--storedCount] = null;  
                            deleted = true;
                            JOptionPane.showMessageDialog(null, "Message with hash " + hashToDelete + " deleted.");
                            break;
                        }
                    }
                    if (!deleted) {
                        JOptionPane.showMessageDialog(null, "No message found with that hash.");
                    }
                    break;

                case "14":
                    if (sentCount == 0) {
                        JOptionPane.showMessageDialog(null, "No sent messages to report.");
                    } else {
                        StringBuilder report = new StringBuilder("Full report of all sent messages:\n\n");
                        for (int i = 0; i < sentCount; i++) {
                            Message m = sentMessages[i];
                            report.append("ID: ").append(m.getMessageID()).append("\n")
                                  .append("Recipient: ").append(m.getRecipient()).append("\n")
                                  .append("Message: ").append(m.getMessageText()).append("\n")
                                  .append("Status: ").append(m.getStatus()).append("\n\n");
                        }
                        JOptionPane.showMessageDialog(null, report.toString());
                    }
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Invalid option. Please select again.");
                    break;
            }
        }
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

//(OpenAI, 2025)
//OpenAI. (2025). ChatGPT (June 2025 version) [Large language model]. https://chat.openai.com/


//(Microsoft Copilot,2025)
// promts : How do I validate a phone number in Java using regex?
 //How do I hash a string in Java for display?
// Best practice for storing messages in an array in Java