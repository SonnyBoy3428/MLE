import random
from random import randint
import numpy


# Number of cities
citycount = int(100)

# A 2D-matrix with all the distances to the cities.
distances = numpy.zeros(shape=(citycount, citycount), dtype=numpy.int)

# The route as a list for the cities. List is distinct
route = random.sample(range(0, citycount), citycount)


# Prints the route
def print_route():
    print(''.join(['{:5}'.format(item) for item in route]))


# Fills the distances matrix with random values
def set_distances():
    for x in range(0, citycount):
        for y in range(0, citycount):
            if x != y:
                distance = randint(0, citycount)

                distances[x][y] = distance
                distances[y][x] = distance


# Prints the distance matric
def print_distances():
    print('\n'.join([''.join(['{:5}'.format(item) for item in row]) for row in distances]))


# Calculates route distance
def calculate_route_distance(route):
    routedistance = 0
    for x in range(0, citycount -1):
        city = route[x]
        nextcity = route[x + 1]
        routedistance += distances[city][nextcity]

    return routedistance


# Swaps two cities in the route
def random_swap(cityindex):
    city2index = cityindex + randint(1, ((citycount-1)-cityindex))

    city1 = route[cityindex]
    route[cityindex] = route[city2index]
    route[city2index] = city1



# Calculates the fitness
def get_fitness(savestate, route):
    fitness = 0
    olddistance = calculate_route_distance(savestate)
    newdistance = calculate_route_distance(route)

    if newdistance < olddistance:
        fitness = 1
    elif newdistance > olddistance:
        fitness = -1

    return fitness


print('\nBeginning route: ')
print_route()

set_distances()
print('\nDistance matrix: ')
print_distances()

threshold = 130
oldfitness = 0
cityindex = 1
savestate = []
if __name__ == '__main__':
    savestate = list(route)
    while oldfitness < threshold:
        random_swap(cityindex)
        cityindex = cityindex + 1

        if cityindex >= citycount-1:
            cityindex = 0

        newfitness = oldfitness + get_fitness(savestate, route)

        if newfitness > oldfitness:
            oldfitness = newfitness
            savestate = list(route)
        else:
            route = list(savestate)

        print(calculate_route_distance(savestate))


print('\nBest route: ')
print_route()
print('\nDistance: ')
print(calculate_route_distance(route))
