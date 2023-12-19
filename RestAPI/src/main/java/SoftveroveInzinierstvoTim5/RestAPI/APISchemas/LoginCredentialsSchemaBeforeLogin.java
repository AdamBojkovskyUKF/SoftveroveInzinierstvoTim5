package SoftveroveInzinierstvoTim5.RestAPI.APISchemas;

public class LoginCredentialsSchemaBeforeLogin {
    String password;
    String email;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
