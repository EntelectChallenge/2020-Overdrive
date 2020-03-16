class Player:
    def __init__(self, pid=None, speed=None, state=None, position=None, power_ups=None, boosting=None, boost_counter=None):
        self.id = pid
        self.position = position
        self.speed = speed
        self.state = state
        self.power_ups = power_ups
        self.boosting = boosting
        self.boost_counter = boost_counter
