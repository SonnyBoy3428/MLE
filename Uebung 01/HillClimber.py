import random
from random import randint

length = 100

route = random.sample(range(0, 100), 100)
distances = [[0 for x in range(length)] for y in range(length)]


def print_route():
    print(''.join(['{:5}'.format(item) for item in route]))


def set_distances():
    for x in range(0,length):
        for y in range (0, length):
            if x != y:
                distance = randint(0, length)

                distances[x][y] = distance
                distances[y][x] = distance


def print_distances():
    print('\n'.join([''.join(['{:5}'.format(item) for item in row]) for row in distances]))


def calculate_route():
    routeSum = int(0)
    for x in range(0, length - 1):
        city = route[x]
        nextCity = route[x + 1]
        routeSum += distances[city][nextCity]

    return routeSum


def swap_cities(index):
    city1 = route[index]
    route[index] = route[index+1]
    route[index+1] = city1


set_distances()
while True:
    routeSum = calculate_route()
    for x in range(0, length-1):
        swap_cities(x)
        newRouteSum = calculate_route()
        if newRouteSum <= routeSum:
            routeSum = newRouteSum
            print(routeSum)
        else:
            swap_cities(x)