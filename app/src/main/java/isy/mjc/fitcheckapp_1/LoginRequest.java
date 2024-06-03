package isy.mjc.fitcheckapp_1;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL = "http://27.96.131.54:8080/2019081047/UserLogin.jsp";
    private Map<String,String> parameters;

    public LoginRequest(String userID, String userPasswd, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPasswd", userPasswd);
    }

    @Override
    public  Map<String, String> getParams() {
        return parameters;
    }
}
