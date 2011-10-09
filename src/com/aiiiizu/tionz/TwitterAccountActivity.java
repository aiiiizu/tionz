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
		setContentView(R.layout.twitter_account);

		// --------------------------------------------------
		// ����������
		this.userNameView = (TextView) this.findViewById(R.id.twitter_user_name);
		this.registerButton = (Button) this.findViewById(R.id.register);
		this.clearButton = (Button) this.findViewById(R.id.clear);

		// --------------------------------------------------
		// �o�^�{�^���̃N���b�N����
		this.registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TwitterAccountActivity.this, TwitterLoginActivity.class);
				startActivity(intent);
			}
		});

		// --------------------------------------------------
		// �N���A�{�^���̃N���b�N����
		this.clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences pref = getSharedPreferences(TwitterConstants.PREF_KEY, MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				// ���̊i�[
				editor.remove(TwitterConstants.SUB_KEY_OAUTH_TOKEN);
				editor.remove(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET);
				editor.remove(TwitterConstants.SUB_KEY_USER_NAME);
				editor.commit();

				// �\������
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
		// �\������
		SharedPreferences pref = getSharedPreferences(TwitterConstants.PREF_KEY, MODE_PRIVATE);
		String token = pref.getString(TwitterConstants.SUB_KEY_OAUTH_TOKEN, SystemConstants.EMPTY);
		String tokenSecret = pref.getString(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET, SystemConstants.EMPTY);
		String userName = pref.getString(TwitterConstants.SUB_KEY_USER_NAME, SystemConstants.EMPTY);

		if (this.isConnected(token, tokenSecret)) {
			// �ڑ��ς̏ꍇ
			this.registerButton.setVisibility(View.GONE);
			this.clearButton.setVisibility(View.VISIBLE);
			this.userNameView.setText(userName);
		} else {
			// ���ڑ��̏ꍇ
			this.registerButton.setVisibility(View.VISIBLE);
			this.clearButton.setVisibility(View.GONE);
			this.userNameView.setText(R.string.twitter_user_name);
		}
	}

	// ==================================================
	// Private Methods
	/**
	 * Twitter�ɐڑ��ς��ۂ��𔻒肵�܂��B
	 * 
	 * @param token OAuth�g�[�N�����
	 * @param tokenSecret OAuth�g�[�N���V�[�N���b�g���
	 * @return�@Twitter�ɐڑ��ς̏ꍇ:true / ���ڑ��̏ꍇ:false
	 */
	private boolean isConnected(String token, String tokenSecret) {
		if (StringUtils.isNullOrEmpty(token)) return false;
		if (StringUtils.isNullOrEmpty(tokenSecret)) return false;
		return true;
	}
}
