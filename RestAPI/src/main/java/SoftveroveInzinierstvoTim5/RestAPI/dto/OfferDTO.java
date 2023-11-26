package SoftveroveInzinierstvoTim5.RestAPI.dto;

public class OfferDTO {
 private int id_offer;
 private int person_id_person;
 private String position;
 private String description;
 private String contract_type;

    public int getId_offer() {
        return this.id_offer;
    }

    public void setId_offer(int id_offer) {
        this.id_offer = id_offer;
    }

    public int getPerson_id_person() {
        return this.person_id_person;
    }

    public void setPerson_id_person(int person_id_person) {
        this.person_id_person = person_id_person;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContract_type() {
        return this.contract_type;
    }

    public void setContract_type(String contract_type) {
        this.contract_type = contract_type;
    }

}
