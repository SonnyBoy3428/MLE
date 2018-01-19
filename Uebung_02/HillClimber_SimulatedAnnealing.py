import random
import numpy


# 100 cities with a maximum distance of 100
CITY_NUMBER = 100
MAX_DISTANCE = 100


# Set distances
def setDistances(distances):
    for i in range(0, CITY_NUMBER):
        for j in range(0,CITY_NUMBER):
            if(i != j):
                randomNumber = random.randint(1, CITY_NUMBER+1)
                distances[i][j] = randomNumber
                distances[j][i] = randomNumber

    return distances


# Swap two random cities
def randomSwap(route):
    index1 = random.randint(0, CITY_NUMBER-1)
    index2 = random.randint(0, CITY_NUMBER-1)

    while(index1 == index2):
        index2 = random.randint(0, CITY_NUMBER-1)

    temp = route[index1]
    route[index1] = route[index2]
    route[index2] = temp

    return route


# Calculate probability
def calculateProbability(fitness, lastFitness, temperature):
    return numpy.exp((fitness - lastFitness)/temperature)


# Calculates total distance of route
def calculateTotalDistance(route, distances):
    totalDistance = 0

    for i in range(0,CITY_NUMBER):
        totalDistance += distances[route[i]][route[i+1]]

        if (i+1) == (CITY_NUMBER-1):
            break

    return totalDistance


# Calculates the fitness of current round trip
def calculateFitness(totalDistance):
    return totalDistance*(-1)


# Prints the results of the round
def printRoundResults(round, route, lastFitness):
    print('Round %d results:' % (round+1))
    print('Route: %s ' % route)
    print('Distance: %d' % (lastFitness*(-1)))
    print('------------------------------------')


# Determines best route
def determineBestRoute(bestDistances):
    index = 0
    minDistance = bestDistances[index]
    for i in range(1,3):
        if(bestDistances[i] < minDistance):
            minDistance = bestDistances[i]
            index = i

    return index


route = None
distances = None
bestRoutes = None
bestDistances = None


# Main method
if __name__ == '__main__':
    distances = numpy.zeros(shape=(CITY_NUMBER,CITY_NUMBER))

    bestRoutes = []
    bestDistances = []

    round = 0

    # Let's try a best out of three
    while round < 3:
        route = random.sample(range(0, CITY_NUMBER), CITY_NUMBER)
        distances = setDistances(distances)

        # Calculate first fitness
        totalDistance = calculateTotalDistance(route, distances)
        lastFitness = calculateFitness(totalDistance)

        temperature = 10
        epsilon = 0.0001

        print('****** Round %d ******' % (round+1))

        while temperature > epsilon:
            saveState = list(route)

            # Swap two random cities
            route = randomSwap(route)

            # Calculate first fitness
            totalDistance = calculateTotalDistance(route, distances)
            fitness = calculateFitness(totalDistance)


            if fitness > lastFitness:
                lastFitness = fitness
                print('Current distance: %d' % ((-1)*lastFitness))
            elif (random.uniform(0,1)) < calculateProbability(fitness, lastFitness, temperature):
                print('Current probability: %f, Current distance: %d' % (calculateProbability(fitness, lastFitness, temperature), ((-1) * fitness)))
                lastFitness = fitness
            else:
                route = list(saveState)

            temperature -= epsilon

        bestRoutes.append(route)
        bestDistances.append((lastFitness*(-1)))
        printRoundResults(round, route, lastFitness)
        round += 1

    index = determineBestRoute(bestDistances)

    print('Best route is route %d: %s' % ((index+1), bestRoutes[index]))
    print('with the distance %d' % bestDistances[index])