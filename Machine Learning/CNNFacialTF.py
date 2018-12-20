# Richard Beech
# 6/27/18
# CNN(convolution neural network) model TF implementation for facial recognition


import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt

from sklearn.utils import shuffle
from utilfac import getImageData, error_rate, init_weight_and_bias, y2indicator, relu

# Define hidden layer object for regular feedforward network

class HiddenLayer(object):
    def __init__(self, M1, M2, an_id):
        """
        :param M1: number of unit for input
        :param M2: num of unit for output
        :param an_id: id for this hidden layer
        """
        self.id = an_id
        self.M1 = M1
        self.M2 = M2
        W, b = init_weight_and_bias(M1, M2)
        #W, b is tf.Var which is updatable
        self.W = tf.Variable(W.astype(np.float32))
        self.b = tf.Variable(b.astype(np.float32))
        self.params = [self.W, self.b]

    def forward(self, X):
        """
        Perfrom computation for one layer (computational graph)
        :param X: Intermediate expression, TF object
        :return: output expressoin for hidden layer
        """
        return tf.nn.relu(tf.matmul(X, self.W) + self.b)

    # -- End of HiddenLayer class --

def init_filter(shape, poolsz):
    """
    Creates initial value of filters using numpy
    dimesion for filter, filter_w filter_h input feature maps (num of input channel)
    output feature mapus
    :param shape: tuple having 4 members, weight matrix for the filter
    :param poolsz:
    :return:
    """
    # creates 4D matrix(filter)
    w = np.random.randn(*shape) / np.sqrt(np.prod(shape[:-1]) +
                                          shape[-1] * np.prod(shape[:2] / np.prod(poolsz)))
    return w.astype(np.float32)

class ConvPoolLayer(object):
    """
    One convolution pool layer
    """
    def __init__(self, mi, mo, fw=5, fh=5, poolsz=(2,2)):
        """
        Reduce you size by the factor of 2 by largest value
        :param mi: input feature maps (input channel)
        :param mo: output feature maps (output channel / number of filters)
        :param fw: filter width
        :param fh: filter height
        :param poolsz: size of pool
        """
        sz = (fw, fh, mi, mo)

        # 4D matrix
        W0 = init_filter(sz, poolsz)

        # Weight for filter
        self.W = tf.Variable(W0)
        # mo is number of filter
        b0 = np.zeros(mo, dtype=np.float32)
        self.b = tf.Variable(b0)
        self.poolsz = poolsz
        self.params = [self.W, self.b]

    def forward(self, X):
        """
        :param X: Input image
        :return:
        """
        # Strides moves filter one pixel at a time
        # Padding, size of output image equals size of input image
        conv_out = tf.nn.conv2d(X, self.W, strides=[1,1,1,1], padding='SAME')
        conv_out = tf.nn.bias_add(conv_out, self.b)
        # Image dim expected to be: NxWxHxChannel
        p1, p2 = self.poolsz
        pool_out = tf.nn.max_pool(
            conv_out,
            ksize=[1,p1,p2,1], # pool size
            strides=[1,p1,p2,1],
            padding='SAME'
        )
        return tf.nn.relu(pool_out)


