package com.aiiiizu.tionz.sns.facebook;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.aiiiizu.tionz.FacebookLoginActivity;
import com.aiiiizu.tionz.sns.AbstractSNSWriter;
import com.facebook.android.Facebook;

/**
 * <pre>
 * Facebook�̃t�B�[�h�֏������ݏ��������{���钊�ۃN���X
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
		// facebook�i�������̎擾
		SharedPreferences pref = this.service.getSharedPreferences(FacebookConstants.PREF_KEY, Service.MODE_PRIVATE);
		Facebook facebook = new Facebook(FacebookConstants.APP_ID);
		facebook.setAccessToken(pref.getString(FacebookConstants.SUB_KEY_ACCESS_TOKEN, null));
		facebook.setAccessExpires(pref.getLong(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES, 0));

		if (this.isConnected(facebook)) {
			Log.d("TionzWidget", "Facebook::Connect");
			// --------------------------------------------------
			// �E�H�[����������
			this.writeWall(facebook, content);
			// --------------------------------------------------
			// �f�o�b�N�p
			// facebook����ؒf
			// this.unconnectFacebook(pref.edit());
		} else {
			Log.d("TionzWidget", "Facebook::Unconnect");
			// --------------------------------------------------
			// facebook�֐ڑ�
			this.connectFacebook();
		}
	}

	// ==================================================
	// Private Methods
	/**
	 * facebook�ɐڑ����Ă��邩�m�F���܂��B
	 * 
	 * @param facebook facebook�F�؃I�u�W�F�N�g
	 * @return �ڑ����Ă���ꍇ�Atrue��Ԃ��܂��B
	 */
	private boolean isConnected(Facebook facebook) {
		try {
			if (!facebook.isSessionValid()) return false;
			// ���[�U�[���擾
			String res = facebook.request("/me");
			// ���[�U�[���擾�Ɏ��s�����ꍇ
			if (res.startsWith("{\"error\"")) return false;
			return true;
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * facebook�ɐڑ����܂��B
	 */
	private void connectFacebook() {
		Intent intent = new Intent(this.service, FacebookLoginActivity.class);
		// Activity�ȊO����Activity���Ăяo���ꍇ�ɕK���K�v�Ȑݒ�
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.service.startActivity(intent);
	}

	// /**
	// * facebook����ؒf���܂��B
	// *
	// * @param editor facebook�i�������
	// */
	// private void unconnectFacebook(SharedPreferences.Editor editor) {
	// editor.remove(FacebookConstants.SUB_KEY_ACCESS_TOKEN);
	// editor.remove(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES);
	// editor.commit();
	// }

	/**
	 * �������g�̃E�H�[���ɏ������݂܂��B
	 * 
	 * @param facebook facebook�F�؃I�u�W�F�N�g
	 * @param content �������ݓ��e
	 */
	private void writeWall(Facebook facebook, String content) {
		try {
			Bundle param = new Bundle();
			param.putString(FacebookConstants.PARAM_MESSAGE, content);
			param.putString(FacebookConstants.PARAM_ACCESS_TOKEN, facebook.getAccessToken());
			// �t�B�[�h�ւ̏�������
			facebook.request("/me/feed", param, "POST");
		} catch (MalformedURLException e) {
			Log.e("TionzWidget", e.getMessage());
		} catch (IOException e) {
			Log.e("TionzWidget", e.getMessage());
		}
	}
}
