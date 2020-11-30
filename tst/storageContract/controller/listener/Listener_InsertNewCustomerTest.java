package storageContract.controller.listener;

import org.junit.jupiter.api.Test;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.controller.ConsoleReader;
import storageContract.controller.ControlModel;
import storageContract.controller.Mode;
import storageContract.controller.events.CustomerInsertEvent;
import storageContract.logic.BusinessLogicImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Listener_InsertNewCustomerTest {

    @Test
    void onInputEvent() {
        String dummy = "James";

        ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));

        Customer dummyCustomer = new CustomerImpl(dummy);

        ConsoleReader consoleMock = mock(ConsoleReader.class);
        when(consoleMock.getMode()).thenReturn(Mode.create);

        CustomerInsertEvent insertEvent = new CustomerInsertEvent(consoleMock, dummy);

        Listener_InsertNewCustomer listener_insertNewCustomer = new Listener_InsertNewCustomer(controlModel);
        listener_insertNewCustomer.onInputEvent(insertEvent);

        assertEquals(controlModel.getModel().getCustomer(dummy).getName(), dummyCustomer.getName() );
    }
}