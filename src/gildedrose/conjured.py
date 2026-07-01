from gildedrose.item import Item


class Conjured(Item):
    """Item that degrades in quality twice as fast as a normal item."""

    item_name = "Conjured Mana Cake"

    def update(self) -> None:
        self._degrade_quality()
        self._degrade_quality()
        self.sell_in -= 1
        if self.sell_in < 0:
            self._degrade_quality()
            self._degrade_quality()
