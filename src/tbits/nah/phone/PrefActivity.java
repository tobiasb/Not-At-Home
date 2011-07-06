package tbits.nah.phone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;

public class PrefActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.pref);

		getPreferenceManager().setSharedPreferencesName("myBlocker");
	}

	@Override
	protected void onStart() {
		super.onStart();

		getPrefs();
	}

	private void getPrefs() {
		// we need to show the user's existing prefs, this isn't done
		// automatically by the activity
		SharedPreferences myprefs = getSharedPreferences("myBlocker", 0);

		((CheckBoxPreference) findPreference("blockCalls")).setChecked(myprefs.getBoolean("blockCalls", false));
	}
}
