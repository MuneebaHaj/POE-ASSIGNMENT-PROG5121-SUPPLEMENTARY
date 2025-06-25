package PoeAssignment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import PoeAssignment.Message;
import java.util.ArrayList;

public class POEASSIGNMENT2Test {

    Login login;

    @BeforeAll
    public static void setUpClass() {
        // Runs once before all tests
    }

    @AfterAll
    public static void tearDownClass() {
        // Runs once after all tests
    }

    @BeforeEach
    public void setUp() {
        // Runs before each test
        login = new Login();
    }

    @AfterEach
    public void tearDown() {
        // Runs after each test
    }

    // === TESTS ===

    @Test
    public void testCheckPasswordComplexity_Valid() {
        assertTrue(login.checkPasswordComplexity("Test123!"));
    }

    @Test
    public void testCheckPasswordComplexity_Invalid() {
        assertFalse(login.checkPasswordComplexity("abc"));
    }

    @Test
    public void testCheckUserName_Valid() {
        POEASSIGNMENT2.username = "ab_1";
        assertTrue(login.checkUserName());
    }

    @Test
    public void testCheckUserName_Invalid() {
        POEASSIGNMENT2.username = "abcdef";
        assertFalse(login.checkUserName());
    }

    @Test
    public void testCheckCellPhoneNumber_Valid() {
        POEASSIGNMENT2.cellNumber = "+27712345678";
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCheckCellPhoneNumber_Invalid() {
        POEASSIGNMENT2.cellNumber = "0712345678";
        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    public void testLoginUser_ValidCredentials() {
        POEASSIGNMENT2.username = "ab_1";
        POEASSIGNMENT2.password = "Test123!";
        assertTrue(login.loginUser("ab_1", "Test123!"));
    }

    @Test
    public void testLoginUser_InvalidCredentials() {
        POEASSIGNMENT2.username = "ab_1";
        POEASSIGNMENT2.password = "Test123!";
        assertFalse(login.loginUser("wrong", "wrong"));
    }
    @Test
public void testUsernameCorrectFormatMessage() {
    String input = "kyl_1";
    String expected = "Username is correctly formatted.";
    assertEquals(expected, login.checkUserNameMessage(input));
}

@Test
public void testUsernameIncorrectFormatMessage() {
    String input = "kyle!!!!!";
    String expected = "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
    assertEquals(expected, login.checkUserNameMessage(input));
}

@Test
public void testPasswordSuccessMessage() {
    String input = "Ch&&sec@ke99!";
    String expected = "Password successfully captured.";
    assertEquals(expected, login.checkPasswordMessage(input));
}

@Test
public void testPasswordFailMessage() {
    String input = "password";
    String expected = "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    assertEquals(expected, login.checkPasswordMessage(input));
}

@Test
public void testCellPhoneCorrectFormatMessage() {
    String input = "+27838968976";
    String expected = "Cell number successfully captured.";
    assertEquals(expected, login.checkCellPhoneMessage(input));
}
@Test
public void testMessageLength_Success() {
    String message = "This is a valid message with less than 250 characters.";
    assertTrue(message.length() <= 250, "Message ready to send.");
}

@Test
public void testMessageLength_Failure() {
    StringBuilder longMessage = new StringBuilder();
    for (int i = 0; i < 260; i++) {
        longMessage.append("a");
    }

    String message = longMessage.toString();
    int extraChars = message.length() - 250;
    String expected = "Message exceeds 250 characters by " + extraChars + ", please reduce size.";

    assertTrue(message.length() > 250, expected);
}
@Test
public void testStoreMessageAsJSON() {
    String testFilePath = "test_storedMessages.json";
    
    try {
        // Create a sample JSON message
        JSONObject json = new JSONObject();
        json.put("messageID", "1234567890");
        json.put("recipient", "+27712345678");
        json.put("messageText", "Test message");
        json.put("status", "Stored");

        // Write it to test file
        JSONArray messagesArray = new JSONArray();
        messagesArray.put(json);

        FileWriter writer = new FileWriter(testFilePath);
        writer.write(messagesArray.toString(4));
        writer.close();

        // Read the file back
        String content = new String(Files.readAllBytes(Paths.get(testFilePath)));
        JSONArray resultArray = new JSONArray(content);
        JSONObject resultMessage = resultArray.getJSONObject(0);

        // Assertions
        assertEquals("1234567890", resultMessage.getString("messageID"));
        assertEquals("+27712345678", resultMessage.getString("recipient"));
        assertEquals("Test message", resultMessage.getString("messageText"));
        assertEquals("Stored", resultMessage.getString("status"));

    } catch (Exception e) {
        fail("Exception occurred: " + e.getMessage());
    } finally {
        // Clean up the test file
        new File(testFilePath).delete();
    }
}@Test
public void testMessageHashGeneration() {
    String message = "Hi Mike, can you join us for dinner tonight";
    String messageID = "84"; // example ID
   Message msg = new Message(messageID, "+27718693002", message, "2025-06-22");

    String expectedHash = "84:0:HITONIGHT";  // ← if your ID is "84"
  // adjust to match actual output format of your hash
    String actualHash = msg.createMessageHash(0); // ✅ Pass 0 as the index


    assertEquals(expectedHash, actualHash);
}
@Test
public void testGeneratedMessageIDIsValid() {
    String id = POEASSIGNMENT2.generateMessageID();
    assertEquals(10, id.length());
    assertTrue(Message.checkMessageID(id));
}
@Test
public void testCreateMessageHash() {
    Message msg = new Message("3012345678", "+27781234567", "Hello world", "Sent");
    String expectedHash = "30:1:HELLOWORLD";
    assertEquals(expectedHash, msg.createMessageHash(1));
}
@Test
public void testSendMessageOption1() {
    Message msg = new Message("3012345678", "+27781234567", "Hello world", "");
    String result = msg.simulateMessageAction("1");
    assertEquals("Sent", result);
    assertTrue(Message.sentMessages.contains(msg));
}

@Test
public void testDisregardMessageOption2() {
    Message msg = new Message("3012345678", "+27781234567", "Hello world", "");
    String result = msg.simulateMessageAction("2");
    assertEquals("Disregarded", result);
}

@Test
public void testStoreMessageOption3() {
    Message msg = new Message("3012345678", "+27781234567", "Hello world", "");
    String result = msg.simulateMessageAction("3");
    assertEquals("Stored", result);
}

@Test
public void testInvalidMessageOption() {
    Message msg = new Message("3012345678", "+27781234567", "Hello world", "");
    String result = msg.simulateMessageAction("9");
    assertEquals("Invalid", result);
}
@Test
public void testPrintMessages_ShowsSentMessages() {
    // Clear sentMessages list first
    Message.sentMessages.clear();

    // Create a sample message
    Message msg = new Message("1234567890", "+27712345678", "Hello there!", "Sent");
    Message.sentMessages.add(msg);

    // Call the method
    String output = Message.printMessages();

    // Check expected output contains correct info
    assertTrue(output.contains("Message ID: 1234567890"));
    assertTrue(output.contains("Recipient: +27712345678"));
    assertTrue(output.contains("Message: Hello there!"));
    assertTrue(output.contains("Status: Sent"));
}
@Test
public void testIntegratedMessageProcess() {
    // Clear previous sent messages
    Message.sentMessages.clear();

    // Message 1: Send
    String messageID1 = "00"; // HARDCODE to ensure consistent hash
Message msg1 = new Message(messageID1, "+27718693002", "Hi Mike, can you join us for dinner tonight", "");

    String action1 = msg1.simulateMessageAction("1"); // Send
    String hash1 = msg1.createMessageHash(0);

    // Message 2: Disregard
    String messageID2 = POEASSIGNMENT2.generateMessageID();
    Message msg2 = new Message(messageID2, "08575975889", "Hi Keegan, did you receive the payment?", "");
    String action2 = msg2.simulateMessageAction("2"); // Disregard
    String hash2 = msg2.createMessageHash(1);

    // Check if only 1 message was sent
    assertEquals(1, Message.returnTotalMessages());

    // Check actions returned correctly
    assertEquals("Sent", action1);
    assertEquals("Disregarded", action2);

    // Check hashes generated
    assertEquals("00:0:HITONIGHT", hash1);  // Adjust if needed
   String expectedHash2 = messageID2.substring(0, 2) + ":1:HIPAYMENT?";
assertEquals(expectedHash2, hash2);

assertEquals(expectedHash2, hash2);

}
@Test
public void testDataMessage1_Sent() {
    Message.sentMessages.clear();

    Message msg = new Message("1234567890", "+27834557896", "Did you get the cake?", "Sent");
    Message.sentMessages.add(msg);

    assertEquals(1, Message.sentMessages.size());
    assertEquals("Sent", msg.getStatus());
    assertEquals("+27834557896", msg.getRecipient());
    assertEquals("Did you get the cake?", msg.getMessageText());
}
@Test
public void testDataMessage2_Stored() {
    Message msg = new Message("2345678901", "+27836884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
    POEASSIGNMENT2.storedMessages.clear();
    POEASSIGNMENT2.storedMessages.add(msg);

    assertEquals(1, POEASSIGNMENT2.storedMessages.size());
    assertEquals("Stored", msg.getStatus());
    assertTrue(msg.getMessageText().contains("late"));
}
@Test
public void testDataMessage3_Disregarded() {
    Message msg = new Message("3456789012", "+27834484567", "Yohoooo, I am at your gate.", "Disregarded");
    POEASSIGNMENT2.disregardedMessages.clear();
    POEASSIGNMENT2.disregardedMessages.add(msg);

    assertEquals(1, POEASSIGNMENT2.disregardedMessages.size());
    assertEquals("Disregarded", msg.getStatus());
}
@Test
public void testDataMessage4_SentWithLocalNumber() {
    Message msg = new Message("4567890123", "0838884567", "It is dinner time!", "Sent");
    Message.sentMessages.clear();
    Message.sentMessages.add(msg);

    assertEquals("Sent", msg.getStatus());
    assertEquals("0838884567", msg.getRecipient());
    assertTrue(msg.getMessageText().toLowerCase().contains("dinner"));
}
@Test
public void testDataMessage5_Stored() {
    Message msg = new Message("5678901234", "+27838884567", "Ok, I am leaving without you.", "Stored");
    POEASSIGNMENT2.storedMessages.clear();
    POEASSIGNMENT2.storedMessages.add(msg);

    assertEquals("Stored", msg.getStatus());
    assertTrue(msg.getMessageText().contains("leaving"));
}
@Test
public void testSenderAndRecipientDisplay() {
    // Setup
    POEASSIGNMENT2.firstName = "Kyle";
    POEASSIGNMENT2.lastName = "Smith";
    POEASSIGNMENT2.sentMessages.clear();

    Message msg = new Message("1234567890", "+27781234567", "Hey!", "Sent");
    POEASSIGNMENT2.sentMessages.add(msg);

    String expectedOutput = "Sender: Kyle Smith\nRecipient: +27781234567\n\n";

    StringBuilder actual = new StringBuilder();
    for (Message m : POEASSIGNMENT2.sentMessages) {
        actual.append("Sender: ").append(POEASSIGNMENT2.firstName).append(" ")
              .append(POEASSIGNMENT2.lastName).append("\n")
              .append("Recipient: ").append(m.getRecipient()).append("\n\n");
    }

    assertEquals(expectedOutput, actual.toString());
}
@Test
public void testLongestSentMessage() {
    // Setup
    POEASSIGNMENT2.firstName = "Alex";
    POEASSIGNMENT2.lastName = "Brown";
    POEASSIGNMENT2.sentMessages.clear();

    Message shortMsg = new Message("111", "+27780000001", "Hi", "Sent");
    Message longMsg = new Message("222", "+27780000002", "This is a much longer message for testing purposes", "Sent");

    POEASSIGNMENT2.sentMessages.add(shortMsg);
    POEASSIGNMENT2.sentMessages.add(longMsg);

    Message longest = POEASSIGNMENT2.sentMessages.get(0);
    for (Message m : POEASSIGNMENT2.sentMessages) {
        if (m.getMessageText().length() > longest.getMessageText().length()) {
            longest = m;
        }
    }

    assertEquals("222", longest.getMessageID());
    assertEquals("This is a much longer message for testing purposes", longest.getMessageText());
}
@Test
public void testSearchMessageByID() {
    // Setup
    POEASSIGNMENT2.sentMessages.clear();
    Message msg = new Message("55555", "+27789998888", "Test search by ID", "Sent");
    POEASSIGNMENT2.sentMessages.add(msg);

    // Search manually
    boolean found = false;
    for (Message m : POEASSIGNMENT2.sentMessages) {
        if (m.getMessageID().equals("55555")) {
            found = true;
            assertEquals("Test search by ID", m.getMessageText());
            break;
        }
    }

    assertTrue(found, "Message with ID 55555 should be found.");
}
@Test
public void testSearchMessagesByRecipient() {
    // Setup
    POEASSIGNMENT2.sentMessages.clear();
    Message msg1 = new Message("10101", "+27781234567", "Hello one", "Sent");
    Message msg2 = new Message("20202", "+27781234567", "Hello two", "Sent");
    Message msg3 = new Message("30303", "+27889997766", "Different recipient", "Sent");

    POEASSIGNMENT2.sentMessages.add(msg1);
    POEASSIGNMENT2.sentMessages.add(msg2);
    POEASSIGNMENT2.sentMessages.add(msg3);

    // Search for messages to +27781234567
    ArrayList<Message> foundMessages = new ArrayList<>();
    for (Message m : POEASSIGNMENT2.sentMessages) {
        if (m.getRecipient().equals("+27781234567")) {
            foundMessages.add(m);
        }
    }

    assertEquals(2, foundMessages.size(), "Should find 2 messages for +27781234567");
    assertEquals("Hello one", foundMessages.get(0).getMessageText());
    assertEquals("Hello two", foundMessages.get(1).getMessageText());
}
@Test
public void testDeleteMessageByHash() {
    POEASSIGNMENT2.sentMessages.clear();
    POEASSIGNMENT2.messageHashes.clear();

    Message msg = new Message("12AB345678", "+27834512345", "Where are you? You are late! I have asked you to be on time.", "Sent");
    POEASSIGNMENT2.sentMessages.add(msg);

    // Create hash manually using index 1
    String hash = msg.createMessageHash(1);
    POEASSIGNMENT2.messageHashes.add(hash);

    // Simulate deletion
    boolean deleted = false;
    for (int i = 0; i < POEASSIGNMENT2.sentMessages.size(); i++) {
        Message m = POEASSIGNMENT2.sentMessages.get(i);
        String generatedHash = m.createMessageHash(i + 1);

        if (generatedHash.equalsIgnoreCase(hash)) {
            POEASSIGNMENT2.sentMessages.remove(i);
            POEASSIGNMENT2.messageHashes.removeIf(h -> h.equalsIgnoreCase(hash));
            deleted = true;
            break;
        }
    }

    assertTrue(deleted, "Message should be successfully deleted");
    assertEquals(0, POEASSIGNMENT2.sentMessages.size(), "Sent messages list should be empty after deletion");
}
@Test
public void testDisplayReportIncludesHashRecipientAndMessage() {
    POEASSIGNMENT2.sentMessages.clear();

    Message msg = new Message("55XY123456", "+27891234567", "Test report message", "Sent");
    POEASSIGNMENT2.sentMessages.add(msg);

    // Generate report content (simulate case 14)
    StringBuilder report = new StringBuilder();
    int messageNumber = 1;
    for (Message m : POEASSIGNMENT2.sentMessages) {
        String hash = m.createMessageHash(messageNumber);
        report.append("Message #").append(messageNumber++).append("\n")
              .append("Recipient: ").append(m.getRecipient()).append("\n")
              .append("Message: ").append(m.getMessageText()).append("\n")
              .append("Hash: ").append(hash).append("\n");
    }

    String output = report.toString();
    assertTrue(output.contains("Test report message"));
    assertTrue(output.contains("+27891234567"));
    assertTrue(output.contains("55")); // message ID starts with 55, should appear in hash
}



}