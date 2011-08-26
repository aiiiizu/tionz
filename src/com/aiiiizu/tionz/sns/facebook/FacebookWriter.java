package com.aiiiizu.tionz.sns.facebook;

import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.aiiiizu.tionz.FacebookLoginActivity;
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
		
		SharedPreferences pref = this.service.getSharedPreferences(FacebookConstants.PREF_KEY, Service.MODE_PRIVATE);
		Facebook facebook = new Facebook(FacebookConstants.APP_ID);
		facebook.setAccessToken(pref.getString(FacebookConstants.SUB_KEY_ACCESS_TOKEN, null));
		facebook.setAccessExpires(pref.getLong(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES, -1));
		Date date = new Date(pref.getLong(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES, -1));
		Log.i("TionzWidget", date.toLocaleString());
		
		if(facebook.isSessionValid()) {
			Log.d("TionzWidget", "Facebook::Connect");
			this.unconnectFacebook(pref.edit());
		} else {
			Log.d("TionzWidget", "Facebook::Unconnect");
			this.connectFacebook();
		}
	}
	
	// ==================================================
	// Private Methods
	private void connectFacebook() {
		Intent intent = new Intent(this.service, FacebookLoginActivity.class);
		// Activity以外からActivityを呼び出す場合に必ず必要な設定
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.service.startActivity(intent);
	}
	
	private void unconnectFacebook(SharedPreferences.Editor editor) {
		editor.remove(FacebookConstants.SUB_KEY_ACCESS_TOKEN);
		editor.remove(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES);
		editor.commit();
	}
}
