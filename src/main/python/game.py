from player import Player

class Game:
    INITIAL_QUESTIONS_COUNT = 50
    WINNING_COINS = 6
    BOARD_SIZE = 12

    def __init__(self):
        self.players = []
        self.current_player_idx = 0
        self.is_getting_out_of_penalty_box = False
        
        self.pop_questions = [f"Pop Question {i}" for i in range(self.INITIAL_QUESTIONS_COUNT)]
        self.science_questions = [f"Science Question {i}" for i in range(self.INITIAL_QUESTIONS_COUNT)]
        self.sports_questions = [f"Sports Question {i}" for i in range(self.INITIAL_QUESTIONS_COUNT)]
        self.rock_questions = [f"Rock Question {i}" for i in range(self.INITIAL_QUESTIONS_COUNT)]

    def has_enough_players(self):
        return len(self.players) >= 2

    def add(self, player_name):
        new_player = Player(player_name)
        self.players.append(new_player)
        print(f"{player_name} was added")
        print(f"They are player number {len(self.players)}")
        return True

    def roll(self, roll):
        player = self.players[self.current_player_idx]
        print(f"{player} is the current player")
        print(f"They have rolled a {roll}")

        if player.in_penalty_box:
            if roll % 2 != 0:
                self.is_getting_out_of_penalty_box = True
                print(f"{player} is getting out of the penalty box")
                self._move_player(roll)
                print(f"{player}'s new location is {player.position}")
                print(f"The category is {self._current_category(player.position)}")
                self._ask_question(self._current_category(player.position))
            else:
                print(f"{player} is not getting out of the penalty box")
                self.is_getting_out_of_penalty_box = False
        else:
            self._move_player(roll)
            print(f"{player}'s new location is {player.position}")
            print(f"The category is {self._current_category(player.position)}")
            self._ask_question(self._current_category(player.position))

    def _move_player(self, roll):
        player = self.players[self.current_player_idx]
        new_position = player.position + roll
        if new_position > self.BOARD_SIZE:
            new_position -= self.BOARD_SIZE
        player.position = new_position

    def _ask_question(self, category):
        if category == "Pop": print(self.pop_questions.pop(0))
        if category == "Science": print(self.science_questions.pop(0))
        if category == "Sports": print(self.sports_questions.pop(0))
        if category == "Rock": print(self.rock_questions.pop(0))

    def _current_category(self, space):
        category_index = (space - 1) % 4
        return ["Pop", "Science", "Sports", "Rock"][category_index]

    def handle_correct_answer(self):
        player = self.players[self.current_player_idx]
        
        if player.in_penalty_box:
            if self.is_getting_out_of_penalty_box:
                print("Answer was correct!!!!")
                player.coins += 1
                print(f"{player} now has {player.coins} Gold Coins.")
                winner = not self._player_has_won()
                self._next_turn()
                return winner
            else:
                self._next_turn()
                return True
        else:
            print("Answer was correct!!!!")
            player.coins += 1
            print(f"{player} now has {player.coins} Gold Coins.")
            winner = not self._player_has_won()
            self._next_turn()
            return winner

    def wrong_answer(self):
        player = self.players[self.current_player_idx]
        print("Question was incorrectly answered")
        print(f"{player} was sent to the penalty box")
        player.in_penalty_box = True
        self._next_turn()
        return True

    def _player_has_won(self):
        return self.players[self.current_player_idx].coins == self.WINNING_COINS

    def _next_turn(self):
        self.current_player_idx += 1
        if self.current_player_idx == len(self.players):
            self.current_player_idx = 0