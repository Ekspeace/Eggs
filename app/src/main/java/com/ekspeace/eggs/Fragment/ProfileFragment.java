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

import java.util.HashMap;

import io.paperdb.Paper;

public class ProfileFragment extends Fragment {
    private static ProfileFragment instance;
    private EditText etName, etPhone, etPassword;
    private View layout;

    public static ProfileFragment getInstance() {
        if (instance == null) {
            instance = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        InitView(view);
        return view;
    }

    private void InitView(View view) {
        etName = view.findViewById(R.id.profile_name);
        etPhone = view.findViewById(R.id.profile_phone);
        etPassword = view.findViewById(R.id.profile_password);

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_container));

        view.findViewById(R.id.profile_button_update).setOnClickListener(view1 -> UpdateUserInfo());
    }

    private void UpdateUserInfo() {
        final String password = etPassword.getText().toString().trim();
        final String name = etName.getText().toString();
        final String phone = etPhone.getText().toString();

        boolean error = false;
        if (!error) {
            if (TextUtils.isEmpty(name)) {
                etName.setError("Name is required");
                error = true;
            }
            if (TextUtils.isEmpty(password)) {
                etPhone.setError("Password is Required.");
                error = true;
            } else if (password.length() < 6) {
                etPassword.setError("Password Must be greater than 6 Characters");
                error = true;
            }
            if (TextUtils.isEmpty(phone)) {
                etPhone.setError("Phone is Required.");
                error = true;
            } else if (phone.length() == 11) {
                etPhone.setError("Phone Must be 10 Numbers");
                error = true;
            }
            if (!password.equals(Common.currentUser.getPassword())) {
                etPassword.setError("Password is incorrect");
                error = true;
            }

            if (error)
                return;
        }
        EventBus.getDefault().postSticky(new LoadingEvent(true));
        if (Common.isOnline(getContext())) {
            FirebaseAuth.getInstance().updateCurrentUser(FirebaseAuth.getInstance().getCurrentUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(id);
                        HashMap user = new HashMap();
                        user.put("name", name);
                        user.put("phone", phone);
                        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                PopUp.Toast(getContext(), layout, "Profile Updated successfully", Toast.LENGTH_LONG);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                EventBus.getDefault().postSticky(new LoadingEvent(false));
                                PopUp.Toast(getContext(), layout, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                            }
                        });

                    } else {
                        EventBus.getDefault().postSticky(new LoadingEvent(false));
                        PopUp.Toast(getContext(), layout, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT);
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
}


