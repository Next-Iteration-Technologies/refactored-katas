from movierental.movie import Movie


class RegularMovie(Movie):

    BASE_CHARGE = 2.0
    FREE_DAYS = 2
    EXTRA_CHARGE = 1.5

    def charge(self, days_rented: int) -> float:
        amount = self.BASE_CHARGE
        if days_rented > self.FREE_DAYS:
            amount += (days_rented - self.FREE_DAYS) * self.EXTRA_CHARGE
        return amount

    def points(self, days_rented: int) -> int:
        return 1
