package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoUpdateEventTest {

    int expected = 9;
    private CargoUpdateEvent cargoUpdateEvent = new CargoUpdateEvent(this, expected);

    @Test
    void getPositionToBeUpdated() {
        assertEquals(expected, cargoUpdateEvent.getPositionToBeUpdated());
    }
}