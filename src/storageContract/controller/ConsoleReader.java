package storageContract.controller;

import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.cargo.MixedCargoLiquidBulkAndUnitisedImpl;
import storageContract.cargo.UnitisedCargoImpl;
import storageContract.cargo.interfaces.Cargo;
import storageContract.controller.events.*;
import storageContract.controller.listener.EventListener;

import java.io.*;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Duration;
import java.util.HashSet;
import java.util.StringTokenizer;

public class ConsoleReader implements CommandListAndMessages {
    private Mode mode;

    private BufferedReader userInputBR;

    private final InputEventHandler<ConsoleInputEvent> consoleEventHandler;
    private final InputEventHandler<CargoInsertEvent> cargoInsertEventHandler;
    private final InputEventHandler<CustomerInsertEvent> customerInsertEventHandler;
    private final InputEventHandler<CargoDeletionEvent> cargoDeletionEventHandler;
    private final InputEventHandler<CustomerDeletionEvent> customerDeletionEventHandler;
    private final InputEventHandler<ObserverAttachEvent> observerAttachEventHandler;
    private final InputEventHandler<ObserverDetachEvent> observerDetachEventHandler;
    private final InputEventHandler<CargoUpdateEvent> cargoUpdateEventHandler;
    private final InputEventHandler<CustomerDisplayEvent> customerDisplayEventHandler;
    private final InputEventHandler<CargoDisplayEvent> cargoDisplayEventHandler;
    private final InputEventHandler<HazardDisplayEvent> hazardDisplayEventHandler;
    private final InputEventHandler<PersistenceTriggerEvent> persistenceEventHandler;
    private final InputEventHandler<CargoPersistenceEvent> cargoPersistenceEventHandler;

    //constructor
    public ConsoleReader(EventListener lExit,
                         EventListener lInsertNewCustomer,
                         EventListener lInsertNewCargo,
                         EventListener lDeleteCargoAtPosition,
                         EventListener lDeleteCustomerWithName,
                         EventListener lAttachObserver,
                         EventListener lDetachObserver,
                         EventListener lUpdateCargo,
                         EventListener lDisplayCustomer,
                         EventListener lDisplayCargo,
                         EventListener lDisplayHazard,
                         EventListener lHandlePersistence,
                         EventListener lHandleCargoPersistence
    ) {
        this.mode = Mode.menu;

        this.consoleEventHandler = new InputEventHandler();
        this.cargoInsertEventHandler = new InputEventHandler<CargoInsertEvent>();
        this.customerInsertEventHandler = new InputEventHandler<CustomerInsertEvent>();
        this.cargoDeletionEventHandler = new InputEventHandler<CargoDeletionEvent>();
        this.customerDeletionEventHandler = new InputEventHandler<CustomerDeletionEvent>();
        this.observerAttachEventHandler = new InputEventHandler<ObserverAttachEvent>();
        this.observerDetachEventHandler = new InputEventHandler<ObserverDetachEvent>();
        this.cargoUpdateEventHandler = new InputEventHandler<CargoUpdateEvent>();
        this.customerDisplayEventHandler = new InputEventHandler<CustomerDisplayEvent>();
        this.cargoDisplayEventHandler = new InputEventHandler<CargoDisplayEvent>();
        this.hazardDisplayEventHandler = new InputEventHandler<HazardDisplayEvent>();
        this.persistenceEventHandler = new InputEventHandler<PersistenceTriggerEvent>();
        this.cargoPersistenceEventHandler = new InputEventHandler<CargoPersistenceEvent>();

        consoleEventHandler.add(lExit);
        cargoInsertEventHandler.add(lInsertNewCargo);
        customerInsertEventHandler.add(lInsertNewCustomer);
        cargoDeletionEventHandler.add(lDeleteCargoAtPosition);
        customerDeletionEventHandler.add(lDeleteCustomerWithName);
        observerAttachEventHandler.add(lAttachObserver);
        observerDetachEventHandler.add(lDetachObserver);
        cargoUpdateEventHandler.add(lUpdateCargo);
        customerDisplayEventHandler.add(lDisplayCustomer);
        cargoDisplayEventHandler.add(lDisplayCargo);
        hazardDisplayEventHandler.add(lDisplayHazard);
        persistenceEventHandler.add(lHandlePersistence);
        cargoPersistenceEventHandler.add(lHandleCargoPersistence);
    }

