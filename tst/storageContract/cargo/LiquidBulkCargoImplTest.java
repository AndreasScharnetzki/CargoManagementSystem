package storageContract.cargo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.interfaces.LiquidBulkCargo;
import storageContract.controller.listener.Listener_UpdateCargo;
import storageContract.logic.BusinessLogicImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class LiquidBulkCargoImplTest {
    private LiquidBulkCargoImpl sampleCargo;

    @BeforeEach
    public void setup() {
        Customer customer = new CustomerImpl("A");
        Collection<Hazard> hazards = new HashSet<Hazard>();
        hazards.add(Hazard.toxic);
        this.sampleCargo = new LiquidBulkCargoImpl(customer, BigDecimal.ONE, Duration.ofSeconds(1), hazards, true);
    }

    @Test
    void isPressurized() {
        assertTrue(sampleCargo.isPressurized());
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
        assertEquals(LiquidBulkCargoImpl.class, sampleCargo.getClass());
    }
}