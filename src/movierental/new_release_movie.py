from movierental.movie import Movie


class NewReleaseMovie(Movie):

    DAILY_RATE = 3.0
    BONUS_POINTS_MIN_DAYS = 1
    STANDARD_POINTS = 1
    BONUS_POINTS = 2

    def charge(self, days_rented: int) -> float:
        return days_rented * self.DAILY_RATE

    def points(self, days_rented: int) -> int:
        if self._qualifies_for_bonus_points(days_rented):
            return self.BONUS_POINTS
        return self.STANDARD_POINTS

    def _qualifies_for_bonus_points(self, days_rented: int) -> bool:
        return days_rented > self.BONUS_POINTS_MIN_DAYS
