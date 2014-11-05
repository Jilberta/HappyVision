package audio_encoder;

import android.content.Context;
import android.os.Environment;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.builder.FragmentedMp4Builder;
import com.googlecode.mp4parser.authoring.builder.SyncSampleIntersectFinderImpl;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Jay on 10/29/2014.
 */
public class MyAudioEncoder {

    public static void encodeSound(String videoPath, int audioID, Context context){
        try {
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();

            String tempFilePath = baseDir+"/Download/"+"opa.m4a";
            InputStream is = context.getResources().openRawResource(audioID);
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(tempFilePath));
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                stream.write(buffer, 0, len);
            }
            if(stream!=null)
                stream.close();

//            Movie countVideo = MovieCreator.build(videoPath);
            String audioPath2 = baseDir+"/Download/"+"count.m4a";


            Movie videoContainer = MovieCreator.build(videoPath);
            Movie audioContainer = MovieCreator.build(tempFilePath);

            Track audioTrack = audioContainer.getTracks().get(0);

            Movie newVideo = new Movie();
            for(Track t : videoContainer.getTracks()){
                if (t.getHandler().equals("vide")) {
                    newVideo.addTrack(t);
                }
            }

            newVideo.addTrack(audioTrack);

            {
                Container out = new DefaultMp4Builder().build(newVideo);
                FileOutputStream fos = new FileOutputStream(new File(baseDir + "/Download/" + "output.mp4"));
                out.writeContainer(fos.getChannel());
                fos.close();
            }
            /*{
                FragmentedMp4Builder fragmentedMp4Builder = new FragmentedMp4Builder();
                fragmentedMp4Builder.setIntersectionFinder(new SyncSampleIntersectFinderImpl(countVideo, null, -1));
                Container out = fragmentedMp4Builder.build(countVideo);
                FileOutputStream fos = new FileOutputStream(new File(baseDir + "/Download/" + "output-frag.mp4"));
                out.writeContainer(fos.getChannel());
                fos.close();
            }*/
            File file = new File(tempFilePath);
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
