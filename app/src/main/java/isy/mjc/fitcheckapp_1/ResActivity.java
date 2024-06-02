package isy.mjc.fitcheckapp_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ResActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);

        // 현재 날짜를 가져오기
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(calendar.getTime());


        TextView tvCurrentDate = findViewById(R.id.tvCurrentDate);
        tvCurrentDate.setText(today);

        final Button btnMakeRes = findViewById(R.id.btnMakeRes);

        btnMakeRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MakeresActivity.class);
                startActivity(intent);
            }
        });
        // 예약된 클래스 이름 받기
        String reservedClass = getIntent().getStringExtra("reservedClass");

        // ListView와 Adapter 설정
        ListView lvResList = findViewById(R.id.lvResList);
        ArrayList<String> reservations = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reservations);
        lvResList.setAdapter(adapter);

        // 예약된 클래스가 있으면 리스트에 추가
        if (reservedClass != null && !reservedClass.isEmpty()) {
            reservations.add(reservedClass);
            adapter.notifyDataSetChanged(); // 리스트뷰 갱신
        }
        lvResList.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ResActivity.this);
            builder.setTitle("예약 삭제");
            builder.setMessage("이 예약을 삭제하시겠습니까?");
            builder.setPositiveButton("예", (dialog, which) -> {
                // '예'를 클릭했을 때 항목 삭제
                String itemToRemove = reservations.get(position);
                reservations.remove(position);
                adapter.notifyDataSetChanged(); // 리스트뷰 갱신
                Toast.makeText(ResActivity.this, itemToRemove + " 예약이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("아니오", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        BottomNavigationView bottom_navi = findViewById(R.id.bottom_navi);
        bottom_navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navi_attend) {
                    Intent intent = new Intent(getApplicationContext(), ResActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.navi_reserve) {
                    Intent intent = new Intent(getApplicationContext(), ResActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.navi_mypage) {
                    Intent intent = new Intent(getApplicationContext(), ResActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

    }
}