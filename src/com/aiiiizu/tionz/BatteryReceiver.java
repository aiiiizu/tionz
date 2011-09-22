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
		// �o�b�e���[��񂪕ω������ۂ̏��\��
		if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
			Log.d("TionzWidget", "battery temp:" + String.valueOf(intent.getIntExtra("temperature", 0)));

			int temperature = intent.getIntExtra("temperature", 0);
			String tempStr = String.valueOf(temperature / 10.0);

			// --------------------------------------------------
			// Tionz�i�������Ƃ��Ē[�����x���i�[
			SharedPreferences pref = context.getSharedPreferences(SystemConstants.PREF_KEY, Service.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString(SystemConstants.SUB_KEY_TEMPERATURE, tempStr);
			editor.commit();

			// --------------------------------------------------
			// �E�B�W�F�b�g�̍X�V
			AppWidgetManager appWidget = AppWidgetManager.getInstance(context);
			ComponentName name = new ComponentName(context, WidgetActivity.class);
			RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget);
			remoteView.setTextViewText(R.id.temp_id, tempStr);
			appWidget.updateAppWidget(name, remoteView);
		}
	}
}
