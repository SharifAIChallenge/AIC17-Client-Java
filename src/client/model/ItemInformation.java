package client.model;

public class ItemInformation extends ContentInformation {
    private int itemId = -1; // food-0, trash-1

    ItemInformation(int id, int itemId) {
        super(id);
        this.itemId = itemId;
    }

    int getItemId() {
        return itemId;
    }

    void setItemId(int itemId) {
        this.itemId = itemId;
    }
}