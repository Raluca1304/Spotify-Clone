package app.pages;

import java.util.ArrayList;

public abstract class TypePage {
    private String pageType = null;

    /**
     *
     * @param result the result
     * @param result2 the result2
     * @param result3 the results=3
     * @return current page
     */
    public abstract String currentPage(ArrayList<String> result,
                                       ArrayList<String> result2,
                                       ArrayList<String> result3);
    public String getPageType() {
        return pageType;
    }

    public void setPageType(final String pageType) {
        this.pageType = pageType;
    }
}
