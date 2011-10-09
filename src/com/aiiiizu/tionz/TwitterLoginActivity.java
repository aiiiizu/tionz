package com.aiiiizu.tionz;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.aiiiizu.tionz.common.SystemConstants;
import com.aiiiizu.tionz.sns.twitter.TionzTwitterFactory;
import com.aiiiizu.tionz.sns.twitter.TwitterConstants;
import com.aiiiizu.utils.StringUtils;

/**
 * <pre>
 * Twitter�A�v���ւ̔F�ؗp�A�N�e�B�r�e�B
 * </pre>
 * 
 * @author maguhiro
 */
public class TwitterLoginActivity extends Activity {
	// ==================================================
	// Fields
	private static RequestToken req = null;

	private static OAuthAuthorization oauth = null;

	// ==================================================
	// Methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("TionzWidget", "TwitterLogin::onCreate");

		// --------------------------------------------------
		// �{Activity�ɑΉ����郌�C�A�E�g��ݒ�
		setContentView(R.layout.login);

		// --------------------------------------------------
		// �Ώۂ̃��C�A�E�g����WebView���擾
		WebView webView = (WebView) findViewById(R.id.login);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		// --------------------------------------------------
		// �Ώۂ�WebView�ɑ΂��郊�X�i�[�ݒ�
		webView.setWebViewClient(new TwitterWebViewClient());

		// --------------------------------------------------
		// ���F��Tionz Twitter�A�v�������쐬
		// Twitetr4j�̐ݒ��ǂݍ���
		Configuration conf = ConfigurationContext.getInstance();
		// Oauth�F�؃I�u�W�F�N�g�쐬
		oauth = new OAuthAuthorization(conf);
		// Oauth�F�؃I�u�W�F�N�g��consumerKey��consumerSecret��ݒ�
		oauth.setOAuthConsumer(TwitterConstants.CONSUMER_KEY, TwitterConstants.CONSUMER_SECRET);
		// �A�v���̔F�؃I�u�W�F�N�g�쐬
		try {
			req = oauth.getOAuthRequestToken(TwitterConstants.CALLBACK_URL);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		String uri = req.getAuthorizationURL();

		// --------------------------------------------------
		// WebView�ɕ\������URL����ݒ�
		webView.loadUrl(uri);
	}

	// ==================================================
	// Inner Classes
	/**
	 * <pre>
	 * Twitter�A�v���pWebView�̃��X�i�[
	 * TwitterLoginActivity�̓����N���X
	 * </pre>
	 * 
	 * @author maguhiro
	 */
	private class TwitterWebViewClient extends WebViewClient {
		// ==================================================
		// Methods
		// �`�抮����Ɏ��s����郁�\�b�h
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d("TionzWidget", "TwitterLogin::onPageFinished");
			Log.d("TionzWidget", "URL:" + url);

			// --------------------------------------------------
			// URL��Twitter�A�v���F�،�̏ꍇ
			if (!StringUtils.isNullOrEmpty(url) && url.startsWith(TwitterConstants.CALLBACK_URL)) {
				String[] urlParameters = url.split("\\?")[1].split("&");

				// --------------------------------------------------
				// �R�[���o�b�NURL����F�ؗ��؃p�����[�^�[�l���擾
				String oauthVerifier = SystemConstants.EMPTY;
				if (urlParameters[0].startsWith(TwitterConstants.PARAM_OAUTH_VERIFIER)) {
					oauthVerifier = urlParameters[0].split("=")[1];
				} else if (urlParameters[1].startsWith(TwitterConstants.PARAM_OAUTH_VERIFIER)) {
					oauthVerifier = urlParameters[1].split("=")[1];
				}

				// --------------------------------------------------
				// �i�������̐ݒ�
				if (!StringUtils.isNullOrEmpty(oauthVerifier)) {
					// �F�ؗ��؏�񂪐ݒ肳��Ă���ꍇ
					this.setting(oauthVerifier);
				}

				// --------------------------------------------------
				// �{Activity�̏I��
				finish();
			}
		}

		// ==================================================
		// Private Methods
		/**
		 * �F�؏����i�������Ɋi�[���܂��B
		 * 
		 * @param oauthVerifier �F�ؗ��ؕ�����
		 */
		private void setting(String oauthVerifier) {
			AccessToken accessToken = null;
			try {
				// --------------------------------------------------
				// Twitter�֘A�̉i��������OAuth�����i�[
				accessToken = oauth.getOAuthAccessToken(req, oauthVerifier);
				SharedPreferences pref = getSharedPreferences(TwitterConstants.PREF_KEY, MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				// ���̊i�[
				editor.putString(TwitterConstants.SUB_KEY_OAUTH_TOKEN, accessToken.getToken());
				editor.putString(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
				Twitter twitter = TionzTwitterFactory.createAuthenticated(accessToken.getToken(), accessToken.getTokenSecret());
				editor.putString(TwitterConstants.SUB_KEY_USER_NAME, twitter.getScreenName());
				editor.commit();
			} catch (TwitterException e) {
				if (e.isCausedByNetworkIssue()) {
					// --------------------------------------------------
					// �l�b�g���[�N�������̃G���[�̏ꍇ
					Log.e("TionzWidget", "Twitter::" + e.getMessage());
					Toast.makeText(TwitterLoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
				} else {
					// --------------------------------------------------
					// Twitter API�����ɂ��G���[�̏ꍇ
					Log.e("TionzWidget", "Twitter::Twitter API Limitation");
					Toast.makeText(TwitterLoginActivity.this, "Twitter API Limitation", Toast.LENGTH_SHORT).show();
				}
				// e.printStackTrace();
			}
		}
	}
}
