package com.aiiiizu.tionz.sns.facebook;

/**
 * <pre>
 * Facebook�֘A�̂̒萔�N���X
 * </pre>
 * 
 * @author maguhiro
 */
public class FacebookConstants {
	// ==================================================
	// Constuctors
	private FacebookConstants() {
		// don't create
	}

	// ==================================================
	// Constants
	// --------------------------------------------------
	// �A�v�����
	/** �A�v���P�[�V����ID */
	public static final String APP_ID = "140796549343964";

	// --------------------------------------------------
	// �p�[�~�b�V�������
	/** �p�[�~�b�V�����F�t�B�[�h�ւ̏������݋��� */
	public static final String PERM_PUBLIC_STREAM = "publish_stream";

	/** �p�[�~�b�V�����F�L�������̂Ȃ�OAuth�g�[�N�������擾�\ */
	public static final String PERM_OFFLINE_ACCESS = "offline_access";

	// --------------------------------------------------
	// �i�������
	/** �i�����Q�ƃL�[ */
	public static final String PREF_KEY = "facebook_setting";

	/** �i�����T�u�Q�ƃL�[�F�g�[�N�� */
	public static final String SUB_KEY_ACCESS_TOKEN = "access_token";

	/** �i�����T�u�Q�ƃL�[�F�g�[�N������ */
	public static final String SUB_KEY_ACCESS_TOKEN_EXPIRES = "access_token_expires";

	// --------------------------------------------------
	// �p�����[�^�[���
	/** �p�����[�^�[���Fmessage */
	public static final String PARAM_MESSAGE = "message";

	/** �p�����[�^�[���Faccess_token */
	public static final String PARAM_ACCESS_TOKEN = "access_token";

}
