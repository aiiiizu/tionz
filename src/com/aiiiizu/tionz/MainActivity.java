package com.aiiiizu.tionz;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MainActivity extends AppWidgetProvider {

	@Override
	public void onEnabled(Context context) {
		Log.v("TionzWidget", "onEnabled");
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.v("TionzWidget", "onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		// サービスの登録、このサービスで更新処理をする。
		context.startService(new Intent(context, TionzService.class));
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.v("TionzWidget", "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		Log.v("TionzWidget", "onDisabled");
		super.onDisabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("TionzWidget", "onReceive");
		super.onReceive(context, intent);
	}

}