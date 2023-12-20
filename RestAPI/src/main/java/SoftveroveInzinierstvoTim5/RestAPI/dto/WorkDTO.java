package SoftveroveInzinierstvoTim5.RestAPI.dto;

public class WorkDTO {
 private int id_work;
 private int offer_id_offer;
 private int account_id_account;
 private byte[] contract;
 private String work_log;
 private String state;
 private String feedback_student;
 private String feedback_company;
 private String mark;
 private String completion_year;
 private byte[] certificate;

    public int getId_work() {
        return this.id_work;
    }

    public void setId_work(int id_work) {
        this.id_work = id_work;
    }

    public int getOffer_id_offer() {
        return this.offer_id_offer;
    }

    public void setOffer_id_offer(int offer_id_offer) {
        this.offer_id_offer = offer_id_offer;
    }

    public int getAccount_id_account() {
        return this.account_id_account;
    }

    public void setAccount_id_account(int account_id_account) {
        this.account_id_account = account_id_account;
    }

    public byte[] getContract() {
        return this.contract;
    }

    public void setContract(byte[] contract) {
        this.contract = contract;
    }

    public String getWork_log() {
        return this.work_log;
    }

    public void setWork_log(String work_log) {
        this.work_log = work_log;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFeedback_student() {
        return this.feedback_student;
    }

    public void setFeedback_student(String feedback_student) {
        this.feedback_student = feedback_student;
    }

    public String getFeedback_company() {
        return this.feedback_company;
    }

    public void setFeedback_company(String feedback_company) {
        this.feedback_company = feedback_company;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCompletion_year() {
        return this.completion_year;
    }

    public void setCompletion_year(String completion_year) {
        this.completion_year = completion_year;
    }

    public byte[] getCertificate() {
        return this.certificate;
    }

    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }

}
