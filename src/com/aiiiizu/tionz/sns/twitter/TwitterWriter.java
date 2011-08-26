package com.aiiiizu.tionz.sns.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.aiiiizu.tionz.TwitterLoginActivity;
import com.aiiiizu.tionz.common.SystemConstants;
import com.aiiiizu.tionz.sns.AbstractSNSWriter;
import com.aiiiizu.utils.StringUtils;

/**
 * <pre>
 * Twitterへツイート処理を実施する抽象クラス
 * </pre>
 * 
 * @author maguhiro
 */
public class TwitterWriter extends AbstractSNSWriter {
	// ==================================================
	// Constructors
	public TwitterWriter(Service service) {
		super(service);
	}

	// ==================================================
	// Methods
	@Override
	public void write(String content) {
		Log.d("TionzWidget", "Twitter::Write");

		// --------------------------------------------------
		// Twitter関連の永続化情報取得
		SharedPreferences pref = this.service.getSharedPreferences(TwitterConstants.PREF_KEY, Service.MODE_PRIVATE);
		String token = pref.getString(TwitterConstants.SUB_KEY_OAUTH_TOKEN, SystemConstants.EMPTY);
		String tokenSecret = pref.getString(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET, SystemConstants.EMPTY);

		// --------------------------------------------------
		// Twitterの接続チェック
		if (this.isConnected(token, tokenSecret)) {
			// 接続済の場合
			Log.d("TionzWidget", "Twitter::Connect");

			try {
				// --------------------------------------------------
				// 認証済みTwitterオブジェクトの作成
				Twitter twitter = TionzTwitterFactory.createAuthenticated(token, tokenSecret);
				// --------------------------------------------------
				// ツイート
				twitter.updateStatus(content);
			} catch (TwitterException e) {
				if (e.isCausedByNetworkIssue()) {
					// ネットワークが原因のエラーの場合
					Log.e("TionzWidget", "Twitter::" + e.getMessage());
					Toast.makeText(this.service, "Network Error", Toast.LENGTH_SHORT).show();
				} else {
					// Twitter API制限によるエラーの場合
					Log.e("TionzWidget", "Twitter::Twitter API Limitation");
					Toast.makeText(this.service, "Twitter API Limitation", Toast.LENGTH_SHORT).show();
				}
				// e.printStackTrace();
			} finally {
				// --------------------------------------------------
				// デバッグ用(リリース時はコメントアウト)
				// Twitterからの接続を切断
				this.unconnectTwitter(pref.edit());
			}
		} else {
			// 未接続の場合
			Log.d("TionzWidget", "Twitter::UnConnect");
			// --------------------------------------------------
			// Twitterに接続
			this.connectTwitter();
		}
	}

	// ==================================================
	// Private Methods
	/**
	 * Twitterに接続済か否かを判定します。
	 * 
	 * @param token OAuthトークン情報
	 * @param tokenSecret OAuthトークンシークレット情報
	 * @return　Twitterに接続済の場合:true / 未接続の場合:false
	 */
	private boolean isConnected(String token, String tokenSecret) {
		if (StringUtils.isNullOrEmpty(token)) return false;
		if (StringUtils.isNullOrEmpty(tokenSecret)) return false;
		return true;
	}

	/**
	 * Twitterに接続するためにTwitterアプリ認証画面に遷移します。
	 */
	private void connectTwitter() {
		Intent intent = new Intent(this.service, TwitterLoginActivity.class);
		// Activity以外からActivityを呼び出す場合に必ず必要な設定
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Twitterアプリ認証画面を表示
		this.service.startActivity(intent);
	}

	/**
	 * Twitterからの接続を切断します。
	 * 
	 * @param editor Twitter関連の永続化情報エディター
	 */
	private void unconnectTwitter(SharedPreferences.Editor editor) {
		// --------------------------------------------------
		// OAuth認証情報を削除
		editor.remove(TwitterConstants.SUB_KEY_OAUTH_TOKEN);
		editor.remove(TwitterConstants.SUB_KEY_OAUTH_TOKEN_SECRET);
		editor.commit();
	}
}
