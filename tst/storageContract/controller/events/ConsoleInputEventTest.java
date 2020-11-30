package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleInputEventTest {

    String expected = "test";
    private ConsoleInputEvent consoleInputEvent = new ConsoleInputEvent(this, expected);

    @Test
    void getText() {
        assertEquals(expected, consoleInputEvent.getText());
    }
}