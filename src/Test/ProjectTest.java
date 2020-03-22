package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import serverclient.PasswordEncryption;

public class ProjectTest {

    /**
     * Tests for the password Encryption
     */
    @Test
    public void test1() {
        String password = "munich";
        String encryptPass = PasswordEncryption.encrypt(password);
        String decryptPass = PasswordEncryption.decrypt(encryptPass);
        assertEquals(password, decryptPass);
    }

    /**
     * Test for regular expression
     */
    @Test
    public void test2() {
        String testString1 = "hengzhaibham@gmail.com";
        assertTrue(testString1.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$"));
        String testString2 = "hengzhaibham.com";
        assertFalse(testString2.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$"));
    }
}
