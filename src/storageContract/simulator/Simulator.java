package storageContract.simulator;

import storageContract.cargo.interfaces.Cargo;
import storageContract.controller.observer.*;
import storageContract.logic.BusinessLogicImpl;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static storageContract.controller.CommandListAndMessages.SYMB_FAIL;
import static storageContract.controller.CommandListAndMessages.SYMB_NICESU;

public class Simulator implements Subject {
    private ArrayList<BusinessLogicImpl> storageUnitList;
    private ArrayList<Observer> observers = new ArrayList<>();

    private final int MAX_STORAGE_CAPACITY = 5;

    //storage unit
    private BusinessLogicImpl su1 = new BusinessLogicImpl(MAX_STORAGE_CAPACITY);
    private BusinessLogicImpl su2 = new BusinessLogicImpl(MAX_STORAGE_CAPACITY);
    private BusinessLogicImpl su3 = new BusinessLogicImpl(MAX_STORAGE_CAPACITY);

    private Thread insertThread_1;
    private Thread insertThread_2;

    private Thread removalThread_1;
    private Thread removalThread_2;
    private Thread removalThread_3;

    private RemovalThread_Observer rto1;
    private RemovalThread_Observer rto2;
    private RemovalThread_Observer rto3;

    private InsertThread_Observer ito1;
    private InsertThread_Observer ito2;

    private final Lock lock=new ReentrantLock(true);            //Semaphore'ish //fairness = FIFO
    private final Condition full=this.lock.newCondition();
    private final Condition empty=this.lock.newCondition();

    public Simulator(){

        //create customer and add to database so attempting to add cargo doesn't cause exception
        String customerName = "Jane";
        this.su1.addToCustomerDatabase(customerName);
        this.su2.addToCustomerDatabase(customerName);
        this.su3.addToCustomerDatabase(customerName);

        storageUnitList = new ArrayList<>();
        storageUnitList.add(su1);
        storageUnitList.add(su2);
        storageUnitList.add(su3);

        //init Threads
        insertThread_1 = new Thread(new InsertThread(storageUnitList, this));
        insertThread_2 = new Thread(new InsertThread(storageUnitList, this));

        removalThread_1 = new Thread(new RemovalThread(storageUnitList,0, this));
        removalThread_2 = new Thread(new RemovalThread(storageUnitList,1, this));
        removalThread_3 = new Thread(new RemovalThread(storageUnitList,2, this));

        //init + add Observer
        rto1 = new RemovalThread_Observer(removalThread_1, this);
        rto2 = new RemovalThread_Observer(removalThread_2, this);
        rto3 = new RemovalThread_Observer(removalThread_3, this);

        ito1 = new InsertThread_Observer(insertThread_1, this);
        ito2 = new InsertThread_Observer(insertThread_2, this);

        observers.add(rto1);
        observers.add(rto2);
        observers.add(rto3);
        observers.add(ito1);
        observers.add(ito2);
    }

    public void runSimulation(){

        //start Threads
        removalThread_1.start();
        removalThread_2.start();
        removalThread_3.start();

        insertThread_1.start();
        insertThread_2.start();
    }

    //================================================================================================================

    public void put(Cargo randomCargo, int randomSU_ID) throws InterruptedException{
        this.lock.lock();
        BusinessLogicImpl randomSU = storageUnitList.get(randomSU_ID);
        try {
            while(randomSU.isFull())    this.empty.await();
            randomSU.addToStorehouse(randomCargo);
            System.out.println(randomCargo.getClass().getSimpleName() + " was stored in SU: " + (randomSU_ID+1) + " remaining storage capacity: " + randomSU.getAvailableStorageCapacity() + "/" + MAX_STORAGE_CAPACITY);
            if(randomSU.isFull())       this.full.signalAll();
           // informObserver();
        }finally {
            this.lock.unlock();
        }
    }

