package carsharing;

public interface CarSharingDAO {
    //create
    void createTable();
    //read
    boolean printTable();
    //update
    void newCompany(String name);
    //delete
    void clear();
}
