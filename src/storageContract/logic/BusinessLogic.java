package storageContract.logic;

import storageContract.cargo.interfaces.Cargo;

public interface BusinessLogic {

    /**
     /**use this method to create a new entry for storehouse Database
     * NOTE: cargo can only be added if customer is already registered at Customer-Database
     * NOTE: entries have to follow this syntax:
     * <<CargoType> <CustomerName> <Value> <StorageDurationInSeconds> <hazards*> (<additionalSpecifications**>)>
     *
     * *multiple entries have to separated by comma; if no entry is necessary at this section use ','
     * ** additional specifications can be made based on CargoType
     *
     * @param cargoToBeAdded = an item to be stored in storehouse
     * @return true if operation was performed successfully, else false
     * @throws IllegalArgumentException due to missing or invalid argument/s
     */
    boolean addToStorehouse(Cargo cargoToBeAdded) throws IllegalArgumentException;

    /**use this method to add a new entry to customer database
     * NOTE: duplicate entries aren't allowed
     *
     * @param userInput = the name of the customer to be added to DAtabase
     * @return [true] in case of successful performed operation , [false] due to failure
     * @throws IllegalArgumentException = argument already exists in Database
     */
    boolean addToCustomerDatabase(String userInput) throws IllegalArgumentException;

    /**Use this method to delete specified entry from customer database
     * NOTE:  this will delete items in storehouse associated to that name on cascade
     * @param customerEntryToBeDeleted = name of the customer you want to remove from database
     * @return = [true] if deletion was successful, [false] if customer was known to database but couldn't be removed
     * @throws IllegalArgumentException = if customer wasn't registered in Database
     */
    boolean deleteCustomer(String customerEntryToBeDeleted) throws IllegalArgumentException;

    /**use this method to delete the item at the specified position
     *
     * @param validPositionToBeDeleted = valid index of an item to be removed from storehouse
     * @throws IllegalArgumentException = n.a.n., out of scope, empty position
     */
    void deleteStockAtPosition(int validPositionToBeDeleted) throws IllegalArgumentException;

    /**use this method to set the date for inspection pf specified cargo to current time
     *
     * @param positionToBeUpdated = index of the item to be updatd
     * @throws IllegalArgumentException = n.a.n., out of scope, empty position
     */
    void updateDateOfInspection(int positionToBeUpdated) throws Exception;;
}
