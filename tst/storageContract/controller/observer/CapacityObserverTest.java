package storageContract.controller.observer;

import org.junit.jupiter.api.Test;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.MixedCargoLiquidBulkAndUnitisedImpl;
import storageContract.cargo.interfaces.Cargo;
import storageContract.logic.BusinessLogicImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//FIXME: ! Contents have difference only in line separators

//source: https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
class CapacityObserverTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Test
    void update() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        int MAX_STORAGE_CAPACITY = 2;

        BusinessLogicImpl logic = new BusinessLogicImpl(MAX_STORAGE_CAPACITY);

        Customer customerMock = mock(CustomerImpl.class);
        when(customerMock.getName()).thenReturn("B");

        Cargo cargoMock = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);
        when(cargoMock.getOwner()).thenReturn(customerMock);

        CapacityObserver capacityObserver = new CapacityObserver(logic);

        logic.attach(capacityObserver);
        logic.addToCustomerDatabase(customerMock.getName());
        logic.addToStorehouse(cargoMock);

        String expected = "\u001B[0;32m✔\u001B[0m: Successfully attached new CapacityObserver\n" +
                "\u001B[33m⚠\u001B[0m: Storehouse almost reached maximum storage capacity.\n";

        String actual = outContent.toString();

        System.setOut(originalOut);
        System.setErr(originalErr);

        assertEquals(expected, actual);
    }
}