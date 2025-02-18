import matplotlib.pyplot as plt
import numpy as np
import random

def gaussian_kernel(X, sigma, centers):
    data = []
    for x in X:
        dist = np.sum((x - centers) ** 2, axis=1)
        data.append(np.exp(-dist / (2 * sigma ** 2)))
    return np.array(data)

class Perceptron:

    def __init__(self, learning_rate=0.1, max_iter=1000):
        self.max_iter = max_iter
        self.learning_rate = learning_rate
        self.weights = None
        self.k = 0

    def fit(self, X, y):
        n_samples, n_features = X.shape
        self.weights = np.zeros(n_features)
        E = np.nonzero(y*self.decision_function(X) <= 0)[0]
        self.k = 0
        while (len(E) > 0 and self.k < self.max_iter):
            i = np.random.choice(E)
            self.weights += self.learning_rate * y[i] * X[i, :]
            E = np.nonzero(y*self.decision_function(X) <= 0)[0]
            self.k += 1
        # print("Steps: ", self.k)
        return self

    def predict(self, X):
        # X = np.c_[X, np.ones((X.shape[0], 1))]
        return np.where(self.decision_function(X) >= 0.0, 1, -1)

    def decision_function(self, X):
        return np.dot(X, self.weights)

    def plot_decision_boundary(self, X, y):
        fig = plt.figure()
        ax = fig.add_subplot(1, 1, 1)
        plt.scatter(X[:, 0], X[:, 1], marker="o", c=y)

        x0_1 = np.amin(X[:, 0])
        x0_2 = np.amax(X[:, 0])

        x1_1 = (-self.weights[0] * x0_1) / self.weights[1]
        x1_2 = (-self.weights[0] * x0_2) / self.weights[1]

        ax.plot([x0_1, x0_2], [x1_1, x1_2], "k")

        ymin = np.amin(X[:, 1])
        ymax = np.amax(X[:, 1])
        ax.set_ylim([ymin-0.1, ymax+0.1])
        ax.set_aspect('equal')
        plt.show()

    def plot_decision_boundary_nonlinear(self, X, y, centers, sigma):
        xx, yy = np.meshgrid(np.linspace(-1, 1, 200), np.linspace(-1, 1, 200))
        grid = np.c_[xx.ravel(), yy.ravel()]
        grid_transformed = gaussian_kernel(grid, sigma, centers)
        grid_transformed = np.hstack([np.ones((grid_transformed.shape[0], 1)), grid_transformed])
        predictions = np.sign(np.dot(grid_transformed, self.weights)).reshape(xx.shape)

        plt.contourf(xx, yy, predictions, levels=0, cmap="coolwarm", alpha=0.7)
        plt.scatter(X[:, 1], X[:, 2], c=y, cmap="coolwarm", edgecolors="k")
        plt.scatter(centers[:, 0], centers[:, 1], c='black')
        plt.title("Nonlinear Perceptron Decision Boundary")
        plt.xlabel("x1")
        plt.ylabel("x2")
        plt.show()
    def accuracy(self, y_true, y_pred):
        accuracy = np.sum(y_true == y_pred) / len(y_true)
        return accuracy

