package com.aiiiizu.tionz;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class TionzService extends Service {

	BatteryReceiver mBatteryReceiver = new BatteryReceiver();

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d("TionzWidget", "TionzService::onStart");

    	// バッテリー情報を取得するための設定
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(Intent.ACTION_BATTERY_CHANGED);
    	registerReceiver(mBatteryReceiver, filter);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
