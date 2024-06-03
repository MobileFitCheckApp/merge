package isy.mjc.fitcheckapp_1;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReserveFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> reservations;
    private ArrayAdapter<String> adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReserveFragment() {
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
    public static ReserveFragment newInstance(String param1, String param2) {
        ReserveFragment fragment = new ReserveFragment();
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
        return inflater.inflate(R.layout.fragment_reserve, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 현재 날짜를 가져오기
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(calendar.getTime());

        TextView tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        tvCurrentDate.setText(today);

        Button btnMakeRes = view.findViewById(R.id.btnMakeRes);

        btnMakeRes.setOnClickListener(v -> {
            MakeresFragment makeresFragment = new MakeresFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout,makeresFragment)
                    .addToBackStack(null)
                    .commit();
        });

        ListView lvResList = view.findViewById(R.id.lvResList);
        reservations = new ArrayList<>(); 
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, reservations);
        lvResList.setAdapter(adapter);

        // Bundle에서 예약된 클래스 이름 받기
        Bundle bundle = getArguments();
        if (bundle != null) {
            String reservedClass = bundle.getString("reservedClass", "");

            // 예약된 클래스가 있으면 리스트에 추가
            ArrayList<String> loadedReservations = loadReservationList();
            reservations.addAll(loadedReservations);
            adapter.notifyDataSetChanged();
        }

        lvResList.setOnItemClickListener((parent, view1, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("예약 삭제");
            builder.setMessage("이 예약을 삭제하시겠습니까?");
            builder.setPositiveButton("예", (dialog, which) -> {
                String itemToRemove = reservations.get(position);
                reservations.remove(position);
                saveReservationList(reservations);
                adapter.notifyDataSetChanged(); // 리스트뷰 갱신
                Toast.makeText(getActivity(), itemToRemove + " 예약이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("아니오", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    // SharedPreferences에서 데이터 불러오기
    private ArrayList<String> loadReservationList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Reservations", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("reservationList", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> reservations = gson.fromJson(json, type);
        return reservations == null ? new ArrayList<>() : reservations;
    }
    // SharedPreferences에 데이터 저장
    private void saveReservationList(ArrayList<String> reservations) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Reservations", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(reservations);
        editor.putString("reservationList", json);
        editor.apply();
    }
    @Override
    public void onResume() {
        super.onResume();
        // 예약 목록을 다시 불러옵니다.
        ArrayList<String> updatedReservations = loadReservationList();
        reservations.clear();
        reservations.addAll(updatedReservations);
        adapter.notifyDataSetChanged();
    }

}