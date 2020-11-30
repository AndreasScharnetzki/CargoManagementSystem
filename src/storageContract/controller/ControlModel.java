package storageContract.controller;

import storageContract.logic.BusinessLogicImpl;

public class ControlModel {
    BusinessLogicImpl model;

    public ControlModel(BusinessLogicImpl model) { this.model = model; }

    public BusinessLogicImpl getModel() {
        return model;
    }

    public void setModel(BusinessLogicImpl model) {
        this.model = model;
    }
}
