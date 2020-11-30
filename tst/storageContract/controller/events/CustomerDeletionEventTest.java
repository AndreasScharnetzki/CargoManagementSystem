package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDeletionEventTest {
    String expected = "A";
    CustomerDeletionEvent customerDeletionEvent = new CustomerDeletionEvent(this, expected);

    @Test
    void getNameToBeDeleted() {
        assertEquals(expected, customerDeletionEvent.getNameToBeDeleted());
    }
}