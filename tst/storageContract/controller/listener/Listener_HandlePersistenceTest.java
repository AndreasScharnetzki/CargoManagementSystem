package storageContract.controller.listener;

import org.junit.jupiter.api.Test;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.cargo.UnitisedCargoImpl;
import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoPersistenceEvent;
import storageContract.controller.events.PersistenceTriggerEvent;
import storageContract.logic.BusinessLogicImpl;

import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class Listener_HandlePersistenceTest {
    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(2));
    private Listener_HandlePersistence listener_handlePersistence = new Listener_HandlePersistence(controlModel);

    private PersistenceTriggerEvent persistenceTriggerEvent_saveJOS = new PersistenceTriggerEvent(this, "saveJOS", "saveJOS.txt");
    private PersistenceTriggerEvent persistenceTriggerEvent_loadJOS = new PersistenceTriggerEvent(this, "loadJOS", "saveJOS.txt");

    @Test
    void onInputEvent() {
        //focussed on JOS
        Customer c = new CustomerImpl("a");
        Customer b = new CustomerImpl("b");
        UnitisedCargoImpl sampleCargo = new UnitisedCargoImpl(c, BigDecimal.ONE, Duration.ofSeconds(1), new HashSet<Hazard>(), true);
        LiquidBulkCargoImpl different_sampleCargo = new LiquidBulkCargoImpl(c, BigDecimal.ONE, Duration.ofSeconds(1), new HashSet<Hazard>(), false);

        controlModel.getModel().addToCustomerDatabase(c.getName());
        controlModel.getModel().addToCustomerDatabase(b.getName());
        controlModel.getModel().addToStorehouse(sampleCargo);
        controlModel.getModel().addToStorehouse(different_sampleCargo);

        assertEquals(sampleCargo,controlModel.getModel().getCargoByPosition(0));
        assertEquals(different_sampleCargo,controlModel.getModel().getCargoByPosition(1));
        listener_handlePersistence.onInputEvent(persistenceTriggerEvent_saveJOS);

        controlModel.getModel().deleteStockAtPosition(0);
        controlModel.getModel().deleteStockAtPosition(1);
        assertNull(controlModel.getModel().getCargoByPosition(0));
        assertNull(controlModel.getModel().getCargoByPosition(1));

        listener_handlePersistence.onInputEvent(persistenceTriggerEvent_loadJOS);
        //assert equal checks for identity by referring to location on disk, hence I only checked for equality of object values
        assertEquals(sampleCargo.getOwner().getName(), controlModel.getModel().getCargoByPosition(0).getOwner().getName());
        assertEquals(sampleCargo.getHazards(), controlModel.getModel().getCargoByPosition(0).getHazards());
        assertEquals(sampleCargo.getDurationOfStorage(), controlModel.getModel().getCargoByPosition(0).getDurationOfStorage());
        assertEquals(sampleCargo.getValue(), controlModel.getModel().getCargoByPosition(0).getValue());

        assertEquals(different_sampleCargo.getOwner().getName(), controlModel.getModel().getCargoByPosition(1).getOwner().getName());
        assertEquals(different_sampleCargo.getHazards(), controlModel.getModel().getCargoByPosition(1).getHazards());
        assertEquals(different_sampleCargo.getDurationOfStorage(), controlModel.getModel().getCargoByPosition(1).getDurationOfStorage());
        assertEquals(different_sampleCargo.getValue(), controlModel.getModel().getCargoByPosition(1).getValue());

        //cleaning up :)
        new File("saveJOS.txt").delete();
    }
}