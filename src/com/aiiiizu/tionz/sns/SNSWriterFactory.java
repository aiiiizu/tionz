package com.aiiiizu.tionz.sns;

import android.app.Service;
import android.content.Intent;

import com.aiiiizu.tionz.common.SystemConstants;
import com.aiiiizu.tionz.sns.facebook.FacebookWriter;
import com.aiiiizu.tionz.sns.twitter.TwitterWriter;
import com.aiiiizu.utils.StringUtils;

/**
 * <pre>
 * SNSWriter用のFactoryクラス
 * </pre>
 * 
 * @author maguhiro
 */
public class SNSWriterFactory {
	// ==================================================
	// Constructors
	private SNSWriterFactory() {
		// don't create
	}

	// ==================================================
	// Static Methods
	/**
	 * @param service 呼び出し元のサービス情報
	 * @param intent 呼び出し元サービスの起動起因のインテント情報
	 * @return アクション名に対応するSNS書込みオブジェクト(対応するものがない場合、nullを返す)
	 */
	public static AbstractSNSWriter create(Service service, Intent intent) {
		// --------------------------------------------------
		// 引数チェック
		if (service == null) return null;
		if (intent == null) return null;
		String actionName = intent.getAction();
		if (StringUtils.isNullOrEmpty(actionName)) return null;

		// --------------------------------------------------
		// SNSWriterの生成
		// Twitter
		if (actionName.equals(SystemConstants.ACTION_TWITTER)) return new TwitterWriter(service);
		// Facebook
		if (actionName.equals(SystemConstants.ACTION_FACEBOOK)) return new FacebookWriter(service);
		// それ以外
		return null;
	}
}
