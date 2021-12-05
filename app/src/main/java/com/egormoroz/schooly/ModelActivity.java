//package com.egormoroz.schooly;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import org.a3dexample.ContentUtils;
//
//import java.io.IOException;
//import java.net.URL;
//import java.net.URLStreamHandler;
//import java.net.URLStreamHandlerFactory;
//
//public class ModelActivity extends Activity {
//
//    private static final int REQUEST_CODE_LOAD_TEXTURE = 1000;
//    private static final int FULLSCREEN_DELAY = 10000;
//
//    /**
//     * Type of model if file name has no extension (provided though content provider)
//     */
//    private int paramType;
//    /**
//     * The file to load. Passed as input parameter
//     */
//    private Uri paramUri;
//    /**
//     * Enter into Android Immersive mode so the renderer is full screen or not
//     */
//    private boolean immersiveMode = true;
//    /**
//     * Background GL clear color. Default is light gray
//     */
//    private float[] backgroundColor = new float[]{0f, 0f, 0f, 1.0f};
//
//    private ModelSurfaceView gLView;
//
//    private SceneLoader scene;
//
//    private Handler handler;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        String uri="https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fpenguin.obj?alt=media&token=347af199-fe1b-4719-92a4-b979f551466f";
//        this.paramUri=Uri.parse(uri);
//        // Try to get input parameters
//        Bundle b = getIntent().getExtras();
//        if (b != null) {
//            this.paramType = b.getString("type") != null ? Integer.parseInt(b.getString("type")) : -1;
//            this.immersiveMode = "true".equalsIgnoreCase(b.getString("immersiveMode"));
//            try {
//                String[] backgroundColors = b.getString("backgroundColor").split(" ");
//                backgroundColor[0] = Float.parseFloat(backgroundColors[0]);
//                backgroundColor[1] = Float.parseFloat(backgroundColors[1]);
//                backgroundColor[2] = Float.parseFloat(backgroundColors[2]);
//                backgroundColor[3] = Float.parseFloat(backgroundColors[3]);
//            } catch (Exception ex) {
//                // Assuming default background color
//            }
//        }
//        Log.i("Renderer", "Params: uri '" + paramUri + "'");
//
//        handler = new Handler(getMainLooper());
//
//        scene = new SceneLoader(this);
//        scene.init();
//
//        // Create a GLSurfaceView instance and set it
//        // as the ContentView for this Activity.
//        try {
//            gLView = new ModelSurfaceView(this);
//            setContentView(gLView);
//        } catch (Exception e) {
//            Toast.makeText(this, "Error loading OpenGL view:\n" +e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//
//        // Show the Up button in the action bar.
//        setupActionBar();
//
//        // TODO: Alert user when there is no multitouch support (2 fingers). He won't be able to rotate or zoom
//        ContentUtils.printTouchCapabilities(getPackageManager());
//
//        setupOnSystemVisibilityChangeListener();
//    }
//
//    /**
//     * Set up the {@link android.app.ActionBar}, if the API is available.
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private void setupActionBar() {
//        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//        // getActionBar().setDisplayHomeAsUpEnabled(true);
//        // }
//    }
//
//
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void setupOnSystemVisibilityChangeListener() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//            return;
//        }
//        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
//            // Note that system bars will only be "visible" if none of the
//            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
//            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
//                // The system bars are visible. Make any desired
//                hideSystemUIDelayed();
//            }
//        });
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            hideSystemUIDelayed();
//        }
//    }
//
//
//    private void toggleImmersive() {
//        this.immersiveMode = !this.immersiveMode;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//            return;
//        }
//        if (this.immersiveMode) {
//            hideSystemUI();
//        } else {
//            showSystemUI();
//        }
//        Toast.makeText(this, "Fullscreen " +this.immersiveMode, Toast.LENGTH_SHORT).show();
//    }
//
//    private void hideSystemUIDelayed() {
//        if (!this.immersiveMode) {
//            return;
//        }
//        handler.removeCallbacksAndMessages(null);
//        handler.postDelayed(this::hideSystemUI, FULLSCREEN_DELAY);
//
//    }
//
//    private void hideSystemUI() {
//        if (!this.immersiveMode) {
//            return;
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            hideSystemUIKitKat();
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            hideSystemUIJellyBean();
//        }
//    }
//
//    // This snippet hides the system bars.
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private void hideSystemUIKitKat() {
//        // Set the IMMERSIVE flag.
//        // Set the content to appear under the system bars so that the content
//        // doesn't resize when the system bars hide and show.
//        final View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                | View.SYSTEM_UI_FLAG_IMMERSIVE);
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void hideSystemUIJellyBean() {
//        final View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LOW_PROFILE);
//    }
//
//    // This snippet shows the system bars. It does this by removing all the flags
//    // except for the ones that make the content appear under the system bars.
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void showSystemUI() {
//        handler.removeCallbacksAndMessages(null);
//        final View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//    }
//
//    public Uri getParamUri() {
//        return paramUri;
//    }
//
//    public int getParamType() {
//        return paramType;
//    }
//
//    public float[] getBackgroundColor() {
//        return backgroundColor;
//    }
//
//    public SceneLoader getScene() {
//        return scene;
//    }
//
//    public ModelSurfaceView getGLView() {
//        return gLView;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        switch (requestCode) {
//            case REQUEST_CODE_LOAD_TEXTURE:
//                // The URI of the selected file
//                final Uri uri = data.getData();
//                if (uri != null) {
//                    Log.i("ModelActivity", "Loading texture '" + uri + "'");
//                    try {
//                        ContentUtils.setThreadActivity(this);
//                        scene.loadTexture(null, uri);
//                    } catch (IOException ex) {
//                        Log.e("ModelActivity", "Error loading texture: " + ex.getMessage(), ex);
//                        Toast.makeText(this, "Error loading texture '" + uri + "'. " + ex
//                                .getMessage(), Toast.LENGTH_LONG).show();
//                    } finally {
//                        ContentUtils.setThreadActivity(null);
//                    }
//                }
//        }
//    }
//}
