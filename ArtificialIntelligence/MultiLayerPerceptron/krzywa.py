import matplotlib.pyplot as plt
import pickle
import numpy as np
import os
import matplotlib


matplotlib.use('TkAgg')

def load_model(filename):
    with open(filename, 'rb') as f:
        return pickle.load(f)

def fake_data(m, domain=np.pi, noise_std=0.1):
    np.random.seed(0)
    X = np.random.rand(m, 2) * domain
    y = np.cos(X[:, 0] * X[:, 1]) * np.cos(2 * X[:, 0]) + np.random.randn(m) * noise_std
    return X, y

def loss_during_fit(approx, X_train, y_train, X_test, y_test):
    keys = list(approx.history_weights.keys())
    epochs = []
    losses_train = []
    losses_test = []
    weights = approx.weights_
    weights0 = approx.weights0_
    for k in keys:
        epochs.append(k + 1)
        approx.weights_ = approx.history_weights[k]
        approx.weights0_ = approx.history_weights0[k]
        losses_train.append(np.mean((approx.predict(X_train) - y_train)**2))
        losses_test.append(np.mean((approx.predict(X_test) - y_test)**2))
    approx.weights_ = weights
    approx.weights0_ = weights0
    return epochs, losses_train, losses_test

def plot_comparison(results):
    fig, axes = plt.subplots(2, 2, figsize=(16, 12))
    axes = axes.flatten()
    for i, (name, (epochs, losses_train, losses_test)) in enumerate(results.items()):
        axes[i].plot(epochs, losses_train, label='Train Loss', color='blue')
        axes[i].plot(epochs, losses_test, label='Test Loss', color='red')
        axes[i].set_title(f'Algorithm: {name}')
        axes[i].set_xlabel('Epoch')
        axes[i].set_ylabel('Loss (MSE)')
        axes[i].legend()
        axes[i].grid(True)
    plt.tight_layout()
    plt.show()

if __name__ == '__main__':
    learning_rate = 1e-3
    activation_name = "relu"
    structure = [128, 128, 64, 64, 32, 32]
    algo_names = ["sgd_simple", "sgd_momentum", "rmsprop", "adam"]

    models = {}
    for algo in algo_names:
        model_filename = f"models/model_lr_{learning_rate}_act_{activation_name}_algo_{algo}_struct_{len(structure)}.pkl"
        if os.path.exists(model_filename):
            models[algo] = load_model(model_filename)

    # Generate data
    domain = 1.5 * np.pi
    noise_std = 0.1
    m_train = 1000
    m_test = 10000
    X_train, y_train = fake_data(m_train, domain, noise_std)
    X_test, y_test = fake_data(m_test, domain, noise_std)

    results = loss_during_fit(models, X_train, y_train, X_test, y_test)
    plot_comparison(results)

    