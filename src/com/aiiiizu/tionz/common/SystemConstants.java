package com.aiiiizu.tionz.common;

/**
 * <pre>
 * Tionz�A�v���V�X�e���֘A�̒萔�N���X
 * </pre>
 * 
 * @author maguhiro
 */
public class SystemConstants {
	// ==================================================
	// Constuctors
	private SystemConstants() {
		// don't create
	}

	// ==================================================
	// Constants
	// --------------------------------------------------
	// ���ʕ�����
	/** �󕶎� */
	public static final String EMPTY = "";
	
	// --------------------------------------------------
	// �����ݓ��e�e���v���[�g
	public static final String TEMP_CONTENT = "���݂̒[�����x��{0}������I #aiiiizu #tionz"; 
	
	// --------------------------------------------------
	// �A�N�V�����萔
	/** Twitter�{�^���N���b�N�A�N�V���� */
	public static final String ACTION_TWITTER = "com.aiiiizu.tionz.twitter";
	/** Facebook�{�^���N���b�N�A�N�V���� */
	public static final String ACTION_FACEBOOK = "com.aiiiizu.tionz.facebook";
	
	// --------------------------------------------------
	// �i�������
	/** �i�����Q�ƃL�[ */
	public static final String PREF_KEY = "tionz_setting";
	/** �i�����T�u�Q�ƃL�[�F�[�����x */
	public static final String SUB_KEY_TEMPERATURE = "temperature";
}
