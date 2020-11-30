package storageContract.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.controller.listener.*;
import storageContract.controller.observer.CapacityObserver;
import storageContract.controller.observer.HazardObserver;
import storageContract.logic.BusinessLogicImpl;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static storageContract.controller.CommandListAndMessages.MSG_listOfCommands;

/************************************************************************************************************************
 *                                                                                                                      *
 *      All tests have differences only in line separators - depending on your IDE settings tests might not pass!       *
 *                                                                                                                      *
 ************************************************************************************************************************/


class ConsoleReaderTest {
    private ConsoleReader cli;
    private BusinessLogicImpl bl;
    private ControlModel controlModel;
    private LiquidBulkCargoImpl sampleCargo = new LiquidBulkCargoImpl(new CustomerImpl("Jim"), BigDecimal.ONE, Duration.ofSeconds(1), new HashSet<Hazard>(), true);

    private final PrintStream standardOut = System.out;
    private final InputStream standardIn = System.in;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private final String successfullInit =
            "\u001B[0;32m✔\u001B[0m: Successfully attached new CapacityObserver\n" +
            "\u001B[0;32m✔\u001B[0m: Successfully attached new HazardObserver\n" +
            "Current mode: \u001B[36mmenu\u001B[0m\n" +
            "Please select from these options:\n"+ System.lineSeparator()+
            MSG_listOfCommands+
            System.lineSeparator()+
            System.lineSeparator();

    private final String backToMainMenu =
            System.lineSeparator()+
            "Current mode: \u001B[36mmenu\u001B[0m\n" +
            "Please select from these options:" +
            System.lineSeparator()+
            System.lineSeparator()+
            MSG_listOfCommands+
            System.lineSeparator()+
            System.lineSeparator()+"> ";


