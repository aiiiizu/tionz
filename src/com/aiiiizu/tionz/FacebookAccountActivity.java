package com.aiiiizu.tionz;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.aiiiizu.tionz.common.SystemConstants;
import com.aiiiizu.tionz.sns.facebook.FacebookConstants;
import com.facebook.android.Facebook;

public class FacebookAccountActivity extends Activity {
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
		setContentView(R.layout.facebook_account);

		// --------------------------------------------------
		// 初期化処理
		this.userNameView = (TextView) this.findViewById(R.id.facebook_user_name);
		this.registerButton = (Button) this.findViewById(R.id.register);
		this.clearButton = (Button) this.findViewById(R.id.clear);

		// --------------------------------------------------
		// 登録ボタンのクリック処理
		this.registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FacebookAccountActivity.this, FacebookLoginActivity.class);
				startActivity(intent);
			}
		});

		// --------------------------------------------------
		// クリアボタンのクリック処理
		this.clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences pref = getSharedPreferences(FacebookConstants.PREF_KEY, MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				// 情報の格納
				editor.remove(FacebookConstants.SUB_KEY_ACCESS_TOKEN);
				editor.remove(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES);
				editor.remove(FacebookConstants.SUB_KEY_USER_NAME);
				editor.commit();

				// 表示制御
				registerButton.setVisibility(View.VISIBLE);
				clearButton.setVisibility(View.GONE);
				userNameView.setText(R.string.facebook_user_name);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		// --------------------------------------------------
		// 表示制御
		SharedPreferences pref = getSharedPreferences(FacebookConstants.PREF_KEY, Service.MODE_PRIVATE);
		Facebook facebook = new Facebook(FacebookConstants.APP_ID);
		facebook.setAccessToken(pref.getString(FacebookConstants.SUB_KEY_ACCESS_TOKEN, null));
		facebook.setAccessExpires(pref.getLong(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES, 0));
		String userName = pref.getString(FacebookConstants.SUB_KEY_USER_NAME, SystemConstants.EMPTY);

		if (this.isConnected(facebook)) {
			// 接続済の場合
			this.registerButton.setVisibility(View.GONE);
			this.clearButton.setVisibility(View.VISIBLE);
			this.userNameView.setText(userName);
		} else {
			// 未接続の場合
			this.registerButton.setVisibility(View.VISIBLE);
			this.clearButton.setVisibility(View.GONE);
			this.userNameView.setText(R.string.facebook_user_name);
		}
	}

	// ==================================================
	// Private Methods
	/**
	 * facebookに接続しているか確認します。
	 * 
	 * @param facebook facebook認証オブジェクト
	 * @return 接続している場合、trueを返します。
	 */
	private boolean isConnected(Facebook facebook) {
		try {
			if (!facebook.isSessionValid()) return false;
			// ユーザー情報取得
			String res = facebook.request("/me");
			// ユーザー情報取得に失敗した場合
			if (res.startsWith("{\"error\"")) return false;
			return true;
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
}
