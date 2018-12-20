# Richard Beech
# 6/20/2018
# ANN TensorFlow version for facial expression recognition

# All variables in tensor flow need to be the same type in order to run this in GPU

import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt
from utilfac import getData, y2indicator, error_rate, init_weight_and_bias
from sklearn.utils import shuffle

# Define hidden layer class
class HiddenLayer(object):
    def __init__(self, M1, M2, an_id):
        """

        :param M1: The input size of the hidden layer
        :param M2: The output size of the hidden layer
        :param an_id: TensorFlow variable id
        """
        W, b = init_weight_and_bias(M1, M2)
        # Initialize the self.W and self.b using numpy array
        # self.W, self.b is tensorflow Variable
        self.W = tf.Variable(W.astype(np.float32))
        self.b = tf.Variable(b.astype(np.float32))
        self.params = [self.W, self.b]

    def forward(self, X):
        """

        :param X: intermediate tensorflow expressoin
        :return: output expression for hidden layer
        """
        return tf.nn.relu(tf.matmul(X, self.W) + self.b)

class ANN(object):
    def __init__(self, hidden_layer_sizes):
        """

        :param hidden_layer_sizes: List of hidden layer sizes
        """
        self.hidden_layer_sizes = hidden_layer_sizes


    def fit(self, X, Y, learning_rate=10e-7, mu=0.99, decay=0.999, reg=10e-3,
            epochs=400, batch_sz=100, show_fig=False):
        """

        :param X:
        :param Y:
        :param learning_rate:
        :param mu: Momentum
        :param decay: Used for RMSprop
        :param reg: regularization to avoid overfitting
        :param epochs:
        :param batch_sz:
        :param show_fig:
        :return:
        """

        print('Facial expressoin recognition')
        print('Shape of X: ', X.shape)
        print('Shape of Y: ', Y.shape)
        print('Sample of X: ', X[:4])
        print()

        K = len(set(Y))

        X, Y = shuffle(X, Y)
        print('Sample of Y (label): ', Y[:4])
        X = X.astype(np.float32)
        Y = y2indicator(Y).astype(np.float32)
        print('K: ', K)
        print('Y which is indicator matrix: ', Y[:4])

        # Create a validation set
        Xvalid, Yvalid = X[-1000:], Y[-1000:]

        # Yvalid_flat is a rank one array containing label
        # Turn the indicator matrix to label matrix
        Yvalid_flat = np.argmax(Yvalid, axis=1)
        print()
        print('Shape of Yvalid: ', Yvalid.shape)
        print('Shape of Yvalid_flat: ', Yvalid_flat.shape)

        # Make new X, Y training set
        X, Y = X[:-1000], Y[:-1000]
        print("Shape of X after final manipulation: ", X.shape)
        print("Shape of Y after final manipulation: ", Y.shape)
        print("Shape of Xvalid after final manipulation: ", Xvalid.shape)
        print("Shape of Yvalid after final manipulation: ", Yvalid.shape)

        # N is num of samples, and D is features
        N, D = X.shape

        self.hidden_layers = []

        M1 = D
        count = 0

        # Create a list containing hidden layer
        # Output of previous layer become input of next layer
        for M2 in self.hidden_layer_sizes:
            # M1 is num of input neuron, M2 is num of output nueron
            # Count is identifer for each layer
            h = HiddenLayer(M1, M2, count)
            M1 = M2
            self.hidden_layers.append(h)
            count += 1

        # Output layer includes K output units
        # self.W is weight between last hidden layer and output layer
        W, b = init_weight_and_bias(M1, K)
        self.W = tf.Variable(W.astype(np.float32))
        self.b = tf.Variable(b.astype(np.float32))

        # Collect paramters for later use
        # Weight and bias for all hidden layers
        self.params = [self.W, self.b]

        # self.hidden_layers is the list of hidden layers
        for h in self.hidden_layers:
            self.params += h.params

        # Define the computational graph for TF
        # Set up input variable for tensor flow
        # None for shape means that you can pass any shape for batch size
        # tfX used for training data
        # tfT is indicator matrix
        tfX = tf.placeholder(tf.float32, shape=(None,D), name='X')
        tfT = tf.placeholder(tf.float32, shape=(None,K), name='T')

        # produce the computational graph
        act = self.forward(tfX) # act means activation in this case

        # Calculate L2_loss
        # rcost is regularization of weight
        rcost = reg * sum([tf.nn.l2_loss(p) for p in self.params])

        # calculate the cost
        cost = tf.reduce_mean(
            tf.nn.softmax_cross_entropy_with_logits(
                logits=act,
                labels=tfT
            )
        ) + rcost

        # Get the prediction given input variable
        prediction = self.predict(tfX)

        # Define train_op, tf optimization func
        # TF will calculate the derivative automatically
        train_op = tf.train.RMSPropOptimizer(learning_rate, decay=decay, momentum=mu).minimize(cost)

        n_batches = N // batch_sz
        print(n_batches)

        costs = []

        # initilize all variabiels
        init = tf.global_variables_initializer()

        # Test each func
        with tf.Session() as session:
            session.run(init)
            batch_no = 2
            Xbatch_T = X[batch_no*batch_sz:(batch_no*batch_sz+batch_sz)]
            Ybatch_T = Y[batch_no*batch_sz:(batch_no*batch_sz+batch_sz)]

            # Test forward func
            hiddenLayer1 = self.hidden_layers[0]
            hiddenLayer2 = self.hidden_layers[1]
            hiddenLayer3 = self.hidden_layers[2]

            # Output for 3 hidden layer
            A1 = hiddenLayer1.forward(tfX)
            A2 = hiddenLayer2.forward(A1)
            A3 = hiddenLayer3.forward(A2)

            shape_A1 = session.run(tf.shape(A1), feed_dict={tfX: Xbatch_T})
            print("Shape of A1: ", shape_A1)
            print()

            shape_A3 = session.run(tf.shape(A3), feed_dict={tfX: Xbatch_T})
            print("Shape of A3: ", shape_A3)
            print()

            shape_act =  session.run(tf.shape(act), feed_dict={tfX: Xbatch_T})
            print("Shape of act: ", shape_act)
            print()

            for i in range(50):
                session.run(train_op, feed_dict={tfX: Xbatch_T, tfT: Ybatch_T})
                value_cost = session.run(cost, feed_dict={tfX: Xbatch_T, tfT: Ybatch_T})
                print("Value of cost after update", value_cost)
                print()

        # Loop through and call train op
        with tf.Session() as session:
            session.run(init)
            for i in range(epochs):
                X, Y = shuffle(X, Y)
                for j in range(n_batches):
                    # Xbatch is training data in one batch
                    # Ybatch is target ind matrix in one batch
                    Xbatch = X[j * batch_sz:(j * batch_sz + batch_sz)]
                    Ybatch = Y[j * batch_sz:(j * batch_sz + batch_sz)]
                    session.run(train_op, feed_dict={tfX: Xbatch, tfT: Ybatch})

                    # Print cost and error on validatoin set at every sixty steps
                    if j % 60 == 0:
                        c = session.run(cost, feed_dict={tfX: Xvalid, tfT: Yvalid})
                        costs.append(c)
                        # Produce prediction for validation set
                        p = session.run(prediction, feed_dict={tfX: Xvalid, tfT: Yvalid})
                        e = error_rate(Yvalid_flat, p)

                        print('i: ', i, ' cost: ', c, ' error rate: ', e)

        if show_fig:
            plt.plot(costs)
            plt.show()

    def predict(self, X):
        """
        :param X:
        :return: label array
        """
        act = self.forward(X)
        return tf.argmax(act,1)

    def forward(self,X):
        """
        Forward function, given input X and produce output
        Func will produce TF computational graph
        :param X: Training data
        :return: computational graph
        """
        Z = X # set z to be current input
        for h in self.hidden_layers:
            Z = h.forward(Z)

        # TF only return activation, not softmax
        return tf.matmul(Z, self.W) + self.b #final output

def main():
    X, Y = getData()

    model = ANN([2000, 1000, 500])
    model.fit(X, Y, show_fig=True)

if __name__ == '__main__':
    main()