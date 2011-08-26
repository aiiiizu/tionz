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
 * Facebook�A�v���ւ̔F�ؗp�A�N�e�B�r�e�B
 * </pre>
 * 
 * @author maguhiro
 */
public class FacebookLoginActivity extends Activity {
	// ==================================================
	// Fields
	/** Facebook�F�؃I�u�W�F�N�g */
	private Facebook facebook = new Facebook(FacebookConstants.APP_ID);
	/** Facebook�p�[�~�b�V������� */
	private String[] permissions = { FacebookConstants.PERM_PUBLIC_STREAM };

	// ==================================================
	// Methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("TionzWidget", "FacebookLogin::onCreate");

		// --------------------------------------------------
		// �{Activity�ɑΉ����郌�C�A�E�g��ݒ�
		setContentView(R.layout.login);
		
		// --------------------------------------------------
		// �F�؏����̎��{
		this.facebook.authorize(this, this.permissions, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {
				Log.d("TionzWidget", "Facebook::onComplete");
				
				// --------------------------------------------------
				// Facebook�֘A�̉i���������i�[
				SharedPreferences pref = getSharedPreferences(FacebookConstants.PREF_KEY, MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putLong(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES, facebook.getAccessExpires());
				editor.putString(FacebookConstants.SUB_KEY_ACCESS_TOKEN, facebook.getAccessToken());
				editor.commit();
				
				// --------------------------------------------------
				// �t�B�[�h��������
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
