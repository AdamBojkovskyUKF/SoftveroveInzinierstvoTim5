package SoftveroveInzinierstvoTim5.RestAPI.dto;


public class PersonDTO {
    private int id_person;
    private String name;
    private String surname;
    private String address;
    private String phone_number;
    private String email;

    public int getId_person() {
        return this.id_person;
    }

    public void setId_person(int id_person) {
        this.id_person = id_person;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return this.phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
