package storageContract.controller.listener;

import storageContract.cargo.interfaces.Cargo;
import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoDisplayEvent;
import storageContract.logic.BusinessLogicImpl;

public class Listener_DisplayCargo implements EventListener<CargoDisplayEvent> {
    /*
    cargo [[Frachttyp]] Anzeige der Frachtstücke ,ggf. gefiltert nach Typ, mit Lagerposition, Einlagerungsdatum und Datum der letzten Inspektion
     */

    private ControlModel controlModel;

    public Listener_DisplayCargo(ControlModel controlModel) {
        this.controlModel = controlModel;
    }

    @Override
    public void onInputEvent(CargoDisplayEvent event) {
        String filter = event.getFilter().toLowerCase();

        switch (filter) {
            case ("liquidbulkcargo"):
            case ("mixedcargoliquidbulkandunitised"):
            case ("unitisedcargo"):
                System.out.println("Displaying Cargo filtered by type [ " + filter + " ]\n");
                printFilteredCargo(filter);
                break;
            default:
                System.out.println("(No Filter identified - if you intended to do so, please refer to either [liquidbulk, mixed, unitised])\n");
                printCargo();
                break;
        }
    }

    private void printCargo() {
        Cargo[] storehouse = controlModel.getModel().getEntireCargo();
        for (int i = 0; i < storehouse.length; i++) {
            if (storehouse[i] != null) {
                printCargoDetailsAtPosition(i, storehouse);
            }
        }
    }

    private void printFilteredCargo(String filter) {
        Cargo[] storehouse = controlModel.getModel().getEntireCargo();
        for (int i = 0; i < storehouse.length; i++) {
            if (storehouse[i] != null && storehouse[i].
                    getClass().
                    getSimpleName().
                    replace("Impl", "").
                    toLowerCase().
                    matches(filter)) {
                printCargoDetailsAtPosition(i, storehouse);
            }
        }
        System.out.println("\n");
    }

    private void printCargoDetailsAtPosition(int i, Cargo[] storehouse){
        Cargo cargo = storehouse[i];
        System.out.println(
                "Cargo @position: " + i + "\n" +
                "Type: " + cargo.getClass().getSimpleName().replace("Impl", "") + "\n" +
                "Owner: " + cargo.getOwner() + "\n" +
                "Value: " + cargo.getValue() + "\n" +
                "Duration of Storage: " + cargo.getDurationOfStorage() + "\n" +
                "Hazards: " + cargo.getHazards() + "\n" +
                "Date of storage: " + controlModel.getModel().getDateOfStorage()[i]  + "\n" +
                "Last Date of Inspection: " + cargo.getLastInspectionDate() + "\n\n" +

                "================================================================================\n"
        );
    }
}
