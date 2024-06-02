package isy.mjc.fitcheckapp_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import isy.mjc.fitcheckapp_1.databinding.ActivityMainBinding;

public class MainActivityAD extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ClassFragment());

        binding.naviButton.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navi_attend) {
                replaceFragment(new AttendFragment());
                Log.d("mytest", "출석현황");
            } else if (item.getItemId() == R.id.navi_reserve) {
                Intent intent = new Intent(MainActivityAD.this, ResActivity.class);
                startActivity(intent);
                Log.d("mytest", "예약현황");
            } else if (item.getItemId() == R.id.navi_mypage) {
                replaceFragment(new MyPageMemberFragment());
                Log.d("mytest", "마이페이지");
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_ad, fragment);
        fragmentTransaction.commit();
    }
}