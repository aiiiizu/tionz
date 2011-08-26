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
	 * @param str チェック対象の文字列
	 * @return 対象の文字列がNULL、または空文字の場合 true を返します。 それ以外の場合 false を返します。
	 */
	public static boolean isNullOrEmpty(String str) {
		if(str == null) return true;
		return (str.length() == 0);
	}
}
