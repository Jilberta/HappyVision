package media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Jay on 10/3/2014.
 */
public class AudioProvider {
//    private static MediaPlayer player;
//    private static AudioManager audioManager;
//    private static int originalVolume;
//    private static boolean isPlaying = false;

//    public static void playMusic(Context context, int musicID){
////        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
////        originalVolume = audioManager
////                .getStreamVolume(AudioManager.STREAM_MUSIC);
////        MediaPlayer player = MediaPlayer.create(context, musicID);
//        try {
//            player.prepare();
//        } catch (IllegalStateException e1) {
//            e1.printStackTrace();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        audioManager
//                .setStreamVolume(AudioManager.STREAM_MUSIC, audioManager
//                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
//        isPlaying = true;
//        player.start();
//    }

    public static boolean playMusic(Context context, MediaPlayer player, AudioManager audioManager,  int musicID){
//        player = MediaPlayer.create(context, musicID);
        try {
            player.prepare();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        audioManager
                .setStreamVolume(AudioManager.STREAM_MUSIC, audioManager
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        player.start();
        return true;
    }

    public static boolean stopMusic(MediaPlayer player, AudioManager audioManager, int originalVolume){
        player.stop();
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                originalVolume, 0);
        player.release();
        player = null;
        return false;
    }

//    public static void releaseAudioProvider(){
//        player.release();
//        player = null;
//    }
//
//    public static boolean isPlaying(){
//        return isPlaying;
//    }
}
