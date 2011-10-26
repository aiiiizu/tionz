package com.aiiiizu.tionz;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aiiiizu.tionz.common.SystemConstants;

public class WidgetActivity extends AppWidgetProvider {

	@Override
	public void onEnabled(Context context) {
		Log.v("TionzWidget", "TionzWidget::onEnabled");
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.v("TionzWidget", "TionzWidget::onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		// --------------------------------------------------
		// �T�[�r�X�̓o�^�B���̃T�[�r�X�ŃE�B�W�F�b�g�X�V����������B
		Intent intent = new Intent(context.getApplicationContext(), TionzService.class);
		intent.putExtra(SystemConstants.INTENT_KEY_ACTIVATOR, SystemConstants.ACTIVATOR_WIDGET);
		context.startService(intent);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.v("TionzWidget", "TionzWidget::onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		Log.v("TionzWidget", "TionzWidget::onDisabled");
		super.onDisabled(context);

		// --------------------------------------------------
		// �T�[�r�X�̒�~�B�E�B�W�F�b�g�����ׂď����ꂽ��T�[�r�X����~������B
		Intent intent = new Intent(context.getApplicationContext(), TionzService.class);
		intent.putExtra(SystemConstants.INTENT_KEY_ACTIVATOR, SystemConstants.ACTIVATOR_WIDGET);
		context.stopService(intent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
//		Log.v("TionzWidget", "TionzWidget::onReceive");
		super.onReceive(context, intent);
	}
}