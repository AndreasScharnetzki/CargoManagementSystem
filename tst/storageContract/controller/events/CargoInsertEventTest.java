package storageContract.controller.events;

import org.junit.jupiter.api.Test;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.cargo.UnitisedCargoImpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CargoInsertEventTest {

    private UnitisedCargoImpl unitisedCargo = new UnitisedCargoImpl(new CustomerImpl("A"), BigDecimal.ONE, Duration.ofSeconds(1), new HashSet<Hazard>(), true);
    private CargoInsertEvent cargoInsertEvent = new CargoInsertEvent(this, unitisedCargo);

    @Test
    void getCargoToBeAdded() {
        assertEquals(unitisedCargo, cargoInsertEvent.getCargoToBeAdded());
    }
}