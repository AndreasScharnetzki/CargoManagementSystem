package storageContract.controller.observer;

import storageContract.logic.BusinessLogicImpl;


import java.io.Serializable;

import static storageContract.controller.CommandListAndMessages.SYMB_OBSERVER_ICON;

public class CapacityObserver implements Observer, Serializable {
    private final BusinessLogicImpl businessLogicReference;

    public CapacityObserver(BusinessLogicImpl objectToBeObserved){
        this.businessLogicReference = objectToBeObserved;
    }

    @Override
    public void update() {
        if(businessLogicReference.getAvailableStorageCapacity() == 1) {
            System.out.println(SYMB_OBSERVER_ICON + "Storehouse almost reached maximum storage capacity.");
        }
    }
}


