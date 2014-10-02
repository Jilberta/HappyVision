package happyvision.jilberta.leavingstone.happyvision;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends Activity {

    private Context context;
    private Camera camera;
    private MediaRecorder recorder;
    private CameraPreview preview;
    private boolean isRecording = false;
    private MediaPlayer player;
    private AudioManager audioManager;
    private int originalVolume;
    private int musicID;
    private static final int MAX_DURATION = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        camera = getCameraInstance();
        preview = new CameraPreview(this, camera);
        FrameLayout framePreview = (FrameLayout) findViewById(R.id.camera_preview_frame);
        framePreview.addView(preview);

        ((Button) findViewById(R.id.capture)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked");
                if (isRecording) {
                    System.out.println("Paused");
                    changeButtonText("Capture");
                    recorder.stop();
                //    player.stop();
                    releaseMediaRecorder();
                    camera.lock();
                    isRecording = false;
//                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
//                            originalVolume, 0);
                } else {
                    System.out.println(" Start Recording");
                    boolean prepared = prepareVideoRecorder();
                    System.out.println("Opaa " + prepared);
                    if (prepared) {
                        System.out.println("Recording");
                        changeButtonText("Pause");
//                        audioManager
//                                .setStreamVolume(
//                                        AudioManager.STREAM_MUSIC,
//                                        audioManager
//                                                .getStreamMaxVolume(AudioManager.STREAM_MUSIC),
//                                        0);
                        recorder.start();
                    //    player.start();
                        isRecording = true;
                    } else {
                        releaseMediaRecorder();
                    }
                }
            }
        });
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    private void changeButtonText(String text){
        ((Button)findViewById(R.id.capture)).setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        releaseCamera();
    }

    private boolean prepareVideoRecorder() {

//        player = MediaPlayer.create(context, musicID);
        recorder = new MediaRecorder();

        camera.unlock();
        recorder.setCamera(camera);

        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        recorder.setOutputFile(getOutputMediaFile().toString());

        recorder.setPreviewDisplay(preview.getHolder().getSurface());
        recorder.setMaxDuration(MAX_DURATION);

        try {
     //       player.prepare();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        }
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            System.out.println("IllegalStateExc " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOExc " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory(), "HappyVision Videos");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("HappyVision Videos", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");

        return mediaFile;
    }

    private void releaseMediaRecorder() {
        if (recorder != null) {
            recorder.reset();
            recorder.release();
            recorder = null;
            camera.lock();
//            player.release();
//            player = null;
        }
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
