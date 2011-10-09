package com.aiiiizu.tionz;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/**
 * @author naoto
 */
public class SettingActivity extends PreferenceActivity {

	private PreferenceScreen _whatTionz;

	private PreferenceScreen _twitterAccount;

	private PreferenceScreen _facebookAccount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);

		_whatTionz = (PreferenceScreen) findPreference("about_key");
		_whatTionz.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference pref) {
				Intent nextActivity = new Intent(SettingActivity.this, AboutActivity.class);
				startActivity(nextActivity);
				return true;
			}
		});

		_twitterAccount = (PreferenceScreen) this.findPreference("account_twitter_key");
		_twitterAccount.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent nextActivity = new Intent(SettingActivity.this, TwitterAccountActivity.class);
				startActivity(nextActivity);
				return true;
			}
		});

		_facebookAccount = (PreferenceScreen) this.findPreference("account_facebook_key");
		_facebookAccount.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent nextActivity = new Intent(SettingActivity.this, FacebookAccountActivity.class);
				startActivity(nextActivity);
				return true;
			}
		});
	}

}
