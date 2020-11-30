package storageContract.controller.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerDisplayEventTest {

    @Test
    void constructorTest(){
        assertNotNull(new CustomerDisplayEvent(this));
    }
}