package com.example.utselibrary.Model;

public class Authors {
    public String firstName, surname, fullName;

    public Authors() {
        // Empty constructor
    }

    public Authors(String firstName, String surname, String fullName) {
        this.firstName = firstName;
        this.surname = surname;
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


}
