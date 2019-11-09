package com.ysxsoft.apaycoin.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.zxing.AmbientLightManager;
import com.ysxsoft.zxing.CaptureActivity;
import com.ysxsoft.zxing.CaptureActivityHandler;
import com.ysxsoft.zxing.IntentSource;
import com.ysxsoft.zxing.Intents;
import com.ysxsoft.zxing.PreferencesActivity;
import com.ysxsoft.zxing.ViewfinderView;
import com.ysxsoft.zxing.camera.CameraManager;

import java.io.IOException;

/**
 * 描述： 扫描
 * 日期： 2018/11/5 0005 10:55
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
@Deprecated
public class ScanActivity extends BaseActivity implements View.OnClickListener,SurfaceHolder.Callback {

    private View img_back;
    private TextView tv_title;
    private SurfaceView surfaceView;
    private ViewfinderView viewfinderView;
    private static final String TAG = CaptureActivity.class.getSimpleName();

    private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
    private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

    private static final String[] ZXING_URLS = {"http://zxing.appspot.com/scan", "zxing://scan/"};

    private static final int HISTORY_REQUEST_CODE = 0x0000bacc;

 /*   private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES =
            EnumSet.of(ResultMetadataType.ISSUE_NUMBER,
                    ResultMetadataType.SUGGESTED_PRICE,
                    ResultMetadataType.ERROR_CORRECTION_LEVEL,
                    ResultMetadataType.POSSIBLE_COUNTRY);*/

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
//    private Result savedResultToShow;
    private TextView statusView;
    private View resultView;
//    private Result lastResult;
    private boolean hasSurface;
    private boolean copyToClipboard;
    private IntentSource source;
    private String sourceUrl;
//    private ScanFromWebPageManager scanFromWebPageManager;
//    private Collection<BarcodeFormat> decodeFormats;
//    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
//    private HistoryManager historyManager;
//    private InactivityTimer inactivityTimer;
//    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;

    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    CameraManager getCameraManager() {
        return cameraManager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//让屏幕保存常亮
        setContentView(R.layout.scan_pay_layout);
        hasSurface = false;
        ambientLightManager = new AmbientLightManager(this);

        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("扫码支付");
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
                case R.id.tv_title:
//               startActivity(CaptureActivity.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewfinderView = getViewById(R.id.viewfinder_view);

        cameraManager = new CameraManager(getApplication());

        viewfinderView.setCameraManager(cameraManager);
        ambientLightManager.start(cameraManager);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(PreferencesActivity.KEY_DISABLE_AUTO_ORIENTATION, true)) {
//            setRequestedOrientation(getCurrentOrientation());
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        Intent intent = getIntent();

        copyToClipboard = prefs.getBoolean(PreferencesActivity.KEY_COPY_TO_CLIPBOARD, true) && (intent == null || intent.getBooleanExtra(Intents.Scan.SAVE_HISTORY, true));

        source = IntentSource.NONE;
        sourceUrl = null;
//        scanFromWebPageManager = null;
//        decodeFormats = null;
        characterSet = null;

        surfaceView = getViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    protected void onPause() {
        cameraManager.closeDriver();
        //historyManager = null; // Keep for onActivityResult
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(com.ysxsoft.zxing.R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
//            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
//                handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
            }
//            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
//            Log.w(TAG, "Unexpected error initializing camera", e);
//            displayFrameworkBugMessageAndExit();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //do thing

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (source == IntentSource.NATIVE_APP_INTENT) {
                    setResult(RESULT_CANCELED);
                    finish();
                    return true;
                }
                if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) /*&& lastResult != null*/) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                // Handle these events so they don't launch the Camera app
                return true;
            // Use volume up/down to turn on light
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.setTorch(false);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.setTorch(true);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(com.ysxsoft.zxing.R.id.restart_preview, delayMS);
        }
    }

}
