package client.model;

public class ItemInformation extends ContentInformation {
    private int itemId = -1; // food-0, trash-1

    public ItemInformation(int id, int itemId) {
        super(id);
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}