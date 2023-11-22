package carsharing;

public interface CarSharingCustomersDAO {
    boolean printCustomers();
    void addCustomer(String newCustomerName);
    void rentCar(int customerID, int carID);
    void checkRentalStatus(int customerID);
    void returnCar(int customerID);
}
