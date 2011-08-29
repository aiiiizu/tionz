package com.aiiiizu.tionz;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
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
	private Twitter twitter = null;

	private RequestToken token = null;

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
		this.twitter = TionzTwitterFactory.createUnauthenticated();
		try {
			// ���N�G�X�g�g�[�N���i�ΏۃA�v���ւ̃A�N�Z�X���j���̍쐬
			this.token = this.twitter.getOAuthRequestToken(TwitterConstants.CALLBACK_URL);
		} catch (Exception e) {
			Log.e("TionzWidget", e.getMessage());
			e.printStackTrace();
		}

		// --------------------------------------------------
		// WebView�ɕ\������URL����ݒ�
		webView.loadUrl(this.token.getAuthenticationURL());
	}

	// ==================================================
	// Private Methods
	private void startService() {
		Intent intent = new Intent(this, TionzService.class);
		intent.setAction(SystemConstants.ACTION_TWITTER);
		intent.putExtra(SystemConstants.INTENT_KEY_ACTIVATOR, SystemConstants.ACTIVATOR_TWITTER);
		this.startService(intent);
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
				// �Ԃ₭
				if (!StringUtils.isNullOrEmpty(oauthVerifier)) {
					// �F�ؗ��؏�񂪐ݒ肳��Ă���ꍇ
					this.tweet(oauthVerifier);
				}

				// --------------------------------------------------
				// �{Activity�̏I��
				finish();
			}
		}

		// ==================================================
		// Private Methods
		/**
		 * �c�C�[�g���܂��B
		 * 
		 * @param oauthVerifier �F�ؗ��ؕ�����
		 */
		private void tweet(String oauthVerifier) {
			AccessToken accessToken = null;
			try {
				// --------------------------------------------------
				// Twitter�֘A�̉i��������OAuth�����i�[
				accessToken = twitter.getOAuthAccessToken(oauthVerifier);
				SharedPreferences pref = getSharedPreferences(TwitterConstants.PREF_KEY, MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				// ���̊i�[
				editor.putString(TwitterConstants.SUB_KEY_OAUTH_TOKEN, accessToken.getToken());
				editor.putString(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
				editor.commit();

				// --------------------------------------------------
				// �T�[�r�X�̋N��
				startService();
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
