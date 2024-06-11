package isy.mjc.fitcheckapp_1;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AttendCheckRequest extends StringRequest {


    final static private String URL = "http://27.96.131.54:8080/2020081040/AttendCheck.jsp";
    private Map<String, String> parameters;

    public AttendCheckRequest(String date, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("date", date);
    }


    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}