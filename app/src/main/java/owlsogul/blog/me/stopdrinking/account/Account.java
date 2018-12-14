package owlsogul.blog.me.stopdrinking.account;

public class Account {

    private boolean isSignIn = false;
    private String accountToken = "";

    public boolean isSignIn() {
        return isSignIn;
    }

    public void setSignIn(boolean signIn) {
        isSignIn = signIn;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }
}
