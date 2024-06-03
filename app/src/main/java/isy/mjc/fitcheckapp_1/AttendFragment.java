package isy.mjc.fitcheckapp_1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendFragment extends Fragment {
    CalendarView calView;
    TextView dateText, viewText;
    RatingBar starRate;
    Button sendBtn, modiBtn;
    ViewFlipper vf;
    EditText writeEdt;

    // 현재 날짜를 가져오기 위한 Calendar 객체 생성
    Calendar calendar = Calendar.getInstance();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AttendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //TODO: ViewInflate하기
        View view = inflater.inflate(R.layout.fragment_attend, container, false);
        calView = view.findViewById(R.id.attend_calendar);
        dateText = view.findViewById(R.id.dateText);
        viewText = view.findViewById(R.id.viewText);
        starRate = view.findViewById(R.id.starRate);
        sendBtn = view.findViewById(R.id.sendBtn);
        modiBtn = view.findViewById(R.id.modiBtn);
        vf = view.findViewById(R.id.vf);
        writeEdt = view.findViewById(R.id.writeEdt);

        //TODO: 별점주기

        starRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                starRate.setRating(rating);
            }
        });

        //TODO: 캘린더뷰 날짜 선택안해도 현재 날짜 가져오기
        // 현재 날짜 가져오기
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

// 현재 날짜를 TextView에 설정
        dateText.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));


        //TODO: 캘린더뷰 이벤트 처리
        // 눌렀을 때 값 가져와서 텍스트 변경, 저장된 starRate, textview내용 불러오기
        // 저장된게 없으면 edittext띄우기 => view flippter이용

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                dateText.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));

            }
        });
        //textview에저장된 값 있다면 보이고 없다면 edittext보이기
        if (viewText.getText().toString().isEmpty()) {
            vf.setDisplayedChild(0);
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewText.setText(writeEdt.getText().toString());
                    vf.setDisplayedChild(1);
                }
            });
        } else {
            vf.setDisplayedChild(1);

        } //수정버튼 구현
         modiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vf.setDisplayedChild(0);
                writeEdt.setText(viewText.getText().toString());
                Log.d("TAG", "onClick: 왜 안되노");

            }
        });
//
//        // 사용자가 입력한 텍스트를 해당 날짜에 맞게 저장하는 함수
//        private void saveTextForDate(int year, int month, int dayOfMonth, String text) {
//            // 여기에 해당 날짜와 텍스트를 저장하는 코드를 추가하세요.
//            // 예시로 SQLite 데이터베이스를 사용하는 코드를 작성해보겠습니다.
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put("date", year + "-" + month + "-" + dayOfMonth); // 예시로 날짜를 "yyyy-mm-dd" 형식으로 저장
//            values.put("text", text);
//            db.insert("text_table", null, values);
//            db.close();
//        }
//
//// 선택된 날짜에 해당하는 텍스트를 가져오는 함수
//        private String getTextForDate(int year, int month, int dayOfMonth) {
//            // 여기에 해당 날짜에 저장된 텍스트를 가져오는 코드를 추가하세요.
//            // 예시로 SQLite 데이터베이스를 사용하는 코드를 작성해보겠습니다.
//            String text = "";
//            SQLiteDatabase db = dbHelper.getReadableDatabase();
//            Cursor cursor = db.query("text_table", new String[]{"text"}, "date = ?",
//                    new String[]{year + "-" + month + "-" + dayOfMonth}, null, null, null);
//            if (cursor.moveToFirst()) {
//                text = cursor.getString(cursor.getColumnIndex("text"));
//            }
//            cursor.close();
//            db.close();
//            return text;
//        }

        return view;
    }
}