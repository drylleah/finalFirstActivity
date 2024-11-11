package com.infoman2.model;

public class Student {

    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber; // Keep as String
    private String email;
    private String gender;

    /**
     *
     * @param id
     * @param firstName
     * @param middleName
     * @param lastName
     * @param phoneNumber
     * @param email
     * @param gender
     */
    public Student(int id, String firstName, String middleName, String lastName, String phoneNumber, String email, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber; // Keep as String
        this.email = email;
        this.gender = gender;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() { // Change return type to String
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) { // Change parameter type to String
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