class CNN(object):
    def __init__(self, convpool_layer_sizes, hidden_layer_sizes):
        """
        :param convpool_layer_sizes: list of tuples (num of feature outmap, filter_w, filter_h)
        :param hidden_layer_sizes: list of hidden layer sizes
        """
        self.convpool_layer_sizes = convpool_layer_sizes
        self.hidden_layer_sizes = hidden_layer_sizes

    def fit(self, X, Y, lr=10e-4, mu=0.99, reg=10e-4,
            decay=0.99999, eps=10e-3, batch_sz=30, epochs=20, show_fig=True):
        lr = np.float32(lr)
        mu = np.float32(mu)
        reg = np.float32(reg)
        decay = np.float32(decay)
        eps = np.float32(eps)
        K = len(set(Y))

        # Create validation set
        X, Y = shuffle(X, Y)
        X = X.astype(np.float32)
        Y = y2indicator(Y).astype(np.float32)

        print('Shape of X in fit: ', X.shape)
        print('Shape of Y: ', Y.shape)

        Xvalid, Yvalid = X[-1000:], Y[-1000:]
        X, Y = X[:-1000], Y[:-1000] #training set
        Yvalid_flat = np.argmax(Yvalid, axis=1) #rank-one array

        print('Shape of Yvalid_fla: ', Yvalid_flat.shape)

        # Init convpool layers
        N, width, height, c = X.shape
        mi = c # input channel
        # outw/outh used to calculate output size of the last conv layer
        outw = width
        outh = height

        #List to hold conv layers
        self.convpool_layers = []

        # mo is output channel (num of filter)
        for mo, fw, fh in self.convpool_layer_sizes:
            layer = ConvPoolLayer(mi, mo, fw, fh)
            self.convpool_layers.append(layer)
            outw = outw // 2
            outh = outh // 2 # shrink the image by factor of 2
            mi = mo

        # list of hidden layers for fully connected network
        self.hidden_layers = []

        # init mlp layers
        # size of input size must be same as output of last convpool layer
        # self.convpool_layer_sizes[-1][0] get the last convlayer's channel number
        # M1 is the num of neurons for input of first hidden layer
        M1 = self.convpool_layer_sizes[-1][0] * outw * outh

        count = 0

        # Create each hidden layer obj
        for M2 in self.hidden_layer_sizes:
            h = HiddenLayer(M1, M2, count)
            self.hidden_layers.append(h)
            M1 = M2
            count += 1

        # Define last hidden layer to output
        # (Logistic layer)
        W, b = init_weight_and_bias(M1, K)
        self.W = tf.Variable(W, 'W_logreg')
        self.b = tf.Variable(b, 'b_logreg')

        # Collect params for later use
        self.params = [self.W, self.b]

        for h in self.convpool_layers:
            self.params += h.params

        for h in self.hidden_layers:
            self.params += h.params

        # Set up TF func and vars
        # Placeholder for input data
        tfX = tf.placeholder(tf.float32, shape=(None, width, height, c), name='X')
        tfY = tf.placeholder(tf.float32, shape=(None, K), name='Y')

        # Form the computational graph
        act = self.forward(tfX) # output of final layer


        # Total cost for all weights and bias in all layers
        rcost = reg * sum([tf.nn.l2_loss(p) for p in self.params])

        cost = tf.reduce_mean(
            tf.nn.softmax_cross_entropy_with_logits(
                logits=act, # comp graph
                labels=tfY # ind matrix
            )
        ) + rcost

        prediction = self.predict(tfX)

        train_op = tf.train.RMSPropOptimizer(lr, decay=decay, momentum=mu).minimize(cost)

        n_batches = N // batch_sz

        # Test each conv/pooling layer for one batch
        init = tf.global_variables_initializer()

        with tf.Session() as session:
            session.run(init)

            Xbatch_T = X[2 * batch_sz: (2 * batch_sz + batch_sz)]
            Ybatch_T = Y[2 * batch_sz: (2 * batch_sz + batch_sz)]

            convpool_layer1 = self.convpool_layers[0]
            conv1 = tf.nn.conv2d(tfX, convpool_layer1.W, strides=[1,1,1,1], padding='SAME')
            conv1_bias = tf.nn.bias_add(conv1, convpool_layer1.b)
            p1, p2 = convpool_layer1.poolsz
            pool_out1 = tf.nn.max_pool(
                conv1_bias,
                ksize=[1,p1,p2,1],
                strides=[1,p1,p1,1],
                padding='SAME'
            )
            conv1_finalOutput = tf.nn.relu(pool_out1)

            conv1_shape = session.run(tf.shape(conv1), feed_dict={tfX: Xbatch_T})
            print('conv1_shape: ', conv1_shape)
            print()

            conv1_final_shape = session.run(tf.shape(conv1_finalOutput), feed_dict={tfX: Xbatch_T})
            print('conv1_final_shape: ', conv1_final_shape)
            print()

            shape_finaloutputs = session.run(tf.shape(act), feed_dict={tfX: Xbatch_T})
            print('shape_finaloutput: ', shape_finaloutputs)
            print()

            value_cost = session.run(cost, feed_dict={tfX: Xbatch_T, tfY: Ybatch_T})
            print('cost before trainin: ', value_cost)

        costs = []
        init = tf.global_variables_initializer()

        with tf.Session() as session:
            session.run(init)

            for i in range(epochs):
                X, Y = shuffle(X, Y)

                for j in range(n_batches):
                    Xbatch = X[j * batch_sz:(j * batch_sz + batch_sz)]
                    Ybatch = Y[j * batch_sz:(j * batch_sz + batch_sz)]

                    session.run(train_op, feed_dict={tfX: Xbatch, tfY: Ybatch})

                    if j % 20 == 0:
                        c = session.run(cost, feed_dict={tfX: Xvalid, tfY: Yvalid})
                        costs.append(c)
                        p = session.run(prediction, feed_dict={tfX: Xvalid, tfY: Yvalid})
                        e = error_rate(Yvalid_flat, p)
                        print('iteration: ', i,j, 'cost: ', c, 'error: ', e)

        if show_fig:
            plt.plot(costs)
            plt.show()




    def forward(self, X):
        """
        :param X:
        :return:
        """
        Z = X

        for c in self.convpool_layers:
            Z = c.forward(Z)

        # Get shape of output of last conv layer and turn into list
        Z_shape = Z.get_shape().as_list()

        # dim of output of conv layer
        # NxWxHxNumOfChannel
        # -1 can be used to infer the shape since we do not know many samples we used
        Z = tf.reshape(Z, [-1, np.prod(Z_shape[1:])])

        for h in self.hidden_layers:
            Z = h.forward(Z)

        return tf.matmul(Z, self.W) + self.b

    def predict(self, X):
        pY = self.forward(X)
        return tf.argmax(pY, 1)

def main():

    # 4D image data
    X, Y = getImageData()

    # Reshape X for TF: NxWxHxC
    X = X.transpose((0,2,3,1))
    print('X shape in main: ', X.shape)

    model = CNN(
        # 20 is number of filters (output channel map
        # 5x5 is the filter size
        convpool_layer_sizes=[(20,5,5),(20,5,5,)],
        hidden_layer_sizes=[500,300]
    )

    model.fit(X, Y)

if __name__ == '__main__':
    main()