    public void relocate(BusinessLogicImpl storage, int storageID) throws InterruptedException{
        this.lock.lock();
        try {
            while(!storage.isFull()) this.full.await();        //block everybody until signal was given

            Cargo oldestCargo = getOldestCargo(storage);
            int aDifferentSU_ID = pickRandomSU_ID_differentFrom(storageID);

            //trying to relocate here - if it fails it will try to relocate to the remaining SU, if that fails as well -> end thread
            try {
                if (storageUnitList.get(aDifferentSU_ID).isFull()) {
                    if (storageUnitList.get(remainingSU(storageID, aDifferentSU_ID)).isFull()) {
                        //At this point, all SUs are full //TODO be aware this statement is only true for 3 SU
                        System.out.println("all Storage Units are filled. END OF SIMULATION.");
                        //TODO KILL THREAD HERE
                        System.exit(0);
                    } else {
                        storageUnitList.get(remainingSU(storageID, aDifferentSU_ID)).addToStorehouse(oldestCargo);
                        System.out.println("REMOVAL_THREAD " + (storageID+1) + " successfully moved oldest Cargo from SU: " + (storageID+1) + " to SU: " + (remainingSU(storageID, aDifferentSU_ID)+1) + " -> remaining storage capacity in SU: "+ remainingSU(storageID, aDifferentSU_ID) + " is now: " + storageUnitList.get(remainingSU(storageID, aDifferentSU_ID)).getAvailableStorageCapacity() + "/" + MAX_STORAGE_CAPACITY);
                    }
                } else {
                    storageUnitList.get(aDifferentSU_ID).addToStorehouse(oldestCargo);
                    System.out.println("REMOVAL_THREAD " + (storageID+1) + " successfully moved oldest Cargo from SU: " + (storageID+1) + " to SU: " + (aDifferentSU_ID+1) + " -> remaining storage capacity in SU: "+ remainingSU(storageID, aDifferentSU_ID) + " is now: " +  storageUnitList.get(aDifferentSU_ID).getAvailableStorageCapacity() + "/" + MAX_STORAGE_CAPACITY);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                System.err.println("Error occurred during relocation process");
            }

            //remove oldest Cargo form assigned SU after successful relocation
            try {
                storage.deleteStockAtPosition(storage.getAssociatedPositions(oldestCargo));
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            //informObserver();
            this.empty.signal();                //signal() = notify all

        }finally {
            this.lock.unlock();
        }
    }

    //this one will return the remaining SU-number that is neither the assigned one nor the one chosen randomly
    private int remainingSU(int assignedSU_ID, int aDifferentSU_ID) {
        LinkedList<Integer> l = new LinkedList<>();
        l.add(0);
        l.add(1);
        l.add(2);

        l.removeFirstOccurrence(assignedSU_ID);
        l.removeFirstOccurrence(aDifferentSU_ID);

        int remain = l.getFirst();
        return remain;
    }

    //method to chose a random SU that is not assigned
    private int pickRandomSU_ID_differentFrom(int assignedSU_ID) {
        Random rand = new Random();
        int randomSU = assignedSU_ID;
        while (randomSU == assignedSU_ID) {
            randomSU = rand.nextInt(3);
        }
        return randomSU;
    }

    private Cargo getOldestCargo(BusinessLogicImpl bl) {
        Cargo[] toBeInspected = bl.getEntireCargo();

        //empty case shouldn't be called due to min store cap is at least 1 and removal only gets called if warehouse is full, but paranoia saves the day
        if (toBeInspected.length == 0) {
            return null;
        }

        Cargo oldestCargo = toBeInspected[0];

        if (toBeInspected.length == 1) {
            return oldestCargo;
        }

        for (int i = 0; i < toBeInspected.length - 1; i++) {
            if (bl.getAssociatedFirstDateOfStorage(toBeInspected[i]) == null){
                return null;
            }
            if (bl.getAssociatedFirstDateOfStorage(toBeInspected[i]).before(bl.getAssociatedFirstDateOfStorage(toBeInspected[i + 1]))) {
                // i = oldest, do nothing
            } else {
                oldestCargo = toBeInspected[i + 1];
            }
        }
        return oldestCargo;
    }

    @Override
    public void attach(Observer o) {
        try{
            observers.add(o);
            System.out.println(SYMB_NICESU + "Successfully attached new " + o.getClass().getSimpleName());
        }catch (Exception e){
            System.out.println(SYMB_FAIL + "Attaching Observer " + o.getClass().getSimpleName() + " failed.");
            e.printStackTrace();
        }
    }

    @Override
    public void detach(Observer o) {
        try{
            observers.remove(o);
            System.out.println(SYMB_NICESU + o.getClass().getSimpleName() + " was successfully detached");
        }catch (Exception e){
            System.out.println(SYMB_FAIL + "Detaching Observer " + o.getClass().getSimpleName() + " failed.");
            e.printStackTrace();
        }
    }

    @Override
    public void informObserver() {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update();
        }
    }
}
