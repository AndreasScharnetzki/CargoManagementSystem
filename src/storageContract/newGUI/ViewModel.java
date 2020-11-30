package storageContract.newGUI;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import storageContract.administration.Customer;
import storageContract.administration.CustomerImpl;
import storageContract.cargo.Hazard;
import storageContract.cargo.LiquidBulkCargoImpl;
import storageContract.cargo.MixedCargoLiquidBulkAndUnitisedImpl;
import storageContract.cargo.UnitisedCargoImpl;
import storageContract.cargo.interfaces.Cargo;
import storageContract.logic.BusinessLogicImpl;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

//source: https://code.makery.ch/library/javafx-tutorial/part2/
public class ViewModel {

    @FXML
    private TableView<Cargo> cargoTable;

    @FXML
    private TableColumn<Integer, String> cargoPosition;

    @FXML
    private ChoiceBox cargoTypeChoiceBox;

    @FXML
    private javafx.scene.control.TextField ownerTextField;
    @FXML
    private javafx.scene.control.TextField valueTextField;
    @FXML
    private javafx.scene.control.TextField durationOfStorageTextField;
    @FXML
    private javafx.scene.control.TextField positionToBeDeletedTextField;


    //@FXML
    //private javafx.scene.control.TextField lastInspectionDateTextField;

    @FXML
    private CheckBox flammableCheckBox;
    @FXML
    private CheckBox toxicCheckBox;
    @FXML
    private CheckBox explosiveCheckBox;
    @FXML
    private CheckBox radioactiveCheckBox;

    @FXML
    private CheckBox pressurizedCheckBox;
    @FXML
    private CheckBox fragileCheckBox;

    private BusinessLogicImpl model;

    private StringProperty ownerName_Property = new SimpleStringProperty();
    private StringProperty value_Property = new SimpleStringProperty();
    private StringProperty durationOfStorage_Property = new SimpleStringProperty();
    private StringProperty hazards_Property = new SimpleStringProperty();
    private StringProperty lastInspectionDate_Property = new SimpleStringProperty();

    //TODO READ ABOUT THAT SHIT
    private ListProperty<Cargo> cargoListProperty = new SimpleListProperty<>();

    public StringProperty OwnerName_Property() {return ownerName_Property;}
    public void setOwnerName_Property(String name) {this.ownerName_Property.set(name);}
    public String getOwnerName_Property() {return this.ownerName_Property.get();}

    public StringProperty value_Property() {return value_Property;}
    public void setValue_Property(String value) {this.value_Property.set(value);}
    public String getValue_Property() {return this.value_Property.get();}

    public StringProperty durationOfStorage_Property() {return durationOfStorage_Property;}
    public void setDurationOfStorage_Property(String duration) {this.durationOfStorage_Property.set(duration);}
    public String getDurationOfStorage_Property() {return this.durationOfStorage_Property.get();}

    public StringProperty hazards_Property() {return hazards_Property;}
    public void setHazards_Property(String hazards) {this.hazards_Property.set(hazards);}
    public String getHazards_Property() {return this.hazards_Property.get();}

    public StringProperty lastInspectionDate_Property() {return lastInspectionDate_Property;}
    public void setLastInspectionDate_Property(String date) {this.lastInspectionDate_Property.set(date);}
    public String getLastInspectionDate_Property() {return this.lastInspectionDate_Property.get();}

    public ListProperty cargoList_Property(){return cargoListProperty;}
    public void setCargoListProperty(ObservableList<Cargo> cargoList) {this.cargoListProperty.set(cargoList);}
    public ObservableList<Cargo> getCargoListProperty() {return this.cargoListProperty.get();}

    public ViewModel() throws Exception{
        this.model = new BusinessLogicImpl(10);
        updateProperty();
    }

    @FXML
    private void initialize(){
        this.cargoTable.itemsProperty().bindBidirectional(cargoListProperty);
    }

