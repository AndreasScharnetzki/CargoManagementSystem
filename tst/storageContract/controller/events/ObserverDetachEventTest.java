package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObserverDetachEventTest {

    private String expected = "CapacityObserver";
    private ObserverDetachEvent observerDetachEvent = new ObserverDetachEvent(this, expected);

    @Test
    void getObserverName() {
        assertEquals(expected, observerDetachEvent.getObserverName());
    }
}