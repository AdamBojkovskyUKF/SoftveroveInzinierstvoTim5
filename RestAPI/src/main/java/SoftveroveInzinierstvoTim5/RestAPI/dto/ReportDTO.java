package SoftveroveInzinierstvoTim5.RestAPI.dto;

public class ReportDTO {
 private int idreport;
 private int creatoraccount_id_account;
 private String type;
 private String content;
 private String timestamp;

    public int getIdreport() {
        return this.idreport;
    }

    public void setIdreport(int idreport) {
        this.idreport = idreport;
    }

    public int getCreatoraccount_id_account() {
        return this.creatoraccount_id_account;
    }

    public void setCreatoraccount_id_account(int creatoraccount_id_account) {
        this.creatoraccount_id_account = creatoraccount_id_account;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
