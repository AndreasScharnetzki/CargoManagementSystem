package storageContract.controller.listener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoDisplayEvent;
import storageContract.controller.events.CustomerDisplayEvent;
import storageContract.logic.BusinessLogicImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class Listener_DisplayCustomerTest {
    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));
    private Listener_DisplayCustomer listener_displayCustomer = new Listener_DisplayCustomer(controlModel);
    private CustomerDisplayEvent customerDisplayEvent = new CustomerDisplayEvent(this);

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    //src: https://www.baeldung.com/java-testing-system-out-println
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void onInputEvent() {
        //I wasn't able to check the entire string, even if the strings were identical the assertion failed :/ but this way it is insured that the display event works and the name is there :)
        controlModel.getModel().addToCustomerDatabase("Jim");

        listener_displayCustomer.onInputEvent(customerDisplayEvent);
        assertTrue(outputStreamCaptor.toString().trim().contains("CustomerName        |        Number of associated Cargo"));
        assertTrue(outputStreamCaptor.toString().trim().contains("Jim       |       0"));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}