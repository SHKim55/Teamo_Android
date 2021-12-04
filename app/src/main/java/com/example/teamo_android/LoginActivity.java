package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText idEdit, passwordEdit;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initElements();
    }

    private void initElements() {
        Button loginBtn = (Button) findViewById(R.id.btn_confirm_login);
        idEdit = (EditText) findViewById(R.id.edit_id_login);
        passwordEdit = (EditText) findViewById(R.id.edit_password_login);
        TextView signupBtn = (TextView) findViewById(R.id.btn_signup_login);

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

    private void saveToken(JSONObject response) throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Authorization", response.get("access_token").toString());
        Log.i("access_token", response.get("access_token").toString());
        editor.commit();
        editor.apply();
    }

    private void logIn() {
        String idText = idEdit.getText().toString();  // id
        String passwordText = passwordEdit.getText().toString();  // password

        String loginApi = getString(R.string.url) + "/auth/login";
        queue = Volley.newRequestQueue(LoginActivity.this);

        // 로그인 객체 생성
        JSONObject loginObject = new JSONObject();
        try {
            loginObject.put("userid", idText);
            loginObject.put("password", passwordText);
        } catch (JSONException e) {
            Log.e("login_JSONException", e.toString());
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginApi, loginObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            saveToken(response);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("save_token_error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                Toast.makeText(LoginActivity.this, "등록되지 않은 사용자입니다.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}