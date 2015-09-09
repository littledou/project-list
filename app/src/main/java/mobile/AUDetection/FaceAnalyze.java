package mobile.AUDetection;

public class FaceAnalyze {

	static{
		System.loadLibrary("face_analyze");
	}
	private long mNativeObj = 0;
	public FaceAnalyze(String CascadeFile,String mAUModelsFile) {
		mNativeObj = nativeCreateObject(CascadeFile,mAUModelsFile);
	}

	public void release() {
		nativeDestroyObject(mNativeObj);
		mNativeObj = 0;
	}
	public long FaceAnalyse(byte[] image_buf, int image_buf_len, FaceAnalyseResult results){
		return nativeFaceAnalyse(mNativeObj, image_buf, image_buf_len, results);
	}
	private static native long nativeCreateObject(String cascadeName,String mAUModelsFile);
	private static native void nativeDestroyObject(long thiz);
	private static native long nativeFaceAnalyse(long thiz, byte[] image_buf, int image_buf_len, FaceAnalyseResult results);
}
