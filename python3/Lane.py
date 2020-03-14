class Lane:
    def __init__(self, position, objectType, player_id):
        self.position = position
        self.object = objectType
        self.occupied_by_player_id = player_id
