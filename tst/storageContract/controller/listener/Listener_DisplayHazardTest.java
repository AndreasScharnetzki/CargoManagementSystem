package storageContract.controller.listener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.controller.ControlModel;
import storageContract.controller.events.CustomerDisplayEvent;
import storageContract.controller.events.HazardDisplayEvent;
import storageContract.logic.BusinessLogicImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class Listener_DisplayHazardTest {

    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));
    private Listener_DisplayHazard listener_displayHazard = new Listener_DisplayHazard(controlModel);

    private HazardDisplayEvent hazardDisplayEvent_include = new HazardDisplayEvent(this, "i");
    private HazardDisplayEvent hazardDisplayEvent_exclude = new HazardDisplayEvent(this, "e");

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    //src: https://www.baeldung.com/java-testing-system-out-println
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void onInputEvent() {
        HashSet hazards = new HashSet<Hazard>();
        hazards.add(Hazard.flammable);
        controlModel.getModel().addToCustomerDatabase("Jim");
        controlModel.getModel().addToStorehouse(new LiquidBulkCargoImpl(new CustomerImpl("Jim"), BigDecimal.ONE, Duration.ofSeconds(1), hazards, true));

        listener_displayHazard.onInputEvent(hazardDisplayEvent_include);
        assertTrue(outputStreamCaptor.toString().trim().contains("The storehouse currently holds cargo labeled as:"));
        assertTrue(outputStreamCaptor.toString().trim().contains("[flammable]"));

        listener_displayHazard.onInputEvent(hazardDisplayEvent_exclude);
        assertTrue(outputStreamCaptor.toString().trim().contains("The storehouse currently doesn't holds cargo labeled as:"));
        assertTrue(outputStreamCaptor.toString().trim().contains("[explosive, toxic, radioactive]"));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}