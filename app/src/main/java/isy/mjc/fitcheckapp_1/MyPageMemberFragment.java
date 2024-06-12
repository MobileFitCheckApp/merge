package isy.mjc.fitcheckapp_1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPageMemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPageMemberFragment extends Fragment {
    private LineChart lineChart;
    private EditText weightInput;
    private Button addWeightButton;

    private List<Entry> weightEntries = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyPageMemberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPageMemberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPageMemberFragment newInstance(String param1, String param2) {
        MyPageMemberFragment fragment = new MyPageMemberFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_page_member, container, false);

        lineChart = view.findViewById(R.id.chart);
        weightInput = view.findViewById(R.id.edt);
        addWeightButton = view.findViewById(R.id.addBtn);

        setupLineChart();

        addWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 몸무게 입력 값 가져오기
                String weightStr = weightInput.getText().toString();
                if (!weightStr.isEmpty()) {
                    float weight = Float.parseFloat(weightStr);
                    addEntry(weight);
                }
            }
        });
        return view;

    }

    private void setupLineChart() {
        lineChart.setDrawBorders(true);
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        // X 축 포맷터 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // 여기서 value는 Entry의 인덱스
                // Entry의 인덱스를 사용하여 해당 날짜를 가져오고, 원하는 형식으로 포맷
                Date date = new Date(); // 여기서는 임의의 날짜를 사용하니 실제로는 Entry에 해당하는 날짜로 설정해야.
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
                return sdf.format(date);
            }
        });
        xAxis.setLabelCount(5);
    }

    private void addEntry(float weight) {
        // 새로운 Entry를 생성하고 weightEntries 리스트에 추가
        Entry entry = new Entry(weightEntries.size(), weight);
        weightEntries.add(entry);

        // LineDataSet을 생성하고 weightEntries 리스트를 설정
        LineDataSet dataSet = new LineDataSet(weightEntries, "Weight");

        // LineData를 생성하고 dataSet을 설정
        LineData data = new LineData(dataSet);

        // LineChart에 데이터 설정
        lineChart.setData(data);

        // 데이터 변경을 알림
        lineChart.notifyDataSetChanged();

        // 그래프 다시 그리기
        lineChart.invalidate();
    }
}