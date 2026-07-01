package gildedrose.itemtypes;

import gildedrose.Item;

public class Conjured extends Item {

    public Conjured(String name, int sellIn, int quality) {
        super(name, sellIn, quality);
    }

    public Conjured(int sellIn, int quality) {
        this("Conjured", sellIn, quality);
    }

    @Override
    public void updateQuality() {
        decreaseQuality();
        decreaseQuality();
        decreaseSellIn();
        applyExpiredPenalty();
    }

    @Override
    protected void applyExpiredPenalty() {
        if (isExpired()) {
            decreaseQuality();
            decreaseQuality();
        }
    }
}
