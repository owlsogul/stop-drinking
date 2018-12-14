package owlsogul.blog.me.stopdrinking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {


    private EditText txtId;
    private EditText txtPw;
    private EditText txtPwConfirmed;
    private EditText txtEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtId = findViewById(R.id.txtId);
        txtPw = findViewById(R.id.txtPw);
        txtPwConfirmed = findViewById(R.id.txtPwConfirmed);
        txtEmail = findViewById(R.id.txtEmail);

    }

    public void onClickRegister(View view){

    }


}
