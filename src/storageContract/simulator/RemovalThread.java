package storageContract.simulator;

import storageContract.controller.observer.Observer;
import storageContract.logic.BusinessLogicImpl;

import java.util.ArrayList;
import java.util.Random;

public class RemovalThread implements Runnable {

    private Random rand = new Random();

    private ArrayList<Observer> observers = new ArrayList<>();

    private Simulator simulatorReference;
    private ArrayList<BusinessLogicImpl> storageUnitList;
    private BusinessLogicImpl assignedSU;
    private final int assignedSU_ID;

    public RemovalThread(ArrayList<BusinessLogicImpl> storageUnitList, int assignedSU_ID, Simulator s) {
        this.simulatorReference = s;
        this.storageUnitList = storageUnitList;
        this.assignedSU = storageUnitList.get(assignedSU_ID);
        this.assignedSU_ID = assignedSU_ID;
    }

    public void run() {
        System.out.println("=====START OF REMOVAL_THREAD (HashCode: " + this.hashCode() + " )======");
        while (true) {
            try {
                simulatorReference.relocate(assignedSU, assignedSU_ID);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        //System.out.println("=====END OF REMOVAL_THREAD (HashCode: " + this.hashCode() + " )======");
    }
}