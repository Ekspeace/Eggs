package com.ekspeace.eggs.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ekspeace.eggs.Constants.Common;
import com.ekspeace.eggs.Constants.PopUp;
import com.ekspeace.eggs.Model.EventBus.LoadingEvent;
import com.ekspeace.eggs.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.eggs.Model.EventBus.RegisterSuccessEvent;
import com.ekspeace.eggs.Model.User;
import com.ekspeace.eggs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.paperdb.Paper;

public class RegisterFragment extends Fragment {
    private static RegisterFragment instance;
    private EditText nameText, phoneText, emailText, passwordText, passwordConfirmText;
    private Button btnRegister;
    private View layout;
    public static RegisterFragment getInstance() {
        if (instance == null) {
            instance = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);
        return view;
    }
    private void initView(View view){
        nameText = view.findViewById(R.id.register_name_et);
        phoneText = view.findViewById(R.id.register_phone_et);
        emailText = view.findViewById(R.id.register_email_et);
        passwordText = view.findViewById(R.id.register_password_et);
        passwordConfirmText = view.findViewById(R.id.register_confirm_password_et);
        btnRegister = view.findViewById(R.id.register_btn);

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_container));
        events();
    }
    private void events(){
        btnRegister.setOnClickListener(view -> RegistrationProcess());
    }
    private void RegistrationProcess(){
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();
        final String name = nameText.getText().toString();
        final String confirm = passwordConfirmText.getText().toString();
        final String phone = phoneText.getText().toString();

        boolean error = false;
        if (!error) {
            if (TextUtils.isEmpty(email)) {
                emailText.setError("Email is Required.");
                error = true;
            }
            if (TextUtils.isEmpty(name)) {
                nameText.setError("Name is required");
                error = true;
            }
            if (TextUtils.isEmpty(password)) {
                passwordText.setError("Password is Required.");
                error = true;
            } else if (password.length() < 6) {
                passwordText.setError("Password Must be greater than 6 Characters");
                error = true;
            }
            if (TextUtils.isEmpty(phone)) {
                phoneText.setError("Phone is Required.");
                error = true;
            } else if (phone.length() == 11) {
                phoneText.setError("Phone Must be 10 Numbers");
                error = true;
            }
            if (!password.equals(confirm)) {
                passwordText.setError("Password does not match");
                passwordConfirmText.setError("Password does not match");
                error = true;
            }

            if (error)
                return;
        }
        EventBus.getDefault().postSticky(new LoadingEvent(true));
        if (Common.isOnline(getContext())) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        PopUp.Toast(getContext(), layout,"Registration successfully",Toast.LENGTH_LONG);
                        Paper.book().destroy();
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(id);
                        User user = new User();
                        user.setId(id);
                        user.setName(name);
                        user.setPhone(phone);
                        user.setEmail(email);
                        user.setPassword(password);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                EventBus.getDefault().postSticky(new RegisterSuccessEvent(true));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                EventBus.getDefault().postSticky(new LoadingEvent(false));
                                PopUp.Toast(getContext(), layout, "Error ! " + task.getException().getMessage(),Toast.LENGTH_SHORT);
                            }
                        });

                    }else {
                        EventBus.getDefault().postSticky(new LoadingEvent(false));
                        PopUp.Toast(getContext(), layout, "Error ! " + task.getException().getMessage(),Toast.LENGTH_SHORT);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    EventBus.getDefault().postSticky(new LoadingEvent(false));
                    PopUp.Toast(getContext(), layout, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT);
                }
            });
        } else {
            EventBus.getDefault().postSticky(new LoadingEvent(false));
            PopUp.Network(getContext(), "Connection", "Please check your internet connectivity and try again");
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
            RegistrationProcess();
        }
    }
}