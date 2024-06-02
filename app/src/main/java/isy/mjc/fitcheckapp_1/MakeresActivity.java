package isy.mjc.fitcheckapp_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MakeresActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeres);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 날짜 포맷 준비
                String selectedDate = String.format(Locale.KOREA, "%d-%02d-%02d", year, month + 1, dayOfMonth);

                // 다이얼로그 표시 함수 호출
                showDialogWithDate(selectedDate);
            }

            private void showDialogWithDate(String date) {
                // 다이얼로그 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(MakeresActivity.this);
                builder.setTitle(date); // 선택된 날짜를 타이틀로 설정

                // 예시 수업 목록
                String[] classes = {"수업 1", "수업 2", "수업 3"};

                builder.setItems(classes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 선택된 항목에 대한 다이얼로그를 표시
                        new AlertDialog.Builder(MakeresActivity.this)
                                .setTitle("예약 확인")
                                .setMessage(classes[which] + "을(를) 예약하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // "예" 버튼 클릭 시, 예약 내역에 추가
                                        Toast.makeText(getApplicationContext(), classes[which] + " 예약됨", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), ResActivity.class);
                                        intent.putExtra("reservedClass", classes[which]);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // "아니오" 버튼 클릭 시 다이얼로그 닫음
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });

                // 취소 버튼
                builder.setNegativeButton("닫기", null);

                // 다이얼로그 표시
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        BottomNavigationView bottom_navi = findViewById(R.id.bottom_navi);
        bottom_navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navi_attend) {
                    return true;
                } else if (item.getItemId() == R.id.navi_reserve) {
                    Intent intent = new Intent(getApplicationContext(), ResActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.navi_mypage) {
                    return true;
                }
                return false;
            }
        });
    }

}