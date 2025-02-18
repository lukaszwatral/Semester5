import numpy as np
import matplotlib.pyplot as plt

class GeneticAlgorithmTSP():

    def __init__(self, n, fitness_func, selection_func="roulette", population_size=1000, T_loops=100, cross_prob=0.9, mutation_prob=0.001, problem="DKP", *args):
        self.n = n
        self.population_size = population_size
        if(problem=="DKP"):
            self.population = np.random.randint(0, 2, (population_size, n))
        elif(problem=="TSP"):
            self.population = np.random.randint(0, n, (population_size, n))
        self.T_loops = T_loops
        self.cross_prob = cross_prob
        self.mutation_prob = mutation_prob
        self.selection_func = selection_func
        self.fitness_func = fitness_func
        self.args = args
        self.fitness_best = []
        self.fitness_mean = []

    def fit(self):
        for t in range(1, self.T_loops):
            fitness_val = np.array([self.fitness_func(i, *self.args) for i in self.population])
            self.fitness_best.append(np.max(fitness_val))
            self.fitness_mean.append(np.mean(fitness_val))
            new_population = []
            if self.selection_func == "roulette":
                parents = self.roulette_selection(fitness_val)
            elif self.selection_func == "rank":
                parents = self.rank_selection(fitness_val)
            for i in range(0, self.population_size, 2):
                if self.crossover_func == "one_point":
                    child1, child2 = self.one_point_crossover(parents[i], parents[i+1])
                elif self.crossover_func == "two_point":
                    child1, child2 = self.two_point_crossover(parents[i], parents[i+1])
                else:
                    child1, child2 = parents[i], parents[i+1]
                new_population.append(self.mutation(child1))
                new_population.append(self.mutation(child2))
            self.population = np.array(new_population)
        best_index = np.argmax(fitness_val)
        return fitness_val[best_index], self.population[best_index]

    def roulette_selection(self, fitness_val):
        prob = fitness_val / np.sum(fitness_val)
        ind = np.random.choice(len(self.population), size=len(self.population), p=prob)
        return self.population[ind]

    def rank_selection(self, fitness_val):
        rank = np.argsort(np.argsort(fitness_val))
        prob = rank / np.sum(rank)
        ind = np.random.choice(len(self.population), size=len(self.population), p=prob)
        return self.population[ind]

    def partially_mapped_crossover(self, x1, x2):
        if np.random.rand() < self.cross_prob:
            points = np.sort(np.random.randint(0, self.n, 2))
            child1 = np.zeros(self.n)
            child2 = np.zeros(self.n)
            child1[points[0]:points[1]] = x1[points[0]:points[1]]
            child2[points[0]:points[1]] = x2[points[0]:points[1]]
            for i in range(self.n):
                if i < points[0] or i >= points[1]:
                    if x2[i] not in child1:
                        child1[i] = x2[i]
                    if x1[i] not in child2:
                        child2[i] = x1[i]
            return child1, child2
        return x1, x2


    def inversion_mutation(self, x):
        if np.random.rand() < self.mutation_prob:
            points = np.sort(np.random.randint(0, self.n, 2))
            x[points[0]:points[1]] = x[points[0]:points[1]][::-1]
        return x

    def plot_curves(self, sol=None):
        plt.plot(self.fitness_best, label="Best fitness")
        plt.plot(self.fitness_mean, label="Mean fitness")
        plt.xlabel("t")
        plt.ylabel("Fitness")
        plt.title("Selection: " + self.selection_func + " - " + str(sol) + "%")
        plt.legend()
        plt.show()
