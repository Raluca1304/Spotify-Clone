package app.pages;

import app.audio.Collections.Playlist;
import app.user.User;

import java.util.ArrayList;

public final class HomePage extends TypePage {
    private User user;
    private ArrayList<String> likedSongs = new ArrayList<>();
    private ArrayList<Playlist> likedPlylists = new ArrayList<>();

    public HomePage(final User user, final ArrayList<String> likedSongs,
                    final ArrayList<Playlist> likedPlylists) {
        this.user = user;
        this.likedSongs = likedSongs;
        this.likedPlylists = likedPlylists;
    }
    public HomePage() {
        setPageType("HomePage");
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
     * @param resultsSongs
     * @param resultsPlaylists
     * @param empty
     * @return print for page
     */
    public String currentPage(final ArrayList<String> resultsSongs,
                              final ArrayList<String> resultsPlaylists,
                              final ArrayList<String> empty) {
        return "Liked songs:\n\t" + resultsSongs + "\n\nFollowed playlists:\n\t"
                + resultsPlaylists;
    }

}