    private void addNewUnitisedCargo() {
        if(model.isKnownCustomer( ownerTextField.getText())){
            Collection<Hazard> hazards = new HashSet<>();
            hazards = getHazardsFromGUI();
            model.addToStorehouse(new UnitisedCargoImpl(

                    model.getCustomer(ownerTextField.getText()),
                    BigDecimal.valueOf(Double.parseDouble(valueTextField.getText())),
                    Duration.ofMinutes(Long.parseLong(durationOfStorageTextField.getText())),
                    hazards,
                    this.fragileCheckBox.isSelected()
            ));
        }else{
            model.addToCustomerDatabase(ownerTextField.getText());
            addNewUnitisedCargo();
        }
    }

    private void addNewMixedCargo() {
        if(model.isKnownCustomer( ownerTextField.getText())){
            Collection<Hazard> hazards = new HashSet<>();
            hazards = getHazardsFromGUI();
            model.addToStorehouse(new MixedCargoLiquidBulkAndUnitisedImpl(

                    model.getCustomer(ownerTextField.getText()),
                    BigDecimal.valueOf(Double.parseDouble(valueTextField.getText())),
                    Duration.ofMinutes(Long.parseLong(durationOfStorageTextField.getText())),
                    hazards,
                    this.pressurizedCheckBox.isSelected(),
                    this.fragileCheckBox.isSelected()
            ));
        }else{
            model.addToCustomerDatabase(ownerTextField.getText());
            addNewMixedCargo();
        }
    }

    private void addNewLiquidBulkCargo() {
        if(model.isKnownCustomer( ownerTextField.getText())){
            Collection<Hazard> hazards = new HashSet<>();
            hazards = getHazardsFromGUI();
            model.addToStorehouse(new LiquidBulkCargoImpl(

                    model.getCustomer(ownerTextField.getText()),
                    BigDecimal.valueOf(Double.parseDouble(valueTextField.getText())),
                    Duration.ofMinutes(Long.parseLong(durationOfStorageTextField.getText())),
                    hazards,
                    this.pressurizedCheckBox.isSelected()
            ));
        }else{
            model.addToCustomerDatabase(ownerTextField.getText());
            addNewLiquidBulkCargo();
        }
    }

    private void updateProperty(){
        List<Cargo> list = new ArrayList<Cargo>();
        Cargo [] contentCopy = model.getEntireCargo();
        for (int i = 0; i < contentCopy.length ; i++) {
            if(contentCopy[i] != null){
                list.add(contentCopy[i]);
            }
        }

        this.cargoListProperty.set(FXCollections.observableList(list));
    }
    private Collection<Hazard> getHazardsFromGUI() {
        Collection<Hazard> hazards = new HashSet<>();
        if(explosiveCheckBox.isSelected()){
            hazards.add(Hazard.explosive);
        }
        if(flammableCheckBox.isSelected()){
            hazards.add(Hazard.flammable);
        }
        if(radioactiveCheckBox.isSelected()){
            hazards.add(Hazard.radioactive);
        }
        if(toxicCheckBox.isSelected()){
            hazards.add(Hazard.toxic);
        }
        return hazards;
    }

    public void addButtonClick(javafx.event.ActionEvent actionEvent) {
        switch (this.cargoTypeChoiceBox.getSelectionModel().getSelectedItem().toString()){
            case("Liquid Cargo"):
                addNewLiquidBulkCargo();
                break;
            case("Mixed Cargo"):
                addNewMixedCargo();
                break;
            case("Unitised Cargo"):
                addNewUnitisedCargo();
                break;
        }
            updateProperty();
    }

    //TODO Continue here
    public void deleteButtonClick(javafx.event.ActionEvent actionEvent) {
        int positionToBeDeleted = -1;
        positionToBeDeletedTextField.setStyle("-fx-text-fill: black;");
        try{
            positionToBeDeleted = Integer.parseInt(positionToBeDeletedTextField.getText());
        } catch (NumberFormatException nfE){
            nfE.printStackTrace();
        }
        //FIXME: not really secure yet...
        if(model.isValidScope(positionToBeDeleted)){
            model.deleteStockAtPosition(positionToBeDeleted);
            updateProperty();
        }else{
            positionToBeDeletedTextField.setStyle("-fx-text-fill: red;");
        }
    }
}

