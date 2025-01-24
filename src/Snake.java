import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Snake implements SnakeGame{
    private int rows;
    private int cols;
    private LinkedList<Point> snakeBody;
    private Set<Point> snakeSet;

    private int steps;

    private Point currentDirection;

    public Snake(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        Point p1 = new Point(0,3);
        Point p2 = new Point(0,2);
        Point p3 = new Point(0,1);
        Point p4 = new Point(0,0);
        snakeBody = new LinkedList<>();
        snakeSet = new HashSet<>();
        snakeBody.add(p1);
        snakeBody.add(p2);
        snakeBody.add(p3);
        snakeBody.add(p4);
        snakeSet.add(p1);
        snakeSet.add(p2);
        snakeSet.add(p3);
        snakeSet.add(p4);
        currentDirection = new Point(0,1);
    }

    public void moveSnake(String snakeDirection){
        getNextPosition(snakeDirection);
        Point head = snakeBody.getFirst();
        // 0,1 -> U , 10, 5
        Point newHead = new Point ((head.x + currentDirection.x + rows) % rows, (head.x + currentDirection.x + rows) % rows);

        if(snakeSet.contains(newHead) && !snakeBody.getLast().equals(newHead)){
            throw new RuntimeException("Game over " + snakeDirection);
        }

        snakeBody.addFirst(newHead);
        snakeSet.add(newHead);
        steps++;
        if(steps % 5 != 0){
            Point tail = snakeBody.removeLast();
            snakeSet.remove(tail);
        }
    }


    private void getNextPosition(String snakeDirection){
        Point nextDirection;
        switch (snakeDirection){
            case "U":
                nextDirection =  new Point(-1,0);
                break;
            case "D":
                nextDirection = new Point(1, 0);
                break;
            case "R":
                nextDirection = new Point(0,1);
                break;
            case "L":
                nextDirection = new Point(0, -1);
                break;
            default:
                throw new RuntimeException("Invalid direction");
        }

        if(!(currentDirection.x + nextDirection.x == 0 && currentDirection.y + nextDirection.y == 0)){
            currentDirection = nextDirection;
        }


    }



}
