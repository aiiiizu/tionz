package com.aiiiizu.tionz;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/**
 * Tionz設定アクティビティ
 *
 * @author naoto
 */
public class SettingActivity extends PreferenceActivity{

	private PreferenceScreen _whatTionz;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        //What Tionz?への遷移
        _whatTionz = (PreferenceScreen)findPreference("about_key");
        _whatTionz.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference pref) {
            	Intent nextActivity = new Intent(SettingActivity.this,AboutActivity.class);
				startActivity(nextActivity);
            	return true;
            }
        });
    }

}
