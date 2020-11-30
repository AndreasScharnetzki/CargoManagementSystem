package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoDisplayEventTest {

    private String expected= "y";
    private CargoDisplayEvent cargoDisplayEvent = new CargoDisplayEvent(this, expected);

    @Test
    void getFilter() {
        assertEquals(expected, cargoDisplayEvent.getFilter());
    }
}