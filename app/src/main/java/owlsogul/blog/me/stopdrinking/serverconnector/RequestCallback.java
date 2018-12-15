package owlsogul.blog.me.stopdrinking.serverconnector;

import org.json.JSONObject;

public interface RequestCallback {

    public void requestCallback(int resCode, JSONObject resObj);

}
