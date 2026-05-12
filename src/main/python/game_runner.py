import random
from game import Game

def main():
    # Inicializamos el juego
    not_a_winner = False
    game = Game()

    # Añadimos a los jugadores
    game.add("Chet")
    game.add("Pat")
    game.add("Sue")

    while True:
        game.roll(random.randint(1, 5))

        if random.randint(0, 9) != 7:
            not_a_winner = game.handle_correct_answer()
        else:
            not_a_winner = game.wrong_answer()

        if not not_a_winner:
            break

if __name__ == "__main__":
    main()