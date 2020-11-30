package storageContract.controller.listener;

import org.junit.jupiter.api.Test;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.UnitisedCargoImpl;
import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoPersistenceEvent;
import storageContract.logic.BusinessLogicImpl;

import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class Listener_HandleCargoPersistenceTest {
    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(21));
    private Listener_HandleCargoPersistence listener_handleCargoPersistence = new Listener_HandleCargoPersistence(controlModel);

    private CargoPersistenceEvent cargoPersistenceEvent_save = new CargoPersistenceEvent(this, "save", 0);
    private CargoPersistenceEvent cargoPersistenceEvent_load = new CargoPersistenceEvent(this, "load", 0);

    @Test
    void onInputEvent() {
        Customer c = new CustomerImpl("a");
        UnitisedCargoImpl sampleCargo = new UnitisedCargoImpl(c, BigDecimal.ONE, Duration.ofSeconds(1), new HashSet<Hazard>(), true);
        controlModel.getModel().addToCustomerDatabase(c.getName());
        controlModel.getModel().addToStorehouse(sampleCargo);

        listener_handleCargoPersistence.onInputEvent(cargoPersistenceEvent_save);
        assertEquals(sampleCargo,controlModel.getModel().getCargoByPosition(0));

        controlModel.getModel().deleteStockAtPosition(0);
        assertNull(controlModel.getModel().getCargoByPosition(0));

        listener_handleCargoPersistence.onInputEvent(cargoPersistenceEvent_load);
        //assert equal checks for identity by referring to location on disk, hence I only checked for equality of object values
        assertEquals(sampleCargo.getOwner().getName(), controlModel.getModel().getCargoByPosition(0).getOwner().getName());
        assertEquals(sampleCargo.getHazards(), controlModel.getModel().getCargoByPosition(0).getHazards());
        assertEquals(sampleCargo.getDurationOfStorage(), controlModel.getModel().getCargoByPosition(0).getDurationOfStorage());
        assertEquals(sampleCargo.getValue(), controlModel.getModel().getCargoByPosition(0).getValue());

        //cleaning up :)
        new File("serializedSingleCargo.txt").delete();
    }
}