package com.axolotl.yanews;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.axolotl.yanews.customize.FabTransform;
import com.axolotl.yanews.retrofit.OauthClient;
import com.axolotl.yanews.retrofit.entity.AddCommentRes;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by axolotl on 16/9/28.
 */

public class AddCommentActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_LINK = "extra_link";

    @BindView(R.id.input_comment)
    EditText inputComment;
    @BindView(R.id.input_layout_comment)
    TextInputLayout inputLayoutComment;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.loading)
    ProgressBar loading;

    @Inject
    OauthClient.Endpoints mBackApi;
    @BindView(R.id.container)
    RelativeLayout container;
    private String mLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comment);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FabTransform.setup(this, container);
        }

        mLink = getIntent().getStringExtra(EXTRA_LINK);
        Timber.d("Link is %s", mLink);
        YaNewApp.getNetComponent(this).inject(this);
        btnSubmit.setOnClickListener(this);
        inputLayoutComment.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                inputLayoutComment.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        String body = inputComment.getText().toString();
        if (TextUtils.isEmpty(body)) {
            inputLayoutComment.setError("评论内容不能为空");
            inputLayoutComment.setEnabled(true);
            return;
        }
        btnSubmit.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
        mBackApi.addComment(mLink, body).enqueue(new Callback<AddCommentRes>() {
            @Override
            public void onResponse(Call<AddCommentRes> call, Response<AddCommentRes> response) {
                Toast.makeText(AddCommentActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AddCommentRes> call, Throwable t) {
                Timber.d("Add Comment Fail: %s", t.getLocalizedMessage());
                loading.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                Toast.makeText(AddCommentActivity.this, "提交失败,请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
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
