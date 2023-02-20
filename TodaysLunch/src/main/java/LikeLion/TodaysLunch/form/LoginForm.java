package LikeLion.TodaysLunch.form;
import javax.validation.constraints.NotEmpty;

public class LoginForm {

    @NotEmpty
    private String nickname;

    @NotEmpty
    private String password;


    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}
