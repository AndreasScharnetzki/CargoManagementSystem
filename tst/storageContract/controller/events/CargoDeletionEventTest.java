package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoDeletionEventTest {

    private int expected = 12;
    private CargoDeletionEvent cargoDeletionEvent = new CargoDeletionEvent(this, expected);

    @Test
    void getPositionToBeDeleted() {
        assertEquals(expected, cargoDeletionEvent.getPositionToBeDeleted());
    }
}