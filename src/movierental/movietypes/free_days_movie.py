from movierental.movie import Movie


class FreeDaysMovie(Movie):

    BASE_CHARGE: float
    FREE_DAYS: int
    EXTRA_CHARGE: float

    def charge(self, days_rented: int) -> float:
        amount = self.BASE_CHARGE
        if self._exceeds_free_days(days_rented):
            amount += self._extra_days(days_rented) * self.EXTRA_CHARGE
        return amount

    def _exceeds_free_days(self, days_rented: int) -> bool:
        return days_rented > self.FREE_DAYS

    def _extra_days(self, days_rented: int) -> int:
        return days_rented - self.FREE_DAYS
