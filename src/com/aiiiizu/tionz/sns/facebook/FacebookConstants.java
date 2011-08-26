package com.aiiiizu.tionz.sns.facebook;

/**
 * <pre>
 * Facebook関連のの定数クラス
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
	// アプリ情報
	/** アプリケーションID */
	public static final String APP_ID = "140796549343964";

	// --------------------------------------------------
	// パーミッション情報
	/** パーミッション：フィードへの書き込み許可 */
	public static final String PERM_PUBLIC_STREAM = "publish_stream";

	// --------------------------------------------------
	// 永続化情報
	/** 永続化参照キー */
	public static final String PREF_KEY = "facebook_setting";
	/** 永続化サブ参照キー：トークン */
	public static final String SUB_KEY_ACCESS_TOKEN = "access_token";
	/** 永続化サブ参照キー：トークン立証 */
	public static final String SUB_KEY_ACCESS_TOKEN_EXPIRES = "access_token_expires";
}
