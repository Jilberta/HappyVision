package item;

import java.io.Serializable;

/**
 * Created by Jay on 10/3/2014.
 */
public class SongItem implements Serializable {
    private String title;
    private String artist;
    private int imageId;
    private int musicId;

    public SongItem (String title, String artist, int imageId, int musicId){
        this.title = title;
        this.artist = artist;
        this.imageId = imageId;
        this.musicId = musicId;
    }

    public String getTitle(){
        return this.title;
    }

    public String getArtist(){
        return this.artist;
    }

    public int getImageId(){
        return this.imageId;
    }

    public int getMusicId(){
        return this.musicId;
    }

}
