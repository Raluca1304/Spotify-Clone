# Project Spotify Clone

## Design Patterns

##### 1. Factory Pattern
###### This pattern was used in order to manage the creation of pages the class FactoryCurrentPage. Factory provides the link between current pages and users classes.
##### 2. Singleton Pattern
###### This pattern was used the concept of a global variable, 'page.' Given the necessity for this value to persist globally and enable seamless updates to 'currentUser' and 'currentPage' from various methods, opting for the Singleton pattern was a natural choice.


## Skel Structure
_New packs and new class added_
* src/
  * app/
      * audio/
        * Collection/
          * **Album**: Adding of the album class which is an extension of AudioCollection;
          * **PodcastOutput**: Adding this class to display the output of a podcast;
      * pages/
          * **FactoryCurrentPage**: Create pages and managing them using design pattern (Factory);
          * **TypePage**: An abstract class that serves as a template for different types of pages in an application;
          * **Page** : Implement using design pattern (Singleton);
          * **ArtistPage**
          * **HomePage**
          * **HostPage**
          * **LikedContentPage**
      * user/
          * userAdditions/
            * Announcement
            * Event
            * Merch

## Program flow

The program starts by _adding users_ (or other commands if we already have users in the json files). These can then have a series of _audio files_, specific _pages_ for each user type, actions and _commands_ that can influence the _change of page_ or _current status_.

The administrator also manages the _deletion_ of a user. Once the command is passed to these methods, the Page instance will be _updated_ only in terms of the _current user_ and _page_.

Within the user and command methods each page will also change the current page and user when necessary.  A key element in this process is the page hierarchy that was built at the beginning of the program; it stores pages that contain information about events, favorite songs, favorite albums, etc., but also the actions that can be performed on them.

