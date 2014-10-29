package happyvision.jilberta.leavingstone.happyvision;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import audio_encoder.MyAudioEncoder;
import camera.CameraPreview;
import item.SongItem;


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
    private String videoPath;
    private VideoView videoView;
    private static final int MAX_DURATION = 15000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        context = this;

        SongItem song = (SongItem) getIntent().getExtras().getSerializable("Song");
        getMusic(song);

        camera = getCameraInstance();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        originalVolume = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        preview = new CameraPreview(this, camera);
        FrameLayout framePreview = (FrameLayout) findViewById(R.id.camera_preview_frame);
        framePreview.addView(preview);

        videoView = (VideoView) findViewById(R.id.video_view);



        ((ImageView) findViewById(R.id.capture)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//                }
                System.out.println("Clicked");
                if (isRecording) {
                    System.out.println("Paused");
                    stopRecording();
                } else {
                    System.out.println(" Start Recording");
                    boolean prepared = prepareVideoRecorder();
                    System.out.println("Opaa " + prepared);
                    if (prepared) {
                        System.out.println("Recording");
                        startRecording();
                    } else {
                        releaseMediaRecorder();
                    }
                }
            }
        });

        ((ImageView) findViewById(R.id.home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraActivity.this, MyActivity.class);
                startActivity(intent);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
//            Uri videoUri = intent.getData();
//           // mVideoView.setVideoURI(videoUri);
//        }
//    }

    private void startRecording(){
//        changeButtonText("Pause");
        changeImageViewImg(R.id.capture, R.drawable.record_pause);
        audioManager
                .setStreamVolume(AudioManager.STREAM_MUSIC, audioManager
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        recorder.start();
        player.start();
        isRecording = true;
    }

    private void stopRecording(){
//        changeButtonText("Capture");
        changeImageViewImg(R.id.capture, R.drawable.record_rec);
        recorder.stop();
        player.stop();
        releaseMediaRecorder();
        camera.lock();
        isRecording = false;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                originalVolume, 0);
        openDialog();
    }

    private void getMusic(SongItem song){
//        musicID = getResources().getIdentifier("joe_cocker_you_can_leave_your_hat_on", "drawable", getApplicationContext().getPackageName());
        musicID = song.getMusicId();
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
//        ((Button)findViewById(R.id.capture)).setText(text);
    }

    private void changeImageViewImg(int imageViewId, int imageId){
        ImageView image = (ImageView) findViewById(imageViewId);
        image.setImageResource(imageId);
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

        player = MediaPlayer.create(context, musicID);
        recorder = new MediaRecorder();

        camera.unlock();
        recorder.setCamera(camera);

        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        String path = getOutputMediaFile().toString();
        videoPath = path;
        recorder.setOutputFile(path);

        recorder.setPreviewDisplay(preview.getHolder().getSurface());
        recorder.setMaxDuration(MAX_DURATION);

        recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                    Toast.makeText(context, "Video Capturing Finished", Toast.LENGTH_LONG).show();
                    stopRecording();
                    String audioPath = String.valueOf(Uri.parse("android.resource://happyvision.jilberta.leavingstone.happyvision/drawable/count"));
                    MyAudioEncoder.encodeSound(videoPath, audioPath);
                    openDialog();
                }
            }
        });

        try {
            player.prepare();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOExc " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void openDialog(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Capturing Finished");
        alertDialogBuilder.setMessage("Bla Bla Bla");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Play", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                findViewById(R.id.record_buttons_layout).setVisibility(View.INVISIBLE);
                videoView.setVideoPath(videoPath);
                videoView.setMediaController(new MediaController(context));
                videoView.start();
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        findViewById(R.id.record_buttons_layout).setVisibility(View.VISIBLE);
                        openDialogAfterPlayingVideo();
                    }
                });
            }
        });
        alertDialogBuilder.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file = new File(videoPath);
                file.delete();
                goImmersiveMode();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openDialogAfterPlayingVideo(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Capturing Finished");
        alertDialogBuilder.setMessage("Bla Bla Bla");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context, "Video is Saved", Toast.LENGTH_SHORT).show();
                goImmersiveMode();
            }
        });
        alertDialogBuilder.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file = new File(videoPath);
                file.delete();
                videoView.stopPlayback();
                goImmersiveMode();
            }
        });
        alertDialogBuilder.setNeutralButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("video/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, videoPath);
                startActivity(Intent.createChooser(sharingIntent, "Share Your Happy Vision using"));
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        goImmersiveMode();
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
            player.release();
            player = null;
            changeImageViewImg(R.id.capture, R.drawable.record_rec);
        }
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void goImmersiveMode(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
