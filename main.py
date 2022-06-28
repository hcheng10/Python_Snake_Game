from turtle import Screen
from snake import Snake
import time

screen = Screen()
screen.setup(width=600, height=600)
screen.bgcolor("black")
screen.title("Python Snake Game")
screen.tracer(0) # Turn turtle animation on/off and set delay for update drawings. 0 means turn it off

snake = Snake()

screen.listen()
screen.onkey(snake.up, "Up")
screen.onkey(snake.down, "Down")
screen.onkey(snake.left, "Left")
screen.onkey(snake.right, "Right")

game_is_on = True
while game_is_on:
    screen.update() # Perform a TurtleScreen update. To be used when tracer is turned off.
    time.sleep(0.1) # 1 sec delay
    snake.move()

screen.exitonclick() # must put end of the project