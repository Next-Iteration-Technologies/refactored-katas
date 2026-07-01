class Item:
    MAX_QUALITY = 50
    MIN_QUALITY = 0

    item_name = None
    _subclasses_by_name = {}

    def __init_subclass__(cls, **kwargs):
        super().__init_subclass__(**kwargs)
        if cls.item_name is not None:
            Item._subclasses_by_name[cls.item_name] = cls

    def __new__(cls, name, sell_in, quality):
        if cls is Item:
            cls = cls._subclass_for(name)
        return super().__new__(cls)

    def __init__(self, name, sell_in, quality):
        self.name = name
        self.sell_in = sell_in
        self.quality = quality

    def __repr__(self):
        return f"{self.name}, {self.sell_in}, {self.quality}"

    @classmethod
    def _subclass_for(cls, name):
        if name in cls._subclasses_by_name:
            return cls._subclasses_by_name[name]
        return Item

    def update(self):
        self._degrade_quality()
        self.sell_in -= 1
        if self.sell_in < 0:
            self._degrade_quality()

    def _degrade_quality(self):
        if self.quality > self.MIN_QUALITY:
            self.quality -= 1

    def _increase_quality(self):
        if self.quality < self.MAX_QUALITY:
            self.quality += 1
