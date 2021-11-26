package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.time.Duration;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private ImageView tempBtn2;
    private EditText idEdit, passwordEdit, nameEdit;
    private Button idCheckBtn, nextBtn;
    private TextView idCondition, passwordCondition, idCheck;
    private Spinner deptNameSpin, admissionYearSpin;

    private String idText, passwordText, nameText, deptNameText, admissionYearText;
    private Boolean checkId = false, checkValidId = false, checkPassword = false, checkName = false, checkDeptName = false, checkAdmissionYear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initElements();
    }

    private void initElements() {
        tempBtn2 = (ImageView) findViewById(R.id.img_teamo_signup);
        idEdit = (EditText) findViewById(R.id.edit_id_signup);
        passwordEdit = (EditText) findViewById(R.id.edit_password_signup);
        nameEdit = (EditText) findViewById(R.id.edit_name_signup);
        idCheckBtn = (Button) findViewById(R.id.btn_id_validation_signup);
        nextBtn = (Button) findViewById(R.id.btn_next_signup);
        idCondition = (TextView) findViewById(R.id.text_id_condition_signup);
        passwordCondition = (TextView) findViewById(R.id.text_password_condition_signup);
        idCheck = (TextView) findViewById(R.id.text_id_check_signup);
        deptNameSpin = (Spinner) findViewById(R.id.spin_dept_name_signup);
        admissionYearSpin = (Spinner) findViewById(R.id.spin_admission_year_signup);

        initOnClickListeners();
    }

    private void initOnClickListeners() {
        tempBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkId && checkValidId && checkPassword && checkName && checkDeptName && checkAdmissionYear)
                    continueRegister();
                else {
                    Toast.makeText(SignupActivity.this, "입력하신 정보를 다시 한 번 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        idCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { checkIdValidity(); }
        });

        idEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String id = idEdit.getText().toString();

                if((id.length() < 5) || !(Pattern.matches("^[a-zA-Z0-9]*$", id))) {
                    idCondition.setVisibility(View.VISIBLE);
                    checkId = false;
                }
                else {
                    idCondition.setVisibility(View.GONE);
                    idText = id;
                    checkId = true;
                }
            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordEdit.getText().toString();

                if((password.length() < 7) || !(Pattern.matches("^[a-zA-Z0-9]*$", password))) {
                    passwordCondition.setVisibility(View.VISIBLE);
                    checkPassword = false;
                }
                else {
                    passwordCondition.setVisibility(View.GONE);
                    passwordText = password;
                    checkPassword = true;
                }
            }
        });

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                nameText = nameEdit.getText().toString();
                checkName = true;
            }
        });

        deptNameSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deptNameText = parent.getItemAtPosition(position).toString();
                checkDeptName = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                deptNameText = "";
                checkDeptName = false;
            }
        });

        admissionYearSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                admissionYearText = parent.getItemAtPosition(position).toString();
                checkAdmissionYear = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                admissionYearText = "";
                checkAdmissionYear = false;
            }
        });
    }

    // ID 중복확인을 위한 서버 통신 파트
    private void checkIdValidity() {


        idCheck.setVisibility(View.VISIBLE);
        checkValidId = true;
        Toast.makeText(SignupActivity.this, "중복확인 버튼 클릭", Toast.LENGTH_SHORT).show();
    }

    // 사용자 정보 저장을 위한 서버 통신 파트
    private void continueRegister() {
        Intent next_intent = new Intent(SignupActivity.this, EmailConfirmationActivity.class);
        next_intent.putExtra("id", idText);
        next_intent.putExtra("password", passwordText);
        next_intent.putExtra("name", nameText);
        next_intent.putExtra("deptName", deptNameText);
        next_intent.putExtra("admissionYear", admissionYearText);

        String data = idText + " " + passwordText + " " + nameText + " " + deptNameText + " " + admissionYearText;
        Log.d("Data", data);

        startActivity(next_intent);
    }
}