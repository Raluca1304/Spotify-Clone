package app.pages;

import app.audio.Collections.Album;
import app.user.userAdditions.Event;
import app.user.userAdditions.Merch;

import java.util.ArrayList;

public final class ArtistPage extends TypePage {
    private ArrayList<Album> artistAlbum = new ArrayList<>();
    private ArrayList<Merch> artistMerch = new ArrayList<>();
    private ArrayList<Event> artistEvent = new ArrayList<>();
    public ArtistPage() {
        setPageType("ArtistPage");
    }

    public ArtistPage(final ArrayList<Album> artistAlbum,
                      final ArrayList<Merch> artistMerch,
                      final ArrayList<Event> artistEvent) {
        this.artistAlbum = artistAlbum;
        this.artistMerch = artistMerch;
        this.artistEvent = artistEvent;
    }

    /**
     * Current page.
     *
     * @param resultsAlbum the album results
     * @param resultsMerch the merch results
     * @param resultsEvent the event results
     * @return print for page
     */
    public String currentPage(final ArrayList<String> resultsAlbum,
                              final ArrayList<String> resultsMerch,
                              final ArrayList<String> resultsEvent) {
        return "Albums:\n\t" + resultsAlbum + "\n\nMerch:\n\t" + resultsMerch
                + "\n\nEvents:\n\t" + resultsEvent;
    }

}
