package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private EditText idEdit, passwordEdit;
    private TextView signupBtn;

    private String idText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initElements();
    }

    private void initElements() {
        loginBtn = (Button) findViewById(R.id.btn_confirm_login);
        idEdit = (EditText) findViewById(R.id.edit_id_login);
        passwordEdit = (EditText) findViewById(R.id.edit_password_login);
        signupBtn = (TextView) findViewById(R.id.btn_signup_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { logIn(); }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void logIn() {
        idText = idEdit.getText().toString();
        passwordText = passwordEdit.getText().toString();

        // 서버랑 id, pw 비교하는 문장 if 안에 작성
        if(true) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("id", idText);
            intent.putExtra("password", passwordText);

            finish();
            startActivity(intent);
        }
        else {
            Toast.makeText(LoginActivity.this, "아이디, 비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show();
        }
    }
}