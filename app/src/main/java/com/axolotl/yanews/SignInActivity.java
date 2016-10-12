package com.axolotl.yanews;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.axolotl.yanews.customize.FabTransform;
import com.axolotl.yanews.retrofit.OauthClient;
import com.axolotl.yanews.retrofit.entity.AuthToken;
import com.axolotl.yanews.retrofit.entity.OAuthToken;
import com.axolotl.yanews.utils.GoogleAuth;
import com.axolotl.yanews.utils.Prefs;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by axolotl on 16/9/12.
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private static int GOOGLE_RC_SIGN_IN = 1;

    @BindView(R.id.btn_google_login)
    SignInButton btnGoogleLogin;
    @BindView(R.id.login_failed_message)
    TextView loginFailedMessage;
    @BindView(R.id.loading)
    ProgressBar loading;

    @Inject
    Prefs prefs;

    @Inject
    OauthClient.Endpoints api;
    @BindView(R.id.container)
    LinearLayout container;

    private GoogleAuth mGoogleAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_layout);
        mGoogleAuth = new GoogleAuth(this);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FabTransform.setup(this, container);
        }
        YaNewApp.getNetComponent(this).inject(this);
        setupView();
    }

    private void setupView() {
        btnGoogleLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        loginFailedMessage.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        startActivityForResult(mGoogleAuth.getIntent(), GOOGLE_RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String oAuthToken = acct.getIdToken();
                prefs.setDisplayName(acct.getDisplayName());
                requestAuthToken(oAuthToken);
            } else {
                loginFail();
                Toast.makeText(this, "登录失败,请检查网络连接",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestAuthToken(String oAuthToken) {
        OAuthToken o = new OAuthToken();
        o.setOauthToken(oAuthToken);
        Call<AuthToken> call = api.getAuthToken(o);
        call.enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                AuthToken authToken = response.body();
                try {
                    prefs.setAuthToken(authToken.getAuthToken());
                    setResult(RESULT_OK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else {
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {
                loginFail();
                Toast.makeText(SignInActivity.this,
                        String.format("无法连接到 %s", OauthClient.BASE_URL), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void loginFail() {
        loading.setVisibility(View.GONE);
        loginFailedMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
    }

    public void dismiss(View view) {
        setResult(Activity.RESULT_CANCELED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
}
