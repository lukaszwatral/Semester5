import numpy
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import KBinsDiscretizer, LabelEncoder, OrdinalEncoder

from NBC import NBC


test = "mushrooms"

# X = OrdinalEncoder(dtype=int).fit_transform(X)

def accuracy(y_true, y_pred):
    return sum(y_true == y_pred) / len(y_true)


if (test == "lenses"):
    data = numpy.genfromtxt('lenses.data', dtype=int)
    X = data[:, 1:-1]
    y = data[:, -1]
    X_train, X_test, y_train, y_test = train_test_split(X, y)
    nbc1 = NBC(laplace_smoothing=False)
    nbc2 = NBC(laplace_smoothing=True)
    nbc1.fit(X_train, y_train)
    nbc2.fit(X_train, y_train)
    print("Zbiór lenses")
    print("-------------------------------")
    print("Dokładność bez LaPlace'a:", accuracy(y_test, nbc1.predict(X_test)))
    print("Dokładność z poprawką LaPlace", accuracy(y_test, nbc2.predict(X_test)))

if (test == "nursery"):
    data = numpy.genfromtxt('nursery.data', dtype=str, delimiter=',')
    X = data[:, :-1]
    y = data[:, -1]
    X_train, X_test, y_train, y_test = train_test_split(X, y)
    nbc1 = NBC(laplace_smoothing=False)
    nbc2 = NBC(laplace_smoothing=True)
    nbc1.fit(X_train, y_train)
    nbc2.fit(X_train, y_train)
    print("Zbiór nursery")
    print("-------------------------------")
    print("Dokładność bez LaPlace'a:", accuracy(y_test, nbc1.predict(X_test)))
    print("Dokładność z poprawką LaPlace", accuracy(y_test, nbc2.predict(X_test)))

if (test == "wine"):
    data = numpy.genfromtxt('wine.data', dtype=float, delimiter=',')
    data = KBinsDiscretizer(3, encode='ordinal', strategy='uniform').fit_transform(data)
    X = data[:, 1:]
    y = data[:, 0]
    X_train, X_test, y_train, y_test = train_test_split(X, y)
    nbc1 = NBC(laplace_smoothing=False)
    nbc2 = NBC(laplace_smoothing=True)
    nbc1.fit(X_train, y_train)
    nbc2.fit(X_train, y_train)
    print("Zbiór wine")
    print("-------------------------------")
    print("Dokładność bez LaPlace'a:", accuracy(y_test, nbc1.predict(X_test)))
    print("Dokładność z poprawką LaPlace", accuracy(y_test, nbc2.predict(X_test)))

if (test == "mushrooms"):
    data = numpy.genfromtxt('agaricus-lepiota.data', dtype=str, delimiter=',')
    X = data[:, 1:]
    X = np.tile(X, (1, 100))
    y = data[:, 0]
    X_train, X_test, y_train, y_test = train_test_split(X, y)
    nbc1 = NBC(laplace_smoothing=False, secure=False)
    nbc2 = NBC(laplace_smoothing=False, secure=True)
    nbc3 = NBC(laplace_smoothing=True, secure=False)
    nbc4 = NBC(laplace_smoothing=True, secure=True)
    nbc1.fit(X_train, y_train)
    nbc2.fit(X_train, y_train)
    nbc3.fit(X_train, y_train)
    nbc4.fit(X_train, y_train)
    print("Zbiór mushrooms")
    print("-------------------------------")
    print("Dokładność bez LaPlace'a:", accuracy(y_test, nbc1.predict(X_test)))
    print("Dokładność bez LaPlace'a LOG:", accuracy(y_test, nbc2.predict(X_test)))
    print("Dokładność z poprawką LaPlace:", accuracy(y_test, nbc3.predict(X_test)))
    print("Dokładność z poprawką LaPlace LOG:", accuracy(y_test, nbc4.predict(X_test)))
