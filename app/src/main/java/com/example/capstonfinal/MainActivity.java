package com.example.capstonfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button mLoginBtn, mResigeterBtn;
    EditText mEmailText, mPasswordText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth =  FirebaseAuth.getInstance();
        //버튼 등록하기
        mResigeterBtn = findViewById(R.id.btn_register2);
        mLoginBtn = findViewById(R.id.btn_login2);
        mEmailText = findViewById(R.id.signup_email2);
        mPasswordText = findViewById(R.id.signup_name2);


        //가입 버튼이 눌리면
        mResigeterBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(MainActivity.this,SignupActivity.class));

            }
        });

        //로그인 버튼이 눌리면
        mLoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email = mEmailText.getText().toString().trim();
                String pwd = mPasswordText.getText().toString().trim();

                if(email.length() == 0 || pwd.length() == 0){
                    Toast.makeText(MainActivity.this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email,pwd)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(MainActivity.this, LoginAcitivity.class);
                                    startActivity(intent);

                                }else{
                                    Toast.makeText(MainActivity.this,"로그인 오류",Toast.LENGTH_SHORT).show();
                                    mEmailText.setText("");
                                    mPasswordText.setText("");
                                    mEmailText.requestFocus();
                                }
                            }
                        });

            }
        });
    }
}


/*public class MainActivity extends AppCompatActivity {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.et_eamil);
        editTextPassword = findViewById(R.id.et_password);
    }

    public void singUp(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            createUser(email, password);
        }
    }

    public void signIn(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 회원가입
    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(MainActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(MainActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 로그인
    private void loginUser(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}*/
