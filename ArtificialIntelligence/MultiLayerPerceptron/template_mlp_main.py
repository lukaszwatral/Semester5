import matplotlib
import numpy as np
import matplotlib.pyplot as plt
from matplotlib import cm
from template_mlp import MLPApproximator
import pickle
import sys
import os

matplotlib.use('TkAgg')

def save_model(approx, filename):
    with open(filename, 'wb') as f:
        pickle.dump(approx, f)

def save_plot(fig, filename):
    fig.savefig(filename)

class Logger(object):
    def __init__(self, filename):
        self.terminal = sys.stdout
        self.log = open(filename, 'a')

    def write(self, message):
        self.terminal.write(message)
        self.log.write(message)

    def flush(self):
        pass

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

def r2_during_fit(approx, X_train, y_train, X_test, y_test):
    keys = list(approx.history_weights.keys())
    epochs = []
    r2s_train = []
    r2s_test = []
    weights = approx.weights_
    weights0 = approx.weights0_
    for k in keys:
        epochs.append(k + 1)
        approx.weights_ = approx.history_weights[k]
        approx.weights0_ = approx.history_weights0[k]
        r2s_train.append(approx.score(X_train, y_train))
        r2s_test.append(approx.score(X_test, y_test))
    approx.weights_ = weights
    approx.weights0_ = weights0
    return epochs, r2s_train, r2s_test

