package com.aiiiizu.tionz.sns.twitter;


import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * <pre>
 * Twitter�I�u�W�F�N�g�𐶐����܂�
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
	 * ���F�؂�Tionz Twitter�A�v�������쐬���܂��B
	 * 
	 * @return ���F�؂�Tionz Twitter�A�v���I�u�W�F�N�g
	 */
	public static Twitter createUnauthenticated() {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET);

		return new TwitterFactory(builder.build()).getInstance();
	}

	/**
	 * �F�؍ς�Tionz Twitter�A�v�������쐬���܂��B
	 * 
	 * @param oauthToken �F��OAuth�g�[�N�����
	 * @param oauthSecret �F��OAuth�g�[�N���V�[�N���b�g���
	 * @return �F�؍ς�Tionz Twitter�A�v���I�u�W�F�N�g
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
