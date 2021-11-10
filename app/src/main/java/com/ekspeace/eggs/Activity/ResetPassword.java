package com.ekspeace.eggs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.eggs.Constants.Common;
import com.ekspeace.eggs.Constants.PopUp;
import com.ekspeace.eggs.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.eggs.Model.EventBus.RegisterSuccessEvent;
import com.ekspeace.eggs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ResetPassword extends AppCompatActivity {
    private Button btnResetPassword;
    private TextView tvBack;
    private EditText etEmail;
    private ImageView ivBack;
    private LinearLayout loading;
    private View layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initView();
        event();
    }
    private void initView(){
        tvBack = findViewById(R.id.ResetPassword_back);
        btnResetPassword = findViewById(R.id.ResetPassword);
        etEmail = findViewById(R.id.reset_password_email_et);
        loading = findViewById(R.id.Progressbar_reset);
        ivBack = findViewById(R.id.reset_password_back);
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));
    }
    private void event(){
        tvBack.setOnClickListener(v -> {
            startActivity(new Intent(ResetPassword.this, Login.class));
            EventBus.getDefault().postSticky(new RegisterSuccessEvent(true));
        });
        btnResetPassword.setOnClickListener(v -> ResetPassword());
        ivBack.setOnClickListener(view -> onBackPressed());
    }
    private void ResetPassword(){
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter your email");
            return;
        }
        loading.setVisibility(View.VISIBLE);
        if (Common.isOnline(ResetPassword.this)) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loading.setVisibility(View.GONE);
                                PopUp.Toast(ResetPassword.this, layout, "Check your email to reset your password!", Toast.LENGTH_SHORT);
                                startActivity(new Intent(ResetPassword.this, Login.class));
                            } else {
                                loading.setVisibility(View.GONE);
                                PopUp.Toast(ResetPassword.this, layout, "Fail to send reset password email, re-enter the email! ", Toast.LENGTH_SHORT);
                                startActivity(new Intent(ResetPassword.this, Login.class));
                            }
                        }
                    });
        } else {
            PopUp.Network(ResetPassword.this, "Connection...", "Please check your internet connectivity and try again");
            loading.setVisibility(View.GONE);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Connection(NetworkConnectionEvent event) {
        if (event.isNetworkConnected()) {
            ResetPassword();
        }
    }
}