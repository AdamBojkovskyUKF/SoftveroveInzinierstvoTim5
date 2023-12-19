package SoftveroveInzinierstvoTim5.RestAPI.dto;

public class CompanyDTO {
 private int id_company;
 private int representative_id_person;
 private String name;
 private String address;

    public int getId_company() {
        return this.id_company;
    }

    public void setId_company(int id_company) {
        this.id_company = id_company;
    }

    public int getRepresentative_id_person() {
        return this.representative_id_person;
    }

    public void setRepresentative_id_person(int representative_id_person) {
        this.representative_id_person = representative_id_person;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
