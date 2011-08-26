package com.aiiiizu.utils;

public class StringUtils {
	// ==================================================
	// Constructors
	private StringUtils() {
		// don't create
	}
	
	// ==================================================
	// Static Methods
	/**
	 * 
	 * @param str �`�F�b�N�Ώۂ̕�����
	 * @return �Ώۂ̕�����NULL�A�܂��͋󕶎��̏ꍇ true ��Ԃ��܂��B ����ȊO�̏ꍇ false ��Ԃ��܂��B
	 */
	public static boolean isNullOrEmpty(String str) {
		if(str == null) return true;
		return (str.length() == 0);
	}
}
