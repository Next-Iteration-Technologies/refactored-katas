from gildedrose.item import Item


class AgedBrie(Item):
    """Item whose quality increases as its sell-by date approaches."""

    item_name = "Aged Brie"

    def update(self) -> None:
        self._increase_quality()
        self.sell_in -= 1
        if self.sell_in < 0:
            self._increase_quality()
