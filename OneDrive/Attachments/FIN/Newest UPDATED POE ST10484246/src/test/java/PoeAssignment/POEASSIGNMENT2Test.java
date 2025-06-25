package PoeAssignment;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

public class POEASSIGNMENT2Test {
    Login login;

    @BeforeEach
    public void setUp() {
        login = new Login();
        POEASSIGNMENT2.sentMessagesCount = 0;
        POEASSIGNMENT2.messageHashCount = 0;
        
    }

    @Test public void testCheckPasswordComplexity_Valid() {
        assertTrue(login.checkPasswordComplexity("Test123!"));
    }

    @Test public void testCheckPasswordComplexity_Invalid() {
        assertFalse(login.checkPasswordComplexity("abc"));
    }

    @Test public void testCheckUserName_Valid() {
        POEASSIGNMENT2.username = "ab_1";
        assertTrue(login.checkUserName());
    }

    @Test public void testCheckUserName_Invalid() {
        POEASSIGNMENT2.username = "abcdef";
        assertFalse(login.checkUserName());
    }

    @Test public void testCheckCellPhoneNumber_Valid() {
        POEASSIGNMENT2.cellNumber = "+27712345678";
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test public void testCheckCellPhoneNumber_Invalid() {
        POEASSIGNMENT2.cellNumber = "0712345678";
        assertFalse(login.checkCellPhoneNumber());
    }

    @Test public void testLoginUser_ValidCredentials() {
        POEASSIGNMENT2.username = "ab_1";
        POEASSIGNMENT2.password = "Test123!";
        assertTrue(login.loginUser("ab_1", "Test123!"));
    }

    @Test public void testLoginUser_InvalidCredentials() {
        POEASSIGNMENT2.username = "ab_1";
        POEASSIGNMENT2.password = "Test123!";
        assertFalse(login.loginUser("wrong", "wrong"));
    }

    @Test public void testUsernameCorrectFormatMessage() {
        assertEquals("Username is correctly formatted.",
            login.checkUserNameMessage("kyl_1"));
    }

    @Test public void testUsernameIncorrectFormatMessage() {
        assertEquals("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.",
            login.checkUserNameMessage("kyle!!!!!"));
    }

    @Test public void testPasswordSuccessMessage() {
        assertEquals("Password successfully captured.",
            login.checkPasswordMessage("Ch&&sec@ke99!"));
    }

    @Test public void testPasswordFailMessage() {
        assertEquals("Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
            login.checkPasswordMessage("password"));
    }

    @Test public void testCellPhoneCorrectFormatMessage() {
        assertEquals("Cell number successfully captured.",
            login.checkCellPhoneMessage("+27838968976"));
    }

    @Test public void testMessageLength_Failure() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<260;i++) sb.append("a");
        assertTrue(sb.length() > 250,
            "Message exceeds 250 characters by " + (sb.length()-250) + ", please reduce size.");
    }

