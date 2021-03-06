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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class EmailConfirmationActivity extends AppCompatActivity {
    private ImageView backBtn;
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
        backBtn = (ImageView) findViewById(R.id.btn_back_emailconfirm);
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
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailConfirmationActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {   // ??? ????????? ?????? activity??? ????????????.
            @Override
            public void onClick(View view) {
                if(checkEmail && checkValidEmail && checkPasscode)
                    confirmRegister();
                else {
                    Toast.makeText(EmailConfirmationActivity.this, "???????????? ????????? ?????? ??? ??? ??????????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        emailCheckBtn.setOnClickListener(new View.OnClickListener() {   // emailCheckBtn ?????? ??????
            @Override
            public void onClick(View view) { checkEmailValidity(); }
        });

        emailEdit.addTextChangedListener(new TextWatcher() {   // valid??? ??????????????? ??????
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

                // ?????? ???????????? ?????? ?????? ?????? ??? ???????????? ?????? ??????
                // ??????????????? ????????? ?????? ????????? checkPasscode = true
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

    // ?????? ????????? ?????? ????????? ?????? ?????? ?????? ??????
    private void checkEmailValidity() {
        queue = Volley.newRequestQueue(EmailConfirmationActivity.this);
        String emailValidationApi = getString(R.string.url) + "/user/email/validation/" + emailText;

        Log.e("email", emailValidationApi);
        final StringRequest request = new StringRequest(Request.Method.GET, emailValidationApi,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("true")) {  // ????????? ??????
                    emailCheck.setVisibility(View.VISIBLE);
                    checkValidEmail = false;
                } else {   // ?????? ??????
                    checkValidEmail = true;
                    SendMail mail = new SendMail();
                    mail.sendSecurityCode(getApplicationContext(), emailText);
                    Log.i("email", emailText + "?????? ?????????");
                    emailPasscodeText = mail.getCode();
                    Log.i("email", emailPasscodeText + "???");
                    signUp();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmailConfirmationActivity.this, "?????? ?????? ??? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    private void signUp() {
        JSONObject signUpObject = new JSONObject();
        try {
            signUpObject.put("id", idText);
            signUpObject.put("password", passwordText);
            signUpObject.put("name", nameText);
            signUpObject.put("email", emailText);
            signUpObject.put("department", deptNameText);
            signUpObject.put("std_num", admissionYearText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue = Volley.newRequestQueue(EmailConfirmationActivity.this);
        String signUpApi = getString(R.string.url) + "/user";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, signUpApi, signUpObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("signUp_success", "???????????? ??????");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmailConfirmationActivity.this, "?????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    // ???????????? ?????? ??? ??? ???????????? ??????
    private void confirmRegister() {
        Intent next_intent = new Intent(EmailConfirmationActivity.this, LoginActivity.class);
        finish();
        startActivity(next_intent);
    }
}