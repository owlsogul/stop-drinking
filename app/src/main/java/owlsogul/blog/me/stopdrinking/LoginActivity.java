package owlsogul.blog.me.stopdrinking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import owlsogul.blog.me.stopdrinking.account.AccountController;
import owlsogul.blog.me.stopdrinking.serverconnector.ErrorCallback;
import owlsogul.blog.me.stopdrinking.serverconnector.RequestCallback;
import owlsogul.blog.me.stopdrinking.serverconnector.ServerConnector;

public class LoginActivity extends AppCompatActivity {

    private EditText txtId;
    private EditText txtPw;

    private Handler validTokenHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                showToast("자동 로그인 완료!");
                String token = (String) msg.obj;
                AccountController.getInstance().signIn(token);
                Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(toMain);
                finish();
            }
            else {
                AccountController.getInstance().signOut();
                SharedPreferences pref = getSharedPreferences("stop-drinking", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("token");
                editor.commit();
            }
        }
    };

    private Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != 0){ // login success
                String token = (String) msg.obj;
                AccountController.getInstance().signIn(token);
                Log.d("LoginActivity", "token을 받아왔습니다. " + token);
                Intent toFirstLogin = new Intent(LoginActivity.this, FirstLoginActivity.class);
                startActivity(toFirstLogin);
                showToast("로그인에 성공하였습니다.");
                finish();

                // token 저장
                SharedPreferences pref = getSharedPreferences("stop-drinking", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("token", token);
                editor.commit();
            }
            else { // login fail
                showToast("로그인에 실패하였습니다.");
                txtPw.setText("");
            }
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
        setContentView(R.layout.activity_login);
        txtId = findViewById(R.id.txtId);
        txtPw = findViewById(R.id.txtPw);

        // 로그인 확인
        SharedPreferences pref = getSharedPreferences("stop-drinking", MODE_PRIVATE);
        final String token = pref.getString("token", null);
        if (token != null){ // 토큰이 존재할 시 바로 메인 이동
            try {
                JSONObject dataObj = new JSONObject();
                dataObj.put("token", token);
                ServerConnector.getInstance().request("validate_token", dataObj, new RequestCallback() {
                    @Override
                    public void requestCallback(int resCode, JSONObject resObj) {
                        Message msg = validTokenHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = token;
                        validTokenHandler.sendMessage(msg);
                        return;
                    }
                }, new ErrorCallback() {
                    @Override
                    public void errorCallback(int errorCode) {
                        validTokenHandler.sendEmptyMessage(0);
                        if (errorCode == 400){
                            showToastInThread("토큰 유효시간이 끝났습니다. 다시 로그인해주세요");
                        }
                        else{
                            showToastInThread("에러가 발생하였습니다:" + errorCode);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void onClickRegister(View view){

        Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
        toRegister.putExtra("memberId", txtId.getText().toString());
        startActivity(toRegister);

    }

    public void onClickLogin(View view){

        String memberId = txtId.getText().toString();
        String memberPw = txtPw.getText().toString();
        if (!validateId(memberId)){
            showToast("아이디가 올바르지 않습니다.");
        }

        if (!validatePassword(memberPw)){
            showToast("비밀번호가 올바르지 않습니다.");
        }

        try {
            ServerConnector conn = ServerConnector.getInstance();
            JSONObject dataObj = new JSONObject();
            dataObj.put("memberId", memberId);
            dataObj.put("memberPw", memberPw);
            conn.request("login", dataObj, new RequestCallback() {
                @Override
                public void requestCallback(int resCode, JSONObject resObj) {
                    try {
                        String token = resObj.getString("token");
                        Message msg = loginHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = token;
                        loginHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loginHandler.sendEmptyMessage(0);
                        showToastInThread("에러가 발생하였습니다:" + resCode);
                    }
                    return;
                }
            }, new ErrorCallback() {
                @Override
                public void errorCallback(int errorCode) {
                    loginHandler.sendEmptyMessage(0);
                    showToastInThread("에러가 발생하였습니다:" + errorCode);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
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

    private boolean validatePassword(String password){
        if (password.length() >= 8){
            return true;
        }
        return false;
    }

}