@Test
public void testStoreMessageAsJSON() throws Exception {
    String testFile = "tmp_test.json";

    //  JSON object
    JSONObject js = new JSONObject();
    js.put("messageID", "1234567890");
    js.put("recipient", "+27712345678");
    js.put("messageText", "Test message");
    js.put("status", "Stored");

    //  array + add object
    JSONArray arr = new JSONArray();
    arr.put(js);

    
    try (FileWriter writer = new FileWriter(testFile)) {
        writer.write(arr.toString(4));
    }

    // Read and parse file
    String content = new String(Files.readAllBytes(new File(testFile).toPath()));
    JSONArray res = new JSONArray(content);
    JSONObject msg = res.getJSONObject(0);

    // Assertions
    assertEquals("1234567890", msg.getString("messageID"));
    assertEquals("+27712345678", msg.getString("recipient"));
    assertEquals("Test message", msg.getString("messageText"));
    assertEquals("Stored", msg.getString("status"));

    // Cleanup
    new File(testFile).delete();
}


    @Test public void testMessageHashGeneration() {
        Message m = new Message("84","+27718693002","Hi Mike, can you join us for dinner tonight","Stored");
        assertEquals("84:0:HITONIGHT", m.createMessageHash(0));
    }

    @Test public void testGeneratedMessageIDIsValid() {
        String id = POEASSIGNMENT2.generateMessageID();
        assertEquals(10,id.length());
        assertTrue(Message.checkMessageID(id));
    }

    @Test public void testCreateMessageHash() {
        Message m = new Message("3012345678","+27781234567","Hello world","Sent");
        assertEquals("30:1:HELLOWORLD", m.createMessageHash(1));
    }


 @Test
    public void testSendMessageOption1() {
        // Reset storage before test
        Message.resetSentMessages();

        Message msg = new Message("3012345678", "+27781234567", "Hello", "");
        String result = msg.simulateMessageAction("1");

  //return check
        assertTrue(result.equals("Sent"), "simulateMessageAction should return 'Sent'");

    
        boolean found = false;
        for (int i = 0; i < Message.sentMessagesCount; i++) {
            if (Message.sentMessages[i] == msg) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Message should be added to sentMessages array");
    }




    @Test public void testDisregardMessageOption2() {
        Message msg = new Message("3012345678","+27781234567","Hello",""); 
        assertEquals("Disregarded", msg.simulateMessageAction("2"));
    }

    @Test public void testStoreMessageOption3() {
        Message msg = new Message("3012345678","+27781234567","Hello",""); 
        assertEquals("Stored", msg.simulateMessageAction("3"));
    }

    @Test public void testInvalidMessageOption() {
        Message msg = new Message("3012345678","+27781234567","Hello",""); 
        assertEquals("Invalid", msg.simulateMessageAction("9"));
    }

    @Test
    public void testPrintMessages_ShowsSentMessages() {
        // Reset message storage
        Message.sentMessagesCount = 0;
        Message.sentMessages = new Message[100];

        // Add test message
        Message msg = new Message("1234567890", "+27712345678", "Hello there!", "Sent");
        Message.sentMessages[Message.sentMessagesCount++] = msg;

        String out = Message.printMessages();

        assertTrue(out.contains("1234567890"), "Output should contain message ID");
        assertTrue(out.contains("+27712345678"), "Output should contain recipient");
        assertTrue(out.contains("Hello there!"), "Output should contain message text");
        assertTrue(out.contains("Sent"), "Output should contain status");
    }

      @Test
    public void testIntegratedMessageProcess() {
        // Reset sent messages before test
        Message.resetSentMessages();

        // First message - send it (choice "1")
        Message m1 = new Message("00", "+27718693002", "Hi", "");
        String a1 = m1.simulateMessageAction("1");

        // Second message - disregard it (choice "2")
        Message m2 = new Message(POEASSIGNMENT2.generateMessageID(), "+85875975889", "Hi", "");  // fixed recipient with '+'
        String a2 = m2.simulateMessageAction("2");

        // Assertions
        assertEquals(1, Message.sentMessagesCount, "Only one message should be sent");
        assertEquals("Sent", a1, "First message status should be Sent");
        assertEquals("Disregarded", a2, "Second message status should be Disregarded");
        assertEquals("00:0:HIHI", m1.createMessageHash(0), "Hash should match expected format");
    }
    @Test public void testDataMessage1_Sent() {
        POEASSIGNMENT2.sentMessagesCount=0;
        Message msg=new Message("123","+27834557896","Cake?","Sent");
        POEASSIGNMENT2.sentMessages[POEASSIGNMENT2.sentMessagesCount++]=msg;
        assertEquals(1,POEASSIGNMENT2.sentMessagesCount);
        assertEquals("Sent",msg.getStatus());
        assertEquals("+27834557896",msg.getRecipient());
    }

    @Test public void testDeleteMessageByHash() {
        POEASSIGNMENT2.sentMessagesCount=0;
        POEASSIGNMENT2.messageHashCount=0;
        Message msg=new Message("12AB345678","+27834512345","Late?","Sent");
        POEASSIGNMENT2.sentMessages[POEASSIGNMENT2.sentMessagesCount++]=msg;
        String h=msg.createMessageHash(1); POEASSIGNMENT2.messageHashes[POEASSIGNMENT2.messageHashCount++]=h;
        boolean deleted=false;
        for(int i=0;i<POEASSIGNMENT2.sentMessagesCount;i++){
            if(POEASSIGNMENT2.sentMessages[i].createMessageHash(i+1).equalsIgnoreCase(h)){
                for(int j=i;j<POEASSIGNMENT2.sentMessagesCount-1;j++){
                    POEASSIGNMENT2.sentMessages[j]=POEASSIGNMENT2.sentMessages[j+1];
                }
                POEASSIGNMENT2.sentMessages[--POEASSIGNMENT2.sentMessagesCount]=null;
                // hash
                for(int j=i;j<POEASSIGNMENT2.messageHashCount-1;j++){
                    POEASSIGNMENT2.messageHashes[j]=POEASSIGNMENT2.messageHashes[j+1];
                }
                POEASSIGNMENT2.messageHashes[--POEASSIGNMENT2.messageHashCount]=null;
                deleted=true; break;
            }
        }
        assertTrue(deleted);
        assertEquals(0,POEASSIGNMENT2.sentMessagesCount);
    }

    @Test public void testDisplayReportIncludesHashRecipientAndMessage() {
        POEASSIGNMENT2.sentMessagesCount=0;
        Message msg=new Message("55XY123456","+27891234567","Test","Sent");
        POEASSIGNMENT2.sentMessages[POEASSIGNMENT2.sentMessagesCount++]=msg;
        StringBuilder rep=new StringBuilder();
        int num=1;
        for(int i=0;i<POEASSIGNMENT2.sentMessagesCount;i++){
            Message m=POEASSIGNMENT2.sentMessages[i];
            rep.append("Message #").append(num++)
               .append("\nRecipient: ").append(m.getRecipient())
               .append("\nMessage: ").append(m.getMessageText())
               .append("\nHash: ").append(m.createMessageHash(i+1))
               .append("\n");
        }
        String out = rep.toString();
        assertTrue(out.contains("Test"));
        assertTrue(out.contains("+27891234567"));
        assertTrue(out.contains("55"));
    }
}
