package owlsogul.blog.me.stopdrinking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import owlsogul.blog.me.stopdrinking.account.AccountController;
import owlsogul.blog.me.stopdrinking.list.AdapterFirstFeedback;
import owlsogul.blog.me.stopdrinking.list.ItemFirstFeedback;
import owlsogul.blog.me.stopdrinking.serverconnector.ErrorCallback;
import owlsogul.blog.me.stopdrinking.serverconnector.RequestCallback;
import owlsogul.blog.me.stopdrinking.serverconnector.ServerConnector;

public class FirstLoginActivity extends AppCompatActivity {

    private String TAG = "FirstLoginActivity";
    private EditText txtSleepHour, txtTension, txtDrinkingYesterday, txtAmountDrink, txtDrinkness;
    private ListView listView;
    private AdapterFirstFeedback adapter;

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

    private Handler toMainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent toMain = new Intent(FirstLoginActivity.this, MainActivity.class);
            startActivity(toMain);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        listView = findViewById(R.id.listInitFeedback);
        txtSleepHour = findViewById(R.id.txtSleepHour);
        txtTension = findViewById(R.id.txtTension);
        txtDrinkingYesterday = findViewById(R.id.txtDrinkingYesterday);
        txtAmountDrink = findViewById(R.id.txtAmountDrink);
        txtDrinkness = findViewById(R.id.txtDrinkness);

        adapter = new AdapterFirstFeedback();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, final int position, long id) {
                new AlertDialog.Builder(FirstLoginActivity.this).setTitle("삭제 확인").setMessage("삭제하시겠습니까?").setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.removeItem(position);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }

    public void onClickAdd(View view){
        try {
            int sleepHour = Integer.parseInt(txtSleepHour.getText().toString());
            if (0 > sleepHour || sleepHour > 24) {
                showToast("0에서 24까지의 숫자를 입력해주세요.");
                txtSleepHour.requestFocus();
                return;
            }
            int tension = Integer.parseInt(txtTension.getText().toString());
            if (1 > tension || tension > 10) {
                showToast("1에서 10까지의 숫자를 입력해주세요.");
                txtTension.requestFocus();
                return;
            }
            int drinkingYesterday = Integer.parseInt(txtDrinkingYesterday.getText().toString());
            if (0 > drinkingYesterday) {
                showToast("0 이상의 수를 입력해주세요.");
                txtDrinkingYesterday.requestFocus();
                return;
            }
            int amountDrink = Integer.parseInt(txtAmountDrink.getText().toString());
            if (0 >= amountDrink) {
                showToast("1 이상의 수를 입력해주세요.");
                txtAmountDrink.requestFocus();
                return;
            }
            int drinkness = Integer.parseInt(txtDrinkness.getText().toString());
            if (1 > drinkness || drinkness > 10) {
                showToast("1에서 10까지의 숫자를 입력해주세요.");
                txtDrinkness.requestFocus();
                return;
            }
            adapter.addItem(sleepHour, tension, drinkingYesterday, amountDrink, drinkness);
            adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            Log.d(TAG, "Parsing-Error-Null");
        } catch (NumberFormatException e){
            Log.d(TAG, "Parsing-Error-Number-Format");
        }
    }

    public void onClickSend(View view){

        try {
            JSONObject dataObj = new JSONObject();
            JSONArray feedbackArr = new JSONArray();
            for (ItemFirstFeedback item : adapter.getItems()){
                JSONObject itemObj = new JSONObject();
                itemObj.put("sleep_hour", item.getSleepHour());
                itemObj.put("tension", item.getTension());
                itemObj.put("drinking_yesterday", item.getDrinkingYesterday());
                itemObj.put("amount_drink", item.getAmountDrink());
                itemObj.put("drinkness", item.getDrinkness());
                feedbackArr.put(itemObj);
            }
            dataObj.put("feedback", feedbackArr);
            dataObj.put("token", AccountController.getInstance().getAccountToken());
            ServerConnector.getInstance().request("add_feedbacks", dataObj, new RequestCallback() {
                @Override
                public void requestCallback(String result) {
                    try {
                        JSONObject resObj = new JSONObject(result);
                        int resCode = resObj.getInt("result");
                        if (resCode == 200){
                            toMainHandler.sendEmptyMessage(0);
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showToastInThread("오류가 발생하였습니다.");
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

    public void onClickSkip(View view){
        toMainHandler.sendEmptyMessage(0);
    }
}
