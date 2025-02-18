import matplotlib

from PERC import Perceptron
import numpy as np
import matplotlib.pyplot as plt
from sklearn import datasets
from sklearn.model_selection import train_test_split

matplotlib.use('TkAgg')



def accuracy(y_true, y_pred):
    accuracy = np.sum(y_true == y_pred) / len(y_true)
    return accuracy



#liniowo-separowalny zbiór danych na płaszczyźnie (wygenerowanych w kontrolowany sposób)
def generate_linear(num_points=100, gam=0.1):
    data = np.random.rand(num_points, 2) * 2 - 1
    data[data[:, 0] < 0, 0] -= gam / 2
    data[data[:, 0] >= 0, 0] += gam / 2
    y = 2.*(data[:, 0] > 0)-1
    alfa = np.random.rand(1).squeeze() * 2 * np.pi
    tmp = np.array([[np.cos(alfa), -np.sin(alfa)], [np.sin(alfa), np.cos(alfa)]])
    X = np.dot(data, tmp)
    return X, y

lr = [0.1, 0.01, 0.001]
nup = [100, 1000, 10000]
ga = [0.1, 0.01, 0.001]
for l in lr:
    for n in nup:
        for g in ga:
            X, y = generate_linear(num_points=n, gam=g)
            perc = Perceptron(learning_rate=l)
            perc.fit(X, y)
            print("Learning rate: ", l, "Number of points: ", n, "Gamma: ", g, "Steps: ", perc.k)

X, y = generate_linear(num_points=100, gam=0.1)
X = np.c_[X, np.ones((X.shape[0], 1))]
perc = Perceptron(learning_rate=0.1)
perc.fit(X, y)
print("Accuracy linear:" ,perc.accuracy(y, perc.predict(X)))
perc.plot_decision_boundary(X, y)



def generate_nonlinear(num_points=100):
    xi1 = np.random.uniform(0, 2 * np.pi, num_points)
    xi2 = np.random.uniform(-1, 1, num_points)

    X = np.column_stack((np.ones(num_points), xi1, xi2))
    y = np.where(np.abs(np.sin(xi1)) > np.abs(xi2), -1, 1)

    return X, y

def plot_dataset(X, y):
    xi1 = X[:, 1]
    xi2 = X[:, 2]

    # Tworzenie wykresu
    plt.figure(figsize=(8, 6))
    plt.scatter(xi1[y == -1], xi2[y == -1], color='red', label='Klasa -1')
    plt.scatter(xi1[y == 1], xi2[y == 1], color='blue', label='Klasa 1')
    plt.axhline(0, color='black', linestyle='--', linewidth=0.7)
    plt.axvline(0, color='black', linestyle='--', linewidth=0.7)
    plt.xlabel('$x_{i1}$')
    plt.ylabel('$x_{i2}$')
    plt.title('Rozkład punktów w zbiorze danych')
    plt.legend()
    plt.grid()
    plt.show()

def normalize_data(data):
    data[:, 1] = (data[:, 1] - np.min(data[:, 1])) / (np.max(data[:, 1]) - np.min(data[:, 1])) * 2 - 1
    data[:, 2] = (data[:, 2] - np.min(data[:, 2])) / (np.max(data[:, 2]) - np.min(data[:, 2])) * 2 - 1
    return data
def gaussian_kernel(X, sigma, centers):
    data = []
    for x in X:
        dist = np.sum((x - centers) ** 2, axis=1)
        data.append(np.exp(-dist / (2 * sigma ** 2)))
    return np.array(data)


# plot_dataset(X, y)
n_centers=100
sigma=0.3
max_iter=2000
learning_rate = 0.01
X, y = generate_nonlinear(num_points=1000)
X_norm = normalize_data(X)
centers = np.random.uniform(-1, 1, (n_centers, X_norm.shape[1] - 1))
X_transformed = gaussian_kernel(X_norm[:, 1:], sigma, centers)
X_transformed = np.hstack([np.ones((X_transformed.shape[0], 1)), X_transformed])

pe = Perceptron(learning_rate=learning_rate, max_iter=max_iter)
pe.fit(X_transformed, y)
print("Accuracy nonlinear: ", pe.accuracy(y, pe.predict(X_transformed)))
pe.plot_decision_boundary_nonlinear(X, y, centers, sigma)

