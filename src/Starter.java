import storageContract.controller.CommandListAndMessages;
import storageContract.controller.ConsoleReader;
import storageContract.controller.ControlModel;
import storageContract.controller.listener.*;
import storageContract.controller.observer.CapacityObserver;
import storageContract.controller.observer.HazardObserver;
import storageContract.controller.observer.Observer;
import storageContract.logic.BusinessLogicImpl;

import java.util.Scanner;

public class Starter implements CommandListAndMessages {
    public static void main(String[] args) {
        int DEFAULT_MAX_CAPACITY = 10;
        ControlModel controlModel;
        Observer capacityObserver;
        Observer hazardObserver;
        Scanner sc = new Scanner(System.in);

        System.out.println(MSG_displayOnStart);
        System.out.print("Please enter the desired storage capacity: " + "\n> ");

        String userInput = sc.next();
        int validCapacity;

        try {
            validCapacity = Integer.parseInt(userInput);
            if (validCapacity <= 0) {
                throw new IllegalArgumentException("Maximal storage Capacity has be a number between [1, " + Integer.MAX_VALUE + "]");
            } else {
                controlModel = new ControlModel(new BusinessLogicImpl(validCapacity));
                System.out.println(SYMB_NICESU + MSG_InitCapacity_success);
            }
        } catch (Exception e) {
            //System.out.println(e.getLocalizedMessage());
            System.err.println(SYMB_FAIL + "couldn't parse input, a new storehouse with default capacity[" + DEFAULT_MAX_CAPACITY + "] will be instantiated.");
            controlModel = new ControlModel(new BusinessLogicImpl(DEFAULT_MAX_CAPACITY));
        }

        capacityObserver = new CapacityObserver(controlModel.getModel());
        hazardObserver = new HazardObserver(controlModel.getModel());

        controlModel.getModel().attach(capacityObserver);
        controlModel.getModel().attach(hazardObserver);

        EventListener lExit = new Listener_Exit();
        EventListener lInsertNewCustomer = new Listener_InsertNewCustomer(controlModel);
        EventListener lInsertNewCargo = new Listener_InsertNewCargo(controlModel);
        EventListener lDeleteCargoAtPosition = new Listener_DeleteCargoAtPosition(controlModel);
        EventListener lDeleteCustomerWithName = new Listener_DeleteCustomerWithName(controlModel);
        EventListener lAttachObserver = new Listener_AttachObserver(controlModel);
        EventListener lDetachObserver = new Listener_DetachObserver(controlModel);
        EventListener lUpdateCargo = new Listener_UpdateCargo(controlModel);
        EventListener lDisplayCustomer = new Listener_DisplayCustomer(controlModel);
        EventListener lDisplayCargo = new Listener_DisplayCargo(controlModel);
        EventListener lDisplayHazard = new Listener_DisplayHazard(controlModel);
        EventListener lHandlePersistence = new Listener_HandlePersistence(controlModel);
        EventListener lHandleCargoPersistence = new Listener_HandleCargoPersistence(controlModel);

        ConsoleReader cli = new ConsoleReader(
                lExit,
                lInsertNewCustomer,
                lInsertNewCargo,
                lDeleteCargoAtPosition,
                lDeleteCustomerWithName,
                lAttachObserver,
                lDetachObserver,
                lUpdateCargo,
                lDisplayCustomer,
                lDisplayCargo,
                lDisplayHazard,
                lHandlePersistence,
                lHandleCargoPersistence
        );

        while (!userInput.equals("exit")) {
            cli.start(System.in, System.out);
            break;
        }
    }
}
