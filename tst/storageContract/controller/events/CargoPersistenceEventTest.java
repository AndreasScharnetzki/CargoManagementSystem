package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoPersistenceEventTest {

    private String expectedCommand = "y";
    private int expectedPosition = 12;
    private CargoPersistenceEvent cargoPersistenceEvent = new CargoPersistenceEvent(this,expectedCommand,expectedPosition);

    @Test
    void getCommand() {
        assertEquals(expectedCommand, cargoPersistenceEvent.getCommand());
    }

    @Test
    void getPosition() {
        assertEquals(expectedPosition, cargoPersistenceEvent.getPosition());
    }
}