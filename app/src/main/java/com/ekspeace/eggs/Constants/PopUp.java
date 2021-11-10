package com.ekspeace.eggs.Constants;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.ekspeace.eggs.Activity.Login;
import com.ekspeace.eggs.Model.Basket;
import com.ekspeace.eggs.Model.EventBus.DeleteEvent;
import com.ekspeace.eggs.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.eggs.R;
import com.ekspeace.eggs.ViewModel.BasketViewModel;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class PopUp extends AppCompatActivity {

    static TextView tvTitle, tvDesc;
    static ImageView imIcon;
    static Button btnClose, btnConfirm;
    static LinearLayout linearLayout;
    static CardView cardView;

    public static void About(Context context){
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.layout_about);
        btnClose = alertDialog.findViewById(R.id.about_close);

        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        btnClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
    public static void Contact(Context context){
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.layout_contact);
        btnClose = alertDialog.findViewById(R.id.contact_close);

        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        btnClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
    public static void Policy(Context context){
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.layout_policy);
        btnClose = alertDialog.findViewById(R.id.policy_close);

        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        btnClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
    public static void SignOut(Context context) {
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.pop_up);

        tvTitle = alertDialog.findViewById(R.id.dialog_title);
        tvDesc = alertDialog.findViewById(R.id.dialog_desc);
        imIcon = alertDialog.findViewById(R.id.dialog_icon);
        btnClose = alertDialog.findViewById(R.id.dialog_close);
        btnConfirm = alertDialog.findViewById(R.id.dialog_confirm);



        tvTitle.setText("Sign Out");
        tvDesc.setText("Are you sure? You want to sign out?");
        imIcon.setImageResource(R.drawable.exit);

        btnConfirm.setVisibility(View.VISIBLE);
        btnClose.setVisibility(View.VISIBLE);


        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        btnClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        btnConfirm.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Paper.book().destroy();
            context.startActivity(new Intent(context, Login.class));
        });

        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
    public static void DeleteDialog(Context context, String title, String message, BasketViewModel basketViewModel, List<Basket> basketList, int Position, View layout) {
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.pop_up);

        TextView tvTitle = alertDialog.findViewById(R.id.dialog_title);
        ImageView imIcon = alertDialog.findViewById(R.id.dialog_icon);
        TextView tvDesc = alertDialog.findViewById(R.id.dialog_desc);
        TextView btnClose = alertDialog.findViewById(R.id.dialog_close);
        TextView btnConfirm = alertDialog.findViewById(R.id.dialog_confirm);

        tvTitle.setText(title);
        tvDesc.setText(message);
        imIcon.setVisibility(View.GONE);
        btnClose.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setText("Yes");
        btnClose.setText("No");

        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }

        btnClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        btnConfirm.setOnClickListener(v -> {
            Basket basket = new Basket(basketList.get(Position).getId());
            basketViewModel.delete(basket);
            Toast(context, layout,"Item deleted successfully", Toast.LENGTH_SHORT);
            alertDialog.dismiss();

        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
    public static void Toast(Context context, View layout, String message, int duration) {
        tvDesc = layout.findViewById(R.id.toast_message);
        linearLayout = layout.findViewById(R.id.custom_toast_container);

        tvDesc.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 40);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
    public static void Network(Context context, String title, String message) {
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.pop_up);
        tvTitle = alertDialog.findViewById(R.id.dialog_title);
        tvDesc = alertDialog.findViewById(R.id.dialog_desc);
        imIcon = alertDialog.findViewById(R.id.dialog_icon);
        cardView = alertDialog.findViewById(R.id.pop_up_cardView);
        btnClose = alertDialog.findViewById(R.id.dialog_close);
        btnConfirm = alertDialog.findViewById(R.id.dialog_confirm);

        tvTitle.setText(title);
        tvDesc.setText(message);
        imIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.internet));


        btnConfirm.setText("Try again");
        btnClose.setText("Close");
        btnConfirm.setVisibility(View.VISIBLE);
        btnClose.setVisibility(View.VISIBLE);
        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }

      btnConfirm.setOnClickListener(v -> {
          alertDialog.cancel();
          alertDialog.dismiss();
          EventBus.getDefault().postSticky(new NetworkConnectionEvent(true));

      });
        btnClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            alertDialog.onBackPressed();
        });

        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        if(!((Activity) context).isFinishing())
        {
            alertDialog.show();
        }


    }

}
