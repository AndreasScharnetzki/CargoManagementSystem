package storageContract.simulator;

import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.cargo.MixedCargoLiquidBulkAndUnitisedImpl;
import storageContract.cargo.UnitisedCargoImpl;
import storageContract.cargo.interfaces.Cargo;
import storageContract.logic.BusinessLogicImpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
//generate a random Cargo object, choose a random Storage Unit, call put()
public class InsertThread implements Runnable{

    private Random random = new Random();
    private ArrayList<BusinessLogicImpl> storageUnitList;
    private Simulator simulator;

    public InsertThread(ArrayList<BusinessLogicImpl> storageUnitList, Simulator s){
        this.storageUnitList = storageUnitList;
        this.simulator = s;
    }

    public void run() {
        System.out.println("=====START OF INSERT_THREAD (HashCode: " + this.hashCode() + ")=====");

        //constantly tries to add a random cargo
        while(true){
            try{
                int randomSU = random.nextInt(3);
                Cargo cargoToBeStored = randomCargo(random.nextInt(3));
                simulator.put( cargoToBeStored, randomSU); //if SU is full, this will run into empty.await()
/*
                //chooses a random storage unit to insert a random cargo
                if(!storageUnitList.get(randomSU).isFull()){
                    storageUnitList.get(randomSU).addToStorehouse(randomCargo(random.nextInt(3))); //0,1,2
                }else{
                    //TODO handle wait here
                    System.out.println("Storage " + randomSU + " is full, calling for removal, waiting until removal was performed.");
                    simulator.informAboutFullStorageUnit(randomSU, Thread.currentThread().getId());
                    try {
                        this.wait();
                    } catch (InterruptedException interruptedException){
                        //got woken up
                        System.out.println("InsertThread received notify Signal, going back to work now.");
                    }
                }

 */
            }catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
        //System.out.println("===== END OF INSERT_THREAD (HASHCODE: " + this.hashCode() + ")=====");
    }

    private Cargo randomCargo(int randomInt){

        String customerName = "Jane";

        //creating sample cargo
        Customer cargoOwner = new CustomerImpl(customerName);
        BigDecimal sampleBigDecimal = new BigDecimal("2");
        Duration sampleDuration = Duration.ofSeconds(300);
        Collection<Hazard> sampleCollection = new HashSet<Hazard>();
        sampleCollection.add(Hazard.radioactive);

        switch(randomInt){
            case 0:
                return new UnitisedCargoImpl(cargoOwner, sampleBigDecimal, sampleDuration, sampleCollection, false);
            case 1:
                return new  MixedCargoLiquidBulkAndUnitisedImpl(cargoOwner, sampleBigDecimal, sampleDuration, sampleCollection, false, true);
            case 2:
                return new LiquidBulkCargoImpl(cargoOwner, sampleBigDecimal, sampleDuration, sampleCollection, false);
            default:
                System.err.println("Error occurred whilst creating a random Cargo.");
                return null;
        }
    }
}
