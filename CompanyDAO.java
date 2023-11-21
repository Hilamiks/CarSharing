package carsharing;

public interface CompanyDAO {
    //create
    void createTable();
    //read
    void printTable();
    //update
    void newCompany(String name);
    //delete
    void clear();
}
