import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler
from sklearn.decomposition import KernelPCA
from sklearn.linear_model import SGDClassifier
from sklearn.naive_bayes import GaussianNB
from matplotlib.colors import ListedColormap

data = pd.read_csv('data.csv')
data.isnull().sum()
data.drop('Unnamed: 32', axis=1, inplace=True)
x = data.iloc[:, 3:]
y = data.diagnosis
y = y.map({'M': 1, 'B': 0})

scaler = StandardScaler()
x = scaler.fit_transform(x)

reduction = KernelPCA(n_components=2, kernel='linear', random_state=1)
x = reduction.fit_transform(x)
colors = ['#0066CC', '#DD0000']

def boundary_line(cls):
    X1, X2 = np.meshgrid(np.arange(start=x[:, 0].min() - 1, stop=x[:, 0].max() + 1, step=0.01),
                         np.arange(start=x[:, 1].min() - 1, stop=x[:, 1].max() + 1, step=0.01))
    plt.contourf(X1, X2, cls.predict(np.array([X1.ravel(), X2.ravel()]).T).reshape(X1.shape),
                 alpha=0.45, cmap=ListedColormap(['#0066CC', '#DD0000']))
    plt.xlim(X1.min(), X1.max())
    plt.ylim(X2.min(), X2.max())
    for i, j in enumerate(np.unique(y)):
        plt.scatter(x[y == j, 0], x[y == j, 1],
                    c=colors[i], label=j)
    plt.axis('off')
    #plt.xticks(fontsize=3)
    #plt.yticks(fontsize=3)


def selected_instances(xs):
    for xss in xs:
        plt.scatter(xss[0], xss[1], color='#ffdb19')


def concept_drift():
    for i in range(0, len(x)):
        xi = x[i]
        if -xi[0] + 2 < xi[1]:
            y[i] = 0
        else:
            y[i] = 1


if __name__ == '__main__':
    fig = plt.figure()
    #classifier = SGDClassifier(n_iter=10, learning_rate='constant', eta0=0.1)
    classifier = GaussianNB()
    classifier.fit(x, y)

    boundary_line(classifier)
    fig.savefig('1.svg', format='svg')

    fig.clear()
    concept_drift()
    xs, ys = x[np.r_[10:15, 60:65,5]], y[np.r_[10:15, 60:65,5]]
    boundary_line(classifier)
    selected_instances(xs)
    fig.savefig('2.svg', format='svg')

    fig.clear()
    for i in range(0, 10):
        classifier.partial_fit(xs, ys)
    boundary_line(classifier)
    selected_instances(xs)
    fig.savefig('3.svg', format='svg')

    fig.clear()
    for i in range(0, 40):
        classifier.partial_fit(xs, ys)
    boundary_line(classifier)
    selected_instances(xs)
    fig.savefig('4.svg', format='svg')

    fig.clear()
    for i in range(0, 50):
        classifier.partial_fit(xs, ys)
    boundary_line(classifier)
    selected_instances(xs)
    fig.savefig('5.svg', format='svg')

    fig.clear()
    for i in range(0, 100):
        classifier.partial_fit(xs, ys)
    boundary_line(classifier)
    selected_instances(xs)
    fig.savefig('6.svg', format='svg')



