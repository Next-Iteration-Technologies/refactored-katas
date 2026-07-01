from gildedrose.item import Item


class AgedBrie(Item):
    item_name = "Aged Brie"

    def update(self):
        self._increase_quality()
        self.sell_in -= 1
        if self.sell_in < 0:
            self._increase_quality()
