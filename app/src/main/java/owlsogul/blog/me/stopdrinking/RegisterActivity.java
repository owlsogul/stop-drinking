package owlsogul.blog.me.stopdrinking;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import owlsogul.blog.me.stopdrinking.serverconnector.ErrorCallback;
import owlsogul.blog.me.stopdrinking.serverconnector.RequestCallback;
import owlsogul.blog.me.stopdrinking.serverconnector.ServerConnector;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtId;
    private EditText txtPw;
    private EditText txtPwConfirmed;
    private EditText txtEmail;

    private Handler successRegisterHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };

    private Handler toastHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = msg.obj.toString();
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }
    };

    public void showToastInThread(String toastMsg){
        Message msg = toastHandler.obtainMessage();
        msg.obj = toastMsg;
        toastHandler.sendMessage(msg);
    }

    public void showToast(String toastMsg){
        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtId = findViewById(R.id.txtId);
        txtPw = findViewById(R.id.txtPw);
        txtPwConfirmed = findViewById(R.id.txtPwConfirmed);
        txtEmail = findViewById(R.id.txtEmail);

        Intent fromIntent = getIntent();
        String memberId = fromIntent.getStringExtra("memberId");
        txtId.setText(memberId);


    }

    public void onClickRegister(View view){

        String memberId = txtId.getText().toString();
        if (!validateId(memberId)){
            showToast("아이디가 잘 못 되었습니다.");
            return;
        }

        String memberEmail = txtEmail.getText().toString();
        if (!validateEmail(memberEmail)){
            showToast("이메일이 잘 못 되었습니다.");
            return;
        }

        String password = txtPw.getText().toString();
        String passwordConfirmed = txtPwConfirmed.getText().toString();
        if (!password.equals(passwordConfirmed)) {
            showToast("비밀번호가 다릅니다.");
            return;
        }
        if (!validatePassword(password)) {
            showToast("비밀번호가 잘 못 되었습니다.");
            return;
        }

        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("memberId", memberId);
            dataObj.put("memberPw", password);
            dataObj.put("memberEmail", memberEmail);
            ServerConnector conn = ServerConnector.getInstance();
            conn.request("register", dataObj, new RequestCallback() {
                @Override
                public void requestCallback(int resCode, JSONObject resObj) {
                    showToastInThread("회원 가입 성공");
                    successRegisterHandler.sendEmptyMessage(0);
                    return;
                }
            }, new ErrorCallback() {
                @Override
                public void errorCallback(int errorCode) {
                    showToastInThread("회원 가입 실패:"+errorCode);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void onClickCheckId(View view){
        Log.d("RegisterActivity", "checkId");
        String memberId = txtId.getText().toString();
        if (validateId(memberId)){
            checkDupId(memberId);
        }
        else {
            showToast("잘못된 아이디입니다. 4글자 이상, 한영숫자, _ 사용가능합니다.");
        }
    }

    public void onClickCheckEmail(View view){
        Log.d("RegisterActivity", "checkEmail");
        String memberEmail = txtEmail.getText().toString();
        if (validateEmail(memberEmail)){
            checkDupEmail(memberEmail);
        }
        else{
            showToast("잘못된 이메일입니다.");
        }
    }

    private boolean validateId(String memberId){
        if (memberId.length() >= 4){
            if (memberId.matches("^[_0-9a-zA-Z가-힣]*$")){
                return true;
            }
        }
        return false;
    }

    private boolean validateEmail(String memberEmail){
        if (memberEmail.length() >= 4){
            if (memberEmail.contains("@") && memberEmail.contains(".")){
                String[] atSplited = memberEmail.split("@");
                if (atSplited.length == 2 && !atSplited[0].isEmpty() && !atSplited[1].isEmpty()){
                    String[] dotSplited = atSplited[1].split("\\.");
                    if (dotSplited.length >= 2 && !dotSplited[0].isEmpty() && !dotSplited[1].isEmpty()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean validatePassword(String password){
        if (password.length() >= 8){
            return true;
        }
        return false;
    }


    private void checkDupId(String memberId){
        ServerConnector conn = ServerConnector.getInstance();
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("memberId", memberId);
            conn.request("check_dup_member_id", dataObj, new RequestCallback() {
                @Override
                public void requestCallback(int resCode, JSONObject resObj) {
                    Log.d("RegisterActivity", resObj.toString());
                    showToastInThread("사용 가능한 아이디입니다.");
                }
            }, new ErrorCallback() {
                @Override
                public void errorCallback(int errorCode) {
                    Log.d("RegisterActivity", ""+errorCode);
                    if (errorCode == 400){
                        showToastInThread("이미 사용 중인 아이디입니다.");
                    }
                    else {
                        showToastInThread("서버 에러가 발생했습니다:" + errorCode);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkDupEmail(String memberEmail){
        ServerConnector conn = ServerConnector.getInstance();
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("memberEmail", memberEmail);
            conn.request("check_dup_member_email", dataObj, new RequestCallback() {
                @Override
                public void requestCallback(int resCode, JSONObject resObj) {
                    Log.d("RegisterActivity", resObj.toString());
                    showToastInThread("사용 가능한 이메일입니다.");
                    return;
                }
            }, new ErrorCallback() {
                @Override
                public void errorCallback(int errorCode) {
                    Log.d("RegisterActivity", ""+errorCode);
                    if (errorCode == 400){
                        showToastInThread("이미 사용중인 이메일입니다.");
                    }
                    else{
                        showToastInThread("서버 에러가 발생했습니다:"+errorCode);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
