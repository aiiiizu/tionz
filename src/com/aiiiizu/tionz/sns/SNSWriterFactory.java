package com.aiiiizu.tionz.sns;

import android.app.Service;
import android.content.Intent;

import com.aiiiizu.tionz.common.SystemConstants;
import com.aiiiizu.tionz.sns.facebook.FacebookWriter;
import com.aiiiizu.tionz.sns.twitter.TwitterWriter;
import com.aiiiizu.utils.StringUtils;

/**
 * <pre>
 * SNSWriter�p��Factory�N���X
 * </pre>
 * 
 * @author maguhiro
 */
public class SNSWriterFactory {
	// ==================================================
	// Constructors
	private SNSWriterFactory() {
		// don't create
	}

	// ==================================================
	// Static Methods
	/**
	 * @param service �Ăяo�����̃T�[�r�X���
	 * @param intent �Ăяo�����T�[�r�X�̋N���N���̃C���e���g���
	 * @return �A�N�V�������ɑΉ�����SNS�����݃I�u�W�F�N�g(�Ή�������̂��Ȃ��ꍇ�Anull��Ԃ�)
	 */
	public static AbstractSNSWriter create(Service service, Intent intent) {
		// --------------------------------------------------
		// �����`�F�b�N
		if (service == null) return null;
		if (intent == null) return null;
		String actionName = intent.getAction();
		if (StringUtils.isNullOrEmpty(actionName)) return null;

		// --------------------------------------------------
		// SNSWriter�̐���
		// Twitter
		if (actionName.equals(SystemConstants.ACTION_TWITTER)) return new TwitterWriter(service);
		// Facebook
		if (actionName.equals(SystemConstants.ACTION_FACEBOOK)) return new FacebookWriter(service);
		// ����ȊO
		return null;
	}
}
