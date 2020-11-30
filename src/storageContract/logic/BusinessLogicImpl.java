package storageContract.logic;

import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.*;
import storageContract.cargo.interfaces.Cargo;
import storageContract.controller.observer.Subject;
import storageContract.controller.observer.Observer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

import static storageContract.controller.CommandListAndMessages.*;

public class BusinessLogicImpl implements BusinessLogic, Subject, Serializable {

    private ArrayList<Customer> customerDatabase = new ArrayList<Customer>();
    private Cargo[] storehouse;
    private Date [] dateOfStorage;
    private boolean full = false;
    private ArrayList<Observer> observers = new ArrayList<>();
    private int MAX_STORAGE_CAPACITY;

    //=====================[WAREHOUSE]==================================================================================

    public Date[] getDateOfStorage() {
        return dateOfStorage;
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }

    public int getMAX_STORAGE_CAPACITY() {
        return MAX_STORAGE_CAPACITY;
    }

    public BusinessLogicImpl(int MAX_STORAGE_CAPACITY) {
        if(MAX_STORAGE_CAPACITY <= 0){
            throw new IllegalArgumentException("Maximal storage Capacity has be a number between [1, " + Integer.MAX_VALUE + "]");
        }
        this.MAX_STORAGE_CAPACITY = MAX_STORAGE_CAPACITY;
        storehouse = new Cargo[this.MAX_STORAGE_CAPACITY];
        dateOfStorage = new Date[MAX_STORAGE_CAPACITY];
    }

    public ArrayList<Customer> getCustomerDatabase() {
        return customerDatabase;
    }

    public int getAvailableStorageCapacity(){
        return getAvailableWarehousePositions().size();
    }

    private LinkedList<Integer> getAvailableWarehousePositions(){
        LinkedList<Integer> freeSlots = new LinkedList<Integer>();
        for (int i = 0; i < storehouse.length; i++) {
            if(storehouse[i] == null){
                freeSlots.add(i);
            }
        }
        return freeSlots;
    }

    private boolean enoughSpaceAvailable(){
        if(getAvailableStorageCapacity() > 0){
            this.full = false;
            return true;
        }else{
            this.full = true;
            //System.err.println("ERROR: currently no free storage space is available.");
            return false;
        }
    }

    public boolean isFull() {
        return full;
    }

    public boolean isValidScope(int userInput){
        if(userInput < 0 || userInput >= (MAX_STORAGE_CAPACITY)){
            return false;
        }
        return true;
    }

    //returns associated positions in storehouse of given cargo
    public int getAssociatedPositions (Cargo cargoToBeInspected) throws Exception {
        for (int j = 0; j < storehouse.length; j++) {
            if(storehouse[j] == cargoToBeInspected){
                return j;
            }
        }
        throw new Exception("Cargo was not found in Storage Unit");
    }

    //Position of Cargo in storehouse[] matches dateOfFirstStorage[]
    public Date getAssociatedFirstDateOfStorage (Cargo toBeInspected){
        try {
            return dateOfStorage[getAssociatedPositions(toBeInspected)];
        } catch (Exception e) {
            System.err.println(SYMB_FAIL + "Unable to find first date of storage to given Cargo");
            e.printStackTrace();
        }
        return null;
    }

    //C.R.U.D.
    public boolean addToStorehouse(Cargo cargoToBeAdded) throws IllegalArgumentException {
        if(cargoToBeAdded == null){
            throw new IllegalArgumentException("ERROR: Cargo-Object hasn't been initialized");
        }
        if( enoughSpaceAvailable() ){
            if(isKnownCustomer( cargoToBeAdded.getOwner().getName() ) ) {
                int positionWhereToStore = getAvailableWarehousePositions().getFirst();

                assignStoragePositionToCargo(cargoToBeAdded);
                storehouse[positionWhereToStore] = cargoToBeAdded;
                dateOfStorage[positionWhereToStore] = new java.util.Date();

                if(getAvailableStorageCapacity() == 0){
                    full = true;
                }

                informObserver();
                return true;
            }else{
                System.out.println(SYMB_FAIL + "Due to business policy unregistered users are not allowed to store cargo.");
                return false;
            }
        }
        System.out.println(SYMB_FAIL + "We are sorry to inform you that our storehouse has reached its maximum storage capacity.");
        return false;
    }

    //HÄSSLICH WIE DIE NACHT!
    private void assignStoragePositionToCargo(Cargo cargoToBeAdded) {
        int positionWhereToStore = getAvailableWarehousePositions().getFirst();
        if(cargoToBeAdded instanceof LiquidBulkCargoImpl){
            ((LiquidBulkCargoImpl) cargoToBeAdded).setStoragePosition(positionWhereToStore);
        }

        if(cargoToBeAdded instanceof MixedCargoLiquidBulkAndUnitisedImpl){
            ((MixedCargoLiquidBulkAndUnitisedImpl) cargoToBeAdded).setStoragePosition(positionWhereToStore);
        }

        if(cargoToBeAdded instanceof UnitisedCargoImpl){
            ((UnitisedCargoImpl) cargoToBeAdded).setStoragePosition(positionWhereToStore);
        }
    }

