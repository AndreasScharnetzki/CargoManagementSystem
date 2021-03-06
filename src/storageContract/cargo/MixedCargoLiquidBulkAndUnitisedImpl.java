package storageContract.cargo;

import storageContract.administration.Customer;
import storageContract.cargo.interfaces.MixedCargoLiquidBulkAndUnitised;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

public class MixedCargoLiquidBulkAndUnitisedImpl implements MixedCargoLiquidBulkAndUnitised, Serializable {

    private Customer owner;
    private BigDecimal value;
    private Duration durationOfStorage;
    private Collection<Hazard> hazards;
    private Date lastInspectionDate;

    private boolean pressurized = false;
    private boolean fragile = false;

    private int storagePosition;

    public MixedCargoLiquidBulkAndUnitisedImpl(Customer owner, BigDecimal value, Duration durationOfStorage,Collection<Hazard> hazards, boolean pressurized, boolean fragile) {
        this.owner = owner;
        this.value = value;
        this.durationOfStorage = durationOfStorage;
        this.hazards = hazards;
        this.pressurized = pressurized;
        this.fragile = fragile;

        this.lastInspectionDate = new java.util.Date();
        this.storagePosition = storagePosition;
    }

    public boolean isPressurized() {
        return pressurized;
    }

    public boolean isFragile() {
        return fragile;
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
