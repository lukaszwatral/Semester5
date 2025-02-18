#
from sklearn.base import BaseEstimator, ClassifierMixin
import numpy as np


class NBC(BaseEstimator, ClassifierMixin):

    def __init__(self, laplace_smoothing=False, secure=True):
        self.laplace_smoothing = laplace_smoothing
        self.X_data = []
        self.y_data = []
        self.secure = secure

    def fit(self, X, y):
        self.X_data = X
        self.y_data = y
        self.y_classes, self.y_cnt = np.unique(y, return_counts=True)
        self.x_attribute_value = [np.unique(x) for x in X.T]
        self.tabele_licznosci = []
        for k, x_j_val in enumerate(self.x_attribute_value):
            tab = np.zeros((len(x_j_val), len(self.y_classes)))
            for i, i_val in enumerate(x_j_val):
                for j, j_val in enumerate(self.y_classes):
                    tab[i, j] = sum((X[:, k] == i_val) & (y == j_val))

            if self.laplace_smoothing:
                tab += 1
            self.tabele_licznosci.append(tab)

        return self

    def predict_proba(self, x):
        if (self.secure):
            log_probs = np.zeros(len(self.y_classes))
            log_prior = np.log(self.y_cnt / len(self.y_data))
            for j, y_val in enumerate(self.y_classes):
                log_probs[j] = log_prior[j]
                for k, x_val in enumerate(x):
                    feature_index = np.where(self.x_attribute_value[k] == x_val)[0][0]
                    if(self.laplace_smoothing):
                        log_probs[j] += np.log((self.tabele_licznosci[k][feature_index, j] + 1) / ((self.y_cnt[j] + len(self.x_attribute_value)) + 1e-10))
                    else:
                        log_probs[j] += np.log((self.tabele_licznosci[k][feature_index, j] + 1e-10) / (self.y_cnt[j] + 1e-10))
            return np.exp(log_probs)
        else:
            probs = np.zeros(len(self.y_classes))
            prior = self.y_cnt / len(self.y_data)
            for j, y_val in enumerate(self.y_classes):
                probs[j] = prior[j]
                for k, x_val in enumerate(x):
                    feature_index = np.where(self.x_attribute_value[k] == x_val)[0][0]
                    if (self.laplace_smoothing):
                        probs[j] *= (self.tabele_licznosci[k][feature_index, j] + 1) / (self.y_cnt[j] + len(self.x_attribute_value))
                    else:
                        probs[j] *= self.tabele_licznosci[k][feature_index, j] / (self.y_cnt[j])
            return probs

    def predict(self, X):
        predictions = [self.y_classes[np.argmax(self.predict_proba(x))] for x in X]
        return np.array(predictions)