    //src: https://www.baeldung.com/java-testing-system-out-println
    @BeforeEach
    public void setUp() {

        System.setOut(new PrintStream(outputStreamCaptor));
        bl = new BusinessLogicImpl(10);
        controlModel = new ControlModel(bl);
        CapacityObserver capacityObserver = new CapacityObserver(bl);
        HazardObserver hazardObserver = new HazardObserver(bl);

        bl.attach(capacityObserver);
        bl.attach(hazardObserver);

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

        cli = new ConsoleReader(
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
    }

    @AfterEach
    public void tearDown() {
        System.setIn(standardIn);
        System.setOut(standardOut);
        outputStreamCaptor.reset();

        try {
            Files.deleteIfExists(Paths.get("saveJOS.txt"));
            Files.deleteIfExists(Paths.get("singleSerialisedCargo.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getMode() {assertEquals(Mode.menu, cli.getMode());}

    //MODE SWITCHES ====================================================================================================

    @Test
    void getMode_enter_insert_mode() {
        //https://bugsdb.com/_en/debug/09bdbc2d248d31d6785ba772ea8689cb
        ByteArrayInputStream simulatedUserInput = new ByteArrayInputStream(("c\nexit").getBytes());
        System.setIn(simulatedUserInput);

        cli.start(System.in, System.out);

        assertEquals(
                successfullInit+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mcreate\u001B[0m"+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "What would you like to insert \u001B[0;32m[ca]\u001B[0mrgo or \u001B[0;32m[cu]\u001B[0mstomer ?\n" +
                        "> \u001B[31m✖\u001B[0m: invalid input, returning to main menu." +
                        backToMainMenu

                , outputStreamCaptor.toString());
    }

    @Test
    void getMode_enter_deletion_mode() {
        //https://bugsdb.com/_en/debug/09bdbc2d248d31d6785ba772ea8689cb
        ByteArrayInputStream simulatedUserInput = new ByteArrayInputStream(("d\nexit").getBytes());
        System.setIn(simulatedUserInput);

        cli.start(System.in, System.out);

        assertEquals(
                successfullInit+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mdelete\u001B[0m"+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "What would you like to delete \u001B[0;32m[ca]\u001B[0mrgo or \u001B[0;32m[cu]\u001B[0mstomer ?\n" +
                        "> \u001B[31m✖\u001B[0m: invalid input, returning to main menu." +
                        backToMainMenu

                , outputStreamCaptor.toString());
    }

    @Test
    void getMode_enter_display_mode() {
        //https://bugsdb.com/_en/debug/09bdbc2d248d31d6785ba772ea8689cb
        ByteArrayInputStream simulatedUserInput = new ByteArrayInputStream(("r\nexit").getBytes());
        System.setIn(simulatedUserInput);

        cli.start(System.in, System.out);

        assertEquals(
                successfullInit+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mread\u001B[0m"+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "Please specify what you would like to display:\n" +
                        "> \u001B[31m✖\u001B[0m: Invalid Input, returning to Main Menu."+
                        backToMainMenu

                , outputStreamCaptor.toString());
    }

    @Test
    void getMode_enter_update_mode() {
        //https://bugsdb.com/_en/debug/09bdbc2d248d31d6785ba772ea8689cb
        ByteArrayInputStream simulatedUserInput = new ByteArrayInputStream(("u\nexit").getBytes());
        System.setIn(simulatedUserInput);

        cli.start(System.in, System.out);

        assertEquals(
                successfullInit+

                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mupdate\u001B[0m"+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "Please specify which cargo should be updated by entering the storage position:\n" +
                        "> \u001B[31m✖\u001B[0m: Invalid Format, please refer to natural numbers only - returning to main menu.\n" +
                        "\u001B[31m✖\u001B[0m: The position you typed in is out of range, returning to main menu"+
                        backToMainMenu

                , outputStreamCaptor.toString());
    }

    @Test
    void getMode_enter_persistence_mode() {
        //https://bugsdb.com/_en/debug/09bdbc2d248d31d6785ba772ea8689cb
        ByteArrayInputStream simulatedUserInput = new ByteArrayInputStream(("p\nexit").getBytes());
        System.setIn(simulatedUserInput);

        cli.start(System.in, System.out);

        assertEquals(
                successfullInit+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mpersistence\u001B[0m"+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "Please specify if you'd like to either <saveJOS>, <loadJOS>, <<save> <CargoPositionToBeSaved>> or <<load> <CargoPositionToBeSaved>>\n" +
                        "> \u001B[31m✖\u001B[0m: Unable to parse input - returning to main menu."+
                        backToMainMenu

                , outputStreamCaptor.toString());
    }

    @Test
    void getMode_enter_config_mode() {
        //https://bugsdb.com/_en/debug/09bdbc2d248d31d6785ba772ea8689cb
        ByteArrayInputStream simulatedUserInput = new ByteArrayInputStream(("config\nexit").getBytes());
        System.setIn(simulatedUserInput);

        cli.start(System.in, System.out);

        assertEquals(
                successfullInit+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mconfiguration\u001B[0m"+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "Please refer to the following syntax: <<add/remove> <name_of_the_observer>>\n" +
                        "> \u001B[31m✖\u001B[0m: Missing argument (ObserverName), returning to Main Menu."+
                        backToMainMenu

                , outputStreamCaptor.toString());
    }

    @Test
    void start() {
        //https://bugsdb.com/_en/debug/09bdbc2d248d31d6785ba772ea8689cb
        String newLine = System.lineSeparator();
        ByteArrayInputStream simulatedUserInput = new ByteArrayInputStream((newLine).getBytes());
        System.setIn(simulatedUserInput);

        cli.start(System.in, System.out);

        assertEquals(
                successfullInit+
                        System.lineSeparator()+
                        MSG_listOfCommands+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "> Current mode: \u001B[36mmenu\u001B[0m\n" +
                        "Please select from these options:\n" +
                        System.lineSeparator()+
                        MSG_listOfCommands+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "> "

                , outputStreamCaptor.toString());
    }

    //complex event chain testing

    @Test
    void complexEventChain() {
        //https://bugsdb.com/_en/debug/09bdbc2d248d31d6785ba772ea8689cb
        ByteArrayInputStream simulatedUserInput = new ByteArrayInputStream((
                "c\n" +                                     //change from menu to create mode
                        "cu\n" +                            //confirm choice on inserting new customer
                        "Jenny\n" +                         //entering new customers name
                        "c\n" +                             //change from menu to create mode
                        "ca\n" +                            //confirm choice on inserting new cargo
                        "UnitisedCargo Jenny 3345.3 360 n\n" +      //passing cargo parameters
                        "r\n" +                                     //change from main menu to display mode
                        "customer\n" +                              //confirm choice on displaying customers and number of associated cargo
                        "r\n" +                                     //change from main menu to display mode
                        "cargo\n" +                                 //confirm choice on displaying all cargo currently stored at storehouse
                        "r\n" +                                     //change from main menu to display mode
                        "hazard e\n" +                              //confirm choice on displaying all hazards that are currently not contained in storehouse
                        "d\n" +                                     //change to deletion mode
                        "cu\n" +                                    //confirm choice on deleting a customer
                        "Jenny\n" +                                 //entering customers name to be deleted
                        "y\n"                                       //confirm choice on cascading deletion

        ).getBytes());

        System.setIn(simulatedUserInput);

        cli.start(System.in, System.out);

        assertEquals(
                successfullInit+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mcreate\u001B[0m"+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "What would you like to insert \u001B[0;32m[ca]\u001B[0mrgo or \u001B[0;32m[cu]\u001B[0mstomer ?\n"+
                        "> \n" +
                        "Please enter customer name: \n" +
                        ">\u001B[0;32m✔\u001B[0m: new customer \u001B[0;32mJenny\u001B[0m was added to database added\n" +
                        "Current mode: \u001B[36mmenu\u001B[0m\n" +
                        "Please select from these options:\n" +
                        System.lineSeparator()+
                        MSG_listOfCommands+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mcreate\u001B[0m\n" +
                        "\n" +
                        "What would you like to insert \u001B[0;32m[ca]\u001B[0mrgo or \u001B[0;32m[cu]\u001B[0mstomer ?\n" +
                        "> \n" +
                        "For adding a Cargo please follow this syntax:\n" +
                        "\u001B[0;32m[CargoType] [CustomerName] [Value] [DurationOfStorageInSeconds] [CommaSeparatedHazards*] [CargoIsFragile(y/n)|CargoIsPressurized(y/n)]\u001B[0m\n" +
                        "\n" +
                        "*Please note that in case multiple hazards shall be stored the listing must\u001B[31m not \u001B[0mcontain any whitespaces\n" +
                        "> \u001B[31m✖\u001B[0m: Not enough arguments - returning to main menu.\n" +
                        "\u001B[31m✖\u001B[0m: Invalid argument(s) - returning to main menu.\n" +
                        "Current mode: \u001B[36mmenu\u001B[0m\n" +
                        "Please select from these options:\n" +
                        System.lineSeparator()+
                        MSG_listOfCommands+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mread\u001B[0m\n" +
                        "\n" +
                        "Please specify what you would like to display:\n" +
                        "> CustomerName        |        Number of associated Cargo\n" +
                        "\n" +
                        "Jenny       |       0\n" +
                        "Current mode: \u001B[36mmenu\u001B[0m\n" +
                        "Please select from these options:\n" +
                        System.lineSeparator()+
                        MSG_listOfCommands+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mread\u001B[0m\n" +
                        "\n" +
                        "Please specify what you would like to display:\n" +
                        "> (No Filter identified - if you intended to do so, please refer to either [liquidbulk, mixed, unitised])\n" +
                        "\n" +
                        "Current mode: \u001B[36mmenu\u001B[0m\n" +
                        "Please select from these options:\n" +
                        System.lineSeparator()+
                        MSG_listOfCommands+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mread\u001B[0m\n" +
                        "\n" +
                        "Please specify what you would like to display:\n" +
                        "> The storehouse currently doesn't holds cargo labeled as:\n" +
                        "\n" +
                        "[explosive, flammable, toxic, radioactive]\n" +
                        "Current mode: \u001B[36mmenu\u001B[0m\n" +
                        "Please select from these options:\n" +
                        System.lineSeparator()+
                        MSG_listOfCommands+
                        System.lineSeparator()+
                        System.lineSeparator()+
                        "> \u001B[0;32m✔\u001B[0m: Mode was switched to: \u001B[36mdelete\u001B[0m\n" +
                        "\n" +
                        "What would you like to delete \u001B[0;32m[ca]\u001B[0mrgo or \u001B[0;32m[cu]\u001B[0mstomer ?\n" +
                        "> \n" +
                        "Please enter customer name: \n" +
                        ">Please be aware that this operation will also delete all Cargo associated with that name. Do you wish to proceed? \u001B[0;32m[y]\u001B[0m/\u001B[0;32m[n]\u001B[0m ?\n" +
                        "\u001B[0;32m✔\u001B[0m: Customer with name \u001B[0;32mJenny\u001B[0m was successfully removed from Database."+
                        backToMainMenu

                , outputStreamCaptor.toString());
    }

}