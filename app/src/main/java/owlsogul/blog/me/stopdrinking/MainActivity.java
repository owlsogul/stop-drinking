package owlsogul.blog.me.stopdrinking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import owlsogul.blog.me.stopdrinking.account.AccountController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){

        AccountController.getInstance().signOut();
        SharedPreferences pref = getSharedPreferences("stop-drinking", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("token");
        editor.commit();

        Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(toLogin);
        finish();
    }

}