    public Mode getMode() {
        return this.mode;
    }

    public void start(InputStream is, OutputStream os) {

        this.userInputBR = new BufferedReader(new InputStreamReader(is));
        PrintStream ps = new PrintStream(os);
        System.setErr(ps); //passing the System.err to the print stream used for UI-Interaction, mainly used for testing purpose

        while (true) {
            System.out.println("Current mode: " + ANSI_CYAN + mode + ANSI_RESET);
            System.out.print(MSG_ChoseOption + MSG_listOfCommands + "\n\n> ");
            try {
                String userInput = userInputBR.readLine();
                if (userInput == null) {
                    break;
                }

                if (userInput.isEmpty()) {
                    continue;
                }
                userInput = userInput.toLowerCase().trim();

                switch (userInput) {
                    case (CREATE_MODE):
                        applyMode(Mode.create);
                        handleCreate();
                        this.mode = Mode.menu;
                        break;

                    case (DELETE_MODE):
                        applyMode(Mode.delete);
                        handleDeletion();
                        this.mode = Mode.menu;
                        break;

                    case (READ_MODE):
                        applyMode(Mode.read);
                        handleRead();
                        this.mode = Mode.menu;
                        break;

                    case (UPDATE_MODE):
                        applyMode(Mode.update);
                        handleUpdate();
                        this.mode = Mode.menu;
                        break;

                    case (PERSISTENCE_MODE):
                        applyMode(Mode.persistence);
                        handlePersistence();
                        this.mode = Mode.menu;
                        break;

                    case (CONFIGURATION_MODE):
                        applyMode(Mode.configuration);
                        handleConfig();
                        this.mode = Mode.menu;
                        break;

                    default:
                        if (userInput.matches("exit")) {
                            ConsoleInputEvent e = new ConsoleInputEvent(this, userInput);
                            if (null != this.consoleEventHandler) {
                                consoleEventHandler.handle(e);
                            }
                        }
                        System.out.println(SYMB_FAIL + ANSI_RED + ERR_DefaultSwitch + ANSI_RESET + MSG_listOfCommands);
                        break;
                }
            } catch (NullPointerException | IOException NPExc) {
                NPExc.printStackTrace();
                System.err.println(SYMB_FAIL + "No Input device detected, the application will now shut down.");
                System.exit(-1);
            }
        }
    }

    private void handleCreate() {
        System.out.print(MSG_INSERT_Cargo_XOR_Customer + "\n> ");

        String decision = readUserInput();


        if (decision.toLowerCase().equals("ca")) {
            Cargo cargoToBeAdded = getCargoArguments();
            if (cargoToBeAdded == null) {
                System.err.println(SYMB_FAIL + "Invalid argument(s) - returning to main menu.");
                return;
            }
            CargoInsertEvent e = new CargoInsertEvent(this, cargoToBeAdded);
            if (this.cargoInsertEventHandler != null) {
                cargoInsertEventHandler.handle(e);
            } else {
                System.out.println(SYMB_FAIL + "Cargo item can not be inserted due to uninitialized EventHandler");
            }

            //Customer
        } else if (decision.toLowerCase().equals("cu")) {
            System.out.print("\n" + EXP_INSERT_CUSTOMER_Syntax + "\n>");
            String customerName = readUserInput();
            CustomerInsertEvent e = new CustomerInsertEvent(this, customerName);
            if (null != this.consoleEventHandler) {
                customerInsertEventHandler.handle(e);
            } else {
                System.out.println(SYMB_FAIL + "Customer can not be added to Database due to uninitialized EventHandler");
            }
            //invalid input
        } else {
            System.out.println(SYMB_FAIL + ERR_Cargo_XOR_Customer_fail);
        }
    }

