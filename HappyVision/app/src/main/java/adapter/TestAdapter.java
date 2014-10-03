package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import happyvision.jilberta.leavingstone.happyvision.CameraActivity;
import happyvision.jilberta.leavingstone.happyvision.MyActivity;
import happyvision.jilberta.leavingstone.happyvision.R;
import item.SongItem;
import media.AudioProvider;
import views.CenteredViewItem;

/**
 * Created by Jay on 10/2/2014.
 */
public class TestAdapter implements ListAdapter {
    public List<SongItem> list = new ArrayList<SongItem>();

    private Context context;
    private static MediaPlayer player;
    private static AudioManager audioManager;
    private static int originalVolume;
    private static boolean isPlaying = false;

    public TestAdapter(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        originalVolume = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SongItem song = (SongItem) getItem(position);

        CenteredViewItem viewItem = new CenteredViewItem(parent.getContext());
        ((ImageView) viewItem.findViewById(R.id.song_image)).setImageResource(song.getImageId());
        ((TextView) viewItem.findViewById(R.id.song_title)).setText(song.getTitle());
        ((TextView) viewItem.findViewById(R.id.song_artist)).setText(song.getArtist());

        ImageView recButton = (ImageView) viewItem.findViewById(R.id.rec_button);
        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(AudioProvider.isPlaying())
//                    AudioProvider.stopMusic();
                if(isPlaying)
                    isPlaying = AudioProvider.stopMusic(player, audioManager, originalVolume);
                Intent cameraActivity = new Intent(context, CameraActivity.class);
                cameraActivity.putExtra("Song", song);
                context.startActivity(cameraActivity);
            }
        });

        final ImageView playButton = (ImageView) viewItem.findViewById(R.id.play_button);
        final ImageView pauseButton = (ImageView) viewItem.findViewById(R.id.pause_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
                player = MediaPlayer.create(context, song.getMusicId());
                isPlaying = AudioProvider.playMusic(context, player, audioManager, song.getMusicId());
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseButton.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.VISIBLE);
                isPlaying = AudioProvider.stopMusic(player, audioManager, originalVolume);
            }
        });

        convertView = viewItem;

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}
