package app.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;

public final class LikedContentPage extends TypePage {
    private User user;
    private ArrayList<Song> allSongs = new ArrayList<>();
    private ArrayList<Playlist> allPlaylists = new ArrayList<>();

    public LikedContentPage(final User user, final ArrayList<Song> allSongs,
                            final ArrayList<Playlist> allPlaylists) {
        this.user = user;
        this.allSongs = allSongs;
        this.allPlaylists = allPlaylists;
    }

    public LikedContentPage() {
        setPageType("LikedContentPage");
    }
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Current page.
     *
     * @param resultsSongs the song results
     * @param resultsPlaylists the plylist results
     * @param empty the empty
     * @return print for page
     */
    public String currentPage(final ArrayList<String> resultsSongs,
                              final ArrayList<String> resultsPlaylists,
                              final ArrayList<String> empty) {
        return "Liked songs:\n\t" + resultsSongs + "\n\nFollowed playlists:\n\t"
                + resultsPlaylists;
    }
}
