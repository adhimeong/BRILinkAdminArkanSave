package id.sch.smkn13bdg.adhi.brilinkadminarkan.getset;

/**
 * Created by adhi on 07/09/18.
 */

public class UserController {

    String idadmin;
    String username;
    String password;

    public UserController(String idadmin, String username, String password) {
        this.idadmin = idadmin;
        this.username = username;
        this.password = password;
    }

    public String getIdadmin() {
        return idadmin;
    }

    public void setIdadmin(String idadmin) {
        this.idadmin = idadmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
