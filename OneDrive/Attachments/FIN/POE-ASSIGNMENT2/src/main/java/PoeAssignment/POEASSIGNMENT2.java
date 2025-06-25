package PoeAssignment;


import javax.swing.JOptionPane;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

// Part 3 Arrays POE

public class POEASSIGNMENT2 {
    

    
    public static ArrayList<Message> sentMessages = new ArrayList<>();
public static ArrayList<Message> disregardedMessages = new ArrayList<>();
public static ArrayList<Message> storedMessages = new ArrayList<>();
public static ArrayList<String> messageHashes = new ArrayList<>();
public static ArrayList<String> messageIDs = new ArrayList<>();



public static        String username;
public static        String password;
public static        String cellNumber;
public static        String firstName;
public static        String lastName;
public static        String messageHash;
public static String cellRegex = "^\\+27\\d{9}$";
    public static void main(String[] args) {
        Login loginClass = new Login();
loadStoredMessagesFromFile();

        

        // Capture First and Last Name 
        firstName = JOptionPane.showInputDialog("Enter your first name:");
        lastName = JOptionPane.showInputDialog("Enter your last name:");

        // Username Validation (RETRY until valid) 
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

        //  Password Validation (retries until its valid) 
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

        //  Cell Number Validation (retries until its valid) 
        while (true) {
            cellNumber = JOptionPane.showInputDialog("Enter your cell phone number (e.g., +27789709652):");

            // Regex: Starts with '+' followed by exactly 11 digits
            

            if (loginClass.checkCellPhoneNumber()) {
                JOptionPane.showMessageDialog(null, "Cell number successfully captured.");
                break;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Cell number is not correctly formatted.\nIt must start with '+' and be followed by exactly 11 digits (e.g., +27789709652).");
            }
        }
// === Show Registration Confirmation ===
JOptionPane.showMessageDialog(null, loginClass.registerUser());

       // === LOGIN PROCESS ===
boolean loginSuccess = false;

while (!loginSuccess) {
    String loginUsername = JOptionPane.showInputDialog("Login - Enter your username:");
    String loginPassword = JOptionPane.showInputDialog("Login - Enter your password:");

    loginSuccess = loginClass.loginUser(loginUsername, loginPassword);
    String loginMessage = loginClass.returnLoginStatus(loginSuccess);
    JOptionPane.showMessageDialog(null, loginMessage);
}
JOptionPane.showMessageDialog(null, "Welcome to QuickChat!");
// After successful login

while (true) {
String option = JOptionPane.showInputDialog(
    "Please choose an option:\n" +
    "1. Send Message\n" +
    "2. Show recently sent messages\n" +
    "3. Show total messages sent\n" +
    "4. Show disregarded messages\n" +
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
        int sentCount = 0;

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
messageIDs.add(messageID);  // Store the generated message ID

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
messageHashes.add(messageHash);  //  Store the generated hash

            JOptionPane.showMessageDialog(null, "Generated Hash: " + messageHash);

           String action = JOptionPane.showInputDialog(
    "What would you like to do with this message?\n" +
    "1. Send Message\n" +
    "2. Disregard Message\n" +
    "3. Store Message to send later"
);


            String status = "";

            if (action.equals("1")) {
                status = "Sent";
                sentCount++;
                JOptionPane.showMessageDialog(null, "Message sent!");

                //  Add to sentMessages
                Message msgObj = new Message(messageID, recipient, message, status);
sentMessages.add(msgObj);  //  Add to sent list in main class

                Message.sentMessages.add(msgObj);

                JOptionPane.showMessageDialog(null,
                    "Message sent successfully!\n\n" +
                    "Message ID: " + messageID + "\n" +
                    "Message Hash: " + messageHash + "\n" +
                    "Recipient: " + recipient + "\n" +
                    "Message: " + message
                );

            } else if (action.equals("2")) {
                status = "Disregarded";
                JOptionPane.showMessageDialog(null, "Message disregarded.");
Message msgObj = new Message(messageID, recipient, message, status);
disregardedMessages.add(msgObj);  //  Add to disregarded list

            } else if (action.equals("3")) {
                status = "Stored";
                JOptionPane.showMessageDialog(null, "Message stored for later.");

                Message msgObj = new Message(messageID, recipient, message, status);
Message.storeMessage(msgObj);  // all the JSON writing code replaced
storedMessages.add(msgObj);  // Add to stored list

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
            "Messages successfully sent: " + sentCount);
        break;

    case "2":
        JOptionPane.showMessageDialog(null, Message.printMessages());
        break;

    case "3":
        int total = Message.returnTotalMessages();
        JOptionPane.showMessageDialog(null, "Total messages sent: " + total);
        break;

       case "4":
        if (disregardedMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No disregarded messages yet.");
        } else {
            StringBuilder dOutput = new StringBuilder("Disregarded Messages:\n\n");
            for (Message m : disregardedMessages) {
    dOutput.append("Message ID: ").append(m.getMessageID()).append("\n")
           .append("Recipient: ").append(m.getRecipient()).append("\n")
           .append("Message: ").append(m.getMessageText()).append("\n")
           .append("Status: ").append(m.getStatus()).append("\n\n");
}

            JOptionPane.showMessageDialog(null, dOutput.toString());
        }
        break;

    case "5":
        if (storedMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No stored messages yet.");
        } else {
            StringBuilder sOutput = new StringBuilder("Stored Messages:\n\n");
            for (Message m : storedMessages) {
    sOutput.append("Message ID: ").append(m.getMessageID()).append("\n")
           .append("Recipient: ").append(m.getRecipient()).append("\n")
           .append("Message: ").append(m.getMessageText()).append("\n")
           .append("Status: ").append(m.getStatus()).append("\n\n");
}

            JOptionPane.showMessageDialog(null, sOutput.toString());
        }
        break;

    case "6":
        if (messageHashes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No message hashes generated yet.");
        } else {
            StringBuilder hOutput = new StringBuilder("Message Hashes:\n\n");
            for (String hash : messageHashes) {
                hOutput.append(hash).append("\n");
            }
            JOptionPane.showMessageDialog(null, hOutput.toString());
        }
        break;

    case "7":
        if (messageIDs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No message IDs generated yet.");
        } else {
            StringBuilder idOutput = new StringBuilder("Message IDs:\n\n");
            for (String id : messageIDs) {
                idOutput.append(id).append("\n");
            }
            JOptionPane.showMessageDialog(null, idOutput.toString());
        }
        break;

    case "8":
        JOptionPane.showMessageDialog(null, "Goodbye!");
        System.exit(0);
        break;
case "9":
    if (sentMessages.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No sent messages available.");
    } else {
        StringBuilder srOutput = new StringBuilder("Senders and Recipients of Sent Messages:\n\n");
        for (Message m : sentMessages) {
            srOutput.append("Sender: ").append(firstName).append(" ").append(lastName).append("\n")
                    .append("Recipient: ").append(m.getRecipient()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, srOutput.toString());
    }
    break;
case "10":
    if (sentMessages.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No sent messages available.");
    } else {
        Message longestMsg = sentMessages.get(0);
        for (Message m : sentMessages) {
            if (m.getMessageText().length() > longestMsg.getMessageText().length()) {
                longestMsg = m;
            }
        }

        JOptionPane.showMessageDialog(null,
            "Longest Sent Message:\n\n" +
            "Sender: " + firstName + " " + lastName + "\n" +
            "Recipient: " + longestMsg.getRecipient() + "\n" +
            "Message ID: " + longestMsg.getMessageID() + "\n" +
            "Message: " + longestMsg.getMessageText() + "\n" +
            "Length: " + longestMsg.getMessageText().length() + " characters"
        );
    }
    break;
case "11":
    String searchID = JOptionPane.showInputDialog("Enter the Message ID to search for:");
    if (searchID == null || searchID.isBlank()) {
        JOptionPane.showMessageDialog(null, "Search cancelled or no ID entered.");
        break;
    }
    boolean found = false;
    for (Message m : sentMessages) {
        if (m.getMessageID().equals(searchID.trim())) {
            JOptionPane.showMessageDialog(null,
                "Message ID: " + m.getMessageID() + "\n" +
                "Recipient: " + m.getRecipient() + "\n" +
                "Message: " + m.getMessageText()
            );
            found = true;
            break;
        }
    }
    if (!found) {
        JOptionPane.showMessageDialog(null, "No sent message found with ID: " + searchID);
    }
    break;
case "12":
    String searchRecipient = JOptionPane.showInputDialog("Enter the recipient number to search for (e.g., +27834557896):");

    if (searchRecipient == null || searchRecipient.isBlank()) {
        JOptionPane.showMessageDialog(null, "Search cancelled or no recipient entered.");
        break;
    }

    StringBuilder results = new StringBuilder("Messages sent to " + searchRecipient + ":\n\n");
    boolean foundAny = false;

    for (Message m : sentMessages) {
        if (m.getRecipient().equalsIgnoreCase(searchRecipient.trim())) {
            results.append("Message ID: ").append(m.getMessageID()).append("\n")
                   .append("Message: ").append(m.getMessageText()).append("\n\n");
            foundAny = true;
        }
    }

    if (foundAny) {
        JOptionPane.showMessageDialog(null, results.toString());
    } else {
        JOptionPane.showMessageDialog(null, "No messages found for recipient: " + searchRecipient);
    }
    break;
case "13":
    String hashToDelete = JOptionPane.showInputDialog("Enter the message hash to delete:");

    if (hashToDelete == null || hashToDelete.isBlank()) {
        JOptionPane.showMessageDialog(null, "No hash entered. Cancelled.");
        break;
    }

    boolean deleted = false;

    for (int i = 0; i < sentMessages.size(); i++) {
        Message m = sentMessages.get(i);
        String generatedHash = m.createMessageHash(i + 1); // using message number as index + 1

        if (generatedHash.equalsIgnoreCase(hashToDelete.trim())) {
            sentMessages.remove(i);
            messageHashes.removeIf(h -> h.equalsIgnoreCase(hashToDelete.trim()));
            JOptionPane.showMessageDialog(null, "Message with hash '" + hashToDelete + "' was deleted.");
            deleted = true;
            break;
        }
    }

    if (!deleted) {
        JOptionPane.showMessageDialog(null, "No message found with hash: " + hashToDelete);
    }
    break;
case "14":
    if (sentMessages.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No sent messages available to display.");
    } else {
        StringBuilder report = new StringBuilder("=== Full Report of Sent Messages ===\n\n");

        int messageNumber = 1;
        for (Message m : sentMessages) {
            String hash = m.createMessageHash(messageNumber);
            report.append("Message #").append(messageNumber++).append("\n")
                  .append("Sender: ").append(firstName).append(" ").append(lastName).append("\n")
                  .append("Recipient: ").append(m.getRecipient()).append("\n")
                  .append("Message ID: ").append(m.getMessageID()).append("\n")
                  .append("Message: ").append(m.getMessageText()).append("\n")
                  .append("Status: ").append(m.getStatus()).append("\n")
                  .append("Hash: ").append(hash).append("\n\n");
        }

        JOptionPane.showMessageDialog(null, report.toString());
    }
    break;

    default:
        JOptionPane.showMessageDialog(null, "Invalid option. Please choose a number from 1 to 14.");
        break;
}

}
    
    }
    public static String generateMessageID() {
    long min = 1000000000L; // minimum 10-digit number
    long max = 9999999999L; // maximum 10-digit number
    long randomNum = min + (long)(Math.random() * (max - min + 1));
    return String.valueOf(randomNum);
}
public static void loadStoredMessagesFromFile() {
    String filePath = "storedMessages.json";

    try {
        File file = new File(filePath);
        if (file.exists()) {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray array = new JSONArray(content);

            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                String messageID = json.getString("messageID");
                String recipient = json.getString("recipient");
                String messageText = json.getString("messageText");
                String status = json.getString("status");

                Message msg = new Message(messageID, recipient, messageText, status);
                storedMessages.add(msg);
            }

            JOptionPane.showMessageDialog(null, "Stored messages loaded from file.");
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error reading storedMessages.json: " + e.getMessage());
    }
}
//(OpenAI, 2025)
//(Microsoft Copilot,2025)
// promts : How do I validate a phone number in Java using regex?
 //How do I hash a string in Java for display?
// Best practice for storing messages in an array in Java
}
