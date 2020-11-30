import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.UnitisedCargoImpl;
import storageContract.logic.BusinessLogicImpl;
import storageContract.serialisation.Persistence;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

public class PersistenceStarter {
    public static void main(String[] args) {
        BusinessLogicImpl bl = new BusinessLogicImpl(1);
        String customerName = "a";
        CustomerImpl c = new CustomerImpl(customerName);

        bl.addToCustomerDatabase(customerName);

        HashSet<Hazard> h = new HashSet<>();
        h.add(Hazard.explosive);
        UnitisedCargoImpl uc = new UnitisedCargoImpl(c, BigDecimal.valueOf(1), Duration.ofSeconds(32), h, true );

        bl.addToStorehouse(uc);

        Persistence.serializeJOS(bl, "saveFile.txt");

        //setting the business logic object to null to simulate a reset
        bl =  null;

        try {
            bl = Persistence.deserializeJOS("saveFile.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("If you read this line, the persistence demo ran successfully.\n" +
                "Check the 'saveFile.txt' for details.");
    }
}
