package storageContract.controller.listener;

import org.junit.jupiter.api.Test;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoDeletionEvent;
import storageContract.controller.events.ObserverAttachEvent;
import storageContract.logic.BusinessLogicImpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class Listener_DeleteCargoAtPositionTest {
    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));
    private LiquidBulkCargoImpl sampleCargo = new LiquidBulkCargoImpl(new CustomerImpl("Jim"), BigDecimal.ONE, Duration.ofSeconds(1), new HashSet<Hazard>(), true);

    private Listener_DeleteCargoAtPosition listener_deleteCargoAtPosition = new Listener_DeleteCargoAtPosition(controlModel);

    private CargoDeletionEvent cargoDeletionEvent = new CargoDeletionEvent(this, 0);

    @Test
    void onInputEvent() {
        controlModel.getModel().addToCustomerDatabase("Jim");
        controlModel.getModel().addToStorehouse(sampleCargo);
        assertEquals(sampleCargo,controlModel.getModel().getCargoByPosition(0));

        listener_deleteCargoAtPosition.onInputEvent(cargoDeletionEvent);
        assertNull(controlModel.getModel().getCargoByPosition(0));
    }
}