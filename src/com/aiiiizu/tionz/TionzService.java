package com.aiiiizu.tionz;

import java.text.MessageFormat;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.aiiiizu.tionz.common.SystemConstants;
import com.aiiiizu.tionz.sns.AbstractSNSWriter;
import com.aiiiizu.tionz.sns.SNSWriterFactory;
import com.aiiiizu.utils.StringUtils;

public class TionzService extends Service {

	private BatteryReceiver mBatteryReceiver = null;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("TionzWidget", "TionzService::onCreate");

		// --------------------------------------------------
		// サービス開始時にバッテリ情報通知を登録する。
		mBatteryReceiver = new BatteryReceiver(this);
		mBatteryReceiver.startReceive();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d("TionzWidget", "TionzService::onStart");

		// --------------------------------------------------
		// サービス起動元の情報を取得
		String activator = intent.getStringExtra(SystemConstants.INTENT_KEY_ACTIVATOR);
		if (StringUtils.isNullOrEmpty(activator)) {
			activator = SystemConstants.ACTIVATOR_WIDGET;
		}
		Log.d("TionzWidget", "TionzService::Activator = " + activator);

		// --------------------------------------------------
		// ウィジェットのレイアウト情報をリモートビューに設定
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget);
		// リモートビュー情報のセットアップ
		this.setupRemoteViews(remoteViews);

		// --------------------------------------------------
		// 各ボタンに対するクリック処理
		AbstractSNSWriter writer = SNSWriterFactory.create(this, intent);
		if (writer != null) {
			// 対象アクション名に対応するSNSWriterが存在する場合

			// --------------------------------------------------
			// Tionz永続化情報から端末温度情報を取得し、SNSに書き込む内容を設定
			SharedPreferences pref = this.getSharedPreferences(SystemConstants.PREF_KEY, MODE_PRIVATE);
			String temperature = pref.getString(SystemConstants.SUB_KEY_TEMPERATURE, SystemConstants.EMPTY);
			// つぶやく文言のフォーマット
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			String formatContent = sharedPreferences.getString("share_contents_key", SystemConstants.EMPTY);
			String content = MessageFormat.format(formatContent, temperature);

			// --------------------------------------------------
			// 対象SNSへ書き込み
			writer.write(content);
		}

		// --------------------------------------------------
		// ウィジェットの更新
		this.updateWidget(remoteViews);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("TionzWidget", "TionzService::onDestroy");

		// --------------------------------------------------
		// サービス終了時にはバッテリ情報通知を解除する。
		mBatteryReceiver.finishReceive();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	// ==================================================
	// Private Methods
	/**
	 * RemoteViewsオブジェクトのセットアップを実施
	 *
	 * @param remoteViews
	 */
	private void setupRemoteViews(RemoteViews remoteViews) {
		// --------------------------------------------------
		// Tweetボタンに対するクリックイベントの追加
		if (true) {
			// ボタンが押された時に発行されるインテントオブジェクトの生成
			Intent intent = new Intent(SystemConstants.ACTION_TWITTER);
			// PendingIntentの設定
			PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
			// クリックイベントの設定
			remoteViews.setOnClickPendingIntent(R.id.tweet_btn, pendingIntent);
		}

		// --------------------------------------------------
		// Facebook(いいね)ボタンに対するクリックイベントの追加
		if (true) {
			// ボタンが押された時に発行されるインテントオブジェクトの生成
			Intent intent = new Intent(SystemConstants.ACTION_FACEBOOK);
			// PendingIntentの設定
			PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
			// クリックイベントの設定
			remoteViews.setOnClickPendingIntent(R.id.like_btn, pendingIntent);
		}

		// --------------------------------------------------
		// Tionz設定画面に対するクリックイベントの追加
		if (true) {
			// ボタンが押された時に発行されるインテントオブジェクトの生成
			Intent intent = new Intent(this,SettingActivity.class);
			// PendingIntentの設定
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
			// クリックイベントの設定
			remoteViews.setOnClickPendingIntent(R.id.temp_id, pendingIntent);
		}
	}

	/**
	 * ウィジェットの更新処理を実行します。
	 *
	 * @param remoteViews
	 */
	private void updateWidget(RemoteViews remoteViews) {
		ComponentName thisWidget = new ComponentName(this, WidgetActivity.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(thisWidget, remoteViews);
	}
}
