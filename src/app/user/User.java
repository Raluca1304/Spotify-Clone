package app.user;

import app.Admin;
import app.audio.Collections.*;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.pages.*;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.user.userAdditions.Announcement;
import app.user.userAdditions.Event;
import app.user.userAdditions.Merch;
import app.utils.Enums;
import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * The type User.
 */
public final class User extends LibraryEntry {
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    private Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;

    @Getter
    @Setter
    private boolean online;
    @Getter
    @Setter
    private String userType;
    @Getter
    private ArrayList<Album> albums;
    @Getter
    private ArrayList<Song> songs;
    @Getter
    private ArrayList<Podcast> podcasts;
    private Page page;
    private HomePage homepage;
    @Getter
    private ArrayList<Event> events;
    @Getter
    private ArrayList<Merch> merches;
    @Getter
    private ArrayList<Announcement> announcements;
    @Setter
    private int artistsLikes;
    private static final int LIMIT = 5;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        super(username);
        this.username = username;
        this.age = age;
        this.city = city;
        this.events = new ArrayList<>();
        this.merches = new ArrayList<>();
        this.announcements = new ArrayList<>();
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        online = true;
        this.userType = userType;
        albums = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        page = new Page();
        homepage = new HomePage();
        page.setCurrentPage(homepage);
        this.setPage(page);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPage(final Page page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

        if (libraryEntries == null) {
            return null;
        }
        for (LibraryEntry libraryEntry : libraryEntries) {
            if (libraryEntry != null) {
                results.add(libraryEntry.getName());
            }
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        LibraryEntry selected = searchBar.select(itemNumber);
        Page page = Page.getInstance();
        if (selected == null) {
            return "The selected ID is too high.";
        }
        if (searchBar.getLastSearchType().equals("artist")) {
            page.setCurrentPage(FactoryCurrentPage.createPage(
                    FactoryCurrentPage.PageType.ArtistPage));
            page.setArtistName(selected.getUsername());
            page.setCurrentUser(selected.getUsername());
            setPage(page);
            return "Successfully selected %s's page.".formatted(selected.getName());
        } else if (searchBar.getLastSearchType().equals("host")) {
            page.setCurrentPage(FactoryCurrentPage.createPage(
                    FactoryCurrentPage.PageType.HostPage));
            page.setCurrentUser(selected.getUsername());
            page.setHostName(selected.getUsername());
            setPage(page);
            return "Successfully selected %s's page.".formatted(selected.getName());
        }

        return "Successfully selected %s.".formatted(selected.getName());
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();
        player.pause();
        return "Playback loaded successfully.";
    }


    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
        && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }
        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlist.setFollowers(getTotalFollowers(playlist.getName()));
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();
        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {

        ArrayList<Song> sortedSongs = new ArrayList<>(likedSongs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        ArrayList<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets preferred songs.
     *
     * @return the preferred songs
     */
    public  ArrayList<String> showPreferedSongsForCommand() {
        ArrayList<Song> sortedSongs = new ArrayList<>(likedSongs);
        ArrayList<String> topSongs = new ArrayList<>();

        for (Song song : sortedSongs) {
            topSongs.add(song.getName());
        }
        return topSongs;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        player.simulatePlayer(time);
    }


    /**
     * Switch connection
     *
     * @return the string
     */
    public String switchConnectionStatus() {
       if (getUserType() == null || Enums.UserType.NORMAL.equals(getUserType())) {
           if (isOnline()) {
               setOnline(false);
           } else {
               setOnline(true);
           }
           return getUsername() + " has changed status successfully.";
       } else {
           return getUsername() + " is not a normal user.";
       }
    }

    /**
     * Add album.
     *
     * @param album the album
     */
    public void addAlbum(final Album album) {
        albums.add(album);
    }

    /**
     * Add podcast
     *
     * @param podcast the podcast
     */
    public void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Add event
     *
     * @param event the event
     */
    public void addEv(final Event event) {
        events.add(event);
    }

    /**
     * Add merch.
     *
     * @param merch the merch
     */
    public void addMer(final Merch merch) {
        merches.add(merch);
    }

    /**
     * Add announcement.
     *
     * @param announcement the announcement
     */
    public void addAn(final Announcement announcement) {
        announcements.add(announcement);
    }


    /**
     * Remove podcast.
     *
     * @param podcastName the podcast name
     * @return the string
     */
    public String removePodcast(final String podcastName) {
        for (Podcast podcast: podcasts) {
            if (podcast.getOwner().equals(getUsername())
                    && podcast.getName().equals(podcastName)) {
                for (User user : Admin.getUsers()) {
                    if (user.isPlaying()) {
                        return getUsername() + " can't delete this podcast.";
                    }
                }
                for (User user : Admin.getUsers()) {
                    user.deleteEpisodes(podcast.getEpisodes());
                }
                podcasts.remove(podcast);
                return getUsername() + " deleted the podcast successfully.";
            }
        }
        return getUsername() + " doesn't have a podcast with the given name.";
    }

    /**
     * Remove the album.
     *
     * @param albumName the album name
     * @return the string
     */
    public String removeAlbum(final String albumName) {

        for (Album album : albums) {
            if (album.getOwner().equals(getUsername()) && album.getName().equals(albumName)) {
                for (User user : Admin.getUsers()) {
                    if (user.isPlaying()) {
                        return getUsername() + " can't delete this album.";
                    }
                }
                for (User user : Admin.getUsers()) {
                    user.deleteSong(album.getSongs());
                }
                albums.remove(album);
                Admin.removeSong(album.getSongs());
                return getUsername() + " deleted the album successfully.";
            }
        }
        return getUsername() + " doesn't have an album with the given name.";
    }

    /**
     * Remove the playlist.
     *
     * @param playlistName the playlist name
     */
    public void removePlaylist(final String playlistName) {
        int poz = 0;
        for (Playlist playlist : followedPlaylists) {
            if (playlist.getName().equals(playlistName)) {
                break;
            }
            poz++;
        }
        if (poz < followedPlaylists.size()) {
            followedPlaylists.remove(poz);
        }
    }

    /**
     * Delete song.
     *
     * @param albumSongs the song from album
     */
    public void deleteSong(final ArrayList<Song> albumSongs) {
        for (Song song : albumSongs) {
            if (likedSongs.contains(song)) {
                likedSongs.remove(song);
            }
        }
    }


    /**
     * Delete episodes.
     *
     * @param episodes the episodes
     */
    public void deleteEpisodes(final List<Episode> episodes) {
        for (Episode episode : episodes) {
            if (podcasts.contains(episode)) {
                podcasts.remove(episode);
            }
        }
    }

    /**
     * Get total followers.
     *
     * @param playlistName the playlist name
     * @return the int
     */
    public int getTotalFollowers(final String playlistName) {
        int total = 0;
        for (User user : Admin.getUsers()) {
            for (Playlist playlist : user.getFollowedPlaylists()) {
                if (playlistName.equals(playlist.getName())) {
                    total++;
                }
            }
        }
        return total;
    }


    /**
     * Is playing.
     *
     * @return the boolean
     */
    public boolean isPlaying() {
        if (player.getPaused()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Print page
     *
     * @return the string
     */
    public String printPage() {

        if (this.page.getCurrentPage() == null) {
            page.setCurrentPage(FactoryCurrentPage
                    .createPage(FactoryCurrentPage.PageType.HomePage));
        }
        if ("HomePage".equals(this.page.getCurrentPage().getPageType())) {
            ArrayList<String> resultsSongs = showPreferredSongs();
            ArrayList<String> resultsPlaylists = getPlaylistFromHome();
            ArrayList<String> empty = new ArrayList<>();
            return this.page.getCurrentPage().currentPage(resultsSongs, resultsPlaylists, empty);
        }
        if ("ArtistPage".equals(this.page.getCurrentPage().getPageType())) {
            ArrayList<String> resultsAlbum = getAlbumsFromLastSelect();
            ArrayList<String> resultsMerch = getMerchesFromLastSelect();
            ArrayList<String> resultsEvent = getEventsFromLastSelect();
            return this.page.getCurrentPage().currentPage(resultsAlbum, resultsMerch, resultsEvent);
        }
        if ("HostPage".equals(this.page.getCurrentPage().getPageType())) {
            ArrayList<String> resultsPodcast = getPodcastsFromLastSelect();
            ArrayList<String> resultsAnnouncement = getAnnouncementFromLastSelect();
            ArrayList<String> empty = new ArrayList<>();
            return this.page.getCurrentPage().
                    currentPage(resultsPodcast, resultsAnnouncement, empty);
        }
        if ("LikedContentPage".equals(this.page.getCurrentPage().getPageType())) {
            ArrayList<String> resultsSongs1 = getSongsFromLiked();
            ArrayList<String> resultsPlaylists1 = getPlaylistsFromLiked();
            ArrayList<String> empty = new ArrayList<>();
            return this.page.getCurrentPage().currentPage(resultsSongs1, resultsPlaylists1, empty);
        }
        return null;
    }

    /**
     * Change page
     *
     * @param commandInput the command input
     * @return the string
     */
    public String changePage(final CommandInput commandInput) {
        TypePage currentPage = this.page.getCurrentPage();

        if (currentPage != null) {
            // set the page if its changed
            if ("Home".equals(commandInput.getNextPage())) {
                currentPage.setPageType("HomePage");
                page.setCurrentPage(FactoryCurrentPage.
                        createPage(FactoryCurrentPage.PageType.HomePage));
                return getUsername() + " accessed "
                        + commandInput.getNextPage() + " successfully.";
            } else if ("LikedContent".equals(commandInput.getNextPage())) {
                currentPage.setPageType("LikedContentPage");
                return getUsername() + " accessed "
                        + commandInput.getNextPage() + " successfully.";
            } else if (!"HostPage".equals(this.page.getCurrentPage().getPageType())
                    || !"ArtistPage".equals(this.page.getCurrentPage().getPageType())) {
                return commandInput.getUsername() + "is trying to access a non-existent page.";
            }
        }
        return null;
    }

    /**
     * Get songs from liked page.
     *
     * @return list the strings
     */
    public ArrayList<String> getSongsFromLiked() {
        ArrayList<String> songsInfoList = new ArrayList<>();

        for (Song song : likedSongs) {
            StringBuilder songsInfo = new StringBuilder(song.getName());
            songsInfo.append(" - ").append(song.getArtist());
            songsInfoList.add(songsInfo.toString());
        }
        return songsInfoList;
    }

    /**
     * Get playlist for home page
     *
     * @return list of strings
     */
    public ArrayList<String> getPlaylistFromHome() {
        ArrayList<String> playlistsongsInfoList = new ArrayList<>();

        if (!playlistsongsInfoList.isEmpty()) {
            playlistsongsInfoList.clear();
        }
        for (Playlist playlist : followedPlaylists) {
            playlistsongsInfoList.add(playlist.getName());
        }
        return playlistsongsInfoList;

    }

    /**
     * Get Playlist from liked page.
     *
     * @return list
     */
    public ArrayList<String> getPlaylistsFromLiked() {
        ArrayList<String> playlistsongsInfoList = new ArrayList<>();

        for (Playlist playlist : followedPlaylists) {
            StringBuilder playlistInfo = new StringBuilder(playlist.getName());
            playlistInfo.append(" - ").append(playlist.getOwner());
            playlistsongsInfoList.add(playlistInfo.toString());
        }
        return playlistsongsInfoList;
    }

    /**
     * Get albums from the last search.
     *
     * @return list
     */
    public ArrayList<String> getAlbumsFromLastSelect() {
        ArrayList<String> albumNames = new ArrayList<>();

        LibraryEntry lastSelected = searchBar.getLastSelected();

        if (lastSelected != null) {
            if ("artist".equals(searchBar.getLastSearchType())) {
                User artist = (User) lastSelected;

                for (Album album : artist.getAlbums()) {
                    albumNames.add(album.getName());
                }
            }
        }
        return albumNames;
    }

    /**
     * Get merch from last select.
     *
     * @return list
     */
    public ArrayList<String> getMerchesFromLastSelect() {
        ArrayList<String> merchInfoList = new ArrayList<>();

        LibraryEntry lastSelected = searchBar.getLastSelected();

        if (lastSelected != null) {
            if ("artist".equals(searchBar.getLastSearchType())) {
                User artist = (User) lastSelected;

                for (Merch merch : artist.getMerches()) {
                    StringBuilder merchInfo = new StringBuilder(merch.getName());
                    merchInfo.append(" - ").append(merch.getPrice()).
                            append(":\n\t").append(merch.getDescription());
                    merchInfoList.add(merchInfo.toString());
                }
            }
        }
        return merchInfoList;
    }


    /**
     * Get events from last select
     *
     * @return the list
     */
    public ArrayList<String> getEventsFromLastSelect() {
        ArrayList<String> eventsInfoList = new ArrayList<>();

        LibraryEntry lastSelected = searchBar.getLastSelected();

        if (lastSelected != null) {
            if ("artist".equals(searchBar.getLastSearchType())) {
                User artist = (User) lastSelected;

                for (Event event : artist.getEvents()) {
                    StringBuilder eventInfo = new StringBuilder(event.getName());
                    eventInfo.append(" - ").append(event.getDate()).
                            append(":\n\t").append(event.getDescription());
                    eventsInfoList.add(eventInfo.toString());
                }
            }
        }

        return eventsInfoList;
    }

    /**
     * Get announcement from last select.
     *
     * @return the list.
     */
    public ArrayList<String> getAnnouncementFromLastSelect() {
        ArrayList<String> announcementInfoList = new ArrayList<>();
        Page page = Page.getInstance();
        User host = Admin.getUser(page.getCurrentUser());

        for (Announcement announcement : host.getAnnouncements()) {
            StringBuilder announcementInfo =
                    new StringBuilder(announcement.getName()).append(":\n\t");
            announcementInfo.append(announcement.getDescription()).append("\n");
            announcementInfoList.add(announcementInfo.toString());

        }

        return announcementInfoList;
    }


    /**
     * Get podcast from last select.
     *
     * @return the list
     */
    public ArrayList<String> getPodcastsFromLastSelect() {
        ArrayList<String> podcastInfoList = new ArrayList<>();

        Page page = Page.getInstance();
        User host = Admin.getUser(page.getCurrentUser());
        for (Podcast podcast : host.getPodcasts()) {
            StringBuilder podcastInfo = new StringBuilder(podcast.getName()).append(":\n\t");

            List<String> episodeInfoList = new ArrayList<>();
            for (Episode episode : podcast.getEpisodes()) {
                String episodeInfo = episode.getName() + " - " + episode.getDescription();
                episodeInfoList.add(episodeInfo);
            }

            podcastInfo.append(episodeInfoList);
            podcastInfo.append("\n");
            podcastInfoList.add(podcastInfo.toString());

        }
        return podcastInfoList;
    }


    /**
     * Show users from album.
     *
     * @return the list
     */
    public ArrayList<AlbumOutput> showUsersAlbums() {
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : albums) {
            albumOutputs.add(new AlbumOutput(album));
        }
        return albumOutputs;
    }

    /**
     * Show the users from podcast.
     *
     * @return list
     */
    public ArrayList<PodcastOutput> showUsersPodcasts() {
        ArrayList<PodcastOutput> podcastOutputs = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastOutputs.add(new PodcastOutput(podcast));
        }
        return podcastOutputs;
    }

    /**
     * Check if a user is normal.
     *
     * @param userToDelete the user to delete
     * @return the boolean
     */
    public boolean checkNormal(final User userToDelete) {
        List<User> users = Admin.getUsers();
        List<Playlist> playlists = Admin.getPlaylists();

        for (User user : users) {
            if (user.getPlayer().getCurrentAudioFile() != null) {
                if (user.getPlayer().equals("playlist")) {
                    for (Playlist playlist : playlists) {
                        ArrayList<Song> songsCurrent = playlist.getSongs();
                        for (Song song : songsCurrent) {
                            if (song.getName().equals(user.getPlayer()
                                    .getCurrentAudioFile().getName())
                                    && playlist.getOwner().equals(userToDelete.getUsername())) {
                                    return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Check if a user is artist.
     *
     * @param userToDelete the user to delete
     * @return the list
     */
    public boolean checkArtist(final User userToDelete) {
        List<User> users = Admin.getUsers();

        for (User user : users)  {
            if (user.getPlayer().getCurrentAudioFile() != null) {
                if (user.getPlayer().getType().equals("song")
                        || user.getPlayer().getType().equals("album")) {
                    for (Album album : userToDelete.getAlbums()) {
                        ArrayList<Song> songsCurrent = album.getSongs();
                        for (Song song : songsCurrent) {
                            if (song.getName().equals(user.getPlayer()
                                    .getCurrentAudioFile().getName())
                                    || album.getName().equals(user.getPlayer()
                                    .getCurrentAudioFile().getName())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Check if the playlist is listening.
     *
     * @param userToDelete the user to delete
     * @return the boolean
     */
    public boolean checkPlaylist(final User userToDelete) {
        for (User user : Admin.getUsers()) {
            Player player = user.getPlayer();

            if (player != null && "playlist".equals(player.getType())
                    && player.getCurrentAudioFile() != null) {
                for (Playlist playlist : Admin.getPlaylists()) {
                    if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
                        return false;
                    }

                    for (Song song : userToDelete.getSongs()) {
                        if (playlist.containsSong(song)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Check if a user is host.
     *
     * @param userToDelete the user to delete
     * @return the boolean
     */
    public boolean checkHost(final User userToDelete) {
        List<User> users = Admin.getUsers();

        for (User user : users)  {
            if (user.getPlayer().getCurrentAudioFile() != null) {
                if (user.getPlayer().getType().equals("podcast")) {
                    for (Podcast podcast : userToDelete.getPodcasts()) {
                        List<Episode> episodeCurrent = podcast.getEpisodes();
                        for (Episode episode : episodeCurrent) {
                            if (episode.getName().equals(user.getPlayer()
                                    .getCurrentAudioFile().getName())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Chech the page
     *
     * @param userToDelete user to delete
     * @return the boolean
     */
    public boolean checkPage(final User userToDelete) {
        List<User> users = Admin.getUsers();

        for (User user : users)  {
            if (user.getPage().getCurrentPage() != null) {
                if (user.getPage().getCurrentPage().getPageType().equals("ArtistPage")
                        || user.getPage().getCurrentPage().getPageType().equals("HostPage")) {
                    if (user.getPage().getArtistName() != null) {
                        if (user.getPage().getArtistName().equals(userToDelete.getName())) {
                            return false;
                        }
                    }
                    if (user.getPage().getHostName() != null) {
                        if (user.getPage().getHostName().equals(userToDelete.getName())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Remove announcement.
     *
     * @param announcementName the announcement name
     * @return the string
     */
    public String removeAnnouncement(final String announcementName) {
        Iterator<Announcement> iterator = announcements.iterator();

        while (iterator.hasNext()) {
            Announcement announcement = iterator.next();

            if (announcement.getName()
                    != null && announcement.getName().equals(announcementName)) {
                iterator.remove();
                return getUsername() + " has successfully deleted the announcement.";
            }
        }
        return getUsername() + " has no announcement with the given name.";
    }

    /**
     * Remove event.
     *
     * @param eventName the event name
     * @return the string
     */
    public String removeEvent(final String eventName) {
        Iterator<Event> iterator = events.iterator();

        while (iterator.hasNext()) {
           Event event = iterator.next();

            if (event.getName() != null && event.getName().equals(eventName)) {
                iterator.remove();
                return getUsername() + " deleted the event successfully.";
            }
        }
        return getUsername() + " has no event with the given name.";
    }

    /**
     * Get artist likes.
     *
     * @return the total likes
     */
    public int getArtistLikes() {
        int totalLikes = 0;
        for (Album album : getAlbums()) {
            totalLikes += album.getTotalLikes();
        }
        return totalLikes;
    }
}
