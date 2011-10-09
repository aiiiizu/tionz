package com.aiiiizu.tionz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.aiiiizu.tionz.common.SystemConstants;
import com.aiiiizu.tionz.sns.twitter.TwitterConstants;
import com.aiiiizu.utils.StringUtils;

public class TwitterAccountActivity extends Activity {
	// ==================================================
	// Fields
	/** ユーザー名欄 */
	private TextView userNameView = null;

	/** 登録ボタン */
	private Button registerButton = null;

	/** クリアボタン */
	private Button clearButton = null;

	// ==================================================
	// Methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_account);

		// --------------------------------------------------
		// 初期化処理
		this.userNameView = (TextView) this.findViewById(R.id.twitter_user_name);
		this.registerButton = (Button) this.findViewById(R.id.register);
		this.clearButton = (Button) this.findViewById(R.id.clear);

		// --------------------------------------------------
		// 登録ボタンのクリック処理
		this.registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TwitterAccountActivity.this, TwitterLoginActivity.class);
				startActivity(intent);
			}
		});

		// --------------------------------------------------
		// クリアボタンのクリック処理
		this.clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences pref = getSharedPreferences(TwitterConstants.PREF_KEY, MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				// 情報の格納
				editor.remove(TwitterConstants.SUB_KEY_OAUTH_TOKEN);
				editor.remove(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET);
				editor.remove(TwitterConstants.SUB_KEY_USER_NAME);
				editor.commit();

				// 表示制御
				registerButton.setVisibility(View.VISIBLE);
				clearButton.setVisibility(View.GONE);
				userNameView.setText(R.string.twitter_user_name);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// --------------------------------------------------
		// 表示制御
		SharedPreferences pref = getSharedPreferences(TwitterConstants.PREF_KEY, MODE_PRIVATE);
		String token = pref.getString(TwitterConstants.SUB_KEY_OAUTH_TOKEN, SystemConstants.EMPTY);
		String tokenSecret = pref.getString(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET, SystemConstants.EMPTY);
		String userName = pref.getString(TwitterConstants.SUB_KEY_USER_NAME, SystemConstants.EMPTY);

		if (this.isConnected(token, tokenSecret)) {
			// 接続済の場合
			this.registerButton.setVisibility(View.GONE);
			this.clearButton.setVisibility(View.VISIBLE);
			this.userNameView.setText(userName);
		} else {
			// 未接続の場合
			this.registerButton.setVisibility(View.VISIBLE);
			this.clearButton.setVisibility(View.GONE);
			this.userNameView.setText(R.string.twitter_user_name);
		}
	}

	// ==================================================
	// Private Methods
	/**
	 * Twitterに接続済か否かを判定します。
	 * 
	 * @param token OAuthトークン情報
	 * @param tokenSecret OAuthトークンシークレット情報
	 * @return　Twitterに接続済の場合:true / 未接続の場合:false
	 */
	private boolean isConnected(String token, String tokenSecret) {
		if (StringUtils.isNullOrEmpty(token)) return false;
		if (StringUtils.isNullOrEmpty(tokenSecret)) return false;
		return true;
	}
}
