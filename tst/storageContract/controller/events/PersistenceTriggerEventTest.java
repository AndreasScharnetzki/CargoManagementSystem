package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceTriggerEventTest {


    private String fileName = "test.txt";
    private String expectedCommand = "save";
    private PersistenceTriggerEvent persistenceTriggerEvent = new PersistenceTriggerEvent(this, expectedCommand, fileName);

    @Test
    void getCommand() {
        assertEquals(expectedCommand, persistenceTriggerEvent.getCommand());
    }

    @Test
    void getFilename() {
        assertEquals(fileName, persistenceTriggerEvent.getFilename());
    }
}