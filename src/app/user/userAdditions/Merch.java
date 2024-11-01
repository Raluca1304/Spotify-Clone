package app.user.userAdditions;

public final class Merch {
    private String name;
    private String description;
    private int price;


    public Merch(final String name, final int price, final String description) {
        this.description = description;
        this.price = price;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
