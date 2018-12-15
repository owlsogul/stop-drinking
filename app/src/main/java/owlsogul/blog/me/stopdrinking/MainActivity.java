package owlsogul.blog.me.stopdrinking;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import owlsogul.blog.me.stopdrinking.account.AccountController;
import owlsogul.blog.me.stopdrinking.serverconnector.ErrorCallback;
import owlsogul.blog.me.stopdrinking.serverconnector.RequestCallback;
import owlsogul.blog.me.stopdrinking.serverconnector.ServerConnector;

public class MainActivity extends AppCompatActivity {

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

    public void onTest(View view){

        EditText txtTension, txtDrinkingYesterday, txtSleepHour;
        txtTension = findViewById(R.id.txtTension);
        txtDrinkingYesterday = findViewById(R.id.txtDrinkingYesterday);
        txtSleepHour = findViewById(R.id.txtSleepHour);

        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("token", AccountController.getInstance().getAccountToken());
            dataObj.put("date", "2018-12-15");
            dataObj.put("company", "");
            dataObj.put("sleep_hour", Integer.valueOf(txtSleepHour.getText().toString()));
            dataObj.put("tension", Integer.valueOf(txtTension.getText().toString()));
            dataObj.put("drinking_yesterday", Integer.valueOf(txtDrinkingYesterday.getText().toString()));
            ServerConnector.getInstance().request("add_party", dataObj, new RequestCallback() {
                @Override
                public void requestCallback(int resCode, JSONObject resObj) {
                    try {
                        showToastInThread("추천 주량" + (int)resObj.get("recommended_drink"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new ErrorCallback() {
                @Override
                public void errorCallback(int errorCode) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
