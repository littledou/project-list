package mobile.AUDetection;

import android.util.Log;

public class LogUtil {

	private boolean flag = true;
	public static void i(String str){
		Log.i("Affdex", str);
	}
	public static void i(String tag,String str){
		Log.i(tag, str);
	}
}
