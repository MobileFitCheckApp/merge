package isy.mjc.fitcheckapp_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import isy.mjc.fitcheckapp_1.databinding.ActivityMainAdmBinding;
import isy.mjc.fitcheckapp_1.databinding.ActivityMainBinding;

public class MainActivityAD extends AppCompatActivity {

    ActivityMainAdmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAdmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ClassFragment());

        binding.naviButton.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navi_class) {
                replaceFragment(new ClassFragment());
                Log.d("mytest", "수업");
            } else if (item.getItemId() == R.id.navi_manage) {
                replaceFragment(new ManageFragment());
                Log.d("mytest", "수강생관리");
            } else if (item.getItemId() == R.id.navi_mypage_ad) {
                replaceFragment(new MyPageAdminFragment());
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