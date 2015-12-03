package com.readface.cafe.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("deprecation")
public class CameraHelper implements Callback, PreviewCallback {

    private Camera camera;
    private CameraManager cameraManager;
    private CameraFacade cameraFacade;
    private SurfaceViewFacade surfaceViewFacade;
    private boolean detecting = false;

    public boolean isDetecting() {
        return detecting;
    }

    public void setDetecting(boolean detecting) {
        this.detecting = detecting;
    }

    private static boolean toggleUp = false;
    private int previewWidth;
    private int previewHeight;
    private Display defaultDisplay;
    int displayRotation;

    public CameraHelper(Context context, SurfaceView sv) {
        this.cameraManager = new CameraManager();
        this.cameraFacade = new CameraFacade(cameraManager);
        this.defaultDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        this.displayRotation = defaultDisplay.getRotation();
        this.surfaceViewFacade = new SurfaceViewFacadeFactory(context).create(sv);
    }

    public void startDetection(int cameraId) {

        try {
            this.camera = this.cameraFacade.acquireCamera(cameraId);
            this.initCameraParams();
            SurfaceHolder holder = this.surfaceViewFacade.getSurfaceView().getHolder();
            holder.addCallback(this);
            this.surfaceViewFacade.setDisplayAspectRatio(this.previewWidth, this.previewHeight, true);
            this.detecting = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TestActivity", "camera open error ");

        }
    }

    private void initCameraParams() {
        Parameters cameraParams = this.camera.getParameters();
        this.setOptimalPreviewSize(cameraParams, 480, 320);
        this.camera.setParameters(cameraParams);
    }

    public void stopDetection() {
        try {
            if (this.detecting) {
                this.stopPreviewing();
                SurfaceHolder holder = this.surfaceViewFacade.getSurfaceView().getHolder();
                holder.removeCallback(this);
                this.camera.release();
                this.camera = null;
                this.detecting = false;
            }
        } catch (Exception e) {

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.startPreviewing(holder);
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.stopPreviewing();
    }

    public interface ImageListener {
        void onDataResults(byte[] data, Camera camera);

        void onError();
    }

    protected ImageListener mImageListener;

    public void setImageListener(ImageListener mImageListener) {
        this.mImageListener = mImageListener;
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mImageListener != null)
            this.mImageListener.onDataResults(data, camera);
        camera.addCallbackBuffer(data);
    }

    private static void setOptimalPreviewFrameRate(Parameters cameraParams) {
        short targetHiMS = 30000;
        List ranges = cameraParams.getSupportedPreviewFpsRange();
        Log.v("CameraHelper", "ranges.size = " + ranges.size());
        if (1 != ranges.size()) {
            String rangeStr = "";

            int[] minDiff;
            for (Iterator optimalRange = ranges.iterator(); optimalRange.hasNext(); rangeStr = rangeStr + "[" + minDiff[0] + "," + minDiff[1] + "]; ") {
                minDiff = (int[]) optimalRange.next();
            }

            Log.v("CameraHelper", "Available frame rates: " + rangeStr);
            int[] optimalRange1 = null;
            int minDiff1 = 2147483647;
            Iterator mIterator = ranges.iterator();

            while (mIterator.hasNext()) {
                int[] range = (int[]) mIterator.next();
                int currentDiff = Math.abs(range[1] - targetHiMS);
                if (currentDiff <= minDiff1) {
                    optimalRange1 = range;
                    minDiff1 = currentDiff;
                }
            }

            cameraParams.setPreviewFpsRange(optimalRange1[0], optimalRange1[1]);
            Log.v("CameraHelper", "Target frame rate : 30.0 chosen: [" + optimalRange1[0] + ", " + optimalRange1[1] + "]");
        }
    }

    private void setOptimalPreviewSize(Parameters cameraParams, int targetWidth, int targetHeight) {
        List<Size> supportedPreviewSizes = cameraParams.getSupportedPreviewSizes();
        for (int i = 0; i < supportedPreviewSizes.size(); i++) {
            Log.v("CameraHelper", "supportedPreviewSizes: " + supportedPreviewSizes.get(i).width + "*" + supportedPreviewSizes.get(i).height);
        }
        if (null == supportedPreviewSizes) {
            Log.v("CameraHelper", "Camera returning null for getSupportedPreviewSizes(), will use default");
        } else {
            Size optimalSize = null;
            double minDiff = 1.7976931348623157E308D;
            Iterator mIterator = supportedPreviewSizes.iterator();

            while (mIterator.hasNext()) {
                Size size = (Size) mIterator.next();
                if ((double) Math.abs(size.width - targetWidth) < minDiff) {
                    optimalSize = size;
                    minDiff = (double) Math.abs(size.width - targetWidth);
                }
            }

            this.previewWidth = optimalSize.width;
            this.previewHeight = optimalSize.height;
            Log.v("CameraHelper", "Preview width: " + this.previewWidth + " height: " + this.previewHeight);
            cameraParams.setPreviewSize(this.previewWidth, this.previewHeight);
        }
    }


    private void setupPreviewWithCallbackBuffers() {
        Parameters params = this.camera.getParameters();
        int previewFormat = params.getPreviewFormat();
        int bitsPerPixel = ImageFormat.getBitsPerPixel(previewFormat);
        Size size = params.getPreviewSize();
        int bufSize = size.width * size.height * bitsPerPixel / 8;
        this.camera.addCallbackBuffer(new byte[bufSize]);
        this.camera.setPreviewCallbackWithBuffer(this);
    }

    private void setCameraDisplayOrientation() {
        CameraInfo info = new CameraInfo();
        this.cameraManager.getCameraInfo(this.cameraFacade.getCameraId(), info);
        short degrees = 0;
        switch (this.displayRotation) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
        }