def run_experiment(learning_rate, activation_name, algo_name, structure):
    # Set up logging
    log_filename = f"logs/lr_{learning_rate}_act_{activation_name}_algo_{algo_name}_struct_{len(structure)}.txt"
    sys.stdout = Logger(log_filename)

    # Generate data
    domain = 1.5 * np.pi
    noise_std = 0.1
    m_train = 1000
    m_test = 10000
    X_train, y_train = fake_data(m_train, domain, noise_std)
    X_test, y_test = fake_data(m_test, domain, noise_std)

    # Initialize and train the model
    approx = MLPApproximator(structure=structure, activation_name=activation_name, targets_activation_name="linear",
                             initialization_name="uniform", algo_name=algo_name, learning_rate=learning_rate,
                             n_epochs=2000, batch_size=10, seed=53674, verbosity_e=100, verbosity_b=10)
    approx.fit(X_train, y_train)

    # Save the model
    model_filename = f"models/model_lr_{learning_rate}_act_{activation_name}_algo_{algo_name}_struct_{len(structure)}.pkl"
    save_model(approx, model_filename)

    # Evaluate the model
    y_pred = approx.predict(X_train)
    mse_train = np.mean((y_pred - y_train) ** 2)
    y_pred_test = approx.predict(X_test)
    mse_test = np.mean((y_pred_test - y_test) ** 2)
    r2_train = approx.score(X_train, y_train)
    r2_test = approx.score(X_test, y_test)

    # Print metrics
    print(f"LOSS TRAIN (MSE): {mse_train}")
    print(f"LOSS TEST (MSE): {mse_test}")
    print(f"R^2 TRAIN: {r2_train}")
    print(f"R^2 TEST: {r2_test}")

    # Generate and save plots
    mesh_size = 50
    X1, X2 = np.meshgrid(np.linspace(0.0, domain, mesh_size), np.linspace(0.0, domain, mesh_size))
    X12 = np.array([X1.ravel(), X2.ravel()]).T
    y_ref = np.cos(X12[:, 0] * X12[:, 1]) * np.cos(2 * X12[:, 0])
    Y_ref = np.reshape(y_ref, (mesh_size, mesh_size))
    y_pred = approx.predict(X12)
    Y_pred = np.reshape(y_pred, (mesh_size, mesh_size))
    epochs, losses_train, losses_test = loss_during_fit(approx, X_train, y_train, X_test, y_test)
    epochs, r2s_train, r2s_test = r2_during_fit(approx, X_train, y_train, X_test, y_test)
    fig = plt.figure(figsize=(16, 9))
    fig.suptitle(f"DATA SETTINGS: domain={domain}, noise_std={noise_std}, m_train={m_train}, m_test={m_test}\n"
                 f"APPROXIMATOR: {approx}", fontsize=8)
    ax_loss = fig.add_subplot(2, 2, 1)
    ax_loss.set_title("TRAIN / TEST LOSS DURING FIT (MSE - MEAN SQUARED ERROR)")
    ax_loss.plot(epochs, losses_train, color="blue", marker=".", label="LOSS ON TRAIN DATA")
    ax_loss.plot(epochs, losses_test, color="red", marker=".", label="LOSS ON TEST DATA")
    ax_loss.legend()
    ax_loss.grid(color="gray", zorder=0, dashes=(4.0, 4.0))
    ax_loss.set_xlabel("EPOCH")
    ax_loss.set_ylabel("SQUARED LOSS")
    ax_r2 = fig.add_subplot(2, 2, 2)
    ax_r2.set_title("TRAIN / TEST $R^2$ DURING FIT (COEF. OF DETERMINATION)")
    ax_r2.plot(epochs, r2s_train, color="blue", marker=".", label="$R^2$ ON TRAIN DATA")
    ax_r2.plot(epochs, r2s_test, color="red", marker=".", label="$R^2$ ON TEST DATA")
    ax_r2.set_ylim(-0.25, 1.05)
    ax_r2.legend()
    ax_r2.grid(color="gray", zorder=0, dashes=(4.0, 4.0))
    ax_r2.set_xlabel("EPOCH")
    ax_r2.set_ylabel("$R^2$")
    ax_train_data = fig.add_subplot(2, 3, 4, projection='3d')
    ax_target = fig.add_subplot(2, 3, 5, projection='3d')
    ax_approximator = fig.add_subplot(2, 3, 6, projection='3d')
    ax_train_data.set_title("TRAINING DATA", pad=-32)
    ax_train_data.scatter(X_train[:, 0], X_train[:, 1], y_train, marker=".")
    ax_target.set_title("TARGET (TO BE APPROXIMATED)", pad=-128)
    ax_target.plot_surface(X1, X2, Y_ref, cmap=cm.get_cmap("Spectral"))
    ax_approximator.set_title("NEURAL APPROXIMATOR")
    ax_approximator.plot_surface(X1, X2, Y_pred, cmap=cm.get_cmap("Spectral"))
    ax_train_data.set_box_aspect([2, 2, 1])
    ax_target.set_box_aspect([2, 2, 1])
    ax_approximator.set_box_aspect([2, 2, 1])
    plt.subplots_adjust(top=0.9, bottom=0.05, left=0.1, right=0.9, hspace=0.25, wspace=0.15)

    plot_filename = f"plots/plot_lr_{learning_rate}_act_{activation_name}_algo_{algo_name}_struct_{len(structure)}.png"
    #save_plot(fig, plot_filename)
    plt.show()

if __name__ == '__main__':
    learning_rates = [1e-2, 1e-3, 1e-4]
    activation_names = ["sigmoid", "relu"]
    algo_names = ["sgd_simple", "sgd_momentum", "rmsprop", "adam"]
    structures = [
        [128, 64, 32],
        [128, 128, 64, 64, 32, 32],
        [64] * 5 + [32] * 5 + [16] * 5 + [8] * 5
    ]

    os.makedirs('logs', exist_ok=True)
    os.makedirs('models', exist_ok=True)
    os.makedirs('plots', exist_ok=True)

    # for lr in learning_rates:
    #     for act in activation_names:
    #         for algo in algo_names:
    #             for struct in structures:
    #                 log_filename = f"logs/lr_{lr}_act_{act}_algo_{algo}_struct_{len(struct)}.txt"
    #                 if os.path.exists(log_filename):
    #                     print(f"Skipping combination lr={lr}, act={act}, algo={algo}, struct={struct} (log file exists)")
    #                     continue
    #                 try:
    #                     run_experiment(lr, act, algo, struct)
    #                 except Exception as e:
    #                     print(f"Error with combination lr={lr}, act={act}, algo={algo}, struct={struct}: {e}")
    #                     continue

    run_experiment(1e-4, "sigmoid", "rmsprop", [128, 64, 32])