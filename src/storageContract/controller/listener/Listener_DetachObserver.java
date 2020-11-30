package storageContract.controller.listener;

import storageContract.controller.ControlModel;
import storageContract.controller.events.ObserverDetachEvent;
import storageContract.controller.observer.Observer;
import storageContract.logic.BusinessLogicImpl;

import java.util.ArrayList;

import static storageContract.controller.CommandListAndMessages.SYMB_FAIL;

public class Listener_DetachObserver implements EventListener<ObserverDetachEvent> {

    private ControlModel controlModel;

    public Listener_DetachObserver(ControlModel controlModel) {
        this.controlModel = controlModel;
    }

    private Observer findObserverByName(String observerName){
        ArrayList<Observer> observers = controlModel.getModel().getObservers();
        for(int i = 0; i<observers.size(); i++){
            if(observers.get(i).getClass().getSimpleName().toLowerCase().equals(observerName)){
                return observers.get(i);
            }
        }
        return null;
    }

    @Override
    public void onInputEvent(ObserverDetachEvent event) {
        Observer observerToBeDetached = findObserverByName(event.getObserverName().toLowerCase());
        if(observerToBeDetached != null){
            controlModel.getModel().detach(observerToBeDetached);
        }else{
            System.err.println(SYMB_FAIL + "no Observer with name("+event.getObserverName()+") found, returning to main menu");
        }
    }
}
