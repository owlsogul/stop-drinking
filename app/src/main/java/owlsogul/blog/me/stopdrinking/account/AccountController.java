package owlsogul.blog.me.stopdrinking.account;

/**
 * 계정 정보(로그인 여부 등)를 관리하는 컨트롤러입니다.
 * Account 클래스를 모델로 합니다.
 *
 */
public class AccountController {

    private Account account;
    private AccountController(){
        account = new Account();
    }
    private static AccountController instance = null;
    public static AccountController getInstance(){
        if (instance == null) instance = new AccountController();
        return instance;
    }

    public boolean isSignIn(){
        return account.isSignIn();
    }

    public void signIn(String accountToken){
        account.setAccountToken(accountToken);
        account.setSignIn(true);
    }

    public void signOut(){
        account.setAccountToken("");
        account.setSignIn(false);
    }

    public String getAccountToken(){
        return this.account.getAccountToken();
    }



}
