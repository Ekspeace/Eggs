package com.ekspeace.eggs.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.eggs.Activity.Main;
import com.ekspeace.eggs.Activity.ResetPassword;
import com.ekspeace.eggs.Constants.Common;
import com.ekspeace.eggs.Constants.PopUp;
import com.ekspeace.eggs.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.eggs.Model.EventBus.LoadingEvent;
import com.ekspeace.eggs.Model.User;
import com.ekspeace.eggs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import io.paperdb.Paper;

public class LoginFragment extends Fragment {
    private static LoginFragment instance;
    private EditText emailText, passwordText;
    private CheckBox cbRemember;
    private TextView forgetView;
    private Button btnLogin;
    private View layout;

    public static LoginFragment getInstance() {
        if (instance == null) {
            instance = new LoginFragment();
        }
        return instance;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
               View view = inflater.inflate(R.layout.fragment_login, container, false);
               initView(view);
               event();
               return view;
    }
    private void initView(View view){
        emailText = view.findViewById(R.id.login_email_et);
        passwordText = view.findViewById(R.id.login_password_et);
        cbRemember = view.findViewById(R.id.login_remember_cb);
        forgetView = view.findViewById(R.id.login_forgot_password);
        btnLogin = view.findViewById(R.id.login_btn);

        Paper.init(getContext());
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_container));

        String UserEmailKey = Paper.book().read(Common.UserEmailKey);
        String UserPasswordKey = Paper.book().read(Common.UserPasswordKey);
        if (UserEmailKey != null) {
            emailText.setText(UserEmailKey);
            passwordText.setText(UserPasswordKey);
        }

    }
    private void event(){
        btnLogin.setOnClickListener(view -> {LoginProcess();});
        forgetView.setOnClickListener(view -> startActivity(new Intent(getContext(), ResetPassword.class)));
    }
    private void LoginProcess() {
        String User_Email = emailText.getText().toString().trim();
        String User_Password = passwordText.getText().toString().trim();

        boolean error = false;
        if (!error) {
            if (TextUtils.isEmpty(User_Email)) {
                emailText.setError("Email is Required.");
                error = true;
            }
            if (TextUtils.isEmpty(User_Password)) {
                passwordText.setError("Password is Required.");
                error = true;
            } else if (User_Password.length() < 6) {
                passwordText.setError("Password Must be greater 5 Characters");
                error = true;
            }
            if (error)
                return;

        }
        EventBus.getDefault().postSticky(new LoadingEvent(true));
        if (cbRemember.isChecked()) {
            Paper.book().write(Common.UserEmailKey, User_Email);
            Paper.book().write(Common.UserPasswordKey, User_Password);
        }
        if (Common.isOnline(getContext())) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(User_Email, User_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(Objects.requireNonNull(task.getResult()).getUser().getUid());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    User user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                                    assert user != null;
                                    user.setName(task.getResult().get("name").toString());
                                    user.setEmail(task.getResult().get("email").toString());
                                    user.setPassword(task.getResult().get("password").toString());
                                    user.setPhone(task.getResult().get("phone").toString());
                                    user.setId(task.getResult().get("id").toString());
                                    if(!User_Password.equals(user.getPassword())){
                                        docRef.update("password", User_Password);
                                    }
                                    Common.currentUser = user;
                                    PopUp.Toast(getContext(), layout, "Your have logged in successfully", Toast.LENGTH_SHORT);
                                    Intent myIntent = new Intent(getContext(), Main.class);
                                    startActivity(myIntent);
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                PopUp.Toast(getContext(), layout, e.getMessage(), Toast.LENGTH_SHORT);
                                EventBus.getDefault().postSticky(new LoadingEvent(false));
                            }
                        });
                    } else {
                        PopUp.Toast(getContext(), layout, "Please Check your credential and try again", Toast.LENGTH_SHORT);
                        EventBus.getDefault().postSticky(new LoadingEvent(false));
                    }
                }
            });
        } else {
            PopUp.Network(getContext(), "Login failed...", "Please check your internet connectivity and try again");
            EventBus.getDefault().postSticky(new LoadingEvent(false));
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
            LoginProcess();
        }
    }
}