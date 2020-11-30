package storageContract.controller.listener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoDisplayEvent;
import storageContract.logic.BusinessLogicImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class Listener_DisplayCargoTest {
    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));
    private LiquidBulkCargoImpl sampleCargo = new LiquidBulkCargoImpl(new CustomerImpl("Jim"), BigDecimal.ONE, Duration.ofSeconds(1), new HashSet<Hazard>(), true);

    private Listener_DisplayCargo listener_displayCargo = new Listener_DisplayCargo(controlModel);

    private CargoDisplayEvent noFilterEvent = new CargoDisplayEvent(this, "");
    private CargoDisplayEvent liquid = new CargoDisplayEvent(this, "LiquidBulkCargo");
    private CargoDisplayEvent unitised = new CargoDisplayEvent(this, "UnitisedCargo");
    private CargoDisplayEvent mixed = new CargoDisplayEvent(this, "MixedCargoLiquidBulkAndUnitised");

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    //src: https://www.baeldung.com/java-testing-system-out-println
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void onInputEvent() {
        //Testing the entire string was not possible due to it will contain information regarding time - but i was able to assure that the filters get recognized correctly
        controlModel.getModel().addToCustomerDatabase("Jim");
        controlModel.getModel().addToStorehouse(sampleCargo);

        listener_displayCargo.onInputEvent(noFilterEvent);
        assertTrue(outputStreamCaptor.toString().trim().contains("(No Filter identified - if you intended to do so, please refer to either [liquidbulk, mixed, unitised])"));

        listener_displayCargo.onInputEvent(liquid);
        assertTrue(outputStreamCaptor.toString().trim().contains("Displaying Cargo filtered by type [ liquidbulkcargo ]"));

        listener_displayCargo.onInputEvent(unitised);
        assertTrue(outputStreamCaptor.toString().trim().contains("Displaying Cargo filtered by type [ unitisedcargo ]"));

        listener_displayCargo.onInputEvent(mixed);
        assertTrue(outputStreamCaptor.toString().trim().contains("Displaying Cargo filtered by type [ mixedcargoliquidbulkandunitised ]"));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}