    private Cargo getCargoArguments() {
        //cargotypeflags
        boolean liquidFlag = false;
        boolean mixedFlag = false;
        boolean unitisedFlag = false;

        Customer tempCustomer = null;
        BigDecimal tempBigDecimal = null;
        Duration tempDuration = null;
        HashSet<Hazard> tempHashSet = new HashSet<Hazard>();

        boolean isPressurized = false;
        boolean isFragile = false;

        String specification = null;
        Cargo cargoToBeAdded = null;

        System.out.print("\n" + MSG_Cargo_Insert_Syntax + "\n> ");

        String cargoArguments = null;
        try {
            cargoArguments = userInputBR.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cargoArguments.isEmpty()) {
            return null;
        }

        StringTokenizer st = new StringTokenizer(cargoArguments);
        if (st.countTokens() < 6) {
            System.out.println(SYMB_FAIL + "Not enough arguments - returning to main menu.");
            return null;
        }
        if (st.countTokens() > 7) {
            System.out.println(SYMB_FAIL + "Too many arguments - returning to main menu.");
            return null;
        }

        switch (st.nextToken()) {
            case "LiquidBulkCargo":
                liquidFlag = true;
                break;
            case "MixedCargoLiquidBulkAndUnitised":
                mixedFlag = true;
                break;
            case "UnitisedCargo":
                unitisedFlag = true;
                break;
            default:
                System.err.println(SYMB_FAIL + "Invalid Cargo Type - returning to main menu.");
                return null;
        }

        //Converting user input to specified data types
        try {

            tempCustomer = new CustomerImpl(st.nextToken());

            tempBigDecimal = BigDecimal.valueOf(Double.parseDouble(st.nextToken()));
            if (tempBigDecimal.longValue() < 0) {
                System.out.println(SYMB_FAIL + ANSI_RED + "Value has to be positive." + ANSI_RESET);
                return null;
            }

            tempDuration = Duration.ofSeconds(Long.parseLong(st.nextToken()));
            if (tempDuration.getSeconds() < 0) {
                System.out.println(SYMB_FAIL + ANSI_RED + "Duration has to be positive." + ANSI_RESET);
                return null;
            }

            String[] hazards = st.nextToken().trim().replaceAll(",", " ").toLowerCase().split(" ");

            //should skip single comma entry
            for (String hazard : hazards) {
                hazard.trim();                  //paranoia
                switch (hazard) {
                    case "explosive":
                        tempHashSet.add(Hazard.explosive);
                        break;
                    case "flammable":
                        tempHashSet.add(Hazard.flammable);
                        break;
                    case "radioactive":
                        tempHashSet.add(Hazard.radioactive);
                        break;
                    case "toxic":
                        tempHashSet.add(Hazard.toxic);
                        break;
                    default:
                        System.out.println(SYMB_FAIL + "Invalid Hazard detected [" + ANSI_RED + hazard + ANSI_RESET + "] - Insertion process failed, returning to main menu.");
                        return null;
                }
            }

            specification = st.nextToken().toLowerCase();

            if (liquidFlag) {
                if (specification.matches("y|n")) {
                    if (specification.matches("[y]")) {
                        isPressurized = true;
                    }
                } else {
                    System.out.println(SYMB_FAIL + "couldn't parse input. Insertion process failed, returning to Main Menu.");
                    return null;
                }
                return cargoToBeAdded = new LiquidBulkCargoImpl(tempCustomer, tempBigDecimal, tempDuration, tempHashSet, isPressurized);
            }

            if (mixedFlag) {
                if (specification.matches("y|n")) {
                    if (specification.matches("[y]")) {
                        isPressurized = true;
                    }
                } else {
                    System.out.println(SYMB_FAIL + "couldn't parse input. Insertion process failed, returning to Main Menu.");
                    return null;
                }
                specification = st.nextToken().toLowerCase();

                if (specification.matches("y|n")) {
                    if (specification.matches("[y]")) {
                        isFragile = true;
                    }
                } else {
                    System.out.println(SYMB_FAIL + "couldn't parse input. Insertion process failed, returning to Main Menu.");
                    return null;
                }
                return cargoToBeAdded = new MixedCargoLiquidBulkAndUnitisedImpl(tempCustomer, tempBigDecimal, tempDuration, tempHashSet, isPressurized, isFragile);
            }

            if (unitisedFlag) {
                if (specification.matches("y|n")) {
                    if (specification.matches("[y]")) {
                        isFragile = true;
                    }
                } else {
                    System.out.println(SYMB_FAIL + "couldn't parse input. Insertion process failed, returning to Main Menu.");
                    return null;
                }
                return cargoToBeAdded = new UnitisedCargoImpl(tempCustomer, tempBigDecimal, tempDuration, tempHashSet, isFragile);
            }

        } catch (NumberFormatException nfExc) {
            System.out.println(SYMB_FAIL + ANSI_RED + "Input Number Format Mismatch (Value) - returning to Main Menu." + ANSI_RESET);
        } catch (DateTimeException dtExc) {
            System.out.println(SYMB_FAIL + ANSI_RED + "Input Format Mismatch (Date) - returning to Main Menu." + ANSI_RESET);
        } catch (Exception e) {
            System.out.println(SYMB_FAIL + ANSI_RED + "Something unexpected happened - Insertion process failed, returning to Main Menu." + ANSI_RESET);
        }
        return null;
    }

