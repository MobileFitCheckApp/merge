package isy.mjc.fitcheckapp_1;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AttendSendRequest extends StringRequest {


    final static private String URL1 = "http://27.96.131.54:8080/2020081040/AttendSend.jsp";
    private Map<String,String> parameters;

    public AttendSendRequest(String dateText,String stars, String content, Response.Listener<String> listener) {
        super(Method.POST, URL1, listener, null);
        parameters = new HashMap<>();
        parameters.put("dateText",dateText);
        parameters.put("stars",stars);
        parameters.put("content",content);

    }


    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}