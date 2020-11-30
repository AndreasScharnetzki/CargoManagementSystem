package storageContract.cargo;

import storageContract.administration.Customer;
import storageContract.cargo.interfaces.LiquidBulkCargo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class LiquidBulkCargoImpl implements LiquidBulkCargo, Serializable {

    private Customer owner;
    private BigDecimal value;
    private Duration durationOfStorage;
    private Collection<Hazard> hazards = new HashSet<Hazard>();
    private Date lastInspectionDate;

    private boolean pressurized = false;

    private int storagePosition;

    public LiquidBulkCargoImpl(Customer owner, BigDecimal value, Duration durationOfStorage, Collection<Hazard> hazards, boolean pressurized){
        this.owner = owner;
        this.value = value;
        this.durationOfStorage = durationOfStorage;
        this.hazards = hazards;
        this.pressurized = pressurized;

        this.lastInspectionDate = new java.util.Date();
        this.storagePosition = storagePosition;
    }

    public boolean isPressurized() {
        return pressurized;
    }

    public Customer getOwner() {
        return this.owner;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Duration getDurationOfStorage() {
        return this.durationOfStorage;
    }

    public Collection<Hazard> getHazards() {
        return this.hazards;
    }

    public Date getLastInspectionDate() {
        return this.lastInspectionDate;
    }

    public int getStoragePosition() {return storagePosition; }

    public void setStoragePosition(int storagePosition) {
        this.storagePosition = storagePosition;
    }
}
