package com.aiiiizu.tionz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.aiiiizu.tionz.sns.facebook.FacebookConstants;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * <pre>
 * Facebookアプリへの認証用アクティビティ
 * </pre>
 * 
 * @author maguhiro
 */
public class FacebookLoginActivity extends Activity {
	// ==================================================
	// Fields
	/** Facebook認証オブジェクト */
	private Facebook facebook = new Facebook(FacebookConstants.APP_ID);
	/** Facebookパーミッション情報 */
	private String[] permissions = { FacebookConstants.PERM_PUBLIC_STREAM };

	// ==================================================
	// Methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("TionzWidget", "FacebookLogin::onCreate");

		// --------------------------------------------------
		// 本Activityに対応するレイアウトを設定
		setContentView(R.layout.login);
		
		// --------------------------------------------------
		// 認証処理の実施
		this.facebook.authorize(this, this.permissions, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {
				Log.d("TionzWidget", "Facebook::onComplete");
				
				// --------------------------------------------------
				// Facebook関連の永続化情報を格納
				SharedPreferences pref = getSharedPreferences(FacebookConstants.PREF_KEY, MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putLong(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES, facebook.getAccessExpires());
				editor.putString(FacebookConstants.SUB_KEY_ACCESS_TOKEN, facebook.getAccessToken());
				editor.commit();
				
				// --------------------------------------------------
				// フィード書き込み
				write();
				
				finish();
			}

			@Override
			public void onFacebookError(FacebookError e) {
				Log.d("TionzWidget", "Facebook::onFacebookError");
				Log.e("TionzWidget", "FacebookError::" + e.getMessage());
				finish();
			}

			@Override
			public void onError(DialogError e) {
				Log.d("TionzWidget", "Facebook::onFacebookError");
				Log.e("TionzWidget", "FacebookError::" + e.getMessage());
				finish();
			}

			@Override
			public void onCancel() {
				Log.d("TionzWidget", "Facebook::onCancel");
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.facebook.authorizeCallback(requestCode, resultCode, data);
	}
	
	private void write() {
		Log.d("TionzWidget", "Facebook::write");
//		this.facebook.request(parameters);
	}
}
