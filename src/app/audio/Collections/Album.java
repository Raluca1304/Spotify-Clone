package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
public final class Album extends AudioCollection {

    @Getter
    private final ArrayList<Song> songs;
    private final String description;
    @Setter
    private int totalLikes;

    public Album(final String name, final String owner, final ArrayList<Song> songs,
                 final String description) {
        super(name, owner);
        this.songs = songs;
        this.description = description;
    }

    @Override
    public int getNumberOfTracks() {
        return  songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    /**
     * Get total likes
     *
     * @return total likes
     */
    public int getTotalLikes() {
        int total = 0;
        for (Song song : songs) {
            total += song.getLikes();
        }
        return total;
    }



    @Override
    public String toString() {
        return "Album{name='" + getName()
                + "', owner='" + getOwner()
                + "', songs=" + songs + '}';
    }
}
