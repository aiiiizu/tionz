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
 * Twitterアプリへの認証用アクティビティ
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
		// 本Activityに対応するレイアウトを設定
		setContentView(R.layout.login);

		// --------------------------------------------------
		// 対象のレイアウトからWebViewを取得
		WebView webView = (WebView) findViewById(R.id.login);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		// --------------------------------------------------
		// 対象のWebViewに対するリスナー設定
		webView.setWebViewClient(new TwitterWebViewClient());

		// --------------------------------------------------
		// 未認証Tionz Twitterアプリ情報を作成
		// Twitetr4jの設定を読み込む
		Configuration conf = ConfigurationContext.getInstance();
		// Oauth認証オブジェクト作成
		oauth = new OAuthAuthorization(conf);
		// Oauth認証オブジェクトにconsumerKeyとconsumerSecretを設定
		oauth.setOAuthConsumer(TwitterConstants.CONSUMER_KEY, TwitterConstants.CONSUMER_SECRET);
		// アプリの認証オブジェクト作成
		try {
			req = oauth.getOAuthRequestToken(TwitterConstants.CALLBACK_URL);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		String uri = req.getAuthorizationURL();

		// --------------------------------------------------
		// WebViewに表示するURL情報を設定
		webView.loadUrl(uri);
	}

	// ==================================================
	// Inner Classes
	/**
	 * <pre>
	 * Twitterアプリ用WebViewのリスナー
	 * TwitterLoginActivityの内部クラス
	 * </pre>
	 * 
	 * @author maguhiro
	 */
	private class TwitterWebViewClient extends WebViewClient {
		// ==================================================
		// Methods
		// 描画完了後に実行されるメソッド
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d("TionzWidget", "TwitterLogin::onPageFinished");
			Log.d("TionzWidget", "URL:" + url);

			// --------------------------------------------------
			// URLがTwitterアプリ認証後の場合
			if (!StringUtils.isNullOrEmpty(url) && url.startsWith(TwitterConstants.CALLBACK_URL)) {
				String[] urlParameters = url.split("\\?")[1].split("&");

				// --------------------------------------------------
				// コールバックURLから認証立証パラメーター値を取得
				String oauthVerifier = SystemConstants.EMPTY;
				if (urlParameters[0].startsWith(TwitterConstants.PARAM_OAUTH_VERIFIER)) {
					oauthVerifier = urlParameters[0].split("=")[1];
				} else if (urlParameters[1].startsWith(TwitterConstants.PARAM_OAUTH_VERIFIER)) {
					oauthVerifier = urlParameters[1].split("=")[1];
				}

				// --------------------------------------------------
				// 永続化情報の設定
				if (!StringUtils.isNullOrEmpty(oauthVerifier)) {
					// 認証立証情報が設定されている場合
					this.setting(oauthVerifier);
				}

				// --------------------------------------------------
				// 本Activityの終了
				finish();
			}
		}

		// ==================================================
		// Private Methods
		/**
		 * 認証情報を永続化情報に格納します。
		 * 
		 * @param oauthVerifier 認証立証文字列
		 */
		private void setting(String oauthVerifier) {
			AccessToken accessToken = null;
			try {
				// --------------------------------------------------
				// Twitter関連の永続化情報のOAuth情報を格納
				accessToken = oauth.getOAuthAccessToken(req, oauthVerifier);
				SharedPreferences pref = getSharedPreferences(TwitterConstants.PREF_KEY, MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				// 情報の格納
				editor.putString(TwitterConstants.SUB_KEY_OAUTH_TOKEN, accessToken.getToken());
				editor.putString(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
				Twitter twitter = TionzTwitterFactory.createAuthenticated(accessToken.getToken(), accessToken.getTokenSecret());
				editor.putString(TwitterConstants.SUB_KEY_USER_NAME, twitter.getScreenName());
				editor.commit();
			} catch (TwitterException e) {
				if (e.isCausedByNetworkIssue()) {
					// --------------------------------------------------
					// ネットワークが原因のエラーの場合
					Log.e("TionzWidget", "Twitter::" + e.getMessage());
					Toast.makeText(TwitterLoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
				} else {
					// --------------------------------------------------
					// Twitter API制限によるエラーの場合
					Log.e("TionzWidget", "Twitter::Twitter API Limitation");
					Toast.makeText(TwitterLoginActivity.this, "Twitter API Limitation", Toast.LENGTH_SHORT).show();
				}
				// e.printStackTrace();
			}
		}
	}
}
