# Author: Richard Beech
# Date: 6/6/2018
# Purpose: Use log regression for facial expression

import numpy as np
import matplotlib.pyplot as plt
from sklearn.utils import shuffle
from LogisticFacialUtil import getBinaryData, sigmoid, sigmoid_cost, error_rate, cost2

class LogisticModel(object):

    # Object inialization
    def __init__(self):
        pass # Nothing happens when pass is executed

    # Fit func is used to train the model
    # Fit is the learning func
    def fit(self, X, Y, learning_rate=10e-7, reg=0*10e-22, epochs=10000, show_fig=False):
        """

        :param X: input data which is 2d numpy array
        :param Y: 1d NumPy target vector
        :param learning_rate: rate of change for weight parameter
        :param reg: regularization to prevent overfitting
        :param epochs: how many iteration will run gradient descent
        :param show_fig: show figure of cost curve
        :return:
        """

        print("shape of X at beginning of fix", X.shape)
        print("shape of Y at beginning of fix", Y.shape)

        # Produce training and validation set
        # Training set is used to train the model
        # Validation set is used to validate performance of data
        # Shuffle all data to randomly sort
        X, Y = shuffle(X,Y)

        # Last 1000 samples are validation dataset
        Xvalid, Yvalid = X[-1000:],Y[-1000:]
        X, Y = X[:-1000],Y[:-1000]

        print("shape of x after dividing", X.shape)
        print("shape of y after dividing", Y.shape)
        print("shape of xvalid after dividing", Xvalid.shape)
        print("shape of yvalid after dividing", Yvalid.shape)

        N, D = X.shape

        # Define weight vector W and bias B
        # Divide by np.sqrt(D) to make our weight small
        # Generate random small weight
        self.W = np.random.rand(D,1) / np.sqrt(D)
        self.b = 0

        print("shape of W", self.W.shape)

        costs = []
        best_validation_error = 1

        # Perform multiple iteration of gradient descent
        # epochs number of iteration
        for i in range(epochs):

            # pY is vector of prediction probability (N x 1)
            pY = self.forward(X)

            # Gradient descent step
            # dZ is N x 1 vector
            # y is N x 1
            # reg*self is regulariation term to prevent weight become to big
            dZ = pY - Y
            # One step of gradient descent
            self.W = self.W - learning_rate*(X.T.dot(dZ) + reg*self.W)
            self.b = self.b - learning_rate*(dZ.sum() + reg*self.b)

            # Error is calculated form validation set, not training set
            # Print out info every 20 iteration
            if i % 20 == 0:
                # Prediction vector from validation set
                pYvalid = self.forward(Xvalid)

                # Calculate error for validatoin dataset
                c = sigmoid_cost(Yvalid, pYvalid)
                costs.append(c)
                e = error_rate(Yvalid, np.round(pYvalid))
                print("i: ", i, " cost: ", c, " error: ", e)
                if e < best_validation_error:
                    best_validation_error = e
        # Outside for loop
        print("best validation error: ", best_validation_error)

        if show_fig:
            plt.plot(costs)
            plt.show()


    def forward(self, X):
        """
        :param X: 2D array
        :return: N x 1 vector of prediction result (double)
        """

        return sigmoid(X.dot(self.W) + self.b)


def main():
    print(np.__version__)
    print()

    X, Y = getBinaryData()
    print('shape of x before class imbalance main,', X.shape)
    print('shape of Y before class imbalance main', Y.shape)

    N1,D1 = X.shape
    # Figure out what is probablity that y = 0

    p0 = np.sum(Y == 0) / float(N1)
    p1 = np.sum(Y == 1) / float(N1)
    print("p0 before balance: ", p0, " p1 before balance: ", p1)

    # Number of sample for class 0 is 9 times more than class 1
    # Highly imbalanced dataset. The logistic regression does not work well w/
    # highly imbalanced data

    # Solve the class imbalance problem by copying class 1 for 9 times

    # get sample for class 0
    X0 = X[Y[:, 0] == 0]  # changed : is required

    # get the sample for class 1
    X1 = X[Y[:, 0] == 1]  # changed

    # copy class1 for 9 times
    X1 = np.repeat(X1, 9, axis=0)  # class 1 is 9 times less than other classes

    # combine class 0 and class 1 row-wise
    X = np.vstack([X0, X1])  # row-wise operation

    # create new target vector
    Y = np.array([0] * len(X0) + [1] * len(X1))
    Y = np.reshape(Y, (Y.shape[0], 1))

    print("shpae of X after imbalance", X.shape)
    print("shpae of Y after imbalance", Y.shape)

    N, D = X.shape # Shape after balancing data
    p0 = np.sum(Y == 0) / float(N)
    p1 = np.sum(Y == 1) / float(N)
    print("p0 after balance: ", p0, " p1 after balance: ", p1)

    model = LogisticModel() # creat the model

    # Build the logistic model
    model.fit(X,Y, show_fig=True)

if __name__=='__main__':
    main()


