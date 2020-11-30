package storageContract.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.*;
import storageContract.cargo.interfaces.Cargo;
import storageContract.cargo.interfaces.UnitisedCargo;
import storageContract.controller.observer.CapacityObserver;
import storageContract.controller.observer.Observer;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BusinessLogicImplTest {

    private String SAMPLE_CUSTOMERS_NAME = "A";

    public Customer sampleCustomer() {
        Customer sampleCustomer = null;
        sampleCustomer = new CustomerImpl(SAMPLE_CUSTOMERS_NAME);
        return sampleCustomer;
    }

    public Cargo sampleUnitisedCargo() {
        BigDecimal sampleBigDecimal = new BigDecimal("2");
        Duration sampleDuration = Duration.ofSeconds(300);
        Collection<Hazard> sampleCollection = new HashSet<Hazard>();
        sampleCollection.add(Hazard.radioactive);
        Cargo sampleCargo = new UnitisedCargoImpl(sampleCustomer(), sampleBigDecimal, sampleDuration, sampleCollection, false);
        return sampleCargo;
    }

    //=====================[CONSTRUCTOR]================================================================================

    @Test
    public void getSampleCargo_ViaMock() {
        Customer sampleCustomer = mock(CustomerImpl.class);
        when(sampleCustomer.getName()).thenReturn("B");
        Cargo sampleCargo = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);
        when(sampleCargo.getOwner()).thenReturn(sampleCustomer);
        assertEquals("B", sampleCargo.getOwner().getName());
    }

    @Test
    void badTest_create_Storehouse_With_negative_capacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            BusinessLogicImpl bl = new BusinessLogicImpl(-1);
        });
    }

    @Test
    void badTest_create_Storehouse_With_Overflow_in_capacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            BusinessLogicImpl bl = new BusinessLogicImpl(Integer.MAX_VALUE + 1);
        });
    }

    @Test
    void badTest_create_Storehouse_With_zero_capacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            BusinessLogicImpl bl = new BusinessLogicImpl(0);
        });
    }

    //=====================[STOREHOUSE]=================================================================================

    //additional method usage to register sample customer before attempting to add cargo
    @Test
    void goodTest_addToStorehouse_add_singleUnitisedCargo() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);

        Cargo unitisedCargoMock = mock(UnitisedCargoImpl.class);
        when(unitisedCargoMock.getOwner()).thenReturn(sampleCustomer());

        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        assertTrue(bl.addToStorehouse(unitisedCargoMock));
    }

    //additional method usage to register sample customer before attempting to add cargo
    @Test
    void goodTest_addToStorehouse_add_singleLiquidBulkCargo() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);

        Cargo liquidBulkCargoMock = mock(LiquidBulkCargoImpl.class);
        when(liquidBulkCargoMock.getOwner()).thenReturn(sampleCustomer());

        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        assertTrue(bl.addToStorehouse(liquidBulkCargoMock));
    }

    //additional method usage to register sample customer before attempting to add cargo
    @Test
    void goodTest_addToStorehouse_add_MixedCargoObject() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);

        Cargo mixedCargoMock = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);
        when(mixedCargoMock.getOwner()).thenReturn(sampleCustomer());

        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        assertTrue(bl.addToStorehouse(mixedCargoMock));
    }

    //additional method usage to register sample customer before attempting to add cargo
    //multiple usage of assert to assure correct premise
    @Test
    void goodTest_addToStorehouse_add_MultipleMixedSampleCargo() {
        BusinessLogicImpl bl = new BusinessLogicImpl(3);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        Cargo unitisedCargoMock = mock(UnitisedCargoImpl.class);
        when(unitisedCargoMock.getOwner()).thenReturn(sampleCustomer());

        Cargo liquidBulkCargoMock = mock(LiquidBulkCargoImpl.class);
        when(liquidBulkCargoMock.getOwner()).thenReturn(sampleCustomer());

        Cargo mixedCargoMock = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);
        when(mixedCargoMock.getOwner()).thenReturn(sampleCustomer());

        assertTrue(bl.addToStorehouse(unitisedCargoMock));
        assertTrue(bl.addToStorehouse(liquidBulkCargoMock));
        assertTrue(bl.addToStorehouse(mixedCargoMock));
    }

    //additional method usage to register sample customer before attempting to add cargo
    //multiple usage of assert to assure correct premise
    @Test
    void goodTest_addToStorehouse_full_storehouse() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        assertTrue(bl.addToStorehouse(sampleUnitisedCargo()));
        assertFalse(bl.addToStorehouse(sampleUnitisedCargo()));
    }

    //additional method usage to register sample customer before attempting to add cargo
    //multiple usage of assert to assure correct premise
    @Test
    void goodTest_addToStorehouse_addAfterDelete() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        assertTrue(bl.addToStorehouse(sampleUnitisedCargo()));
        assertFalse(bl.addToStorehouse(sampleUnitisedCargo()));

        bl.deleteStockAtPosition(0);

        assertTrue(bl.addToStorehouse(sampleUnitisedCargo()));
    }

    @Test
    void badTest_addToStorehouse_unregistered_Customer() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertNull(bl.getCargoByPosition(0));
        bl.addToStorehouse(sampleUnitisedCargo());
        assertNull(bl.getCargoByPosition(0));
    }

    @Test
    void badTest_addToStorehouse_nullObject() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertThrows(IllegalArgumentException.class, () -> {
            bl.addToStorehouse(null);
        });
    }

    //additional method usage to register sample customer before attempting to add cargo
    //multiple usage of assert to assure correct premise
    @Test
    void goodTest_getCargoByType() {
        BusinessLogicImpl bl = new BusinessLogicImpl(3);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        Cargo expected = sampleUnitisedCargo();
        bl.addToStorehouse(expected);

        //getByCargoType takes name of the Interface to be searched for as argument
        Cargo actual = bl.getCargoByType(UnitisedCargo.class.getSimpleName()).getFirst();

        //comparing field by field, since "expected" and "actual" objects are two separate entities
        assertEquals(expected.getOwner().getName(), actual.getOwner().getName());
        assertEquals(expected.getHazards(), actual.getHazards());
        assertEquals(expected.getDurationOfStorage(), actual.getDurationOfStorage());
        assertEquals(expected.getValue(), actual.getValue());
        assertEquals(expected.getLastInspectionDate(), actual.getLastInspectionDate());
        assertEquals(expected.getOwner(), actual.getOwner());
    }

    @Test
    void getCargoByPosition_using_Mockito() {

        Customer sampleCustomer = mock(CustomerImpl.class);
        when(sampleCustomer.getName()).thenReturn("Moleman");
        Cargo c = mock(LiquidBulkCargoImpl.class);
        when(c.getOwner()).thenReturn(sampleCustomer);

        Cargo sampleCargo = sampleUnitisedCargo();

        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(sampleCustomer.getName());
        assertTrue(bl.addToStorehouse(c));
        assertEquals(c, bl.getCargoByPosition(0));
    }

    //additional method usage to register sample customer before attempting to add cargo
    //multiple usage of assert to assure correct premise
    //For the sampleDate I used : https://stackoverflow.com/questions/2877532/set-date-in-a-single-line, author="tangens", answered May 20 '10 at 20:31
    @Test
    void goodTest_updateDateOfInspection_using_Mockito() {
        BusinessLogicImpl bl = new BusinessLogicImpl(3);

        Customer customerMock = mock(CustomerImpl.class);
        when(customerMock.getName()).thenReturn("B");
        Cargo cargoMock = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);
        when(cargoMock.getOwner()).thenReturn(customerMock);

        bl.addToCustomerDatabase(customerMock.getName());
        assertTrue(bl.addToStorehouse(cargoMock));

        Date sampleDate = null;
        try {
            sampleDate = new SimpleDateFormat("yyyyMMdd").parse("20001001");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        when(cargoMock.getLastInspectionDate()).thenReturn(sampleDate);

        bl.updateDateOfInspection(0);
        assertEquals(new java.util.Date(), bl.getCargoByPosition(0).getLastInspectionDate());
    }

    //additional method usage to register sample customer before attempting to add cargo
    //multiple usage of assert to assure correct premise
    @Test
    void goodTest_deleteStockAtPosition() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        assertTrue(bl.addToStorehouse(sampleUnitisedCargo()));
        assertFalse(bl.addToStorehouse(sampleUnitisedCargo())); //prove that storehouse is full

        bl.deleteStockAtPosition(0);
        assertTrue(bl.addToStorehouse(sampleUnitisedCargo()));
    }

    //additional method usage to register sample customer before attempting to add cargo
    //multiple usage of assert to assure correct premise
    @Test
    void badTest_deleteStockAtPosition_position_out_of_scope_positive() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        assertTrue(bl.addToStorehouse(sampleUnitisedCargo()));
        assertThrows(IllegalArgumentException.class, () -> {
            bl.deleteStockAtPosition(1);
        });
    }

    //additional method usage to register sample customer before attempting to add cargo
    //multiple usage of assert to assure correct premise
    @Test
    void badTest_deleteStockAtPosition_position_out_of_scope_negative() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        assertTrue(bl.addToStorehouse(sampleUnitisedCargo()));
        assertThrows(IllegalArgumentException.class, () -> {
            bl.deleteStockAtPosition(-1);
        });
    }

    //=====================[CUSTOMER]===================================================================================

    //since Customer Database is private, this test makes use of the getCustomer() method as well
    @Test
    void goodTest_addToCustomerDatabase() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        Customer actual = sampleCustomer();
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        assertEquals(actual.getName(), bl.getCustomer(SAMPLE_CUSTOMERS_NAME).getName());
    }

    @Test
    void badTest_addToCustomerDatabase_duplicate_entry() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertTrue(bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME));
        assertThrows(IllegalArgumentException.class, () -> {
            bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        });
    }

    @Test
    void goodTest_getCustomer() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        assertEquals(sampleCustomer().getName(), bl.getCustomer(SAMPLE_CUSTOMERS_NAME).getName());
    }

    @Test
    void badTest_getCustomer_unknownCustomer() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertNull(bl.getCustomer(SAMPLE_CUSTOMERS_NAME));
    }

    //usage of multiple methods due to assure correct premise
    @Test
    void goodTest_deleteCustomer() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);

        //assuring that customer is database
        assertEquals(SAMPLE_CUSTOMERS_NAME, bl.getCustomer(SAMPLE_CUSTOMERS_NAME).getName());
        bl.deleteCustomer(SAMPLE_CUSTOMERS_NAME);

        assertNull(bl.getCustomer(SAMPLE_CUSTOMERS_NAME));
    }

    @Test
    void badTest_deleteCustomer_unknown_Customer() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertThrows(IllegalArgumentException.class, () -> {
            bl.deleteCustomer(SAMPLE_CUSTOMERS_NAME);
        });
    }

    //usage of multiple methods due to assure correct premise
    @Test
    void badTest_deleteCustomer_empty_Argument() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertThrows(IllegalArgumentException.class, () -> {
            bl.deleteCustomer("");
        });
    }

    @Test
    void goodTest_isFull() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        assertFalse(bl.isFull());

        bl.addToStorehouse(sampleUnitisedCargo());
        assertTrue(bl.isFull());
    }

    @Test
    void getEntireCargo() {
        BusinessLogicImpl bl = new BusinessLogicImpl(2);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        UnitisedCargo u1 = (UnitisedCargoImpl) sampleUnitisedCargo();
        UnitisedCargoImpl u2 = (UnitisedCargoImpl) sampleUnitisedCargo();
        bl.addToStorehouse(u1);
        bl.addToStorehouse(u2);
        Cargo[] actual = bl.getEntireCargo();
        assertEquals(u1, actual[0]);
        assertEquals(u2, actual[1]);
    }


    @Test
    void goodTest_getEntireCargo_emptyStorehouse() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        Cargo[] actual = bl.getEntireCargo();
        assertNull(actual[0]);
    }

    @Test
    void detach() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        CapacityObserver co = new CapacityObserver(bl);
        assertEquals(bl.getObservers().size(), 0);
        bl.attach(co);
        assertEquals(bl.getObservers().size(), 1);
        bl.detach(co);
        assertEquals(bl.getObservers().size(), 0);
    }

    @Test
    void getDateOfStorage() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        Date[] dateOfStorage = bl.getDateOfStorage();
        assertNull(dateOfStorage[0]);
        bl.addToCustomerDatabase(sampleCustomer().getName());
        bl.addToStorehouse(sampleUnitisedCargo());
        dateOfStorage = bl.getDateOfStorage();
        assertNotNull(dateOfStorage[0]);
    }

    @Test
    void getObservers() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        CapacityObserver co = new CapacityObserver(bl);
        assertEquals(bl.getObservers().size(), 0);
        bl.attach(co);
        assertEquals(bl.getObservers().size(), 1);
    }

    @Test
    void getMAX_STORAGE_CAPACITY() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertEquals(1, bl.getMAX_STORAGE_CAPACITY());
    }

    @Test
    void getCustomerDatabase() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        ArrayList<Customer> customerDatabase = bl.getCustomerDatabase();
        assertEquals(0, customerDatabase.size());
        bl.addToCustomerDatabase(sampleCustomer().getName());
        customerDatabase = bl.getCustomerDatabase();
        assertNotNull(customerDatabase.get(0));
    }

    @Test
    void getAvailableStorageCapacity() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertEquals(1, bl.getAvailableStorageCapacity());
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        bl.addToStorehouse(sampleUnitisedCargo());
        assertEquals(0, bl.getAvailableStorageCapacity());
    }

    @Test
    void isFull() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertFalse(bl.isFull());
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        bl.addToStorehouse(sampleUnitisedCargo());
        assertTrue(bl.isFull());
    }

    @Test
    void isValidScope() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertFalse(bl.isValidScope(-1));
        assertTrue(bl.isValidScope(0));
        assertFalse(bl.isValidScope(1));
    }

    @Test
    void getAssociatedPositions() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertThrows(Exception.class, () -> {
            bl.getAssociatedPositions(sampleUnitisedCargo());
        });

        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        bl.addToStorehouse(sampleUnitisedCargo());

        try {
            assertEquals(0, bl.getAssociatedPositions(sampleUnitisedCargo()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAssociatedFirstDateOfStorage() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);

        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        Cargo unitisedCargo = sampleUnitisedCargo();
        bl.addToStorehouse(unitisedCargo);

        long timeStamp = new java.util.Date().getTime();
        assertTrue(timeStamp > bl.getAssociatedFirstDateOfStorage(unitisedCargo).getTime() - 1000 &&
                timeStamp < bl.getAssociatedFirstDateOfStorage(unitisedCargo).getTime() + 1000);
    }

    @Test
    void addToStorehouse() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        Cargo unitisedCargo = sampleUnitisedCargo();

        assertFalse(bl.addToStorehouse(unitisedCargo));
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        assertTrue(bl.addToStorehouse(unitisedCargo));
    }

    @Test
    void getCargoByType() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        Cargo unitisedCargo = sampleUnitisedCargo();

        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        assertTrue(bl.addToStorehouse(unitisedCargo));

        LinkedList<Cargo> filteredCargo = bl.getCargoByType(sampleUnitisedCargo().getClass().getSimpleName().replace("Impl", ""));
        assertEquals(1, filteredCargo.size());

        filteredCargo = bl.getCargoByType("LiquidBulkCargo");
        assertEquals(0, filteredCargo.size());
    }

    @Test
    void getCargoByPosition() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        Cargo unitisedCargo = sampleUnitisedCargo();
        bl.addToStorehouse(unitisedCargo);

        assertEquals(unitisedCargo, bl.getCargoByPosition(0));
    }

    @Test
    void updateStorehouse() {

        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        Cargo unitisedCargo = sampleUnitisedCargo();
        bl.addToStorehouse(unitisedCargo);

        assertEquals(unitisedCargo, bl.getCargoByPosition(0));

        Cargo[] empty = new Cargo[10];
        bl.updateStorehouse(empty);
        assertNull(bl.getCargoByPosition(0));
    }

    @Test
    void updateDateOfInspection() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        Cargo unitisedCargo = sampleUnitisedCargo();
        bl.addToStorehouse(unitisedCargo);

        long old = bl.getCargoByPosition(0).getLastInspectionDate().getTime();
        try {
            Thread.sleep(100);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        bl.updateDateOfInspection(0);
        long updated = bl.getCargoByPosition(0).getLastInspectionDate().getTime();

        assertNotEquals(old, updated);
    }

    @Test
    void deleteStockAtPosition() {

        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(SAMPLE_CUSTOMERS_NAME);
        Cargo unitisedCargo = sampleUnitisedCargo();
        bl.addToStorehouse(unitisedCargo);

        assertEquals(unitisedCargo, bl.getCargoByPosition(0));
        bl.deleteStockAtPosition(0);
        assertNull(bl.getCargoByPosition(0));
    }

    @Test
    void addToCustomerDatabase() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        Customer c = sampleCustomer();
        bl.addToCustomerDatabase(c.getName());
        assertEquals(c.getName(), bl.getCustomer(c.getName()).getName());
    }

    @Test
    void isKnownCustomer() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertFalse(bl.isKnownCustomer(sampleCustomer().getName()));
        bl.addToCustomerDatabase(sampleCustomer().getName());
        assertTrue(bl.isKnownCustomer(sampleCustomer().getName()));
    }

    @Test
    void getCustomer() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(sampleCustomer().getName());
        String sampleName = bl.getCustomer(sampleCustomer().getName()).getName();
        assertTrue(sampleCustomer().getName().matches(sampleName));
    }

    @Test
    void deleteCustomer() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(sampleCustomer().getName());
        assertEquals(1, bl.getCustomerDatabase().size());
        bl.deleteCustomer(sampleCustomer().getName());
        assertNull(bl.getCustomer(sampleCustomer().getName()));
    }

    @Test
    void attach() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        assertEquals(0, bl.getObservers().size());
        bl.attach(new CapacityObserver(bl));
        assertEquals(1, bl.getObservers().size());
    }

    //difference only in line separator
    @Test
    void informObserver() {
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outputStreamCaptor));

        BusinessLogicImpl bl = new BusinessLogicImpl(2);
        bl.attach(new CapacityObserver(bl));
        bl.addToCustomerDatabase(sampleCustomer().getName());
        bl.addToStorehouse(sampleUnitisedCargo());
        bl.informObserver();

        assertEquals(
                "\u001B[0;32m✔\u001B[0m: Successfully attached new CapacityObserver\n" +
                        "\u001B[33m⚠\u001B[0m: Storehouse almost reached maximum storage capacity.\n" +
                        "\u001B[33m⚠\u001B[0m: Storehouse almost reached maximum storage capacity.\n"
                , outputStreamCaptor.toString());

    }

    @AfterEach
    public void tearDown() {
        System.setIn(System.in);
        System.setOut(System.out);
    }
}