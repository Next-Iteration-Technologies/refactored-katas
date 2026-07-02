from abc import ABC, abstractmethod


class Movie(ABC):

    def __init__(self, title: str):
        self._title = title

    @property
    def title(self) -> str:
        return self._title

    @abstractmethod
    def charge(self, days_rented: int) -> float:
        pass

    @property
    def earns_bonus_points(self) -> bool:
        return False
