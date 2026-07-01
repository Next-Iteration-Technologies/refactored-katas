from gildedrose.item import Item


class BackstagePasses(Item):
    item_name = "Backstage passes to a TAFKAL80ETC concert"
    MEDIUM_LEAD_DAYS = 10
    SHORT_LEAD_DAYS = 5

    def update(self):
        self._increase_quality()
        if self.sell_in <= self.MEDIUM_LEAD_DAYS:
            self._increase_quality()
        if self.sell_in <= self.SHORT_LEAD_DAYS:
            self._increase_quality()

        self.sell_in -= 1

        if self.sell_in < 0:
            self.quality = 0
