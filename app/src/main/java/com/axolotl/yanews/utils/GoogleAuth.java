package com.axolotl.yanews.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.axolotl.yanews.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


public class GoogleAuth implements
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "GoogleAuth";
    private GoogleApiClient mGoogleApiClient;

    private Context mContext;

    public GoogleAuth(Context context) {
        mContext = context;
        mGoogleApiClient = buildClient();
        mGoogleApiClient.connect();
    }

    private GoogleApiClient buildClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.server_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(mContext)
                //.enableAutoManage(mContext /* Activity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(mContext, "Connection failed, please try to sign in again", Toast.LENGTH_LONG).show();
    }

    public Intent getIntent() {
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    public GoogleApiClient getClient() {
        return mGoogleApiClient;
    }

}
