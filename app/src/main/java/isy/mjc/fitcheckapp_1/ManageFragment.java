package isy.mjc.fitcheckapp_1;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment {
    EditText addId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String uesrID;

    public ManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment ManageFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static ManageFragment newInstance(String userId) {
        ManageFragment fragment = new ManageFragment();
        Bundle args = new Bundle();
        args.putString("USER_ID", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //유저 아이디 저장하기
            uesrID = getArguments().getString("USER_ID");
        }
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        ListView listview = view.findViewById(R.id.managelist);
        ManageListAdapter adapter = new ManageListAdapter();

        Log.d("dd","유저 아이디"+uesrID);
        //아이디는 넘겨받았지만 ,,, 코드가 아니라 미쓰..
        adapter.addItem(new ManageListItem("김회원","ms.kim"));
        listview.setAdapter(adapter);

        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_addmem, container, false);

        addId = dialogView.findViewById(R.id.addId);

        // 플로팅버튼 눌러서 항목 추가
        FloatingActionButton floatingBtn = view.findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.setView(dialogView);
                dlg.setNegativeButton("취소하기", null);
                //등록하기 버튼 누르면 아이디 보내서 등록하기
                dlg.setPositiveButton("등록하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String id = addId.getText().toString();
                        Log.d("TAG", "onClick: "+id);

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    //오류가 납니다..
                                    if (success) {
                                        String name = jsonResponse.getString("name");
                                        String memID = jsonResponse.getString("memID");
                                        adapter.addItem(new ManageListItem(name,memID));
                                        listview.setAdapter(adapter);
                                        Log.d("TAG", "onResponse: "+name+","+memID);

                                    } else {
                                        Log.d("fail","수강생 등록 x");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        ManageAddRequest MARequest = new ManageAddRequest(id,uesrID,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        queue.add(MARequest);

                    }
                });
                dlg.show(); // 다이얼로그 표시
            }
        });
        return view;
    }
}
