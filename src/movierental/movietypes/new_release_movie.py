from movierental.movie import Movie


class NewReleaseMovie(Movie):

    DAILY_CHARGE = 3.0

    def charge(self, days_rented: int) -> float:
        return days_rented * self.DAILY_CHARGE

    @property
    def earns_bonus_points(self) -> bool:
        return True
