package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HazardDisplayEventTest {

    private String expected = "e";
    private HazardDisplayEvent hazardDisplayEvent = new HazardDisplayEvent(this, expected);

    @Test
    void getFilter() {
        assertEquals(expected, hazardDisplayEvent.getFilter());
    }
}