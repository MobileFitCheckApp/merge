package isy.mjc.fitcheckapp_1;

import android.annotation.SuppressLint;
import android.os.Bundle;

import org.json.JSONException;

import com.android.volley.VolleyError;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

public class AttendFragment extends Fragment {
    CalendarView calView;
    TextView dateText, viewText;
    RatingBar starRate;
    Button sendBtn, modiBtn, modiSendBtn;
    ViewFlipper vf;
    EditText writeEdt, modiEdt;
    MaterialCalendarView mcv;

    // db전송 별값 전역변수로 선언하기!
    float stars;  // 변경: float로 선언
    // request 전송용 string
    String date, content;

    // 현재 날짜를 가져오기 위한 Calendar 객체 생성 하기
    //push 하기
    Calendar calendar = Calendar.getInstance();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AttendFragment() {
        // Required empty public constructor
    }

    public static AttendFragment newInstance(String param1, String param2) {
        AttendFragment fragment = new AttendFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attend, container, false);
        calView = view.findViewById(R.id.attend_calendar);
        dateText = view.findViewById(R.id.dateText);
        viewText = view.findViewById(R.id.viewText);
        starRate = view.findViewById(R.id.starRate);
        sendBtn = view.findViewById(R.id.sendBtn);
        modiBtn = view.findViewById(R.id.modiBtn);
        vf = view.findViewById(R.id.vf);
        writeEdt = view.findViewById(R.id.writeEdt);

        // 수정용
        modiEdt = view.findViewById(R.id.modiEdt);
        modiSendBtn = view.findViewById(R.id.modiSendBtn);

        vf.setVisibility(View.INVISIBLE);

        starRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                stars = rating; // 별점을 float로 저장
                Log.d("mytest", "별값" + stars); //<-얘는 stars(전역변수)에 저장됨
            }
        });

        // 현재 날짜 가져오기
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // 현재 날짜를 TextView에 설정
        dateText.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));

        // 캘린더뷰 이벤트 처리
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                writeEdt.setText(""); // 다른 날짜 선택시 이전 날짜에서 쓰던것 초기화
                dateText.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                date = dateText.getText().toString(); // 값 저장해서 보냄
                Log.d("mytest", "선택한 날짜: " + date);
                vf.setVisibility(View.VISIBLE);

                // attendcheck: date 값 보내서 해당하는 스키마에 content 값이 저장되어있는지 조회하기
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                vf.setDisplayedChild(1);
                                String content = jsonResponse.getString("tvText");
                                float stars = (float) jsonResponse.getDouble("star");
                                viewText.setText(content);
                                starRate.setRating(stars);
                            } else {
                                vf.setDisplayedChild(0);
                                starRate.setRating(0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                AttendCheckRequest ACRequest = new AttendCheckRequest(date, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(ACRequest);
            }
        });

        // 등록버튼 구현
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edittext에 입력한 값 가져다가 db에 저장하고
                // 저장한 값 tvt에 연결해서 보이게 하기
                date = dateText.getText().toString(); // 값 저장해서 보냄
                content = writeEdt.getText().toString();
                Log.d("tag", "버튼 클릭(날짜, 별, 내용)" + date + "," + stars + "," + content);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                vf.setDisplayedChild(1);
                                viewText.setText(content);
                                starRate.setRating(stars);
                                Log.d("tag", "저장되나");
                            } else {
                                // 등록 실패 시 처리
                                Log.d("VolleyResponse", "Registration failed");
                                Log.d("tag", "버튼 클릭(날짜, 별, 내용)" + date + "," + stars + "," + content);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                String star = Float.toString(stars);
                AttendSendRequest ASRequest = new AttendSendRequest(date, star, content, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(ASRequest);
            }
        });

        // 수정버튼 구현
        modiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vf.setDisplayedChild(2);
                modiEdt.setText(viewText.getText().toString());
                Log.d("mytest", "제대로 수정 tv로 넘어가나");
            }
        });

        modiSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = modiEdt.getText().toString();
                Log.d("mytest", "수정버튼");

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 업데이트 구문 작성하는 곳
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                vf.setDisplayedChild(1);
                                viewText.setText(content);
                                starRate.setRating(stars);
                                Log.d("tag", "저장되나");
                            } else {
                                // 등록 실패 시 처리
                                Log.d("VolleyResponse", "Registration failed");
                                Log.d("tag", "버튼 클릭(날짜, 별, 내용)" + date + "," + stars + "," + content);
                                Log.e("JSONError", "Response is not a valid JSON: " + response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                String star = Float.toString(stars);
                AttendModiRequest modiRequest = new AttendModiRequest(date, star, content, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(modiRequest);
            }
        });

        return view;
    }
}
