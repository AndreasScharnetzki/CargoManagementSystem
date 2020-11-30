package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObserverAttachEventTest {

    private String expected = "CapacityObserver";
    private ObserverAttachEvent observerAttachEvent = new ObserverAttachEvent(this, expected);

    @Test
    void getObserverName() {
        assertEquals(expected, observerAttachEvent.getObserverName());
    }
}