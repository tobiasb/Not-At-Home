package tbits.nah.phone;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

@SuppressWarnings("rawtypes")
public class PhoneReceiver extends BroadcastReceiver {

	private static final String TAG = "blockCall";

	@Override
	public void onReceive(Context context, Intent intent) {

		String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

		if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

			SharedPreferences p = context.getSharedPreferences("myBlocker", 0);

			if (p.getBoolean("blockCalls", false)) {
				terminateCall(context);
			}
		}
	}

	private void terminateCall(Context context) {
		// http://www.google.com/codesearch/p?hl=en#zvQ8rp58BUs/trunk/phone/src/i4nc4mp/myLock/phone/CallPrompt.java&q=itelephony%20package:http://mylockforandroid%5C.googlecode%5C.com

		try {
			Log.v(TAG, "Get getTeleService...");

			Object telephonyService = getTelephonyServiceObject(context);

			Class telephonyInterface = Class.forName("com.android.internal.telephony.ITelephony");

			if (telephonyService != null && telephonyInterface != null) {
				getAndInvokeMethod(telephonyInterface, telephonyService, "silenceRinger");
				getAndInvokeMethod(telephonyInterface, telephonyService, "endCall");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "FATAL ERROR: could not connect to telephony subsystem");
			Log.e(TAG, "Exception object: " + e);
		}
	}

	private Object getTelephonyServiceObject(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		try {
			Class c = Class.forName(tm.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			return m.invoke(tm);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void getAndInvokeMethod(Class c, Object target, String methodName) {

		try {
			Method m = c.getMethod(methodName);
			m.invoke(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
