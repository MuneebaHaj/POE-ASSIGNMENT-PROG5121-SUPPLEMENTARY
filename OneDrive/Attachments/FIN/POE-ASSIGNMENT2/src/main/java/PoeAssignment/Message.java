package PoeAssignment;

import org.json.JSONObject;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;


public class Message {
    // Fields
    private String messageID;
    private String messageHash = POEASSIGNMENT2.messageHash;
    private String recipient;
    private String messageText;
    private String status;
    public static ArrayList<Message> sentMessages = new ArrayList<>();

    // Constructor
    public Message(String messageID, String recipient, String messageText, String status) {
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        this.status = status;
    }
    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getStatus() {
        return status;
    }

    // Row 1: Boolean checkMessageID()
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }
    public boolean checkRecipientCell() {
    return recipient != null && recipient.length() <= 13 && recipient.startsWith("+");
}
public String createMessageHash(int messageNumber) {
    String firstWord = "";
    String lastWord = "";

    if (messageText != null && !messageText.trim().isEmpty()) {
    String[] words = messageText.trim().split(" ");
        firstWord = words[0];
        lastWord = words[words.length - 1];
    }

    String firstTwoDigits = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;

    String hash = (firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();

    return hash;
}
public String SentMessage() {
    String choice = JOptionPane.showInputDialog("""
        What would you like to do with this message?
        1. Send Message
        2. Disregard Message
        3. Store Message to send later
    """);

    switch (choice) {
        case "1":
            JOptionPane.showMessageDialog(null, "Message sent!");
            Message.sentMessages.add(this);  // Add this message object to the static list
            return "Sent";
        case "2":
            JOptionPane.showMessageDialog(null, "Message disregarded.");
            return "Disregarded";
        case "3":
            JOptionPane.showMessageDialog(null, "Message stored for later.");
            return "Stored";
        default:
            JOptionPane.showMessageDialog(null, "Invalid choice. Disregarding message.");
            return "Invalid";
    }
}public static String printMessages() {
    if (sentMessages.isEmpty()) {
        return "No messages have been sent yet.";
    }

    StringBuilder output = new StringBuilder("Recently Sent Messages:\n\n");

    for (Message m : sentMessages) {
        output.append("Message ID: ").append(m.messageID).append("\n")
                .append("messageHash: ").append(m.messageHash).append("\n")
              .append("Recipient: ").append(m.recipient).append("\n")
              .append("Message: ").append(m.messageText).append("\n")
              .append("Status: ").append(m.status).append("\n\n");
    }

    return output.toString();
}

public static int returnTotalMessages() {
    return sentMessages.size();
}
public static void storeMessage(Message msg) {
    JSONObject json = new JSONObject();
    json.put("messageID", msg.messageID);
    json.put("recipient", msg.recipient);
    json.put("messageText", msg.messageText);
    json.put("status", msg.status);

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
        writer.write(messagesArray.toString(4));  // Pretty print
        writer.close();

        JOptionPane.showMessageDialog(null, "Message stored in storedMessages.json.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Failed to store message: " + e.getMessage());
    }
}
public static boolean checkMessageID(String messageID) {
    return messageID.length() <= 10;
}
public String simulateMessageAction(String choice) {
    switch (choice) {
        case "1":
            sentMessages.add(this);
            return "Sent";
        case "2":
            return "Disregarded";
        case "3":
            return "Stored";
        default:
            return "Invalid";
    }
}

   
}