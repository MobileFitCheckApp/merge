package isy.mjc.fitcheckapp_1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private AlertDialog dialog;

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

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = etId.getText().toString();
                final String userPasswd = etPasswd.getText().toString();
                // ID와 비밀번호가 비어 있는지 확인
                if (TextUtils.isEmpty(userID) || TextUtils.isEmpty(userPasswd)) {
                    Toast.makeText(LoginActivity.this, "ID 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                //success키의 value에 따라 로그인 성공 / 계정 재확인 처리
                                if (success) {
                                    Log.d("mytest", "로그인 성공");
                                    // 로그인 성공 처리
                                    Toast.makeText(LoginActivity.this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                    // ID에 따라 다른 액티비티로 이동
                                    Intent intent;
                                    if (userID.contains("admin")) {
                                        intent = new Intent(LoginActivity.this, MainActivityAD.class);
                                        intent.putExtra("USER_ID", userID);
                                    } else {
                                        intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("USER_ID", userID);
                                    }
                                    startActivity(intent);
                                    finish(); // 현재 액티비티 종료
                                } else {
                                    Log.d("mytest", "로그인 실패");
                                    // 계정 재확인 처리
                                    Toast.makeText(LoginActivity.this, "ID 또는 비밀번호가 일치하지 않습니다. 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e("mytest", "예외 발생", e);
                                e.printStackTrace();
                            }

                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(userID, userPasswd, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this.getApplicationContext());
                    queue.add(loginRequest);
                }
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

