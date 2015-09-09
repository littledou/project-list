package mobile.AUDetection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.view.SurfaceView;


public class Detector {

	private final static String TAG = "Detector";
	private ImageListener mImageListener;
	private boolean isRunning;
	private CameraHelper mCameraHelper;
	private FaceAnalyze mJniHelper;
	private File mCascadeFile;
	private File mAUModelsFile;
	private boolean startDetect =  false;
	public Detector(Context mContext, SurfaceView cameraPreview) {
		mCameraHelper = new CameraHelper(mContext, this, cameraPreview);

		try {
			InputStream is =mContext.getAssets().open("haarcascade_frontalface_alt2.xml");
			File cascadeDir = mContext.getDir("cascade", Context.MODE_PRIVATE);
			mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt2.xml");
			FileOutputStream os = new FileOutputStream(mCascadeFile);
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			is.close();
			os.close();

			InputStream is2 = mContext.getAssets().open("au_models.xml");
			File auModelsDir = mContext.getDir("auModels", Context.MODE_PRIVATE);
			mAUModelsFile = new File(auModelsDir, "au_models.xml");
			FileOutputStream os2 = new FileOutputStream(mAUModelsFile);
			byte[] buffer2 = new byte[4096];
			int bytesRead2;
			while ((bytesRead2 = is2.read(buffer2)) != -1) {
				os2.write(buffer2, 0, bytesRead2);
			}
			is2.close();
			os2.close();
			mJniHelper = new FaceAnalyze(mCascadeFile.getAbsolutePath(), mAUModelsFile.getAbsolutePath());

			LogUtil.i(TAG, "jni load success"+":/n"+mCascadeFile.getAbsolutePath()+"/n:"+mAUModelsFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		this.isRunning = true;
		startDetect = true;
		if(mJniHelper==null){
			throw new IllegalStateException("mJniHelper not be null");
		}
		mCameraHelper.startDetection();
	}

	public void stop() {
		if(!this.isRunning) {
			throw new IllegalStateException("must call start() before stop()");
		} else {
			this.isRunning = false;
			mCameraHelper.stopDetection();
		}
	}
	void detectInFrame(final byte[] bytes, final int width, final int height) {

		if(startDetect){//start 
			startDetect = false;
			new Thread(new Runnable() {
				@Override
				public void run() {
					YuvImage yuvImage = new YuvImage(bytes, ImageFormat.NV21, width, height, null);
					if(yuvImage!=null){  
						try {
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							yuvImage.compressToJpeg(new Rect(0, 0,width,height), 80, bos);  
							byte[] byteArray = bos.toByteArray();
							FaceAnalyseResult mResult = new FaceAnalyseResult();
							long statusCode = mJniHelper.FaceAnalyse(byteArray, byteArray.length, mResult);
							LogUtil.i(TAG, "statusCode = "+statusCode);
							if(statusCode==0){
								mImageListener.onImageResults(mResult, width, height);
							}
							bos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}  finally{
							startDetect = true;
						}
					} 
				}
			}).start();;
		}
	}
	public boolean isRunning() {
		return this.isRunning;
	}

	public void setImageListener(ImageListener mImageListener) {
		this.mImageListener = mImageListener;
	}
	public  ImageListener getImageListener(){
		return mImageListener;
	}
	public interface ImageListener {
		void onImageResults(FaceAnalyseResult face, int width, int height);
	}
}
