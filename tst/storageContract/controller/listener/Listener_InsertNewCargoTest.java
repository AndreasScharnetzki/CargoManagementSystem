package storageContract.controller.listener;

import org.junit.jupiter.api.Test;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.cargo.UnitisedCargoImpl;
import storageContract.cargo.interfaces.Cargo;
import storageContract.controller.ConsoleReader;
import storageContract.controller.ControlModel;
import storageContract.controller.Mode;
import storageContract.controller.events.CargoInsertEvent;
import storageContract.controller.events.CustomerInsertEvent;
import storageContract.logic.BusinessLogicImpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class Listener_InsertNewCargoTest {
    @Test
    void onInputEvent() {
        String dummy = "James";
        ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));
        Customer dummyCustomer = new CustomerImpl(dummy);

        controlModel.getModel().addToCustomerDatabase(dummy);

        Cargo sampleCargo = new UnitisedCargoImpl(dummyCustomer, BigDecimal.ONE, Duration.ofSeconds(1), new HashSet<Hazard>(), true);

        CargoInsertEvent insertEvent = new CargoInsertEvent(this, sampleCargo);

        Listener_InsertNewCargo listener_insertNewCargo = new Listener_InsertNewCargo(controlModel);
        listener_insertNewCargo.onInputEvent(insertEvent);

        assertEquals(dummyCustomer.getName(), controlModel.getModel().getCargoByPosition(0).getOwner().getName());
    }
}

