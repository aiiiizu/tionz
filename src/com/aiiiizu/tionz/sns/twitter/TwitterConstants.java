package com.aiiiizu.tionz.sns.twitter;

/**
 * <pre>
 * Twitter�֘A�̂̒萔�N���X
 * </pre>
 * 
 * @author maguhiro
 */
public class TwitterConstants {
	// ==================================================
	// Constuctors
	private TwitterConstants() {
		// don't create
	}

	// ==================================================
	// Constants
	// --------------------------------------------------
	// �A�v�����
	/** �A�v���̃L�[��� */
	public static final String CONSUMER_KEY = "EezqZbcnX9GPf1HcBFUZg";
	/** �A�v���̃V�[�N���b�g��� */
	public static final String CONSUMER_SECRET = "7GvlcCBANnh0Gi2MkkE4ztpDeLH3g8zm5SovyO9bQ0";
	
	// --------------------------------------------------
	// �F�،�R�[���o�b�NURL���֘A
	/** Twitter�A�v�����F���ɃR�[���o�b�N�����URL(�J�n) */
	public static final String CALLBACK_URL = "tionz://oauth";
	/** Twitter�A�v�����F���ɃR�[���o�b�N�����URL�̔F�؃g�[�N���p�����[�^�[��� */
	public static final String PARAM_OAUTH_TOKEN = "oauth_token";
	/** Twitter�A�v�����F���ɃR�[���o�b�N�����URL�̔F�ؗ��؃p�����[�^�[��� */
	public static final String PARAM_OAUTH_VERIFIER = "oauth_verifier";
	
	// --------------------------------------------------
	// �i�������
	/** �i�����Q�ƃL�[ */
	public static final String PREF_KEY = "twitter_setting";
	/** �i�����T�u�Q�ƃL�[�FOAuth�g�[�N�� */
	public static final String SUB_KEY_OAUTH_TOKEN = "oauth_token";
	/** �i�����T�u�Q�ƃL�[�FOAuth�g�[�N���V�[�N���b�g */
	public static final String SUB_KEY_OAUTH_TOKEN_SECRET = "oauth_token_secret";
}