    public LinkedList<Cargo> getCargoByType(String cargoType){
        String cargoTypeAsString = null;
        if(getAvailableStorageCapacity() == MAX_STORAGE_CAPACITY){
            System.err.println("The operation cannot be performed due to no cargo has been stored yet.");
            return null;
        }
        else{
            LinkedList<Cargo> cargoByType = new LinkedList<>();
            for (int i = 0; i < storehouse.length ; i++) {
                if(storehouse[i] != null){
                    cargoTypeAsString = storehouse[i].getClass().getSimpleName();
                    if(cargoTypeAsString.equals(cargoType.concat("Impl")) ){
                        cargoByType.add(storehouse[i]);
                    }
                }
            }
            return cargoByType;
        }
    }

    public Cargo getCargoByPosition(int cargoPosition){
        if(!isValidScope(cargoPosition)){
            throw new IllegalArgumentException("ERROR: invalid scope");
        }
        if(storehouse[cargoPosition] == null){
            return null;
        }
        else{
            return storehouse[cargoPosition];
        }
    }

    public Cargo[] getEntireCargo() {
        return this.storehouse;
    }

    public void updateStorehouse(Cargo[] updatedVersion) {
        this.storehouse = updatedVersion;
    }

    public void updateDateOfInspection(int validPositionToBeUpdated){
        Cargo toBeUpdated = storehouse[validPositionToBeUpdated];

        Customer tempCustomer = toBeUpdated.getOwner();
        BigDecimal tempValue = toBeUpdated.getValue();
        Duration tempDurationOfStorage = toBeUpdated.getDurationOfStorage();

        if(toBeUpdated instanceof LiquidBulkCargoImpl){
            storehouse[validPositionToBeUpdated] = new LiquidBulkCargoImpl(
                    tempCustomer, tempValue, tempDurationOfStorage,(toBeUpdated.getHazards()),
                    ((LiquidBulkCargoImpl) toBeUpdated).isPressurized());
        }
        if(toBeUpdated instanceof MixedCargoLiquidBulkAndUnitisedImpl){
            storehouse[validPositionToBeUpdated] = new MixedCargoLiquidBulkAndUnitisedImpl(
                    tempCustomer, tempValue, tempDurationOfStorage, (toBeUpdated.getHazards()),
                    ((MixedCargoLiquidBulkAndUnitisedImpl) toBeUpdated).isPressurized(),
                    ((MixedCargoLiquidBulkAndUnitisedImpl) toBeUpdated).isFragile());
        }
        if(toBeUpdated instanceof UnitisedCargoImpl){
            storehouse[validPositionToBeUpdated] = new UnitisedCargoImpl(
                    tempCustomer, tempValue, tempDurationOfStorage, (toBeUpdated.getHazards()),
                    ((UnitisedCargoImpl) toBeUpdated).isFragile());
        }
    }

    public void deleteStockAtPosition(int validPositionToBeDeleted) throws IllegalArgumentException {
        if(isValidScope(validPositionToBeDeleted)){
            if(storehouse[validPositionToBeDeleted] == null){
                throw new IllegalArgumentException("No Cargo is stored at this position.");
            }
            storehouse[validPositionToBeDeleted] = null;
            dateOfStorage[validPositionToBeDeleted] = null;

            full = false;

            informObserver();
        } else{
            throw new IllegalArgumentException("The position you were referring to is invalid.");
        }
    }
    
    //=====================[CUSTOMERS]==================================================================================

    private void deleteAssociatedCargo(String customerEntryToBeDeleted) {
        Cargo tempCargo = null;
        for (int i = 0; i < MAX_STORAGE_CAPACITY; i++) {
            if (storehouse[i] != null) {
                tempCargo = storehouse[i];
                if (tempCargo.getOwner().equals(customerEntryToBeDeleted.trim())) {
                    deleteStockAtPosition(i);
                    full = false;
                }
            }
        }
        informObserver();
    }

    //C.R.U.D.
    public boolean addToCustomerDatabase(String userInput){
        if(!isKnownCustomer(userInput)){
            Customer newCustomer = new CustomerImpl(userInput);
            customerDatabase.add(newCustomer);
            return true;
        } else {
            throw new IllegalArgumentException("WARNING: the name you wanted to add to customer database, already exists.");
        }
    }

    public boolean isKnownCustomer(String customerName){
        Customer temp = null;
        for (int i = 0; i < customerDatabase.size() ; i++) {
            temp = customerDatabase.get(i);
            if(temp.getName().equals(customerName)){
                return true;
            }
        }
        return false;
    }

    public Customer getCustomer(String userInput){
        Customer temp = null;
        for (int i = 0; i < customerDatabase.size() ; i++) {
            temp = customerDatabase.get(i);
            if(temp.getName().equals(userInput)){
                return customerDatabase.get(i);
            }
        }
        System.out.println(SYMB_FAIL + "The customer with name " + ANSI_RED + userInput + ANSI_RESET + " is not registered in Database.");
        return null;
    }

    public boolean deleteCustomer(String customerEntryToBeDeleted) throws IllegalArgumentException {
        if (customerEntryToBeDeleted == null || customerEntryToBeDeleted.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty name field.");
        } else {
            if(getCustomer(customerEntryToBeDeleted) == null){
                throw new IllegalArgumentException("Unknown customer.");
            }else{
                customerDatabase.remove(getCustomer(customerEntryToBeDeleted));
                //remove associated cargo to maintain consistency of storehouse
                deleteAssociatedCargo(customerEntryToBeDeleted);
                return true;
            }
        }
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