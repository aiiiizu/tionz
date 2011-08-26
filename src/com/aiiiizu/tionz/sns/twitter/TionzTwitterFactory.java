package com.aiiiizu.tionz.sns.twitter;


import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * <pre>
 * Twitterオブジェクトを生成します
 * </pre>
 * 
 * @author maguhiro
 * 
 */
public class TionzTwitterFactory {
	// =============================================
	// Constructors
	private TionzTwitterFactory() {
		// don't create
	}

	// =============================================
	// Static Methods
	/**
	 * 未認証のTionz Twitterアプリ情報を作成します。
	 * 
	 * @return 未認証のTionz Twitterアプリオブジェクト
	 */
	public static Twitter createUnauthenticated() {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET);

		return new TwitterFactory(builder.build()).getInstance();
	}

	/**
	 * 認証済のTionz Twitterアプリ情報を作成します。
	 * 
	 * @param oauthToken 認証OAuthトークン情報
	 * @param oauthSecret 認証OAuthトークンシークレット情報
	 * @return 認証済のTionz Twitterアプリオブジェクト
	 */
	public static Twitter createAuthenticated(String oauthToken, String oauthSecret) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET);
		builder.setOAuthAccessToken(oauthToken);
		builder.setOAuthAccessTokenSecret(oauthSecret);

		return new TwitterFactory(builder.build()).getInstance();
	}
}