        int result;
        if (info.facing == 1) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        this.camera.setDisplayOrientation(result);
    }

    private void startPreviewing(SurfaceHolder holder) {
        this.setCameraDisplayOrientation();
        this.surfaceViewFacade.prepareForPreview(this.previewWidth, this.previewHeight);

        try {
            this.camera.setPreviewDisplay(holder);
        } catch (IOException var3) {
            Log.i("CameraHelper", "Unable to start camera preview" + var3.getMessage());
        }

        this.camera.setOneShotPreviewCallback(new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (mImageListener != null)
                    CameraHelper.this.mImageListener.onDataResults(data, camera);
                CameraHelper.this.setupPreviewWithCallbackBuffers();
            }
        });

        this.camera.startPreview();
    }

    private void stopPreviewing() {
        this.camera.stopPreview();
        this.camera.setPreviewCallback((PreviewCallback) null);
        this.surfaceViewFacade.cleanupAfterPreview();
    }

    static class CameraManager {
        CameraManager() {
        }

        int getNumberOfCameras() {
            return Camera.getNumberOfCameras();
        }

        void getCameraInfo(int cameraId, CameraInfo cameraInfo) {
            Camera.getCameraInfo(cameraId, cameraInfo);
        }

        Camera open(int cameraId) {
            return Camera.open(cameraId);
        }
    }

    static class CameraFacade {
        private int cameraId;
        private CameraManager cameraManager;

        CameraFacade(CameraManager cameraManager) {
            if (cameraManager == null) {
                throw new NullPointerException("cameraManager must be non-null");
            } else {
                this.cameraManager = cameraManager;
            }
        }

        Camera acquireCamera(int cameraId) throws IllegalStateException {
            int cameraToOpen = cameraId;
            int cnum = this.cameraManager.getNumberOfCameras();
            int cameraID = -1;
            CameraInfo caminfo = new CameraInfo();

            for (int result = 0; result < cnum; ++result) {
                this.cameraManager.getCameraInfo(result, caminfo);
                if (caminfo.facing == cameraToOpen) {
                    cameraID = result;
                    break;
                }
            }

            if (cameraID == -1) {
                throw new IllegalStateException("This device does not have a camera of the requested type");
            } else {
                Camera var10;
                try {
                    var10 = this.cameraManager.open(cameraID);
                } catch (RuntimeException var9) {
                    String msg = "Camera is unavailable. Please close the app that is using the camera and then try again.\nError:  " + var9.getMessage();
                    throw new IllegalStateException(msg, var9);
                }

                this.cameraId = cameraID;
                return var10;
            }
        }

        int getCameraId() {
            return this.cameraId;
        }
    }

    static class SurfaceViewFacade {
        private WindowManager windowManager;
        private Context context;
        private SurfaceView surfaceView;
        private boolean usingPrivateSurfaceView;
        private LayoutParams privateLayoutParams;

        SurfaceViewFacade(Context cntx, SurfaceView providedSurfaceView) {
            this.context = cntx;
            this.windowManager = (WindowManager) cntx.getSystemService(Context.WINDOW_SERVICE);
            if (null == providedSurfaceView) {
                this.surfaceView = this.createPrivateSurfaceView();
                this.usingPrivateSurfaceView = true;
            } else {
                this.surfaceView = providedSurfaceView;
            }

        }

        private SurfaceView createPrivateSurfaceView() {
            SurfaceView result = new SurfaceView(this.context);
            this.privateLayoutParams = new LayoutParams(1, 1, 2006, 262144, -3);
            this.privateLayoutParams.gravity = 85;
            return result;
        }

        void setDisplayAspectRatio(int cameraImageWidth, int cameraImageHeight, boolean forceSizeChange) {
            if (!this.usingPrivateSurfaceView) {
                int viewHeight = this.surfaceView.getMeasuredHeight();
                if (0 == viewHeight) {
                    return;
                }

                int viewWidth = this.surfaceView.getMeasuredWidth();
                float viewRatio = (float) viewWidth / (float) viewHeight;
                float cameraRatio = (float) cameraImageWidth / (float) cameraImageHeight;
                if (viewWidth < viewHeight) {
                    cameraRatio = (float) cameraImageHeight / (float) cameraImageWidth;
                }

                if ((double) Math.abs(viewRatio - cameraRatio) > 0.01D) {
                    if (viewRatio > cameraRatio) {
                        viewWidth = Math.round((float) viewHeight * cameraRatio);
                    } else {
                        viewHeight = Math.round((float) viewWidth / cameraRatio);
                    }

                    this.surfaceView.getHolder().setFixedSize(viewWidth, viewHeight);
                } else if (forceSizeChange) {
                    if (CameraHelper.toggleUp) {
                        ++viewWidth;
                    } else {
                        --viewWidth;
                    }

                    CameraHelper.toggleUp = !CameraHelper.toggleUp;
                    this.surfaceView.getHolder().setFixedSize(viewWidth, viewHeight);
                }
            }

        }

        void prepareForPreview(int previewWidth, int previewHeight) {
            this.setDisplayAspectRatio(previewWidth, previewHeight, false);
            if (this.usingPrivateSurfaceView) {
                this.windowManager.addView(this.surfaceView, this.privateLayoutParams);
            }

        }

        void cleanupAfterPreview() {
            if (this.usingPrivateSurfaceView) {
                this.windowManager.removeView(this.surfaceView);
            }

        }

        SurfaceView getSurfaceView() {
            return this.surfaceView;
        }
    }

    static class SurfaceViewFacadeFactory {
        Context context;

        SurfaceViewFacadeFactory(Context context) {
            this.context = context;
        }

        SurfaceViewFacade create(SurfaceView surfaceView) {
            return new SurfaceViewFacade(this.context, surfaceView);
        }
    }

}
