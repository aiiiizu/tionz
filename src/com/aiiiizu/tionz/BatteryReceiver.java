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
 * �o�b�e���[���x��ʒm����N���X�B
 *
 * startReceve() �Œʒm���J�n���AfinishReceive() �Œʒm���I�����܂��B
 * SCREEN_OFF �` SCREEN_ON �̊Ԃ͒ʒm����߂܂��B
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
	 * �o�b�e���[���x�̒ʒm���J�n����B
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
	 * �o�b�e���[���x�̒ʒm���I������B
	 */
	public void finishReceive() {
		mService.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		// --------------------------------------------------
		// �X�N���[���I�t�Ȃ�o�b�e�����x�ʒm�ꎞ����
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			finishReceive();
			// finishReceive()���邾�����ƁASCREEN_ON�̂��߂̐ݒ肪
			// �N���A����Ă��܂��̂ł����Őݒ肷��B
			mService.registerReceiver(this, new IntentFilter(Intent.ACTION_SCREEN_ON));
		}
		// --------------------------------------------------
		// �X�N���[���I���Ȃ�o�b�e�����x�ʒm�ꎞ�����̉���
		else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			startReceive();
		}
		// --------------------------------------------------
		// �o�b�e���[��񂪕ω������ۂ̏��\��
		else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
			// --------------------------------------------------
			// �[�����x�̌v�Z
			int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
			String tempStr = String.valueOf(temperature / 10.0);

			Log.d("TionzWidget", "BatteryReceiver::battery temp:" + tempStr);

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
