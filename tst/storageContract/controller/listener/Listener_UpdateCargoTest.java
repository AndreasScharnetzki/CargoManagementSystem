package storageContract.controller.listener;

import org.junit.jupiter.api.Test;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoDisplayEvent;
import storageContract.controller.events.CargoUpdateEvent;
import storageContract.logic.BusinessLogicImpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class Listener_UpdateCargoTest {
    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));
    private LiquidBulkCargoImpl sampleCargo = new LiquidBulkCargoImpl(new CustomerImpl("Jim"), BigDecimal.ONE, Duration.ofSeconds(1), new HashSet<Hazard>(), true);

    private Listener_UpdateCargo listener_updateCargo = new Listener_UpdateCargo(controlModel);

    private CargoUpdateEvent cargoUpdateEvent = new CargoUpdateEvent(this,0 );

    @Test
    void onInputEvent() {
        //kinda hard to do this precisely..this is what i came up with
        controlModel.getModel().addToCustomerDatabase("Jim");
        controlModel.getModel().addToStorehouse(sampleCargo);
        long momentPastInsertion = new java.util.Date().getTime();
        assertTrue(momentPastInsertion > controlModel.getModel().getCargoByPosition(0).getLastInspectionDate().getTime());
        listener_updateCargo.onInputEvent(cargoUpdateEvent);
        assertFalse(momentPastInsertion > controlModel.getModel().getCargoByPosition(0).getLastInspectionDate().getTime());
    }
}