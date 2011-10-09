package com.aiiiizu.tionz.sns.twitter;

/**
 * <pre>
 * Twitter関連のの定数クラス
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
	// アプリ情報
	/** アプリのキー情報 */
	public static final String CONSUMER_KEY = "EezqZbcnX9GPf1HcBFUZg";
	/** アプリのシークレット情報 */
	public static final String CONSUMER_SECRET = "7GvlcCBANnh0Gi2MkkE4ztpDeLH3g8zm5SovyO9bQ0";
	
	// --------------------------------------------------
	// 認証後コールバックURL情報関連
	/** Twitterアプリ承認時にコールバックされるURL(開始) */
	public static final String CALLBACK_URL = "tionz://oauth";
	/** Twitterアプリ承認時にコールバックされるURLの認証トークンパラメーター情報 */
	public static final String PARAM_OAUTH_TOKEN = "oauth_token";
	/** Twitterアプリ承認時にコールバックされるURLの認証立証パラメーター情報 */
	public static final String PARAM_OAUTH_VERIFIER = "oauth_verifier";
	
	// --------------------------------------------------
	// 永続化情報
	/** 永続化参照キー */
	public static final String PREF_KEY = "twitter_setting";
	/** 永続化サブ参照キー：OAuthトークン */
	public static final String SUB_KEY_OAUTH_TOKEN = "oauth_token";
	/** 永続化サブ参照キー：OAuthトークンシークレット */
	public static final String SUB_KEY_OAUTH_TOKEN_SECRET = "oauth_token_secret";
	/** 永続化サブ参照キー；ユーザー名 */
	public static final String SUB_KEY_USER_NAME = "user_name";
}
