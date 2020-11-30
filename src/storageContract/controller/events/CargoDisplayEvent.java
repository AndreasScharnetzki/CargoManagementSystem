package storageContract.controller.events;

import java.util.EventObject;

public class CargoDisplayEvent extends EventObject{

    private String filter;

    public String getFilter() {
        return filter;
    }

    public CargoDisplayEvent(Object source, String filter) {
        super(source);
        this.filter = filter;
    }
}
