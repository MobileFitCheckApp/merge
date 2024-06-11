package isy.mjc.fitcheckapp_1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Locale;

import android.util.Log;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;


public class MakeresFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CalendarView calendarView;
//    private Set<CalendarDay> reservedDates;
    private String selectedDate;
    private String mParam1;
    private String mParam2;

    public MakeresFragment() {
        // Required empty public constructor
    }

    public static MakeresFragment newInstance(String param1, String param2) {
        MakeresFragment fragment = new MakeresFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_makeres, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarView = view.findViewById(R.id.calendar);
        if (calendarView != null) {
            calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
                String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                fetchCourseList(selectedDate);
            });
        }
    }


    public void fetchCourseList(String date) {
        String url = "http://27.96.131.54:8080/2020081040/Classes.jsp?class_schedule=" + date;
        Log.d("fetchCourseList", "URL: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("fetchCourseList", "Response received: " + response.toString());
                try {
                    List<String> classList = new ArrayList<>();
                    List<Boolean> isReservedList = new ArrayList<>();
                    ArrayList<String> reservedClasses = loadReservationList();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject course = response.getJSONObject(i);
                        String classSchedule = course.getString("class_schedule");

                        if (classSchedule.equals(date)) {
                            String className = course.getString("class_id");
                            String classTime = course.getString("class_time");
                            String classInfo = className + " - " + classTime;
                            classList.add(classInfo);
                            isReservedList.add(reservedClasses.contains(classInfo));
                        }
                    }

                    if (classList.isEmpty()) {
                        classList.add("등록된 수업 없음");
                        isReservedList.add(false);
                    }

                    String[] classes = classList.toArray(new String[0]);
                    Boolean[] isReservedArray = isReservedList.toArray(new Boolean[0]);

                    getActivity().runOnUiThread(() -> showDialogWithDate(date, classes, isReservedArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("fetchCourseList", "Volley error: " + error.toString());
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void showDialogWithDate(String date, String[] classes, Boolean[] isReservedArray) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(date);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, classes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                if (isReservedArray[position]) {
                    textView.setTextColor(Color.RED); // 글씨 색만 빨간색으로 변경
                } else {
                    textView.setTextColor(Color.BLACK); // 예약되지 않은 경우 기본 색상으로 설정
                }
                textView.setTextSize(15);
                return view;
            }
        };

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isReservedArray[which]) {
                    Toast.makeText(getActivity(), "이미 예약한 수업입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    String classInfo = classes[which];
                    String classSchedule = null;
                    showReservationConfirmationDialog(classInfo, classSchedule);

                }
            }
        });

        builder.setNegativeButton("닫기", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showReservationConfirmationDialog(String classInfo,String classSchedule) {
        new AlertDialog.Builder(getActivity())
                .setTitle("예약 확인")
                .setMessage(classInfo + "을(를) 예약하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), classInfo + " 예약됨", Toast.LENGTH_SHORT).show();
                        // 예약된 클래스 정보를 Bundle을 통해 전달
                        Bundle bundle = new Bundle();
                        bundle.putString("reservedClass", classInfo);
                        bundle.putString("classSchedule", classSchedule);
                        // ReserveFragment 인스턴스 생성 및 Bundle 전달
                        ReserveFragment reserveFragment = new ReserveFragment();
                        reserveFragment.setArguments(bundle);
                        saveReservationToSharedPrefs(classInfo);
                        // FragmentManager를 사용하여 FragmentTransaction 시작
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, reserveFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void saveReservationToSharedPrefs(String reservedClass) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Reservations", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        ArrayList<String> reservations = loadReservationList();
        reservations.add(reservedClass);
        String json = gson.toJson(reservations);
        editor.putString("reservationList", json);
        editor.apply();
    }
    private ArrayList<String> loadReservationList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Reservations", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("reservationList", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> reservations = gson.fromJson(json, type);
        return reservations == null ? new ArrayList<>() : reservations;
    }
}
