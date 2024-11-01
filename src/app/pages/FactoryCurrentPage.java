package app.pages;

public final class FactoryCurrentPage {
    public enum PageType {
        HomePage, ArtistPage, HostPage, LikedContentPage
    }

    private FactoryCurrentPage() {
    }

    /**
     *
     * Create page.
     *
     * @param pageType the page type
     * @return page type
     */
    public static TypePage createPage(final PageType pageType) {
        return switch (pageType) {
            case HomePage -> new HomePage();
            case ArtistPage -> new ArtistPage();
            case HostPage -> new HostPage();
            case LikedContentPage -> new LikedContentPage();
        };
    }
}
