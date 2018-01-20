import random


# Population count, mutation rate and replacement rate
POPULATION_COUNT = 40
MUTATION_RATE = 20
REPLACEMENT_RATE = 0.25


# Population, the fitnesses and the optimum
population = [""]*POPULATION_COUNT
populationFitness = [int]*POPULATION_COUNT
newPopulation = []
optimum = 213
optimumString = '{0:08b}'.format(optimum) # 11010101


# MaxFitness and fitnessThreshold
maxFitness = 0
fitnessThreshold = -1


# Generates a new population
def initializePopulation():
    for i in range(0,POPULATION_COUNT):
        randomNumber = random.randint(0,256)
        population[i] = '{0:08b}'.format(randomNumber)

        # Calculate fitness of individual
        fitness = calculateFitnesses(randomNumber)
        populationFitness[i] = fitness


# Calculates fitnesses of populations
def calculateFitnesses(individual):
    return (-1)*((individual - optimum)**2)


# Selection
def selection():
    individualAmount = (1-REPLACEMENT_RATE)*POPULATION_COUNT

    for i in range(0,int(individualAmount)):
        newIndividual = int(population[selectIndividual()],2)

        newPopulation.append(newIndividual)


# Selects an individual
def selectIndividual():
    randomNumber = random.uniform(0,1)
    sum = 0
    index = random.randint(0,POPULATION_COUNT-1)
    while sum < randomNumber:
        index += 1
        index %= (POPULATION_COUNT-1)
        sum += calculateProbabilities(index)

    return index


# Calculates probability
def calculateProbabilities(index):
    return populationFitness[index]/getTotalFitness()


# Sums up all the fitnesses
def getTotalFitness():
    fitnessSum = 0
    for i in range(0, POPULATION_COUNT):
        fitnessSum += populationFitness[i]

    return fitnessSum


# Crossover
def crossover():
    individualAmount = (REPLACEMENT_RATE * POPULATION_COUNT)/2

    for i in range(0,int(individualAmount)):
        indexIndividual1 = selectIndividual()
        indexIndividual2 = selectIndividual()

        while indexIndividual1 == indexIndividual2:
            if(indexIndividual1 == POPULATION_COUNT-1):
                indexIndividual2 = indexIndividual1 - 1
            else:
                indexIndividual2 = indexIndividual1+1

        individual1 = population[indexIndividual1]
        individual2 = population[indexIndividual2]

        randomCut = random.randint(0,7)

        newIndividual1 = individual1[0:randomCut] + individual2[randomCut:8]
        newIndividual2 = individual2[0:randomCut] + individual1[randomCut:8]

        newPopulation.append(int(newIndividual1,2))
        newPopulation.append(int(newIndividual2,2))


# Mutation
def mutation():
    for i in range(0, MUTATION_RATE):
        index = selectIndividual()
        individual = newPopulation[index]
        individualString = '{0:08b}'.format(individual)

        randomBit = random.randint(0, 7)

        if individualString[randomBit] == '1':
            individualString = individualString[0:randomBit] + '0' + individualString[randomBit+1:len(individualString)]
        else:
            individualString = individualString[0:randomBit] + '1' + individualString[randomBit + 1:len(individualString)]

        newPopulation[index] = int(individualString,2)


# Update
def update():
    for i in range(0,len(newPopulation)):
        worstFitnessIndex = populationFitness.index(min(populationFitness))
        population.remove(population[worstFitnessIndex])
        population.append('{0:08b}'.format(newPopulation[i]))
        populationFitness.remove(populationFitness[worstFitnessIndex])
        fitness = calculateFitnesses(newPopulation[i])
        populationFitness.append(fitness)

    newPopulation.clear()


# Main program
if __name__ == '__main__':
    initializePopulation()

    maxFitness = getTotalFitness()

    print("START FITNESS:", maxFitness, "OPTIMUM:", "{0:08b}".format(optimum))
    print("START POPULATION:", population)
    print()

    while maxFitness < fitnessThreshold:
        selection()
        crossover()
        mutation()
        update()
        maxFitness = getTotalFitness()
        print("CURRENT FITNESS:", maxFitness, " ", population)

    print()
    print("END FITNESS:", maxFitness)
    print("END POPULATION:", population)