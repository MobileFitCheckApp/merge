package isy.mjc.fitcheckapp_1;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ManageAddRequest extends StringRequest {


    final static private String URL = "http://27.96.131.54:8080/2020081040/ManageAdd.jsp";
    private Map<String, String> parameters;

    public ManageAddRequest(String id, String uesrID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("memid", id);
        parameters.put("userID", uesrID);
    }


    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}