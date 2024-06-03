package isy.mjc.fitcheckapp_1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class MakeresFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        return inflater.inflate(R.layout.fragment_makeres, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CalendarView calendar = view.findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = String.format(Locale.KOREA, "%d-%02d-%02d", year, month + 1, dayOfMonth);

                showDialogWithDate(selectedDate);
            }

            private void showDialogWithDate(String date) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(date);

                String[] classes = {"수업 1", "수업 2", "수업 3"};

                builder.setItems(classes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("예약 확인")
                                .setMessage(classes[which] + "을(를) 예약하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getActivity(), classes[which] + " 예약됨", Toast.LENGTH_SHORT).show();
                                        // 예약된 클래스 정보를 Bundle을 통해 전달
                                        Bundle bundle = new Bundle();
                                        bundle.putString("reservedClass", classes[which]);

                                        // ReserveFragment 인스턴스 생성 및 Bundle 전달
                                        ReserveFragment reserveFragment = new ReserveFragment();
                                        reserveFragment.setArguments(bundle);

                                        saveReservationToSharedPrefs(classes[which]);

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
                    // SharedPreferences에 예약 정보 저장
                    private void saveReservationToSharedPrefs(String reservedClass) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Reservations", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();

                        // 기존에 저장된 예약 목록을 불러옵니다.
                        ArrayList<String> reservations = loadReservationList();
                        // 새로운 예약을 추가합니다.
                        reservations.add(reservedClass);
                        // 변경된 예약 목록을 다시 저장합니다.
                        String json = gson.toJson(reservations);
                        editor.putString("reservationList", json);
                        editor.apply();
                    }

                    // SharedPreferences에서 예약 목록을 불러오는 메서드
                    private ArrayList<String> loadReservationList() {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Reservations", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = sharedPreferences.getString("reservationList", null);
                        Type type = new TypeToken<ArrayList<String>>() {}.getType();
                        ArrayList<String> reservations = gson.fromJson(json, type);
                        return reservations == null ? new ArrayList<>() : reservations;
                    }
                });

                builder.setNegativeButton("닫기", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
