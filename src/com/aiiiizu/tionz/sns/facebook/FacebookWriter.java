package com.aiiiizu.tionz.sns.facebook;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Service;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aiiiizu.tionz.sns.AbstractSNSWriter;
import com.facebook.android.Facebook;

/**
 * <pre>
 * Facebookのフィードへ書き込み処理を実施する抽象クラス
 * </pre>
 * 
 * @author maguhiro
 */
public class FacebookWriter extends AbstractSNSWriter {
	// ==================================================
	// Constructors
	public FacebookWriter(Service service) {
		super(service);
	}

	// ==================================================
	// Methods
	@Override
	public void write(String content) {
		Log.d("TionzWidget", "Facebook::Write");

		// --------------------------------------------------
		// facebook永続化情報の取得
		SharedPreferences pref = this.service.getSharedPreferences(FacebookConstants.PREF_KEY, Service.MODE_PRIVATE);
		Facebook facebook = new Facebook(FacebookConstants.APP_ID);
		facebook.setAccessToken(pref.getString(FacebookConstants.SUB_KEY_ACCESS_TOKEN, null));
		facebook.setAccessExpires(pref.getLong(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES, 0));

		if (this.isConnected(facebook)) {
			Log.d("TionzWidget", "Facebook::Connect");
			// --------------------------------------------------
			// ウォール書き込み
			this.writeWall(facebook, content);
		} else {
			Log.d("TionzWidget", "Facebook::Unconnect");
			Toast.makeText(this.service, "Facebook のアカウント設定がされていません", Toast.LENGTH_SHORT).show();
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

	/**
	 * 自分自身のウォールに書き込みます。
	 * 
	 * @param facebook facebook認証オブジェクト
	 * @param content 書き込み内容
	 */
	private void writeWall(Facebook facebook, String content) {
		try {
			Bundle param = new Bundle();
			param.putString(FacebookConstants.PARAM_MESSAGE, content);
			param.putString(FacebookConstants.PARAM_ACCESS_TOKEN, facebook.getAccessToken());
			// フィードへの書き込み
			facebook.request("/me/feed", param, "POST");
			Toast.makeText(this.service, "Facebook に書き込みました", Toast.LENGTH_SHORT).show();
		} catch (MalformedURLException e) {
			Log.e("TionzWidget", e.getMessage());
			Toast.makeText(this.service, "Facebook の書き込みに失敗しました", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Log.e("TionzWidget", e.getMessage());
			Toast.makeText(this.service, "Facebook の書き込みに失敗しました", Toast.LENGTH_SHORT).show();
		}
	}
}
