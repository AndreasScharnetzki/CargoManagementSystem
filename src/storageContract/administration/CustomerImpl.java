package storageContract.administration;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

import static storageContract.controller.CommandListAndMessages.SYMB_FAIL;

public class CustomerImpl implements Customer, Serializable {

    private String name;
    private BigDecimal maxValue = BigDecimal.ZERO;
    private Duration maxDurationOfStorage =Duration.ZERO;

    public CustomerImpl(String name){
        if(name == null){throw new IllegalArgumentException(SYMB_FAIL + "Creating a new customer failed.");}
        if (validateSyntax(name)) {
            this.name = name;
        }else{
            throw new IllegalArgumentException("Creating a new customer failed.");
        }
    }

    public String getName() {
        return this.name;
    }

    public String toString(){
        return this.name;
    }

    public BigDecimal getMaxValue() {
        return this.maxValue;
    }

    public Duration getMaxDurationOfStorage() {
        return this.maxDurationOfStorage;
    }

    private boolean validateSyntax(String payload) {
        if (payload.trim().isEmpty() || payload.equals(System.lineSeparator())) {
            throw new IllegalArgumentException("ERROR: empty input, to create an entry, please use at last one character of the charset [A-Za-z]");
        }else if(payload.matches("exit")){
            System.err.println(SYMB_FAIL + "Invalid Name, due to this word being already assigned to a command. Returning to Main Menu now.");
            return false;
        }if(payload.trim().length()>30){
            System.err.println(SYMB_FAIL + "the name field is limited to 30 characters. Returning to Main Menu now.");
            return false;
        }
        if (payload.trim().matches("[A-Z]?[a-z]*")) {
            return true;
        } else {
            throw new IllegalArgumentException("ERROR: invalid input, please refer to the charset [A-Za-z]");
        }
    }
}
