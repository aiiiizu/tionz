package com.aiiiizu.tionz;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.aiiiizu.tionz.common.SystemConstants;
import com.aiiiizu.tionz.sns.facebook.FacebookConstants;
import com.aiiiizu.tionz.sns.twitter.TwitterConstants;

/**
 * @author naoto
 */
public class SettingActivity extends PreferenceActivity {

	private PreferenceScreen _whatTionz;

	private PreferenceScreen _twitterAccount;

	private PreferenceScreen _facebookAccount;

	private ListPreference _contentList;

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

		_contentList = (ListPreference) this.findPreference("share_contents_key");
		_contentList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
				String contents = sharedPreferences.getString("share_contents_key", SystemConstants.EMPTY);
				_contentList.setSummary(contents);
				return true;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String contents = sharedPreferences.getString("share_contents_key", SystemConstants.EMPTY);
		_contentList.setSummary(contents);

		SharedPreferences twitterPref = this.getSharedPreferences(TwitterConstants.PREF_KEY, Service.MODE_PRIVATE);
		_twitterAccount.setSummary(twitterPref.getString(TwitterConstants.SUB_KEY_USER_NAME,
				this.getString(R.string.setting_account_twitter)));

		SharedPreferences facebookPref = this.getSharedPreferences(FacebookConstants.PREF_KEY, Service.MODE_PRIVATE);
		_facebookAccount.setSummary(facebookPref.getString(FacebookConstants.SUB_KEY_USER_NAME,
				this.getString(R.string.setting_account_facebook)));
	}
}
