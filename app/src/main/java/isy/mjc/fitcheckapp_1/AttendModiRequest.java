package isy.mjc.fitcheckapp_1;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AttendModiRequest extends StringRequest {


    final static private String URL = "http://27.96.131.54:8080/2020081040/AttendModi.jsp";
    private Map<String,String> parameters;

    public AttendModiRequest(String date, String star, String content, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("date",date);
        parameters.put("star",star);
        parameters.put("content",content);

    }


    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}