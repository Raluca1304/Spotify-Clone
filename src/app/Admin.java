package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.pages.Page;
import app.pages.HostPage;
import app.pages.TypePage;
import app.pages.HomePage;
import app.pages.ArtistPage;
import app.user.User;
import app.utils.Enums;
import fileio.input.CommandInput;
import fileio.input.PodcastInput;
import fileio.input.UserInput;
import fileio.input.SongInput;
import fileio.input.EpisodeInput;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import static app.utils.Enums.UserType.HOST;
import static app.utils.Enums.UserType.ARTIST;
import static app.utils.Enums.UserType.NORMAL;


/**
 * The type Admin.
 */


public final class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;

    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();

        for (UserInput userInput : userInputList) {
            User user = new User(userInput.getUsername(), userInput.getAge(), userInput.getCity());
            users.add(user);
            Page page = Page.getInstance();
            if ("artist".equals(user.getUserType())) {
                ArtistPage artistPage = new ArtistPage();
                page.addArtistPage(artistPage);
                page.setCurrentPage(artistPage);
            } else if ("host".equals(user.getUserType())) {
                HostPage hostPage = new HostPage();
                page.addHostPage(hostPage);
                page.setCurrentPage(hostPage);
            } else {
                HomePage userHomePage = new HomePage();
                page.addHomePage(userHomePage);
                page.setCurrentPage(userHomePage);
            }
        }
    }


    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Remove one song.
     *
     * @param allSongs the all songs
     */
    public static void removeSong(final List<Song> allSongs) {
       for (Song song : allSongs) {
           songs.remove(song);
       }
    }

    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Add podcast.
     *
     * @param podcast the podcast
     */
    public static void addPodcats(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Add song.
     *
     * @param songList the song list
     */
    public static void addSongs(final List<Song> songList) {
        for (Song song : songList) {
            songs.add(song);
        }
    }


    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (username != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            if (user.isOnline()) {
                user.simulateTime(elapsed);
            }
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
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
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }

    /**
     * Gets online users.
     *
     * @return online users
     */
    public static List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isOnline() && user.getUserType() == null) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    /**
     * Add users.
     *
     * @param commandInput the commandInput
     */
    public static void addUser(final CommandInput commandInput) {
        User existingUser = getUser(commandInput.getUsername());

        if (existingUser == null) {
            User newUser = new User(commandInput.getUsername(), commandInput.getAge(),
                    commandInput.getCity());
            users.add(newUser);

            switch (commandInput.getType()) {
                case "user":
                    newUser.setUserType(String.valueOf(NORMAL));
                    Page newpage = new Page();
                    newpage.setCurrentUser(newUser.getUsername());
                    TypePage homepage = new HomePage();
                    newpage.setCurrentPage(homepage);
                    newUser.setPage(newpage);
                    break;
                case "artist":
                    newUser.setUserType(String.valueOf(ARTIST));
                    Page newpageartist = new Page();
                    newpageartist.setCurrentUser(newUser.getUsername());
                    TypePage aristPage = new ArtistPage();
                    newpageartist.setCurrentPage(aristPage);
                    newUser.setPage(newpageartist);
                    break;
                case "host":
                    newUser.setUserType(String.valueOf(Enums.UserType.HOST));
                    Page newpagehost = new Page();
                    newpagehost.setCurrentUser(newUser.getUsername());
                    TypePage hostpage = new HostPage();
                    newpagehost.setCurrentPage(hostpage);
                    newUser.setPage(newpagehost);
                    break;
                default:
                    users.remove(newUser);
            }
        }
    }

    /**
     * Get all artists.
     *
     * @return artists
     */
    public static List<User> getArtists() {
        List<User> artists = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType() != null) {
                Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());
                if (userType == ARTIST) {
                    artists.add(user);
                }
            }
        }
        return artists;
    }

    /**
     * Get all hosts.
     *
     * @return hosts
     */
    public static List<User> getHosts() {
        List<User> hosts = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType() != null) {
                Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());
                if (userType == HOST) {
                    hosts.add(user);
                }
            }
        }
        return hosts;
    }

    /**
     * Get all albums.
     *
     * @return albums
     */
    public static List<Album> getAlbums() {
        List<Album> allAlbums = new ArrayList<>();
        for (User user : users) {
            allAlbums.addAll(user.getAlbums());
        }
        return allAlbums;
    }

    /**
     * Get users.
     *
     * @return users
     */
    public static List<User> getUsers() {
        List<User> allUsers = new ArrayList<>();
        for (User user : users) {
            allUsers.add(user);
        }
        return allUsers;
    }

    /**
     * Get top 5 albums.
     *
     * @return top 5 albums
     */
    public static List<String> getTop5Albums() {
        List<Album> allAlbums = new ArrayList<>();
        for (Album album : getAlbums()) {
            int totalLikesInAlbum = album.getTotalLikes();
            album.setTotalLikes(totalLikesInAlbum);
            allAlbums.add(album);
        }
        allAlbums.sort(
                Comparator.comparingInt(Album::getTotalLikes).reversed()
                        .thenComparing(Album::getName)
        );
        // limit just first 5 albums from allAlbums
        List<Album> top5Albums = allAlbums.subList(0, Math.min(LIMIT, allAlbums.size()));

        List<String> top5AlbumNames = new ArrayList<>();
        for (Album album : top5Albums) {
            top5AlbumNames.add(album.getName());
        }

        return top5AlbumNames;
    }

    /**
     * Get top 5 artists.
     *
     * @return top 5 artists
     */
    public static List<String> getTop5Artist() {
        List<User> artists = new ArrayList<>();
        for (User user : getArtists()) {
            for (Album album : user.getAlbums()) {
                int totalLikesInAlbum = album.getTotalLikes();
                user.setArtistsLikes(user.getArtistLikes() + totalLikesInAlbum);
            }
            artists.add(user);
        }
        artists.sort(Comparator.comparingInt(User::getArtistLikes).reversed());
        List<User> top5Artists = artists.subList(0, Math.min(LIMIT, artists.size()));
        List<String> top5ArtistsName = new ArrayList<>();
        for (User user : top5Artists) {
            top5ArtistsName.add(user.getName());
        }
        return top5ArtistsName;
    }

    /**
     * Get all users.
     *
     * @return all users
     */
    public static List<String> getAllUsers() {
        List<String> normalUsers = new ArrayList<>();
        List<String> artists = new ArrayList<>();
        List<String> hosts = new ArrayList<>();

        for (User user : users) {
            String userString = user.getUsername();
            if (user.getUserType() == null
                    || Enums.UserType.NORMAL.equals(Enums.UserType.valueOf(user.getUserType()))) {
                normalUsers.add(userString);
            } else if (Enums.UserType.ARTIST.equals(Enums.UserType.valueOf(user.getUserType()))) {
                artists.add(userString);
            } else if (Enums.UserType.HOST.equals(Enums.UserType.valueOf(user.getUserType()))) {
                hosts.add(userString);
            }
        }

        List<String> result = new ArrayList<>();
        result.addAll(normalUsers);
        result.addAll(artists);
        result.addAll(hosts);

        return result;
    }

    /**
     * Delete user.
     *
     * @param commandInput the commandInput
     * @return the message for the user
     */
    public static String deleteUser(final CommandInput commandInput) {

        User user = getUser(commandInput.getUsername());

        if ((user.getUserType() == null
                || Enums.UserType.NORMAL.equals(Enums.UserType.valueOf(user.getUserType())))
                && user.checkNormal(user) &&  user.checkPlaylist(user)) {
            for (User userFromAdmin : users) {
                for (Playlist playlist : getPlaylists()) {
                    if (playlist.getOwner().equals(user.getUsername())) {
                        if (playlist.getFollowers() > 0) {
                            playlist.decreaseFollowers();
                        }
                        userFromAdmin.removePlaylist(playlist.getName());
                    }
                }
            }
            users.remove(user);
            return commandInput.getUsername() + " was successfully deleted.";
        } else if ((Enums.UserType.ARTIST.equals(Enums.UserType.valueOf(user.getUserType())))
                && user.checkArtist(user)
                && user.checkPage(user) && user.checkPlaylist(user)) {
            for (Album album : getAlbums()) {
                user.removeAlbum(album.getName());
            }
            users.remove(user);
            return commandInput.getUsername() + " was successfully deleted.";
        } else if ((Enums.UserType.HOST.equals(Enums.UserType.valueOf(user.getUserType())))
                && user.checkHost(user)
                && user.checkPage(user)) {
            user.checkPage(user);
            for (Podcast podcast : getPodcasts()) {
                user.removePodcast(podcast.getName());
            }
            users.remove(user);
            return commandInput.getUsername() + " was successfully deleted.";
        }
            return commandInput.getUsername() + " can't be deleted.";
    }


}
