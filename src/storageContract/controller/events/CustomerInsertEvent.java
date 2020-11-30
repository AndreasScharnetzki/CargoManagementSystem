package storageContract.controller.events;

import java.util.EventObject;

public class CustomerInsertEvent extends EventObject {

    private String customerName;

    public String getCustomerName(){
        return this.customerName;
    }

    public CustomerInsertEvent(Object source, String customerName) {
        super(source);
        this.customerName = customerName;
    }
}
