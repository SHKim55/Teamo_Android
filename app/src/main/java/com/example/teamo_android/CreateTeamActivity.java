package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.teamo_android.databinding.ActivityCreateTeamBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreateTeamActivity extends AppCompatActivity {
    private RequestQueue queue;
    private String subjectName, semesterYear, semesterMonth, professorName, classNumber;
    private boolean checkSubject = false, checkSemesterYear = false, checkSemesterMonth = false, checkProfessor = false, checkClassNumber = false;
    private String year = null, month = null;
    private ActivityCreateTeamBinding binding;
    private ArrayAdapter yearAdapter, monthAdapter;
    private Team newTeam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_team);
        initElements();
        initOnClickListener();

    }

    private void initElements() {
        yearAdapter = ArrayAdapter.createFromResource(
                this, R.array.year, R.layout.spinner_item);
        yearAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.semesterYearSpinner.setAdapter(yearAdapter);
        monthAdapter = ArrayAdapter.createFromResource(
                this, R.array.month, R.layout.spinner_item
        );
        monthAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.semesterMonthSpinner.setAdapter(monthAdapter);
    }

    private void initOnClickListener() {
        binding.semesterYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checkSemesterYear = i != 0;    // 0이면 false, 나머지는 true
                year = binding.semesterYearSpinner.getItemAtPosition(i).toString();
                Toast.makeText(CreateTeamActivity.this, binding.semesterYearSpinner.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.semesterMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checkSemesterMonth = i != 0;
                String monthText = binding.semesterMonthSpinner.getItemAtPosition(i).toString();
                month = String.valueOf(monthText.charAt(0)) + "S";
                Toast.makeText(CreateTeamActivity.this, binding.semesterMonthSpinner.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.inputNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewTeam();
                transferToIntent();
            }
        });
    }

    private void setNewTeam() {
        String subjectText = binding.subjectEdit.getText().toString();
        if(!subjectText.equals("")) {
            checkSubject = true;
            newTeam.setSubject(subjectText);
        }
        else checkSubject = false;

        String professorText = binding.professorNameEdit.getText().toString();
        if(!professorText.equals("")) {
            checkProfessor = true;
            newTeam.setProfessor(professorText);
        }
        else checkProfessor = false;

        String classNumberText = binding.classEdit.getText().toString();
        if(!classNumberText.equals("")) {
            checkClassNumber = true;
            newTeam.setCourseClass(classNumberText);
        }
        else checkClassNumber = false;
    }

    // 입력된 정보를 Intent 로 전송
    private void transferToIntent() {
        if (checkSubject && checkProfessor && checkSemesterMonth && checkSemesterYear && checkClassNumber) {
            Intent intent = new Intent(CreateTeamActivity.this, CreatePostContentActivity.class);
            intent.putExtra("team", newTeam);
            startActivity(intent);
        } else {
            Toast.makeText(CreateTeamActivity.this, "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}