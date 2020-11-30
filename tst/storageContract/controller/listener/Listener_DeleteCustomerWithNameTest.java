package storageContract.controller.listener;

import org.junit.jupiter.api.Test;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoDeletionEvent;
import storageContract.controller.events.CustomerDeletionEvent;
import storageContract.logic.BusinessLogicImpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class Listener_DeleteCustomerWithNameTest {

    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));
    private Listener_DeleteCustomerWithName listener_deleteCustomerWithNameTest = new Listener_DeleteCustomerWithName(controlModel);
    private CustomerDeletionEvent cargoDeletionEvent = new CustomerDeletionEvent(this, "Jim");

    @Test
    void onInputEvent() {
        assertEquals(0,controlModel.getModel().getCustomerDatabase().size());
        controlModel.getModel().addToCustomerDatabase("Jim");
        assertEquals(1,controlModel.getModel().getCustomerDatabase().size());

        listener_deleteCustomerWithNameTest.onInputEvent(cargoDeletionEvent);
        assertEquals(0,controlModel.getModel().getCustomerDatabase().size());
    }
}