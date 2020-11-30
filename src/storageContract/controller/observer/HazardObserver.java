package storageContract.controller.observer;

import storageContract.cargo.Hazard;
import storageContract.cargo.interfaces.Cargo;
import storageContract.logic.BusinessLogicImpl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

import static storageContract.controller.CommandListAndMessages.*;

public class HazardObserver implements Observer, Serializable {
    private BusinessLogicImpl logicRef;
    private Cargo[] storehouse = null;

    private HashSet<Hazard> temp = new HashSet<>();
    private int before, after;

    public HazardObserver(BusinessLogicImpl objectToBeObserved){
        this.logicRef = objectToBeObserved;
    }

    public void update() {

        before = temp.size();
        temp.clear();

        storehouse = logicRef.getEntireCargo();

        for (int i = 0; i < storehouse.length ; i++) {
            if(storehouse[i] != null){
                temp.addAll(storehouse[i].getHazards());
            }
        }

        //idea: if size of hashset differs, there must have been a change in the hazards contained

        after = temp.size();

        if(before!=after){
            System.out.println(SYMB_OBSERVER_ICON + "A change in the hazards stored has been noticed, the Storehouse contains the following hazards: " + ANSI_PURPLE + temp + ANSI_RESET);
        }
    }
}


