package storageContract.controller.observer;

import org.junit.jupiter.api.Test;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.MixedCargoLiquidBulkAndUnitisedImpl;
import storageContract.cargo.interfaces.Cargo;
import storageContract.logic.BusinessLogicImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//FIXME: ! Contents have difference only in line separators

class HazardObserverTest {

    //source: https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Test
    void goodTest_update_addingNewHazard() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        int MAX_STORAGE_CAPACITY = 3;

        BusinessLogicImpl logic = new BusinessLogicImpl(MAX_STORAGE_CAPACITY);

        Customer customerMock = mock(CustomerImpl.class);
        when(customerMock.getName()).thenReturn("B");

        Cargo cargoMock = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);
        when(cargoMock.getOwner()).thenReturn(customerMock);
        HashSet<Hazard> hazards = new HashSet<>();
        hazards.add(Hazard.toxic);

        when(cargoMock.getHazards()).thenReturn(hazards);

        HazardObserver hazardObserver = new HazardObserver(logic);

        logic.attach(hazardObserver);

        logic.addToCustomerDatabase(customerMock.getName());
        logic.addToStorehouse(cargoMock);

        String expected = "\u001B[0;32m✔\u001B[0m: Successfully attached new HazardObserver\n" +
                "\u001B[33m⚠\u001B[0m: A change in the hazards stored has been noticed, " +
                "the Storehouse contains the following hazards: \u001B[35m[toxic]\u001B[0m" + System.lineSeparator();

        String actual = outContent.toString();

        System.setOut(originalOut);
        System.setErr(originalErr);

        assertEquals(expected, actual);
    }

    @Test
    void goodTest_update_removingHazard() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        int MAX_STORAGE_CAPACITY = 3;

        BusinessLogicImpl logic = new BusinessLogicImpl(MAX_STORAGE_CAPACITY);

        Customer customerMock = mock(CustomerImpl.class);
        when(customerMock.getName()).thenReturn("B");

        Cargo cargoMock = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);
        when(cargoMock.getOwner()).thenReturn(customerMock);
        HashSet<Hazard> hazards = new HashSet<>();
        hazards.add(Hazard.toxic);

        when(cargoMock.getHazards()).thenReturn(hazards);

        HazardObserver hazardObserver = new HazardObserver(logic);

        logic.attach(hazardObserver);

        logic.addToCustomerDatabase(customerMock.getName());
        logic.addToStorehouse(cargoMock);
        logic.deleteStockAtPosition(0);

        String expected = "\u001B[0;32m✔\u001B[0m: Successfully attached new HazardObserver\n" +
                "\u001B[33m⚠\u001B[0m: A change in the hazards stored has been noticed, " +
                "the Storehouse contains the following hazards: \u001B[35m[toxic]\u001B[0m" + System.lineSeparator();

        String actual = outContent.toString();

        System.setOut(originalOut);
        System.setErr(originalErr);

        assertEquals(expected, actual);
    }
}