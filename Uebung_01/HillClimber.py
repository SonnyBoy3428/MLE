import numpy
import random


# Total amount of cities
CITY_NUMBER = 100
# Max distance between two cities
CITY_MAX_DISTANCE = 100


# Sets the distances between the cities (random numbers between 1 and 100)
def setDistances(distances):
    for i in range(0,CITY_NUMBER):
        for j in range(0,CITY_NUMBER):
            if i != j:
                randomNumber = random.randint(1,CITY_MAX_DISTANCE)
                distances[i][j] = randomNumber
                distances[j][i] = randomNumber

    return distances


# Randomly swaps two cities
def randomSwap(cityPath):
    randomIndex1 = random.randint(0, CITY_NUMBER-1)
    randomIndex2 = random.randint(0, CITY_NUMBER-1)

    while randomIndex1 == randomIndex2:
        randomIndex2 = random.randint(0, CITY_NUMBER-1)

    temp = cityPath[randomIndex1]
    cityPath[randomIndex1] = cityPath[randomIndex2]
    cityPath[randomIndex2] = temp

    return cityPath


# Calculates the total distance of the round trip
def calculateTotalDistance(cityPath):
    totalDistance = 0
    for i in range(0,CITY_NUMBER):
        totalDistance += distances[cityPath[i]][cityPath[i+1]]

        if((i+1) == (CITY_NUMBER-1)):
            break

    return totalDistance


# Calculates the fitness of a round trip
def getFitness(totalDisteance):
    return totalDisteance *(-1)


# Returns the index of the best round trip
def determineBestRoundTrip(bestDistances):
    minDistances = bestDistances[0]
    index = 0

    for i in range(1,3):
        if bestDistances[i] < minDistances:
            minDistances = bestDistances[i]
            index = i

    return index


# Prints the results of the current round
def printRoundResult(round, roundtrip, distance):
    print('\nRound %d results:' % (round+1))
    print('Roundtrip: %s' % roundtrip)
    print('Distance: %d' % distance)
    print('------------------------------------------------------\n')


cityPath = None
distances = None
saveState = None
threshhold = -1000


# Main-method
if __name__ == '__main__':
    round = 0
    bestRoundtrips = []
    bestDistances = []

    # The best route out of three is picked
    while round < 3:
        cityPath = random.sample(range(0, CITY_NUMBER), CITY_NUMBER)
        distances = numpy.zeros(shape=(CITY_NUMBER, CITY_NUMBER))
        distances = setDistances(distances)

        totalDistance = calculateTotalDistance(cityPath)
        lastFitness = getFitness(totalDistance)

        tries = 0

        print('****** Round %d ******' % (round+1))

        # As long as the threshold is not reached and it wasn't tried 30000 times continue to optimize
        while (lastFitness < threshhold) and (tries <= 30000):
            # Copy current state
            saveState = list(cityPath)

            # Do a random swap
            cityPath = randomSwap(cityPath)

            # Calculate fitness of new route
            totalDistance = calculateTotalDistance(cityPath)
            fitness = getFitness(totalDistance)

            # If fitness improved take it
            if fitness > lastFitness:
                lastFitness = fitness
                tries = 0

                print('Current Distance: %d' % ((-1)*lastFitness))
            # If it didn't then ceep the old route and continue
            else:
                cityPath = list(saveState)
                tries += 1

        # Save the best route of the round as well as the distance
        bestRoundtrips.append(cityPath)
        bestDistances.append(((-1) * lastFitness))

        printRoundResult(round, bestRoundtrips[round], bestDistances[round])

        round += 1

    # Determine the best out of three round trips
    bestIndex = determineBestRoundTrip(bestDistances)

    print('Best route is Route %d:' % (bestIndex+1))
    print(bestRoundtrips[bestIndex])
    print('With the smallest distance: ')
    print(bestDistances[bestIndex])