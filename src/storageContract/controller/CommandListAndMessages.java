package storageContract.controller;

public interface CommandListAndMessages {
    //colours (source: https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println)
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_CYAN = "\u001B[36m";
    String ANSI_GREEN = "\033[0;32m";
    String ANSI_PURPLE = "\u001B[35m";


    //command enums
    String CREATE_MODE = "c";
    String DELETE_MODE = "d";
    String READ_MODE = "r";
    String UPDATE_MODE = "u";
    String PERSISTENCE_MODE = "p";
    String CONFIGURATION_MODE = "config";

    //UIMessages & Errors
    String MSG_displayOnStart = ANSI_YELLOW + "C A R G O   M A N A G E M E N T   S Y S T E M\n\n" + ANSI_RESET;

    String MSG_AskForTCPorUDP = "Please specify if you want to use the application via " + ANSI_GREEN + "[TCP]" + ANSI_RESET + "/" + ANSI_GREEN +  "[UDP]" + ANSI_RESET + ": ";
    String MSG_TCPorUDP_success = "D U M M Y: The application will now run as client: D U M M Y";
    String ERR_TCPorUDP_fail = "Couldn't parse input, the application will run in offline Mode.";

    String MSG_AskForInitCapacity = "Please specify storehouse capacity (value has to fit into the interval [1, " + Integer.MAX_VALUE + "]): ";
    String ERR_InitCapacity_fail = "ERROR: invalid input - the application will now start with a storehouse of default size(10)";
    String MSG_InitCapacity_success = "A new Storehouse was successfully initialized.";

    String MSG_ChoseOption = "Please select from these options:\n\n";

    //insert mode
    String MSG_INSERT_Cargo_XOR_Customer = "What would you like to insert " + ANSI_GREEN + "[ca]" + ANSI_RESET + "rgo or " + ANSI_GREEN + "[cu]" + ANSI_RESET + "stomer ?";
    String ERR_Cargo_XOR_Customer_fail = "invalid input, returning to main menu.";

    String MSG_Cargo_Insert_Syntax = "For adding a Cargo please follow this syntax:\n" + ANSI_GREEN +
            "[CargoType] [CustomerName] [Value] [DurationOfStorageInSeconds] [CommaSeparatedHazards*] [CargoIsFragile(y/n)|CargoIsPressurized(y/n)]" + ANSI_RESET +
            "\n\n*Please note that in case multiple hazards shall be stored the listing must" +ANSI_RED+ " not " + ANSI_RESET+ "contain any whitespaces";
    //Deletion mode
    String MSG_DELETE_Cargo_XOR_Customer = "What would you like to delete " + ANSI_GREEN + "[ca]" + ANSI_RESET + "rgo or " + ANSI_GREEN + "[cu]" + ANSI_RESET + "stomer ?";
    String MSG_DELETE_AskForPositionToBeDeleted = "Please enter the position to be deleted:";

    String MSG_DELETE_WARNING = "Please be aware that this operation will also delete all Cargo associated with that name. Do you wish to proceed? " +  ANSI_GREEN + "[y]" + ANSI_RESET + "/" + ANSI_GREEN + "[n]" + ANSI_RESET + " ?";

    //listing available commands
    String MSG_listOfCommands =
            ANSI_GREEN + "[c]" + ANSI_RESET + "reate\n" +
                    ANSI_GREEN + "[d]" + ANSI_RESET + "elete\n" +
                    ANSI_GREEN + "[r]" + ANSI_RESET + "ead\n" +
                    ANSI_GREEN + "[u]" + ANSI_RESET + "pdate\n" +
                    ANSI_GREEN + "[p]" + ANSI_RESET + "ersistence\n" +
                    ANSI_GREEN + "[config]" + ANSI_RESET + "uration\n" +
                    ANSI_GREEN + "[exit]" + ANSI_RESET;

    //Explanations of Options
    String EXP_INSERT_Cargo_SYNTAX = "To add Cargo please follow this syntax:\n\n" +
            ANSI_GREEN +"[CargoType] [CustomerName] [Value] [StorageTime in seconds] " +
            "[comma separated Hazards(single comma for none)] [specifications]" + ANSI_RESET;

    String MSG_isPressurized = "Is the Cargo pressurized " +  ANSI_GREEN + "[y]" + ANSI_RESET + "/" + ANSI_GREEN + "[n]" + ANSI_RESET + " ?";
    String MSG_isFragile = "Is the Cargo fragile " +  ANSI_GREEN + "[y]" + ANSI_RESET + "/" + ANSI_GREEN + "[n]" + ANSI_RESET + " ?";

    String EXP_INSERT_CUSTOMER_Syntax = "Please enter customer name: ";

    String ERR_DefaultSwitch = "Couldn't parse input, please refer to the options shown below.\n\n";

    //Symbols
    String SYMB_NICESU = ANSI_GREEN + "✔"+  ANSI_RESET + ": ";
    String SYMB_FAIL = ANSI_RED + "✖" + ANSI_RESET+ ": ";
    String SYMB_CANCEL = ANSI_YELLOW + "↺" + ANSI_RESET+ ": ";
    String SYMB_OBSERVER_ICON = ANSI_YELLOW + "⚠" + ANSI_RESET+ ": ";
}
