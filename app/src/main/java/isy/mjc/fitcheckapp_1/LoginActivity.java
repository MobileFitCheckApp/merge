package isy.mjc.fitcheckapp_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    //TODO: 이 액티비티가 일단 메인으로 작동함
    // 라디오버튼 파악해서 main액티비티나 mainad액티비티로 이동
    // ad => admin

    private AlertDialog dialog;

    //라디오버튼
    RadioGroup rdGroup;
    RadioButton mem, adm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etId = findViewById(R.id.etId);
        final EditText etPasswd = findViewById(R.id.etPasswd);
        final Button btnlogin = findViewById(R.id.btnlogin);
        final TextView tvIdFind = findViewById(R.id.tvIdFind);
        final TextView tvPwFind = findViewById(R.id.tvPwFind);
        final TextView tvSign = findViewById(R.id.tvSign);

        //라디오버튼
        rdGroup = findViewById(R.id.rdGroup);
        mem = findViewById(R.id.radio_mem);
        adm = findViewById(R.id.radio_adm);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = etId.getText().toString();
                final String userPasswd = etPasswd.getText().toString();

                //TODO 로그인에서 화면 넘어가도록 설계하기 -> 원래는 아님
                    Log.d("mytest","라디오버튼");
                    // 선택된 라디오 버튼의 id를 가져옴
                    int selectedId = rdGroup.getCheckedRadioButtonId();

                    Intent memintent = new Intent(LoginActivity.this, MainActivity.class);
                    Intent admintent = new Intent(LoginActivity.this, MainActivityAD.class);

                    // 선택된 라디오 버튼이 없을 경우
                    if (selectedId == -1) {
                        Toast.makeText(LoginActivity.this, "No option selected", Toast.LENGTH_SHORT).show();
                    } else {
                        // 선택된 라디오 버튼을 가져옴
                        RadioButton selectedRadioButton = findViewById(selectedId);
                        if (selectedRadioButton == mem) {
                            startActivity(memintent);
                        } else {
                            startActivity(admintent);
                        }
                    }
//
//                    Log.d("mytest","로그인확인");
//                    Response.Listener<String> responseListener = new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonResponse = new JSONObject(response);
//                                boolean success = jsonResponse.getBoolean("success");
//
//                                //success키의 value에 따라 로그인 성공 / 계정 재확인 처리
//                                if (success) {
//                                    // 로그인 성공 처리
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                                    builder.setMessage("로그인에 성공했습니다.")
//                                            .setPositiveButton("확인", null)
//                                            .create()
//                                            .show();
//                                } else {
//                                    // 계정 재확인 처리
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                                    builder.setMessage("ID 또는 비밀번호가 일치하지 않습니다. 다시 확인해 주세요.")
//                                            .setNegativeButton("확인", null)
//                                            .create()
//                                            .show();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    };
//                    LoginRequest loginRequest = new LoginRequest(userID, userPasswd, responseListener);
//                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this.getApplicationContext());
//                    queue.add(loginRequest);
//                }
            }
        });

        tvIdFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아이디 찾기 화면으로 이동
                Intent idFindIntent = new Intent(LoginActivity.this, IdFindActivity.class);
                startActivity(idFindIntent);
            }
        });

        tvPwFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비밀번호 찾기 화면으로 이동
                Intent pwFindIntent = new Intent(LoginActivity.this, PwFindActivity.class);
                startActivity(pwFindIntent);
            }
        });

        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 화면으로 이동
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}

