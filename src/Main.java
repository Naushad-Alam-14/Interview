/*
* The snake can move up, down, left or right in a 2-dimensional board of arbitrary size.

Let’s try to implement the base logic of this game.

Rules:

Every time moveSnake() is called, the snake moves up, down, left or right

0,1 -> 0,2
LL - contains(O(n)) (O(1))
Set -> Hashset
1 -> hash() -> index [] index based - 16 0.75

The snake’s initial size is 3 and grows by 1 every 5 moves
wrapping over wall
The game ends when the snake hits itself,

We can use the following as a starting point (pseudo-code):

interface SnakeGame {
    moveSnake(snakeDirection);
    isGameOver();
}
* */

public class Main {
    public static void main(String[] args) {


        SnakeGame snakeGame = new Snake(10, 10);
        while (true){
            snakeGame.moveSnake("R");
            snakeGame.moveSnake("U");
            snakeGame.moveSnake("L");
            snakeGame.moveSnake("D");

        }

    }
}