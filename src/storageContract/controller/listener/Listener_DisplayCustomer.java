package storageContract.controller.listener;

import storageContract.administration.Customer;
import storageContract.cargo.interfaces.Cargo;
import storageContract.controller.ControlModel;
import storageContract.logic.BusinessLogicImpl;

import java.util.ArrayList;

public class Listener_DisplayCustomer implements EventListener {
    private ControlModel controlModel;

    public Listener_DisplayCustomer(ControlModel controlModel) {
        this.controlModel = controlModel;
    }

    @Override
    public void onInputEvent(Object event) {
        ArrayList<Customer> customerDatabase = controlModel.getModel().getCustomerDatabase();

        System.out.println("CustomerName        |        Number of associated Cargo\n");
        for(int i = 0; i < customerDatabase.size(); i++)
        {
            if(customerDatabase.get(i)!= null){
                System.out.println(customerDatabase.get(i).getName() +
                "       |       " +
                getAmountOfAssociatedCargo(customerDatabase.get(i).getName()));
            }
        }
    }

    private int getAmountOfAssociatedCargo(String owner) {
        Cargo[] storehouse = controlModel.getModel().getEntireCargo();
        int counter = 0;
        for(int i = 0; i < storehouse.length; i++)
        {
            if(storehouse[i] != null){
                if(storehouse[i].getOwner().getName().matches(owner)){
                    counter++;
                }
            }
        }
        return counter;
    }
}
