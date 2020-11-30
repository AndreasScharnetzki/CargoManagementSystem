package storageContract.cargo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.logic.BusinessLogicImpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UnitisedCargoImplTest {
    private UnitisedCargoImpl sampleCargo;

    @BeforeEach
    public void setup() {
        Customer customer = new CustomerImpl("A");
        Collection<Hazard> hazards = new HashSet<Hazard>();
        hazards.add(Hazard.toxic);
        this.sampleCargo = new UnitisedCargoImpl(customer, BigDecimal.ONE, Duration.ofSeconds(1), hazards, true);
    }

    @Test
    void isFragile() {
        assertTrue(sampleCargo.isFragile());
    }

    @Test
    void getOwner() {
        assertEquals("A", sampleCargo.getOwner().getName());
    }

    @Test
    void getValue() {
        assertEquals(BigDecimal.ONE, sampleCargo.getValue());
    }

    @Test
    void getDurationOfStorage() {
        assertEquals(Duration.ofSeconds(1), sampleCargo.getDurationOfStorage());
    }

    @Test
    void getHazards() {
        assertTrue(sampleCargo.getHazards().contains(Hazard.toxic));
        assertEquals(1, sampleCargo.getHazards().size());
    }

    @Test
    void getLastInspectionDate() {
        //proving that get inspection date differs less than 1 second; i am aware that this is not an ideal solution
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(sampleCargo.getOwner().getName());
        bl.addToStorehouse(sampleCargo);
        assertTrue(sampleCargo.getLastInspectionDate().getTime() + 1000 > new java.util.Date().getTime());
        assertTrue(sampleCargo.getLastInspectionDate().getTime() -1000 < new java.util.Date().getTime());
    }

    @Test
    void getStoragePosition() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase(sampleCargo.getOwner().getName());
        bl.addToStorehouse(sampleCargo);
        assertEquals(0, sampleCargo.getStoragePosition());
    }

    @Test
    void setStoragePosition() {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        sampleCargo.setStoragePosition(100);
        assertEquals(100, sampleCargo.getStoragePosition());
    }

    @Test
    void constructorTest_good() {
        assertEquals(UnitisedCargoImpl.class, sampleCargo.getClass());
    }
}