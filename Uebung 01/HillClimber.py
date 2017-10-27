from random import randint

length = 100

route = [length]
distances = [[0 for x in range(length)] for y in range(length)]


def set_distances():
    distance = randint(0, 100)