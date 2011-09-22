package com.aiiiizu.tionz;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.aiiiizu.tionz.common.SystemConstants;

public class BatteryReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// バッテリー情報が変化した際の情報表示
		if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
			Log.d("TionzWidget", "battery temp:" + String.valueOf(intent.getIntExtra("temperature", 0)));

			int temperature = intent.getIntExtra("temperature", 0);
			String tempStr = String.valueOf(temperature / 10.0);

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
