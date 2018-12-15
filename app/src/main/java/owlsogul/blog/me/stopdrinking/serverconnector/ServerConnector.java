package owlsogul.blog.me.stopdrinking.serverconnector;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerConnector {

    private ServerConnector() {}
    private static ServerConnector instance;
    public static ServerConnector getInstance(){
        if (instance == null) instance = new ServerConnector();
        return instance;
    }

    public static final int CONNECTION_TIME = 100004;
    public static final String serverHost = "165.194.35.106";
    public static final int serverPort = 31234;

    public void request(final String command, final JSONObject data, final RequestCallback requestCallback, final ErrorCallback errorCallback){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = new Socket(serverHost, serverPort);
                    socket.setSoTimeout(CONNECTION_TIME);
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

                    // to server
                    JSONObject requestObj = new JSONObject();
                    requestObj.put("command", command);
                    requestObj.put("data", data);
                    pw.println(requestObj.toString());
                    pw.flush();
                    Log.d("ServerConnector", "to Server" + requestObj.toString());

                    // from server
                    String result = br.readLine(); // 한줄씩 들어가는 임시 보관소
                    JSONObject resObj = new JSONObject(result);
                    int resCode = resObj.getInt("result");
                    if (resCode == 200) {
                        requestCallback.requestCallback(resCode, resObj);
                    } else {
                        errorCallback.errorCallback(resCode);
                    }
                    Log.d("ServerConnector", "from Server, " + result);

                    // close socket
                    br.close();
                    pw.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    errorCallback.errorCallback(1000);
                } catch (JSONException e) {
                    e.printStackTrace();
                    errorCallback.errorCallback(1001);
                } finally {
                    if (socket != null){
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

}
