package storageContract.controller.events;

import java.util.EventObject;

public class HazardDisplayEvent extends EventObject {

    public String getFilter() {
        return filter;
    }

    private String filter;
    public HazardDisplayEvent(Object source, String filter) {
        super(source);
        this.filter = filter;
    }
}
