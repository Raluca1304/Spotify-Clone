package app;

import app.audio.Collections.*;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.User;
import app.user.userAdditions.Announcement;
import app.user.userAdditions.Event;
import app.user.userAdditions.Merch;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static app.user.userAdditions.Event.isValidDateFormat;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    private CommandRunner() {
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();
        ObjectNode objectNode = objectMapper.createObjectNode();
        ArrayList<String> nullArray = new ArrayList();
        if (user == null) {
            return null;
        }
        ArrayList<String> results = user.search(filters, type);
        String message = "Search returned " + results.size() + " results";

        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (!user.isOnline()) {
            message = user.getUsername() + " is offline.";
            objectNode.put("message", message);
            objectNode.put("results", objectMapper.valueToTree(nullArray));
        } else {
            objectNode.put("message", message);
            objectNode.put("results", objectMapper.valueToTree(results));
        }
        return objectNode;
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }

        String message = user.select(commandInput.getItemNumber());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }

        String message = user.load();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }

        String message = user.repeat();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message;
        if (user.isOnline()) {
            message = user.like();
        } else {
            message = commandInput.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }

        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }

        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }

        String message = user.follow();


        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }

        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        ArrayList<String> songs = user.showPreferedSongsForCommand();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Get top 5 albums
     *
     * @param commandInput the command input
     * @return the top 5 albums
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Get online users.
     *
     * @param commandInput the command input
     * @return the online users
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> users = Admin.getOnlineUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }

    /**
     * Get top 5 artists.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode getTop5Artists(final CommandInput commandInput) {
        List<String> artist = Admin.getTop5Artist();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(artist));

        return objectNode;
    }

    /**
     * Get all users.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> users = Admin.getAllUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }

    /**
     * Switch connection status.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        } else {
            String message = user.switchConnectionStatus();
            objectNode.put("message", message);
        }

        return objectNode;
    }

    /**
     * Add users.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addUsers(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user != null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " is already taken.");
        } else {
            Admin.addUser(commandInput);
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " has been added successfully.");
        }
        return objectNode;
    }

    /**
     * Add album.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();

        User user = Admin.getUser(commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return objectNode;
        }

        Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());

        if (userType == null || userType != Enums.UserType.ARTIST) {
            objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }

        for (Album album : user.getAlbums()) {
            if (album.getName().equals(commandInput.getName())) {
                objectNode.put("message", commandInput.getUsername()
                        + " has another album with the same name.");
                return objectNode;
            }
        }

        Set<String> addedSongs = new HashSet<>();
        for (SongInput songInput : commandInput.getSongs()) {
            if (!addedSongs.add(songInput.getName())) {
                objectNode.put("message", commandInput.getUsername()
                        + " has the same song at least twice in this album.");
                return objectNode;
            }
        }

        List<Song> songs = commandInput.getSongs().stream()
                .map(songInput -> new Song(
                        songInput.getName(),
                        songInput.getDuration(),
                        songInput.getAlbum(),
                        songInput.getTags(),
                        songInput.getLyrics(),
                        songInput.getGenre(),
                        songInput.getReleaseYear(),
                        songInput.getArtist()
                ))
                .collect(Collectors.toList());

        Admin.addSongs(songs);
        user.addAlbum(new Album(commandInput.getName(), commandInput.getUsername(),
                new ArrayList<>(songs), commandInput.getDescription()));

        objectNode.put("message", commandInput.getUsername()
                + " has added new album successfully.");

        return objectNode;
    }


    /**
     *
     * Add podcast.
     *
     * @param commandInput the command input
     * @return object node
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return objectNode;
        }

        if (user.getUserType() == null) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }

        Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());


        if (userType != Enums.UserType.HOST) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }

        for (Podcast podcast : user.getPodcasts()) {
            if (podcast.getName().equals(commandInput.getName())) {
                objectNode.put("message", commandInput.getUsername()
                        + " has another podcast with the same name.");
                return objectNode;
            }
        }

        Set<String> addedEpisodes = new HashSet<>();
        for (EpisodeInput episodeInput : commandInput.getEpisodes()) {
            if (!addedEpisodes.add(episodeInput.getName())) {
                objectNode.put("message", commandInput.getUsername()
                        + " has the same episode in this podcast.");
                return objectNode;
            }
        }

        List<Episode> episodes = commandInput.getEpisodes().stream()
                .map(episodeInput -> new Episode(
                        episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()
                ))
                .collect(Collectors.toList());

        Admin.addPodcats(new Podcast(commandInput.getName(), commandInput.getUsername(), episodes));
        user.addPodcast(new Podcast(commandInput.getName(), commandInput.getUsername(),
                new ArrayList<>(episodes)));

        objectNode.put("message", commandInput.getUsername()
                + " has added new podcast successfully.");
        return objectNode;
    }

    /**
     * Add event.
     *
     * @param commandInput the command input
     * @return object node
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());

        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return objectNode;
        }

        if (user.getUserType() == null) {
            objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }

        Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());

        if (userType == null || userType != Enums.UserType.ARTIST) {
            objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }
        for (Event event : user.getEvents()) {
            if (event.getName().equals(commandInput.getName())) {
                objectNode.put("message", commandInput.getUsername()
                        + " has another event with the same name.");
                return objectNode;
            }
        }
        if (!isValidDateFormat(commandInput.getDate())) {
            objectNode.put("message", "Event for " + commandInput.getUsername()
                    + " does not have a valid date.");
            return objectNode;
        }
        user.addEv(new Event(commandInput.getName(), commandInput.getDate(),
                commandInput.getDescription()));
        objectNode.put("message", commandInput.getUsername()
                + " has added new event successfully.");
        return objectNode;
    }

    /**
     * Add merch.
     *
     * @param commandInput the command input
     * @return object node
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());

        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return objectNode;
        }

        if (user.getUserType() == null) {
            objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }

        Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());

        if (userType == null || userType != Enums.UserType.ARTIST) {
            objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }

        for (Merch merch : user.getMerches()) {
            if (merch.getName().equals(commandInput.getName())) {
                objectNode.put("message", commandInput.getUsername()
                        + " has merchandise with the same name.");
                return objectNode;
            }
        }
        if (commandInput.getPrice() < 0) {
            objectNode.put("message", "Price for merchandise can not be negative.");
            return objectNode;
        }
        user.addMer(new Merch(commandInput.getName(), commandInput.getPrice(),
                commandInput.getDescription()));
        objectNode.put("message", commandInput.getUsername()
                + " has added new merchandise successfully.");
        return objectNode;
    }

    /**
     * Add announcement
     *
     * @param commandInput the command input
     * @return the objdect node
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());

        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return objectNode;
        }

        if (user.getUserType() == null) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }

        Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());

        if (userType == null || userType != Enums.UserType.HOST) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }
        for (Announcement announcement : user.getAnnouncements()) {
            if (announcement.getName().equals(commandInput.getName())) {
                objectNode.put("message", commandInput.getUsername()
                        + " has merchandise with the same name.");
                return objectNode;
            }
        }
        user.addAn(new Announcement(commandInput.getName(),
                commandInput.getDescription()));
        objectNode.put("message", commandInput.getUsername()
                + " has successfully added new announcement.");
        return objectNode;
    }


    /**
     * Print current page
     *
     * @param commandInput the command input
     * @return object node
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (!user.isOnline()) {
            objectNode.put("message", commandInput.getUsername() + " is offline.");
        }

        if (user.getFollowedPlaylists() != null) {
            if (user.isOnline() && user != null) {
                String message = user.printPage();
                objectNode.put("message", message);
            }
        }
        return objectNode;
    }


    /**
     * Change page.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        String message = user.changePage(commandInput);
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show album
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showAlbum(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<AlbumOutput> albums = user.showUsersAlbums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Show podcasts
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PodcastOutput> podcasts = user.showUsersPodcasts();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(podcasts));

        return objectNode;
    }

    /**
     * Delete user.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        String message;
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            message = "The username " + commandInput.getUsername()
                    + " doesn't exist.";
            objectNode.put("message", message);
            return objectNode;
        }
        message = Admin.deleteUser(commandInput);
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Remove podcast.
     *
     * @param commandInput the command input
     * @return object node
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());

        if (userType == null || userType != Enums.UserType.HOST) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return objectNode;
        }

        String message = user.removePodcast(commandInput.getName());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Remove announcement.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());

        if (userType == null || userType != Enums.UserType.HOST) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return objectNode;
        }

        String message = user.removeAnnouncement(commandInput.getName());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Remove album.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return objectNode;
        }
        Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());

        if (userType == null || userType != Enums.UserType.ARTIST) {
            objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }

        String message = user.removeAlbum(commandInput.getName());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Remove event.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        Enums.UserType userType = Enums.UserType.valueOf(user.getUserType());

        if (userType == null || userType != Enums.UserType.ARTIST) {
            objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return objectNode;
        }

        String message = user.removeEvent(commandInput.getName());
        objectNode.put("message", message);
        return objectNode;
    }

}
