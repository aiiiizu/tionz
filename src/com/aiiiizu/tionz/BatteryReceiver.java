package com.aiiiizu.tionz;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class BatteryReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// バッテリー情報が変化した際の情報表示
		if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
			Log.d("TionzWidget", "battery temp:" + String.valueOf(intent.getIntExtra("temperature", 0)));

			int temperature = intent.getIntExtra("temperature", 0);

			AppWidgetManager appWidget = AppWidgetManager.getInstance(context);
			ComponentName name = new ComponentName(context, MainActivity.class);
			RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.main);
			remoteView.setTextViewText(R.id.temp_id, String.valueOf(temperature / 10.0));
			appWidget.updateAppWidget(name, remoteView);
		}
	}
}
