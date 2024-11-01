package app.pages;

import app.audio.Collections.Podcast;
import app.user.userAdditions.Announcement;

import java.util.ArrayList;

public final class HostPage extends TypePage {
    private ArrayList<Podcast> hostPodcasts = new ArrayList<>();
    private ArrayList<Announcement> hostAnnouncements = new ArrayList<>();
    public HostPage() {
        setPageType("HostPage");
    }


    public HostPage(final ArrayList<Podcast> hostPodcasts,
                    final ArrayList<Announcement> hostAnnouncements) {
        this.hostPodcasts = hostPodcasts;
        this.hostAnnouncements = hostAnnouncements;
    }

    /**
     * Current page.
     *
     * @param resultsPodcast the podcast results
     * @param resultsAnnouncement the announcemnet results
     * @param empty the empty
     * @return print for page
     */
    public String currentPage(final ArrayList<String> resultsPodcast,
                              final ArrayList<String> resultsAnnouncement,
                              final ArrayList<String> empty) {
        return "Podcasts:\n\t" + resultsPodcast + "\n\nAnnouncements:\n\t" + resultsAnnouncement;
    }
}
