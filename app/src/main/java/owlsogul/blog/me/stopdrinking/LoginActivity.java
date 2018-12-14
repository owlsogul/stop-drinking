package owlsogul.blog.me.stopdrinking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText txtId;
    private EditText txtPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtId = findViewById(R.id.txtId);
        txtPw = findViewById(R.id.txtPw);

    }

    public void onClickRegister(View view){

    }

    public void onClickLogin(View view){

    }
}
