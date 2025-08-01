package PoeAssignment;

import javax.swing.JOptionPane;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MenuHandler {

    public static void handleMenu() {
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
                case "1": sendMessage(); break;
                case "2": showSentMessages(); break;
                case "3": showTotalMessagesSent(); break;
                case "5": showStoredMessages(); break;
                case "6": showMessageHashes(); break;
                case "7": showMessageIDs(); break;
                case "8": quitProgram(); break;
                case "9": showSenderRecipient(); break;
                case "10": showLongestMessage(); break;
                case "11": searchByID(); break;
                case "12": searchByRecipient(); break;
                case "13": deleteByHash(); break;
                case "14": showFullReport(); break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option. Please select again.");
                    break;
            }
        }
    }

    private static void sendMessage() {
        int totalMessages = 0;
        int messageCount = 1;
        int sentMsgCount = 0;

        try {
            totalMessages = Integer.parseInt(
                JOptionPane.showInputDialog("How many messages would you like to send?")
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number. Returning to main menu.");
            return;
        }

        for (int i = 0; i < totalMessages; i++) {
            String recipient;
            while (true) {
                recipient = JOptionPane.showInputDialog("Enter recipient's cell number (e.g., +27781234567):");
                if (recipient != null && recipient.matches(POEASSIGNMENT2.cellRegex)) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Invalid recipient number. It must start with '+27' and be followed by exactly 9 digits.");
                }
            }

            String messageID = POEASSIGNMENT2.generateMessageID();
            POEASSIGNMENT2.messageIDs[POEASSIGNMENT2.idCount++] = messageID;  // Store the generated message ID

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

            POEASSIGNMENT2.messageHash = (firstTwoDigits + ":" + currentMessageNumber + ":" + firstWord + lastWord).toUpperCase();
            POEASSIGNMENT2.messageHashes[POEASSIGNMENT2.hashCount++] = POEASSIGNMENT2.messageHash;  // generated hash is stored

            JOptionPane.showMessageDialog(null, "Generated Hash: " + POEASSIGNMENT2.messageHash);

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
                POEASSIGNMENT2.sentMessages[POEASSIGNMENT2.sentCount++] = msgObj;

                JOptionPane.showMessageDialog(null,
                    "Message sent successfully!\n\n" +
                    "Message ID: " + messageID + "\n" +
                    "Message Hash: " + POEASSIGNMENT2.messageHash + "\n" +
                    "Recipient: " + recipient + "\n" +
                    "Message: " + message
                );

            } else if ("2".equals(action)) {
                status = "Disregarded";
                JOptionPane.showMessageDialog(null, "Message disregarded.");
                Message msgObj = new Message(messageID, recipient, message, status);
                POEASSIGNMENT2.disregardedMessages[POEASSIGNMENT2.disregardedCount++] = msgObj;

            } else if ("3".equals(action)) {
                status = "Stored";
                JOptionPane.showMessageDialog(null, "Message stored for later.");

                Message msgObj = new Message(messageID, recipient, message, status);
                Message.storeMessage(msgObj);  // Keep your JSON writing method here
                POEASSIGNMENT2.storedMessages[POEASSIGNMENT2.storedCount++] = msgObj;

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
    }

    private static void showSentMessages() {
        if (POEASSIGNMENT2.sentCount == 0) {
            JOptionPane.showMessageDialog(null, "No sent messages to display.");
        } else {
            StringBuilder output = new StringBuilder("Recently Sent Messages:\n\n");
            for (int i = 0; i < POEASSIGNMENT2.sentCount; i++) {
                Message m = POEASSIGNMENT2.sentMessages[i];
                output.append("Message ID: ").append(m.getMessageID()).append("\n")
                      .append("Recipient: ").append(m.getRecipient()).append("\n")
                      .append("Message: ").append(m.getMessageText()).append("\n")
                      .append("Status: ").append(m.getStatus()).append("\n\n");
            }
            JOptionPane.showMessageDialog(null, output.toString());
        }
    }

    private static void showTotalMessagesSent() {
        JOptionPane.showMessageDialog(null, "Total messages sent: " + POEASSIGNMENT2.sentCount);
    }

    private static void showStoredMessages() {
        if (POEASSIGNMENT2.storedCount == 0) {
            JOptionPane.showMessageDialog(null, "No stored messages yet.");
        } else {
            StringBuilder sOutput = new StringBuilder("Stored Messages:\n\n");
            for (int i = 0; i < POEASSIGNMENT2.storedCount; i++) {
                Message m = POEASSIGNMENT2.storedMessages[i];
                sOutput.append("Message ID: ").append(m.getMessageID()).append("\n")
                       .append("Recipient: ").append(m.getRecipient()).append("\n")
                       .append("Message: ").append(m.getMessageText()).append("\n")
                       .append("Status: ").append(m.getStatus()).append("\n\n");
            }
            JOptionPane.showMessageDialog(null, sOutput.toString());
        }
    }

    private static void showMessageHashes() {
        if (POEASSIGNMENT2.hashCount == 0) {
            JOptionPane.showMessageDialog(null, "No message hashes generated yet.");
        } else {
            StringBuilder hOutput = new StringBuilder("Message Hashes:\n\n");
            for (int i = 0; i < POEASSIGNMENT2.hashCount; i++) {
                hOutput.append(POEASSIGNMENT2.messageHashes[i]).append("\n");
            }
            JOptionPane.showMessageDialog(null, hOutput.toString());
        }
    }

    private static void showMessageIDs() {
        if (POEASSIGNMENT2.idCount == 0) {
            JOptionPane.showMessageDialog(null, "No message IDs generated yet.");
        } else {
            StringBuilder idOutput = new StringBuilder("Message IDs:\n\n");
            for (int i = 0; i < POEASSIGNMENT2.idCount; i++) {
                idOutput.append(POEASSIGNMENT2.messageIDs[i]).append("\n");
            }
            JOptionPane.showMessageDialog(null, idOutput.toString());
        }
    }

    private static void quitProgram() {
        JOptionPane.showMessageDialog(null, "Quitting program. Goodbye!");
        System.exit(0);
    }

    private static void showSenderRecipient() {
        if (POEASSIGNMENT2.sentCount == 0) {
            JOptionPane.showMessageDialog(null, "No sent messages yet.");
        } else {
            StringBuilder senderRecipientOutput = new StringBuilder("Sender and Recipient of Sent Messages:\n\n");
            for (int i = 0; i < POEASSIGNMENT2.sentCount; i++) {
                Message m = POEASSIGNMENT2.sentMessages[i];
                senderRecipientOutput.append("Sender: ").append(POEASSIGNMENT2.firstName).append(" ").append(POEASSIGNMENT2.lastName).append("\n")
                                     .append("Recipient: ").append(m.getRecipient()).append("\n\n");
            }
            JOptionPane.showMessageDialog(null, senderRecipientOutput.toString());
        }
    }

    private static void showLongestMessage() {
        if (POEASSIGNMENT2.sentCount == 0) {
            JOptionPane.showMessageDialog(null, "No sent messages yet.");
        } else {
            int longestIndex = 0;
            int maxLength = 0;
            for (int i = 0; i < POEASSIGNMENT2.sentCount; i++) {
                if (POEASSIGNMENT2.sentMessages[i].getMessageText().length() > maxLength) {
                    maxLength = POEASSIGNMENT2.sentMessages[i].getMessageText().length();
                    longestIndex = i;
                }
            }
            Message longestMsg = POEASSIGNMENT2.sentMessages[longestIndex];
            JOptionPane.showMessageDialog(null, "Longest sent message:\n\n" + longestMsg.getMessageText());
        }
    }

    private static void searchByID() {
        String searchID = JOptionPane.showInputDialog("Enter the message ID to search for:");
        boolean foundID = false;
        for (int i = 0; i < POEASSIGNMENT2.sentCount; i++) {
            if (POEASSIGNMENT2.sentMessages[i].getMessageID().equalsIgnoreCase(searchID)) {
                Message m = POEASSIGNMENT2.sentMessages[i];
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
    }

    private static void searchByRecipient() {
        String recipientSearch = JOptionPane.showInputDialog("Enter the recipient number to search for:");
        StringBuilder foundMessages = new StringBuilder("Messages sent to " + recipientSearch + ":\n\n");
        boolean foundRecipient = false;
        for (int i = 0; i < POEASSIGNMENT2.sentCount; i++) {
            if (POEASSIGNMENT2.sentMessages[i].getRecipient().equals(recipientSearch)) {
                Message m = POEASSIGNMENT2.sentMessages[i];
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
    }

    private static void deleteByHash() {
        String hashToDelete = JOptionPane.showInputDialog("Enter the message hash to delete:");
        boolean deleted = false;

        // Search stored messages for hash
        for (int i = 0; i < POEASSIGNMENT2.storedCount; i++) {
            String candidateHash = generateHashFromMessage(POEASSIGNMENT2.storedMessages[i]);
            if (candidateHash.equalsIgnoreCase(hashToDelete)) {
                for (int j = i; j < POEASSIGNMENT2.storedCount - 1; j++) {
                    POEASSIGNMENT2.storedMessages[j] = POEASSIGNMENT2.storedMessages[j + 1];
                }
                POEASSIGNMENT2.storedMessages[--POEASSIGNMENT2.storedCount] = null;  
                deleted = true;
                JOptionPane.showMessageDialog(null, "Message with hash " + hashToDelete + " deleted.");
                break;
            }
        }
        if (!deleted) {
            JOptionPane.showMessageDialog(null, "No message found with that hash.");
        }
    }

    private static void showFullReport() {
        if (POEASSIGNMENT2.sentCount == 0) {
            JOptionPane.showMessageDialog(null, "No sent messages to report.");
        } else {
            StringBuilder report = new StringBuilder("Full report of all sent messages:\n\n");
            for (int i = 0; i < POEASSIGNMENT2.sentCount; i++) {
                Message m = POEASSIGNMENT2.sentMessages[i];
                report.append("ID: ").append(m.getMessageID()).append("\n")
                      .append("Recipient: ").append(m.getRecipient()).append("\n")
                      .append("Message: ").append(m.getMessageText()).append("\n")
                      .append("Status: ").append(m.getStatus()).append("\n\n");
            }
            JOptionPane.showMessageDialog(null, report.toString());
        }
    }

    private static String generateHashFromMessage(Message msg) {
        String firstTwoDigits = msg.getMessageID().substring(0, 2);
        String[] words = msg.getMessageText().split(" ");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        int messageNumber = 1; // fixed in original, so keep same

        return (firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }
}
//(OpenAI, 2025)
//OpenAI. (2025). ChatGPT (June 2025 version) [Large language model]. https://chat.openai.com/
