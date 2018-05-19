package com.agroneo.app.oauth;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.utils.HR;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.string.google:
                break;
            case R.string.facebook:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LinearLayout choose = new LinearLayout(this);
        choose.setOrientation(LinearLayout.VERTICAL);
        choose.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        choose.addView(new Provider(this, R.drawable.google, R.string.google));
        
        choose.addView(new HR(this));
        choose.addView(new Provider(this, R.drawable.facebook, R.string.facebook));

        choose.addView(new HR(this));
        choose.addView(new Provider(this, R.drawable.twitter, R.string.twitter));

        choose.addView(new HR(this));
        choose.addView(new Provider(this, R.drawable.microsoft, R.string.microsoft));

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(choose);
        adb.show();
    }

    protected void oAuth(Account account) {


    }

    private class Provider extends LinearLayout {

        public Provider(Context context, int logo, int text) {
            super(context);

            setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            setOrientation(LinearLayout.HORIZONTAL);
            setPadding(15, 15, 15, 15);
            setClickable(true);

            ImageView img = new ImageView(context);
            img.setImageDrawable(getResources().getDrawable(logo));
            img.setLayoutParams(new ViewGroup.LayoutParams(60,60));
            img.setAdjustViewBounds(true);
            addView(img);

            TextView title = new TextView(context);
            title.setText(text);

            title.setPadding(10, 0, 10, 0);

            title.setTypeface(null, Typeface.BOLD);
            title.setTextSize(18);
            addView(title);
            setId(text);
            setOnClickListener(AuthActivity.this);
        }
    }

}
