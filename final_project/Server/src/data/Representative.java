package data;

import java.io.Serializable;

public class Representative implements Serializable
{
    private int employeeCode;
    private String firstName;
    private String id;


    public Representative()
    {
    }

    public Representative(String firstName, String id)
    {
        this.firstName = firstName;
        this.id = id;
    }

    public int getEmployeeCode()
    {
        return employeeCode;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getId()
    {
        return id;
    }

    public void setEmployeeCode(int employeeCode)
    {
        this.employeeCode = employeeCode;
    }

    @Override
    public String toString()
    {
        return "Representative{" +
                "employeeCode=" + employeeCode +
                ", firstName='" + firstName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}