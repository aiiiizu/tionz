package com.aiiiizu.tionz.sns.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.aiiiizu.tionz.TwitterLoginActivity;
import com.aiiiizu.tionz.common.SystemConstants;
import com.aiiiizu.tionz.sns.AbstractSNSWriter;
import com.aiiiizu.utils.StringUtils;

/**
 * <pre>
 * Twitter�փc�C�[�g���������{���钊�ۃN���X
 * </pre>
 * 
 * @author maguhiro
 */
public class TwitterWriter extends AbstractSNSWriter {
	// ==================================================
	// Constructors
	public TwitterWriter(Service service) {
		super(service);
	}

	// ==================================================
	// Methods
	@Override
	public void write(String content) {
		Log.d("TionzWidget", "Twitter::Write");

		// --------------------------------------------------
		// Twitter�֘A�̉i�������擾
		SharedPreferences pref = this.service.getSharedPreferences(TwitterConstants.PREF_KEY, Service.MODE_PRIVATE);
		String token = pref.getString(TwitterConstants.SUB_KEY_OAUTH_TOKEN, SystemConstants.EMPTY);
		String tokenSecret = pref.getString(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET, SystemConstants.EMPTY);

		// --------------------------------------------------
		// Twitter�̐ڑ��`�F�b�N
		if (this.isConnected(token, tokenSecret)) {
			// �ڑ��ς̏ꍇ
			Log.d("TionzWidget", "Twitter::Connect");

			try {
				// --------------------------------------------------
				// �F�؍ς�Twitter�I�u�W�F�N�g�̍쐬
				Twitter twitter = TionzTwitterFactory.createAuthenticated(token, tokenSecret);
				// --------------------------------------------------
				// �c�C�[�g
				twitter.updateStatus(content);
			} catch (TwitterException e) {
				if (e.isCausedByNetworkIssue()) {
					// �l�b�g���[�N�������̃G���[�̏ꍇ
					Log.e("TionzWidget", "Twitter::" + e.getMessage());
					Toast.makeText(this.service, "Network Error", Toast.LENGTH_SHORT).show();
				} else {
					// Twitter API�����ɂ��G���[�̏ꍇ
					Log.e("TionzWidget", "Twitter::Twitter API Limitation");
					Toast.makeText(this.service, "Twitter API Limitation", Toast.LENGTH_SHORT).show();
				}
				// e.printStackTrace();
			} finally {
				// --------------------------------------------------
				// �f�o�b�O�p(�����[�X���̓R�����g�A�E�g)
				// Twitter����̐ڑ���ؒf
				//this.unconnectTwitter(pref.edit());
			}
		} else {
			// ���ڑ��̏ꍇ
			Log.d("TionzWidget", "Twitter::UnConnect");
			// --------------------------------------------------
			// Twitter�ɐڑ�
			this.connectTwitter();
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

	/**
	 * Twitter�ɐڑ����邽�߂�Twitter�A�v���F�؉�ʂɑJ�ڂ��܂��B
	 */
	private void connectTwitter() {
		Intent intent = new Intent(this.service, TwitterLoginActivity.class);
		// Activity�ȊO����Activity���Ăяo���ꍇ�ɕK���K�v�Ȑݒ�
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Twitter�A�v���F�؉�ʂ�\��
		this.service.startActivity(intent);
	}

	// /**
	// * Twitter����̐ڑ���ؒf���܂��B
	// *
	// * @param editor Twitter�֘A�̉i�������G�f�B�^�[
	// */
	// private void unconnectTwitter(SharedPreferences.Editor editor) {
	// // --------------------------------------------------
	// // OAuth�F�؏����폜
	// editor.remove(TwitterConstants.SUB_KEY_OAUTH_TOKEN);
	// editor.remove(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET);
	// editor.commit();
	// }
}
