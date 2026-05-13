package data;

public class Representative {

    private static int nextCode = 1000;

    private int employeeCode;
    private String firstName;
    private String id;

    public Representative() {
    }

    public Representative(String firstName, String id) {
        this.employeeCode = nextCode++;
        this.firstName = firstName;
        this.id = id;
    }

    public int getEmployeeCode() {
        return employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public void setEmployeeCode(int employeeCode) {
        this.employeeCode = employeeCode;
    }

    @Override
    public String toString() {
        return "Representative{" +
                "employeeCode=" + employeeCode +
                ", firstName='" + firstName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
