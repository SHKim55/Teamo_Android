package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Pattern;

public class EmailConfirmationActivity extends AppCompatActivity {
    private ImageView tempBtn3;
    private EditText emailEdit, passcodeEdit;
    private Button emailCheckBtn, signupBtn;
    private TextView emailCondition, emailCheck, passcodeCheck;

    private String emailText, passcodeText, emailPasscodeText;
    private String idText, passwordText, nameText, deptNameText, admissionYearText;
    private Boolean checkEmail = false, checkValidEmail = false, checkPasscode = false;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        getDataFromIntent();
        initElements();
    }

    private void getDataFromIntent() {
        Intent prev_intent = getIntent();
        idText = prev_intent.getStringExtra("id");
        passwordText = prev_intent.getStringExtra("password");
        nameText = prev_intent.getStringExtra("name");
        deptNameText = prev_intent.getStringExtra("deptName");
        admissionYearText = prev_intent.getStringExtra("admissionYear");
    }

    private void initElements() {
        tempBtn3 = (ImageView) findViewById(R.id.img_teamo_emailconfirm);
        emailEdit = (EditText) findViewById(R.id.edit_email_emailconfirm);
        passcodeEdit = (EditText) findViewById(R.id.edit_passcode_emailconfirm);
        emailCheckBtn = (Button) findViewById(R.id.btn_email_validation_emailconfirm);
        signupBtn = (Button) findViewById(R.id.btn_signup_emailconfirm);
        emailCondition = (TextView) findViewById(R.id.text_email_condition_emailconfirm);
        emailCheck = (TextView) findViewById(R.id.text_email_check_emailconfirm);
        passcodeCheck = (TextView) findViewById(R.id.text_passcode_check_emailconfirm);

        initOnClickListeners();
    }

    private void initOnClickListeners() {
        tempBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailConfirmationActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {   // 다 맞으면 다음 activity로 넘어간다.
            @Override
            public void onClick(View view) {
                if(checkEmail && checkValidEmail && checkPasscode)
                    confirmRegister();
                else {
                    Toast.makeText(EmailConfirmationActivity.this, "입력하신 정보를 다시 한 번 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        emailCheckBtn.setOnClickListener(new View.OnClickListener() {   // emailCheckBtn 중복 확인
            @Override
            public void onClick(View view) { checkEmailValidity(); }
        });

        emailEdit.addTextChangedListener(new TextWatcher() {   // valid한 이메일인지 확인
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString();
                if(!Pattern.matches("^[a-zA-Z0-9]+@[cau.ac.kr]+$", email)) {
                    emailCondition.setVisibility(View.VISIBLE);
                    checkEmail = false;
                }
                else {
                    emailCondition.setVisibility(View.GONE);
                    emailText = email;
                    checkEmail = true;
                }
            }
        });

        passcodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String passcode = s.toString();
                passcodeCheck.setVisibility(View.GONE);
                passcodeText = passcode;
                checkPasscode = true;

                // 학교 이메일로 확인 메일 전송 및 인증번호 생성 필요
                // 인증번호와 입력한 값이 같으면 checkPasscode = true
                if(!passcode.equals(emailPasscodeText)) {
                    passcodeCheck.setVisibility(View.VISIBLE);
                    checkPasscode = false;
                }
                else {
                    passcodeCheck.setVisibility(View.GONE);
                    passcodeText = passcode;
                    checkPasscode = true;
                }
            }
        });
    }

    // 학교 이메일 형식 확인을 위한 서버 통신 파트
    private void checkEmailValidity() {
        queue = Volley.newRequestQueue(EmailConfirmationActivity.this);
        String emailValidationApi = getString(R.string.url) + "/user/email/validation/" + emailText;

        Log.e("email", emailValidationApi);
        final StringRequest request = new StringRequest(Request.Method.GET, emailValidationApi,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("true")) {  // 중복된 경우
                    emailCheck.setVisibility(View.VISIBLE);
                    checkValidEmail = false;
                } else {   // 없는 경우
                    checkValidEmail = true;
                    SendMail mail = new SendMail();
                    mail.sendSecurityCode(getApplicationContext(), emailText);
                    Log.i("email", emailText + "으로 전송함");
                    emailPasscodeText = mail.getCode();
                    Log.i("email", emailPasscodeText + "임");
                    //sendMail();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmailConfirmationActivity.this, "서버 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    private void sendMail() {

    }

    // 회원가입 완료 후 홈 화면으로 이동
    private void confirmRegister() {
        Intent next_intent = new Intent(EmailConfirmationActivity.this, LoginActivity.class);
        next_intent.putExtra("id", idText);
        next_intent.putExtra("password", passwordText);
        next_intent.putExtra("name", nameText);
        next_intent.putExtra("deptName", deptNameText);
        next_intent.putExtra("admissionYear", admissionYearText);

        finish();
        startActivity(next_intent);
    }
}