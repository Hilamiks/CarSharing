package carsharing;

public interface CarSharingCarsDAO {
    //create
    void createCarTable();

    //read
    void printTable(int compID);

    //update
    void newCar(String name, int compID);

    //delete
    void clear();
}
