package storageContract.controller.listener;

import storageContract.cargo.Hazard;
import storageContract.cargo.interfaces.Cargo;
import storageContract.controller.ControlModel;
import storageContract.controller.events.HazardDisplayEvent;
import storageContract.logic.BusinessLogicImpl;

import java.util.*;

import static storageContract.controller.CommandListAndMessages.SYMB_FAIL;

public class Listener_DisplayHazard implements EventListener<HazardDisplayEvent> {
    private ControlModel controlModel;

    public Listener_DisplayHazard(ControlModel controlModel) {
        this.controlModel = controlModel;
    }

    @Override
    public void onInputEvent(HazardDisplayEvent event) {

        switch(event.getFilter().toLowerCase()){
            case("i"):
                System.out.println("The storehouse currently holds cargo labeled as:\n");
                System.out.println(getContainedHazards());
                break;

            case("e"):
                System.out.println("The storehouse currently doesn't holds cargo labeled as:\n");
                ArrayList<Hazard> remainingHazards = new ArrayList<Hazard>(Arrays.asList(Hazard.values()));
                remainingHazards.removeAll(getContainedHazards());
                System.out.println(remainingHazards);
                break;

            default:
                System.err.println(SYMB_FAIL + "unable to identify filter - please refer to [i]ncluded/[e]xcluded, returning to main menu");
                break;
        }
    }

    private HashSet<Hazard> getContainedHazards(){
        HashSet<Hazard> containedHazards = new HashSet<Hazard>();
        Cargo[] storehouse = controlModel.getModel().getEntireCargo();

        for(int i = 0; i < storehouse.length; i++)
        {
            if(storehouse[i] != null){
                containedHazards.addAll(storehouse[i].getHazards());
            }
        }
        return containedHazards;
    }
}
