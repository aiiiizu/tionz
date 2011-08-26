package com.aiiiizu.tionz.common;

/**
 * <pre>
 * Tionzアプリシステム関連の定数クラス
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
	// 共通文字列
	/** 空文字 */
	public static final String EMPTY = "";
	
	// --------------------------------------------------
	// 書込み内容テンプレート
	public static final String TEMP_CONTENT = "現在の端末温度は{0}℃だよ！ #aiiiizu #tionz"; 
	
	// --------------------------------------------------
	// アクション定数
	/** Twitterボタンクリックアクション */
	public static final String ACTION_TWITTER = "com.aiiiizu.tionz.twitter";
	/** Facebookボタンクリックアクション */
	public static final String ACTION_FACEBOOK = "com.aiiiizu.tionz.facebook";
	
	// --------------------------------------------------
	// 永続化情報
	/** 永続化参照キー */
	public static final String PREF_KEY = "tionz_setting";
	/** 永続化サブ参照キー：端末温度 */
	public static final String SUB_KEY_TEMPERATURE = "temperature";
}
