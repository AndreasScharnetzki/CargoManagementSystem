package storageContract.serialisation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storageContract.cargo.Hazard;
import storageContract.cargo.UnitisedCargoImpl;
import storageContract.cargo.interfaces.Cargo;
import storageContract.logic.BusinessLogic;
import storageContract.logic.BusinessLogicImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceTest {

    //Focussed my testing on single Cargo and JOS

    private BusinessLogicImpl bl;
    Cargo sampleCargo;

    @BeforeEach
    public void setUp() {
        bl = new BusinessLogicImpl(1);
        bl.addToCustomerDatabase("a");
        HashSet<Hazard> hazards = new HashSet<Hazard>();
        hazards.add(Hazard.toxic);
        sampleCargo = new UnitisedCargoImpl(bl.getCustomer("a"), BigDecimal.ONE, Duration.ofSeconds(1), hazards, true);
    }

    @AfterEach
    public void tearDown() {
        try {
            Files.deleteIfExists(Paths.get("saveJOS.txt"));
            Files.deleteIfExists(Paths.get("serializedSingleCargo.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void serializeJOS() {
        Persistence.serializeJOS(bl, "saveJOS.txt");
        assertTrue(new File("saveJOS.txt").exists());
    }

    @Test
    void badTest_serializeJOS_invalid_logic() {

        assertThrows(IllegalArgumentException.class, () -> {
            Persistence.serializeJOS(null, "saveJOS.txt");
        });
    }

    @Test
    void badTest_serializeJOS_invalid_Filename_null() {

        assertThrows(IllegalArgumentException.class, () -> {
            Persistence.serializeJOS(bl, null);
        });
    }

    //testing the for correct class
    @Test
    void deserializeJOS(){
        Persistence.serializeJOS(bl, "saveJOS.txt");
        BusinessLogicImpl newBL = null;
        try {
            newBL = Persistence.deserializeJOS("saveJOS.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(BusinessLogicImpl.class, newBL.getClass());
    }

    //testing for attributes, hence multiple asserts were used
    @Test
    void deserializeJOS_attributes(){
        String expected = "test";
        bl.addToCustomerDatabase(expected);
        bl.addToStorehouse(sampleCargo);

        Persistence.serializeJOS(bl, "saveJOS.txt");
        BusinessLogicImpl newBL = null;
        try {
            newBL = Persistence.deserializeJOS("saveJOS.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(expected, newBL.getCustomer(expected).getName());
        assertEquals(sampleCargo.getOwner().getName(), newBL.getCargoByPosition(0).getOwner().getName());
    }


    @Test
    void badTest_deserializeJOS_FileNotFound() {
        assertThrows(FileNotFoundException.class, () -> {
            BusinessLogicImpl newBL = Persistence.deserializeJOS("foo.txt");
        });
    }

    @Test
    void badTest_deserializeJOS_nullFile() {
        assertThrows(IllegalArgumentException.class, () -> {
            BusinessLogicImpl newBL = Persistence.deserializeJOS(null);
        });
    }


    @Test
    void serializeSingleCargo() {
        bl.addToStorehouse(sampleCargo);
        Persistence.serializeSingleCargo(bl,0);
        assertTrue(new File("serializedSingleCargo.txt").exists());
    }

    //checking all accessible attributes, hence multiple asserts were used
    @Test
    void deserializeSingleCargo() {
        bl.addToStorehouse(sampleCargo);
        Persistence.serializeSingleCargo(bl,0);
        Cargo actual = Persistence.deserializeSingleCargo();

        assertEquals(sampleCargo.getOwner().getName(), actual.getOwner().getName());
        assertEquals(sampleCargo.getHazards(), actual.getHazards());
        assertEquals(sampleCargo.getLastInspectionDate(), actual.getLastInspectionDate());
        assertEquals(sampleCargo.getValue(), actual.getValue());
        assertEquals(sampleCargo.getDurationOfStorage().getSeconds(), actual.getDurationOfStorage().getSeconds());
    }
}