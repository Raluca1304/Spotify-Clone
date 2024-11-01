package app.pages;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public final class Page {
    private static Page instance = null;
    private  ArrayList<HomePage> homePages = new ArrayList<>();
    @Getter @Setter
    private  ArrayList<ArtistPage> artistPages = new ArrayList<>();
    @Getter @Setter
    private  ArrayList<HostPage> hostPages = new ArrayList<>();
    private TypePage currentPage;
    @Getter
    private String currentUser;
    @Getter @Setter
    private String artistName;
    @Getter @Setter
    private String hostName;

    public Page() {
        HomePage userHomePage = new HomePage();
        homePages.add(userHomePage);

        ArtistPage artistPage = new ArtistPage();
        artistPages.add(artistPage);

        HostPage hostPage = new HostPage();
        hostPages.add(hostPage);
    }

    /**
     * @return instance
     */
    public static Page getInstance() {
        if (instance == null) {
            instance = new Page();
        }
        return instance;
    }
    public void setCurrentPage(final TypePage currentPage) {
        this.currentPage = currentPage;
    }

    public void setCurrentUser(final String currUser) {
        this.currentUser = currUser;
    }

    public TypePage getCurrentPage() {

        return currentPage;
    }

    /**
     * Add home page.
     *
     * @param homePage the home page
     */
    public void addHomePage(final HomePage homePage) {
        homePages.add(homePage);
    }

    /**
     * Add artist page.
     *
     * @param artistPage the artist page
     */
    public void addArtistPage(final ArtistPage artistPage) {
        artistPages.add(artistPage);
    }

    /**
     * Add host page.
     *
     * @param hostPage the host page
     */
    public void addHostPage(final HostPage hostPage) {
        hostPages.add(hostPage);
    }

}
