package storageContract.controller.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerInsertEventTest {

    private String expected ="A";
    private CustomerInsertEvent customerInsertEvent= new CustomerInsertEvent(this, expected);

    @Test
    void getCustomerName() {
        assertEquals(expected, customerInsertEvent.getCustomerName());
    }
}