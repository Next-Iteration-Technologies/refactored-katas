from movierental.movie import Movie


class NewReleaseMovie(Movie):

    DAILY_RATE = 3.0
    BONUS_POINTS_MIN_DAYS = 1

    def charge(self, days_rented: int) -> float:
        return days_rented * self.DAILY_RATE

    def points(self, days_rented: int) -> int:
        return 2 if days_rented > self.BONUS_POINTS_MIN_DAYS else 1
