class Player:
    def __init__(self, name):
        self.name = name
        self.position = 1
        self.coins = 0
        self.in_penalty_box = False

    def __str__(self):
        return self.name