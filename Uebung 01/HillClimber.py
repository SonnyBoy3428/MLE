import random
from random import randint

# Number of cities
length = 100

# The route as a list of the cities. The list is distinct.
route = random.sample(range(0, 100), 100)

# A 2D-matrix with all the distances to the cities.
distances = [[0 for x in range(length)] for y in range(length)]


# Prints the route
def print_route():
    print(''.join(['{:5}'.format(item) for item in route]))


# Fills the distances matrix with random values
def set_distances():
    for x in range(0, length):
        for y in range(0, length):
            if x != y:
                distance = randint(0, length)

                distances[x][y] = distance
                distances[y][x] = distance


# Prints the distance matric
def print_distances():
    print('\n'.join([''.join(['{:5}'.format(item) for item in row]) for row in distances]))


# Calculates route distance
def calculate_route_distance():
    routedistance = int(0)
    for x in range(0, length - 1):
        city = route[x]
        nextcity = route[x + 1]
        routedistance += distances[city][nextcity]

    return routedistance


# Swaps two cities in the route
def swap_cities(index):
    city1 = route[index]
    route[index] = route[index+1]
    route[index+1] = city1


print('Starting route: ')
print_route()

set_distances()
print('\nDistance matrix: ')
print_distances()

oldRouteDistance = calculate_route_distance()
threshold = 1000
counter = 0
exitLoop = False

# Swap cities until oprimal route is found
while not exitLoop:
    for x in range(0, length-1):
        swap_cities(x)
        newRouteDistance = calculate_route_distance()

        # If new route is better then take this route. If not undo swap
        if newRouteDistance < oldRouteDistance:
            counter = 0
            oldRouteDistance = newRouteDistance
            print(oldRouteDistance)
        else:
            swap_cities(x)
            print(oldRouteDistance)
            counter = counter + 1

            # End it if threshold is reached and now better route can be found
            if counter >= threshold:
                exitLoop = True
                break


print('\nBest route: ')
print_route()
print('\nDistance: ')
print(calculate_route_distance())
