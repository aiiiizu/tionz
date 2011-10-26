package com.aiiiizu.tionz;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.aiiiizu.tionz.common.SystemConstants;

/**
 * バッテリー温度を通知するクラス。
 *
 * startReceve() で通知を開始し、finishReceive() で通知を終了します。
 * SCREEN_OFF ～ SCREEN_ON の間は通知をやめます。
 *
 * @author daisuke
 *
 */
public class BatteryReceiver extends BroadcastReceiver {

	private Service mService = null;

	/**
	 *
	 * @param service
	 */
	public BatteryReceiver(Service service) {
		mService = service;
	}

	/**
	 * バッテリー温度の通知を開始する。
	 */
	public void startReceive() {
		try {
			mService.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			mService.registerReceiver(this, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		} catch ( RuntimeException e ) {
			Log.e("TionzWidget", "BatteryReceiver::" + e);
		}
	}

	/**
	 * バッテリー温度の通知を終了する。
	 */
	public void finishReceive() {
		mService.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		// --------------------------------------------------
		// スクリーンオフならバッテリ温度通知一時解除
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			finishReceive();
			// finishReceive()するだけだと、SCREEN_ONのための設定が
			// クリアされてしまうのでここで設定する。
			mService.registerReceiver(this, new IntentFilter(Intent.ACTION_SCREEN_ON));
		}
		// --------------------------------------------------
		// スクリーンオンならバッテリ温度通知一時解除の解除
		else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			startReceive();
		}
		// --------------------------------------------------
		// バッテリー情報が変化した際の情報表示
		else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
			// --------------------------------------------------
			// 端末温度の計算
			int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
			String tempStr = String.valueOf(temperature / 10.0);

			Log.d("TionzWidget", "BatteryReceiver::battery temp:" + tempStr);

			// --------------------------------------------------
			// Tionz永続化情報として端末温度を格納
			SharedPreferences pref = context.getSharedPreferences(SystemConstants.PREF_KEY, Service.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString(SystemConstants.SUB_KEY_TEMPERATURE, tempStr);
			editor.commit();

			// --------------------------------------------------
			// ウィジェットの更新
			AppWidgetManager appWidget = AppWidgetManager.getInstance(context);
			ComponentName name = new ComponentName(context, WidgetActivity.class);
			RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget);
			remoteView.setTextViewText(R.id.temp_id, tempStr);
			appWidget.updateAppWidget(name, remoteView);
		}
	}
}
