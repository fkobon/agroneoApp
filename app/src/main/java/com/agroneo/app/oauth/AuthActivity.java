package com.agroneo.app.oauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.agroneo.app.R;
import com.google.android.gms.common.AccountPicker;

public class AuthActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                new String[]{"com.google", "com.facebook.auth.login", "com.twitter.android.auth.login"},
                true, getString(R.string.oauth_info), null, null, null);
        startActivityForResult(intent, 0);

        //setContentView(R.layout.connect);
    }
}
