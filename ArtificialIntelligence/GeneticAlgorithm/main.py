import numpy as np
import matplotlib
import matplotlib.pyplot as plt
from GeneticAlgorithm import GeneticAlgorithm
from GeneticAlgorithmTSP import GeneticAlgorithmTSP
from itertools import permutations
from sympy.utilities.iterables import multiset_permutations

matplotlib.use("TkAgg")
def DKP(n, scale):
    items = np.ceil(scale * np.random.rand(n, 2)).astype('int32')
    C = int(np.ceil(0.5 * 0.5 * n * scale))
    v = items[:, 0]
    c = items[:, 1]
    return C, v, c

def solve_DKP(costs, weights, C):
    n = len(costs)
    v = np.zeros((n+1, C+1))
    for j in range(1, C+1):
        for i in range(1, n+1):
            if (weights[i-1] > j):
                v[i, j] = v[i-1, j]
            else:
                v[i, j] = max(v[i-1, j], v[i-1, j-weights[i-1]] + costs[i-1])
    solution = np.zeros(n, dtype='int32')
    w = C
    for i in range(n, 0, -1):
        if (v[i, w] != v[i-1, w]):
            solution[i-1] = 1
            w -= weights[i-1]
    return v[n, C], solution

def fitness_func(backpack, costs, weights, C, punishment="hard"):
    v = np.dot(backpack, costs)
    w = np.dot(backpack, weights)
    if (w > C):
        if(punishment == "hard"):
            return 0
        elif(punishment == "soft"):
            return v - (w - C)
    return v


def TSP(n):

    matrix = np.random.randint(1, 10, size=(n, n))
    matrix = 2 * np.abs(matrix - matrix.T)
    return matrix



def fitness_func_TSP(route, distance_matrix):
    route = np.array(route, dtype=int)
    n = len(route)
    dist = 0
    for k in range(n):
        dist += distance_matrix[route[k-1], route[k]]
    return dist


def solve_TSP(matrix):
    best_dist = np.inf
    best_route = None
    for p in multiset_permutations(range(len(matrix))):
        dist = fitness_func_TSP(p, matrix)
        if dist < best_dist:
            best_dist = dist
            best_route = p
    return best_dist, best_route




if __name__ == '__main__':
    # n = 100
    # scale = 2000
    # population_size = 1000
    # T_loops = 100
    # cross_prob = 0.9
    # mutation_prob = 1e-3
    # punishment = "hard"
    # selection = "roulette"
    # crossover = "one_point"

    # C, costs, weights = DKP(n, scale)
    # ga = GeneticAlgorithm(n, fitness_func, selection, crossover, population_size, T_loops, cross_prob, mutation_prob, "TSP", costs, weights, C, punishment)
    # best_fitness, best_solution = ga.fit()
    # exact_fitness, exact_solution = solve_DKP(costs, weights, C)
    #
    # print("Best fitness: ", best_fitness)
    # print("Exact fitness: ", exact_fitness)
    # print("Comparation fitness: ", best_fitness / exact_fitness)
    # print("Comparation solution %: ", np.sum(best_solution == exact_solution) / n * 100)
    # ga.plot_curves(f"{best_fitness / exact_fitness * 100:.2f}")

    n = 10
    population_size = 1000
    T_loops = 100
    cross_prob = 0.8
    mutation_prob = 0.1
    punishment = "hard"
    selection = "rank"
    crossover = "partially_mapped"
    problem = "TSP"
    dists = TSP(n)
    best_dist, best_route = solve_TSP(dists)
    print(best_dist, best_route)
    ga = GeneticAlgorithm(n, fitness_func_TSP, selection, crossover, population_size, T_loops, cross_prob, mutation_prob,
                          problem, dists)
    best_fitness, best_solution, best_overall= ga.fit()

    print("Best fitness: ", best_overall)
    # print("Exact fitness: ", exact_fitness)
    # print("Comparation fitness: ", best_fitness / exact_fitness)
    # print("Comparation solution %: ", np.sum(best_solution == exact_solution) / n * 100)
    # ga.plot_curves(f"{best_fitness / exact_fitness * 100:.2f}")
    ga.plot_curves(best_fitness)

