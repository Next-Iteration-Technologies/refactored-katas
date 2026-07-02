from gildedrose.item import Item


class GildedRose:
    """Runs the nightly quality/sell_in update across a shop's inventory."""

    def __init__(self, items: list[Item]) -> None:
        self.items = items

    def update_quality(self) -> None:
        for item in self.items:
            item.update()
