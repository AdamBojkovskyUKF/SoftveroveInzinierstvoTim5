package SoftveroveInzinierstvoTim5.RestAPI.dto;

public class CommunicationDTO {
    private int id_communication;
    private int account_id_account;
    private int account_id_account1;
    private String messages;

    public int getId_communication() {
        return this.id_communication;
    }

    public void setId_communication(int id_communication) {
        this.id_communication = id_communication;
    }

    public int getAccount_id_account() {
        return this.account_id_account;
    }

    public void setAccount_id_account(int account_id_account) {
        this.account_id_account = account_id_account;
    }

    public int getAccount_id_account1() {
        return this.account_id_account1;
    }

    public void setAccount_id_account1(int account_id_account1) {
        this.account_id_account1 = account_id_account1;
    }

    public String getMessages() {
        return this.messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
    

}