    private void handleDeletion() {
        System.out.print(MSG_DELETE_Cargo_XOR_Customer + "\n> ");
        String decision = readUserInput();

        //Cargo
        if (decision.toLowerCase().equals("ca")) {
            System.out.print(MSG_DELETE_AskForPositionToBeDeleted + "\n> ");
            int positionToBeDeleted = readUserInput_Int(); //beware that number can still be negative at this point, but should be caught by logic layer

            CargoDeletionEvent e = new CargoDeletionEvent(this, positionToBeDeleted);
            if (this.cargoDeletionEventHandler != null) {
                cargoDeletionEventHandler.handle(e);
            } else {
                System.out.println(SYMB_FAIL + "Cargo item can not be deleted due to uninitialized EventHandler");
            }

            //Customer//
        } else if (decision.toLowerCase().equals("cu")) {
            System.out.print("\n" + EXP_INSERT_CUSTOMER_Syntax + "\n>");
            String nameToBeDeleted = "";
            try {
                nameToBeDeleted = userInputBR.readLine();
                if (!nameToBeDeleted.matches("[A-Z]?[a-z]*")) {
                    System.out.println(SYMB_FAIL + "Invalid input, please refer to the charset [A-Za-z]. Returning to Main Menu now.");
                    return;
                }
                if (nameToBeDeleted.isEmpty()) {
                    System.out.println(SYMB_FAIL + "Empty Input, returning to Main Menu.");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(SYMB_FAIL + e.getLocalizedMessage() + " Returning to Main Menu.");
                return;
            }

            System.out.println(MSG_DELETE_WARNING);

            decision = "";
            try {
                decision = userInputBR.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (decision.matches("y|n")) {
                if (decision.matches("y")) {
                    CustomerDeletionEvent e = new CustomerDeletionEvent(this, nameToBeDeleted);
                    if (null != this.customerDeletionEventHandler) {
                        customerDeletionEventHandler.handle(e);
                    } else {
                        System.out.println(SYMB_FAIL + "Customer can not be removed from Database due to uninitialized EventHandler");
                    }
                } else {
                    System.out.println(SYMB_CANCEL + "Operation was successfully canceled.");
                    return;
                }
            } else {
                System.out.println(SYMB_FAIL + "Invalid Input, returning to Main Menu.");
                return;
            }

            //invalid input
        } else {
            System.out.println(SYMB_FAIL + ERR_Cargo_XOR_Customer_fail);
            return;
        }
    }

    private void handleConfig() {
        System.out.print("Please refer to the following syntax: <<add/remove> <name_of_the_observer>>" + "\n> ");

        String decision = readUserInput();
        StringTokenizer st = new StringTokenizer(decision);

        String operation = null;
        String observerName = null;

        if (st.hasMoreTokens()) {
            operation = st.nextToken().toLowerCase();
            if (st.hasMoreTokens()) {
                observerName = st.nextToken().toLowerCase();
            } else {
                System.out.println(SYMB_FAIL + "Missing argument (ObserverName), returning to Main Menu.");
                return;
            }
        } else {
            System.out.println(SYMB_FAIL + "Missing argument, returning to Main Menu.");
            return;
        }

        switch (operation.toLowerCase()) {
            case ("add"):
                ObserverAttachEvent e = new ObserverAttachEvent(this, observerName);
                if (null != this.observerAttachEventHandler) {
                    observerAttachEventHandler.handle(e);
                } else {
                    System.out.println(SYMB_FAIL + "Observer can not be attached due to uninitialized EventHandler");
                }
                break;
            case ("remove"):
                ObserverDetachEvent f = new ObserverDetachEvent(this, observerName);
                if (null != this.observerDetachEventHandler) {
                    observerDetachEventHandler.handle(f);
                } else {
                    System.out.println(SYMB_FAIL + "Observer can not be detached due to uninitialized EventHandler");
                }
                break;
            default:
                System.out.println(SYMB_FAIL + "Invalid Input, returning to Main Menu.");
        }
    }

    private void handlePersistence() {
        System.out.print("Please specify if you'd like to either <saveJOS>, <loadJOS>, <<save> <CargoPositionToBeSaved>> or <<load> <CargoPositionToBeSaved>>" + "\n> ");
        String position = "";
        int positionToBeValidated = -1;
        StringTokenizer st = new StringTokenizer(readUserInput());

        if (!st.hasMoreTokens()) {
            System.err.println(SYMB_FAIL + "Empty input - returning to main menu.");
            return;
        }

        try{
        switch (st.nextToken()) {
            case ("saveJOS"):
                PersistenceTriggerEvent saveJOS = new PersistenceTriggerEvent(this, "saveJOS", "saveJOS.txt");
                if (this.persistenceEventHandler != null) {
                    persistenceEventHandler.handle(saveJOS);
                } else {
                    System.out.println(SYMB_FAIL + "Serialisation failed due to uninitialized EventHandler");
                }
                break;

            case ("loadJOS"):
                PersistenceTriggerEvent loadJOS = new PersistenceTriggerEvent(this, "loadJOS", "saveJOS.txt");
                if (this.persistenceEventHandler != null) {
                    persistenceEventHandler.handle(loadJOS);
                } else {
                    System.out.println(SYMB_FAIL + "De-serialisation failed due to uninitialized EventHandler");
                }
                break;

            case ("saveJBP"):
                System.out.println(SYMB_FAIL + "JBP functionality is currently under construction, sorry for the inconvenience");
                /*
                PersistenceTriggerEvent saveJBP = new PersistenceTriggerEvent(this, "saveJBP", "saveJBP.txt");
                if (this.persistenceEventHandler != null) {
                    persistenceEventHandler.handle(saveJBP);
                } else {
                    System.out.println(SYMB_FAIL + "Serialisation failed due to uninitialized EventHandler");
                }
                */

                break;

            case ("loadJBP"):
                System.out.println(SYMB_FAIL + "JBP functionality is currently under construction, sorry for the inconvenience");
                /*
                PersistenceTriggerEvent loadJBP = new PersistenceTriggerEvent(this, "loadJBP", "saveJBP.txt");
                if (this.persistenceEventHandler != null) {
                    persistenceEventHandler.handle(loadJBP);
                } else {
                    System.out.println(SYMB_FAIL + "De-serialisation failed due to uninitialized EventHandler");
                }

                 */
                break;

            case ("save"):
                if (!st.hasMoreTokens()) {
                    System.err.println(SYMB_FAIL + "Missing argument - returning to main menu.");
                    return;
                }
                position = st.nextToken().trim();
                try {
                    positionToBeValidated = Integer.parseInt(position);
                } catch (Exception e) {
                    System.err.println(SYMB_FAIL + "An Error occurred whilst trying to convert the position argument - returning to main menu.");
                    return;
                }
                CargoPersistenceEvent saveSingleCargo = new CargoPersistenceEvent(this, "save", positionToBeValidated);
                if (this.cargoPersistenceEventHandler != null) {
                    cargoPersistenceEventHandler.handle(saveSingleCargo);
                } else {
                    System.out.println(SYMB_FAIL + "Serialisation of Cargo at position " + positionToBeValidated + " failed due to uninitialized EventHandler");
                }
                break;

            case ("load"):
                if (!st.hasMoreTokens()) {
                    System.err.println(SYMB_FAIL + "Missing argument - returning to main menu.");
                    return;
                }
                position = st.nextToken().trim();
                try {
                    positionToBeValidated = Integer.parseInt(position);
                } catch (Exception e) {
                    System.err.println(SYMB_FAIL + "An Error occurred whilst trying to convert the position argument - returning to main menu.");
                    return;
                }
                CargoPersistenceEvent loadSingleCargo = new CargoPersistenceEvent(this, "load", positionToBeValidated);
                if (this.cargoPersistenceEventHandler != null) {
                    cargoPersistenceEventHandler.handle(loadSingleCargo);
                    System.out.println(SYMB_NICESU + "Successfully loaded Cargo to position(" + position + ")");
                } else {
                    System.out.println(SYMB_FAIL + "Serialisation of Cargo at position " + positionToBeValidated + " failed due to uninitialized EventHandler");
                }
                break;

            default:
                System.err.println(SYMB_FAIL + "Unable to parse input - returning to main menu.");
                break;
        }
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            System.out.println(SYMB_FAIL + "Due to a persistence related error, the desired operation was cancelled - returning to main menu.");
            return;
        }
    }

    private void handleUpdate() {
        System.out.print("Please specify which cargo should be updated by entering the storage position:" + "\n> ");

        int positionToBeUpdated = readUserInput_Int();
        CargoUpdateEvent e = new CargoUpdateEvent(this, positionToBeUpdated);

        if (null != this.cargoUpdateEventHandler) {
            cargoUpdateEventHandler.handle(e);
        } else {
            System.out.println(SYMB_FAIL + "Customer can not be removed from Database due to uninitialized EventHandler");
        }
    }

    private void handleRead() {
        System.out.print("Please specify what you would like to display:" + "\n> ");

        String input = readUserInput();
        StringTokenizer st = new StringTokenizer(input);

        String type = null;
        String filter = "";

        if (st.hasMoreTokens()) {
            type = st.nextToken().toLowerCase();
        } else {
            System.out.println(SYMB_FAIL + "Missing argument, returning to Main Menu.");
            return;
        }

        switch (type) {
            case ("customer"):
                CustomerDisplayEvent e = new CustomerDisplayEvent(this);

                if (null != this.customerDisplayEventHandler) {
                    customerDisplayEventHandler.handle(e);
                } else {
                    System.out.println(SYMB_FAIL + "Customers can not be displayed due to uninitialized EventHandler");
                }
                break;

            case ("cargo"):
                if (st.hasMoreTokens()) {
                    filter = st.nextToken().toLowerCase();
                }

                CargoDisplayEvent f = new CargoDisplayEvent(this, filter);

                if (null != this.cargoDisplayEventHandler) {
                    cargoDisplayEventHandler.handle(f);
                } else {
                    System.out.println(SYMB_FAIL + "Cargo can not be displayed due to uninitialized EventHandler");
                }
                break;

            case ("hazard"):
                if (st.hasMoreTokens()) {
                    filter = st.nextToken().toLowerCase();
                } else {
                    System.out.println(SYMB_FAIL + "Missing argument ([i,e]), returning to Main Menu.");
                    return;
                }

                HazardDisplayEvent h = new HazardDisplayEvent(this, filter);

                if (null != this.hazardDisplayEventHandler) {
                    hazardDisplayEventHandler.handle(h);
                } else {
                    System.out.println(SYMB_FAIL + "List of hazards can not be displayed due to uninitialized EventHandler");
                }

                break;
            default:
                System.out.println(SYMB_FAIL + "Invalid Input, returning to Main Menu.");
        }
    }

    private void applyMode(Mode newMode) {
        this.mode = newMode;
        System.out.println(SYMB_NICESU + "Mode was switched to: " + ANSI_CYAN + this.mode + ANSI_RESET + "\n");
    }

    private String readUserInput() {
        String userInput = "";
        try {
            userInput = userInputBR.readLine();
            if (userInput == null) {
                userInput = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInput;
    }

    private int readUserInput_Int() {
        String userInput = "";
        int userInputAsInt = -1;

        try {
            userInput = userInputBR.readLine();
            userInputAsInt = Integer.parseInt(userInput);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(SYMB_FAIL + "Invalid Format, please refer to natural numbers only - returning to main menu.");
        }
        return userInputAsInt;
    }
}