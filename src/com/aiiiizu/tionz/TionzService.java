package com.aiiiizu.tionz;

import java.text.MessageFormat;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
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

	BatteryReceiver mBatteryReceiver = new BatteryReceiver();

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d("TionzWidget", "TionzService::onStart");

		// --------------------------------------------------
		// �T�[�r�X�N�����̏����擾
		String activator = intent.getStringExtra(SystemConstants.INTENT_KEY_ACTIVATOR);
		if (StringUtils.isNullOrEmpty(activator)) {
			activator = SystemConstants.ACTIVATOR_WIDGET;
		}
		Log.d("TionzWidget", "TionzService::Activator = " + activator);

		// --------------------------------------------------
		// �E�B�W�F�b�g�̃��C�A�E�g���������[�g�r���[�ɐݒ�
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget);
		// �����[�g�r���[���̃Z�b�g�A�b�v
		this.setupRemoteViews(remoteViews);
		
		// --------------------------------------------------
		// �e�{�^���ɑ΂���N���b�N����
		AbstractSNSWriter writer = SNSWriterFactory.create(this, intent);
		if (writer != null) {
			// �ΏۃA�N�V�������ɑΉ�����SNSWriter�����݂���ꍇ

			// --------------------------------------------------
			// Tionz�i������񂩂�[�����x�����擾���ASNS�ɏ������ޓ��e��ݒ�
			SharedPreferences pref = this.getSharedPreferences(SystemConstants.PREF_KEY, MODE_PRIVATE);
			String temperature = pref.getString(SystemConstants.SUB_KEY_TEMPERATURE, SystemConstants.EMPTY);
			// �Ԃ₭�����̃t�H�[�}�b�g
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			String formatContent = sharedPreferences.getString("share_contents_key", SystemConstants.EMPTY);
			String content = MessageFormat.format(formatContent, temperature);

			// --------------------------------------------------
			// �Ώ�SNS�֏�������
			writer.write(content);
		}

		// --------------------------------------------------
		// �o�b�e���[�����擾���邽�߂̐ݒ�
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(mBatteryReceiver, filter);

		// --------------------------------------------------
		// �E�B�W�F�b�g�̍X�V
		this.updateWidget(remoteViews);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	// ==================================================
	// Private Methods
	/**
	 * RemoteViews�I�u�W�F�N�g�̃Z�b�g�A�b�v�����{
	 *
	 * @param remoteViews
	 */
	private void setupRemoteViews(RemoteViews remoteViews) {
		// --------------------------------------------------
		// Tweet�{�^���ɑ΂���N���b�N�C�x���g�̒ǉ�
		if (true) {
			// �{�^���������ꂽ���ɔ��s�����C���e���g�I�u�W�F�N�g�̐���
			Intent intent = new Intent(SystemConstants.ACTION_TWITTER);
			// PendingIntent�̐ݒ�
			PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
			// �N���b�N�C�x���g�̐ݒ�
			remoteViews.setOnClickPendingIntent(R.id.tweet_btn, pendingIntent);
		}

		// --------------------------------------------------
		// Facebook(������)�{�^���ɑ΂���N���b�N�C�x���g�̒ǉ�
		if (true) {
			// �{�^���������ꂽ���ɔ��s�����C���e���g�I�u�W�F�N�g�̐���
			Intent intent = new Intent(SystemConstants.ACTION_FACEBOOK);
			// PendingIntent�̐ݒ�
			PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
			// �N���b�N�C�x���g�̐ݒ�
			remoteViews.setOnClickPendingIntent(R.id.like_btn, pendingIntent);
		}

		// --------------------------------------------------
		// Tionz�ݒ��ʂɑ΂���N���b�N�C�x���g�̒ǉ�
		if (true) {
			// �{�^���������ꂽ���ɔ��s�����C���e���g�I�u�W�F�N�g�̐���
			Intent intent = new Intent(this,SettingActivity.class);
			// PendingIntent�̐ݒ�
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
			// �N���b�N�C�x���g�̐ݒ�
			remoteViews.setOnClickPendingIntent(R.id.temp_id, pendingIntent);
		}
	}

	/**
	 * �E�B�W�F�b�g�̍X�V���������s���܂��B
	 *
	 * @param remoteViews
	 */
	private void updateWidget(RemoteViews remoteViews) {
		ComponentName thisWidget = new ComponentName(this, WidgetActivity.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(thisWidget, remoteViews);
	}
}
