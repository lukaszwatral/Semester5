import numpy as np
import matplotlib.pyplot as plt

class GeneticAlgorithm():

    def __init__(self, n, fitness_func, selection_func="roulette", crossover_func="one_point", population_size=1000,
                 T_loops=100, cross_prob=0.9, mutation_prob=0.001, problem="DKP", *args):
        self.n = n
        self.population_size = population_size
        if (problem == "DKP"):
            self.population = np.random.randint(0, 2, (population_size, n))
        elif (problem == "TSP"):
            self.population = np.array([np.random.permutation(n) for _ in range(population_size)])
        self.T_loops = T_loops
        self.cross_prob = cross_prob
        self.mutation_prob = mutation_prob
        self.selection_func = selection_func
        self.crossover_func = crossover_func
        self.fitness_func = fitness_func
        self.problem = problem
        self.args = args
        self.fitness_best = []
        self.fitness_mean = []
        self.fitness_best_global = []
        self.best_overall_list = []  # Add this line

    def fit(self):
        best_overall = np.inf
        for t in range(1, self.T_loops):
            fitness_val = np.array([self.fitness_func(i, *self.args) for i in self.population])
            self.fitness_best.append(np.min(fitness_val))
            self.fitness_mean.append(np.mean(fitness_val))
            curr_best = np.argmin(fitness_val)
            self.fitness_best_global.append(fitness_val[curr_best])
            if fitness_val[curr_best] < best_overall:
                best_overall = fitness_val[curr_best]
            self.best_overall_list.append(best_overall)  # Add this line
            new_population = []
            if self.selection_func == "roulette":
                parents = self.roulette_selection(fitness_val)
            elif self.selection_func == "rank":
                parents = self.rank_selection(fitness_val)
            for i in range(0, self.population_size, 2):
                if self.crossover_func == "one_point":
                    child1, child2 = self.one_point_crossover(parents[i], parents[i + 1])
                elif self.crossover_func == "two_point":
                    child1, child2 = self.two_point_crossover(parents[i], parents[i + 1])
                elif self.crossover_func == "partially_mapped":
                    child1, child2 = self.partially_mapped_crossover(parents[i], parents[i + 1])
                else:
                    child1, child2 = parents[i], parents[i + 1]
                if self.problem == "DKP":
                    new_population.append(self.mutation(child1))
                    new_population.append(self.mutation(child2))
                elif self.problem == "TSP":
                    new_population.append(self.inversion_mutation(child1))
                    new_population.append(self.inversion_mutation(child2))
            self.population = np.array(new_population)
        if self.problem == "DKP":
            best_index = np.argmax(fitness_val)
        elif self.problem == "TSP":
            best_index = np.argmin(fitness_val)
        return fitness_val[best_index], self.population[best_index], best_overall

    def roulette_selection(self, fitness_val):
        prob = fitness_val / np.sum(fitness_val)
        ind = np.random.choice(len(self.population), size=len(self.population), p=prob)
        return self.population[ind]

    def rank_selection(self, fitness_val):
        rank = np.argsort(np.argsort(fitness_val))
        prob = rank / np.sum(rank)
        ind = np.random.choice(len(self.population), size=len(self.population), p=prob)
        return self.population[ind]

    def one_point_crossover(self, x1, x2):
        if np.random.rand() < self.cross_prob:
            point = np.random.randint(0, self.n)
            child1 = np.concatenate((x1[:point], x2[point:]))
            child2 = np.concatenate((x2[:point], x1[point:]))
            return child1, child2
        return x1, x2

    def two_point_crossover(self, x1, x2):
        if np.random.rand() < self.cross_prob:
            points = np.sort(np.random.randint(0, self.n, 2))
            child1 = np.concatenate((x1[:points[0]], x2[points[0]:points[1]], x1[points[1]:]))
            child2 = np.concatenate((x2[:points[0]], x1[points[0]:points[1]], x2[points[1]:]))
            return child1, child2
        return x1, x2

    def partially_mapped_crossover(self, x1, x2):
        if np.random.rand() < self.cross_prob:
            points = np.sort(np.random.randint(0, self.n, 2))
            child1 = np.full(self.n, -1)
            child2 = np.full(self.n, -1)
            child1[points[0]:points[1]] = x1[points[0]:points[1]]
            child2[points[0]:points[1]] = x2[points[0]:points[1]]

            for i in range(self.n):
                if i < points[0] or i >= points[1]:
                    if x2[i] not in child1:
                        child1[i] = x2[i]
                    else:
                        for j in range(self.n):
                            if j not in child1:
                                child1[i] = j
                                break
                    if x1[i] not in child2:
                        child2[i] = x1[i]
                    else:
                        for j in range(self.n):
                            if j not in child2:
                                child2[i] = j
                                break
            if (len(set(child1)) < self.n):
                print(child1, x1)
                assert(len(set(child1))==len(child1))

            if (len(set(child2)) < self.n):
                print(child2, x2)
                assert (len(set(child2)) == len(child2))
            return child1, child2
        return x1, x2

    def mutation(self, x):
        if np.random.rand() < self.mutation_prob:
            point = np.random.randint(0, self.n)
            x[point] = 1 - x[point]
        return x

    def inversion_mutation(self, x):
        if np.random.rand() < self.mutation_prob:
            points = np.sort(np.random.randint(0, self.n, 2))
            x[points[0]:points[1]] = x[points[0]:points[1]][::-1]
        return x

    def plot_curves(self, sol=None):
        plt.plot(self.fitness_best, label="Best fitness")
        plt.plot(self.fitness_mean, label="Mean fitness")
        plt.plot(self.best_overall_list, label="Best overall fitness")  # Add this line
        plt.xlabel("t")
        plt.ylabel("Fitness")
        plt.legend()
        plt.show()
