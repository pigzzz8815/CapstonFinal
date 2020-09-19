package com.example.capstonfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    EditText mEmailText, mPasswordText, mPasswordcheckText, mName;
    Button mregisterBtn, mvalidateBtn;


    private FirebaseAuth firebaseAuth;

    private boolean isValidEamil(String email){
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd(String pwd) {
        return PASSWORD_PATTERN.matcher(pwd).matches();
    }

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{6,20}$");
    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
        firebaseAuth =  FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        mEmailText = findViewById(R.id.signup_email2);
        mPasswordText = findViewById(R.id.signup_pw2);
        mPasswordcheckText = findViewById(R.id.signup_pwck2);
        mregisterBtn = findViewById(R.id.btn_login2);
        mvalidateBtn = findViewById(R.id.signup_emailchk);
        mName = findViewById(R.id.signup_name2);

        //파이어베이스 user 로 접근



        //중복체크버튼 클릭리스너
        mvalidateBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final String email = mEmailText.getText().toString().trim();


                if(!isValidEamil(email) || email.length() == 0){
                    Toast.makeText(SignupActivity.this, "이메일이 공백이거나 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    mEmailText.setText("");
                    mEmailText.requestFocus();
                }else {
                    Toast.makeText(SignupActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                    /*val intentAuth = Intent(this, Auth::class.java)
                    intentAuth.putExtra("이메일 값", signup_email.text.toString())
                    startActivity(intentAuth)//이메일 인증 액티비티로 넘어감*/
                }
            }
        });

        //가입버튼 클릭리스너   -->  firebase에 데이터를 저장한다.
        mregisterBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                /*//이메일 인증을 위한 actioncodesetting
                final ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                        .setUrl("https://capstonfinal.page.link/6RQi")
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName("com.example.capstonfirst", false, null)
                        .build();*/

                //가입 정보 가져오기
                final String email = mEmailText.getText().toString().trim();
                final String pwd = mPasswordText.getText().toString().trim();
                String pwdcheck = mPasswordcheckText.getText().toString().trim();

                if(email.length() == 0){
                    Toast.makeText(SignupActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pwd.length() == 0 || !isValidPasswd(pwd) || pwd.length() < 6){
                    Toast.makeText(SignupActivity.this, "비밀번호가 공백이거나 형식이 맞지 않습니다. Tip : 숫자, 영문, 특수문자 조합 6~16자리로 해야합니다.", Toast.LENGTH_SHORT).show();
                    mPasswordText.setText("");
                    mPasswordText.requestFocus();
                    return;
                }

                if(pwd.equals(pwdcheck)) {
                    Log.d(TAG, "등록 버튼 " + email + " , " + pwd);
                    final ProgressDialog mDialog = new ProgressDialog(SignupActivity.this);
                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();

                    //파이어베이스에 신규계정 등록하기
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //가입 성공시
                            if (task.isSuccessful()) {


                                //사용자 인증메일 보내기.
                                mDialog.dismiss();

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = mName.getText().toString().trim();

                                //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                HashMap<Object, String> hashMap = new HashMap<>();

                                hashMap.put("uid", uid);
                                hashMap.put("email", email);
                                hashMap.put("name", name);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).setValue(hashMap);

                                /*//인증과정
                                sendSignInLink(email, actionCodeSettings);


                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                Intent intentEmail = getIntent();
                                String emailLink = intentEmail.getData().toString();
                                if(auth.isSignInWithEmailLink(emailLink)){
                                    String eml = "pigzzz0000@naver.com";

                                    auth.signInWithEmailLink(eml, emailLink)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful()){
                                                        Log.d(TAG, "이메일 링크 인증이 완료되었습니다!");
                                                        AuthResult result = task.getResult();
                                                    }else {
                                                        Log.e(TAG, "이메일 링크 인증이 정상적으로 되지 않았습니다.");
                                                        return;
                                                    }
                                                }
                                            });*/
                                //가입이 이루어져을시 가입 화면을 빠져나감.
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(SignupActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            }else {
                                mDialog.dismiss();
                                Toast.makeText(SignupActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                mEmailText.setText("");
                                return;  //해당 메소드 진행을 멈추고 빠져나감.
                            }

                        }
                    });

                    //비밀번호 오류시
                }else{

                    Toast.makeText(SignupActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }

    public void sendSignInLink(String email, ActionCodeSettings actionCodeSettings) {
        // [START auth_send_sign_in_link]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
        // [END auth_send_sign_in_link]
    }
}

