from __future__ import annotations


class Item:
    """Base inventory item: quality drops by 1 per day, twice as fast once
    the sell-by date has passed. Never goes below MIN_QUALITY.

    Subclasses opt into name-based dispatch (see ``__new__``) by setting the
    ``item_name`` class attribute; they are auto-registered via
    ``__init_subclass__`` so this module never has to import them.
    """

    MAX_QUALITY = 50
    MIN_QUALITY = 0

    item_name: str | None = None
    _subclasses_by_name: dict[str, type[Item]] = {}

    def __init_subclass__(cls, **kwargs) -> None:
        super().__init_subclass__(**kwargs)
        if cls.item_name is not None:
            Item._subclasses_by_name[cls.item_name] = cls

    def __new__(cls, name: str, sell_in: int, quality: int) -> Item:
        if cls is Item:
            cls = cls._subclass_for(name)
        return super().__new__(cls)

    def __init__(self, name: str, sell_in: int, quality: int) -> None:
        self.name = name
        self.sell_in = sell_in
        self.quality = quality

    def __repr__(self) -> str:
        return f"{self.name}, {self.sell_in}, {self.quality}"

    @classmethod
    def _subclass_for(cls, name: str) -> type[Item]:
        if name in cls._subclasses_by_name:
            return cls._subclasses_by_name[name]
        return Item

    def update(self) -> None:
        """Apply one day's quality/sell_in change."""
        self._degrade_quality()
        self.sell_in -= 1
        if self.sell_in < 0:
            self._degrade_quality()

    def _degrade_quality(self) -> None:
        if self.quality > self.MIN_QUALITY:
            self.quality -= 1

    def _increase_quality(self) -> None:
        if self.quality < self.MAX_QUALITY:
            self.quality += 1
