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
	/** ���[�U�[���� */
	private TextView userNameView = null;

	/** �o�^�{�^�� */
	private Button registerButton = null;

	/** �N���A�{�^�� */
	private Button clearButton = null;

	// ==================================================
	// Methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_account);

		// --------------------------------------------------
		// ����������
		this.userNameView = (TextView) this.findViewById(R.id.facebook_user_name);
		this.registerButton = (Button) this.findViewById(R.id.register);
		this.clearButton = (Button) this.findViewById(R.id.clear);

		// --------------------------------------------------
		// �o�^�{�^���̃N���b�N����
		this.registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FacebookAccountActivity.this, FacebookLoginActivity.class);
				startActivity(intent);
			}
		});

		// --------------------------------------------------
		// �N���A�{�^���̃N���b�N����
		this.clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences pref = getSharedPreferences(FacebookConstants.PREF_KEY, MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				// ���̊i�[
				editor.remove(FacebookConstants.SUB_KEY_ACCESS_TOKEN);
				editor.remove(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES);
				editor.remove(FacebookConstants.SUB_KEY_USER_NAME);
				editor.commit();

				// �\������
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
		// �\������
		SharedPreferences pref = getSharedPreferences(FacebookConstants.PREF_KEY, Service.MODE_PRIVATE);
		Facebook facebook = new Facebook(FacebookConstants.APP_ID);
		facebook.setAccessToken(pref.getString(FacebookConstants.SUB_KEY_ACCESS_TOKEN, null));
		facebook.setAccessExpires(pref.getLong(FacebookConstants.SUB_KEY_ACCESS_TOKEN_EXPIRES, 0));
		String userName = pref.getString(FacebookConstants.SUB_KEY_USER_NAME, SystemConstants.EMPTY);

		if (this.isConnected(facebook)) {
			// �ڑ��ς̏ꍇ
			this.registerButton.setVisibility(View.GONE);
			this.clearButton.setVisibility(View.VISIBLE);
			this.userNameView.setText(userName);
		} else {
			// ���ڑ��̏ꍇ
			this.registerButton.setVisibility(View.VISIBLE);
			this.clearButton.setVisibility(View.GONE);
			this.userNameView.setText(R.string.facebook_user_name);
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
}
