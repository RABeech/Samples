# Author: Adam Beech
# Date: 6/13/2018
# Purpose: This is one-hidden layer neural network for facial recognition

# Softmax for multi-class classification
# Warning: This program requires much more RAM than your previous files since we will use all classes

import numpy as np
import matplotlib.pyplot as plt
from ANN1FacialUtil import getData, softmax, cost2, y2indicator, error_rate, relu
from sklearn.utils import shuffle


class ANN(object):
	def __init__(self, M):
		"""
		:param M: number of hidden unit in the hidden layer
		"""
		self.M = M	# this is the property for the object

	# function for learning
	def fit(self, X, Y, learning_rate = 10e-7, reg = 10e-7, epochs = 7000, show_fig = False):
		X, Y = shuffle(X, Y) # make sure you get random data

		# make traning and validation dataset
		Xvalid, Yvalid = X[-1000:], Y[-1000:]	# last 1000 samples
		X, Y = X[:-1000], Y[:-1000]

		N, D = X.shape
		K = len(set(Y[:,0]))
		T = y2indicator(Y)

		# W1 is weight matrics of hidden layer
		# we want to make sure variance of W1 is 1/D so that my weight is small
		# self.W1 make W1 instance variable for object
		# we initialize W1 randomly using normal distribution
		self.W1 = np.random.randn(D, self.M) / np.sqrt(D)
		self.b1 = np.zeros((1, self.M))

		# W2 is weight matrics for output layer
		self.W2 = np.random.randn(self.M, K) / np.sqrt(self.M)
		self.b2 = np.zeros((1, K))

		costs = []
		best_validation_error = 1
		# perform epochs number of iteration for gradient descent
		for i in range(epochs):
			# A1 is activation for first hidden layer
			# A2 is output of output layer
			A2, A1 = self.forward(X)

			dZ2 = A2 - T	# how you want to change Z2 to minimize the loss
			dW2 = A1.T.dot(dZ2)
			db2 = dZ2.sum(axis = 0, keepdims = True)

			# purpose of reg * self.W2 is to make sure the weight is small in order to
			# prevent over fitting
			self.W2 = self.W2 - learning_rate * (dW2 + reg * self.W2)
			self.b2 = self.b2 - learning_rate * (db2 + reg * self.b2)

			dA1 = dZ2.dot(self.W2.T)
			# 1 - A1 * A1 is derivative of tanh
			dZ1 = dA1 * (1 - A1 * A1)
			dW1 = X.T.dot(dZ1)
			db1 = dZ1.sum(axis = 0, keepdims = True)
			self.W1 = self.W1 - learning_rate * (dW1 + reg * self.W1)
			self.b1 = self.b1 - learning_rate * (db1 + reg * self.b1)

			# check our cross-validation resuilts
			if i % 10 == 0:
				# underscore means I do not care about this value
				pYvalid, _ = self.forward(Xvalid)
				c = cost2(Yvalid, pYvalid)	# cost2 is used for multi-class classification
				costs.append(c)
				# testResults is rank-one array
				testResults = np.argmax(pYvalid, axis = 1)
				# I want testResults to be nx1 column vectors
				testResults = np.reshape(testResults, (testResults.shape[0], 1))
				e = error_rate(Yvalid, testResults)
				print("i: ", i, " cost: ", c, " error: ", e, " best_error: ", best_validation_error)

				if e < best_validation_error:
					best_validation_error = e
				# end if
			# end if
		# end of for-loop
		# after the for-loop
		print("best_validation_error: ", best_validation_error)

		if show_fig:
			plt.plot(costs)
			plt.show()
	# end of fit function

	def forward(self, X):
		"""
		:param X: N x D matrix
		:return: N x K normalized prediction matrix (A2) and N x M activation matrix for first
				 hidden layer
		"""
		Z1 = X.dot(self.W1) + self.b1
		A1 = np.tanh(Z1)	# A1 is activation value for first hidden layer

		# A2 is prediction (output) for the output layer
		Z2 = A1.dot(self.W2) + self.b2
		A2 = softmax(Z2)	# softmax is used for multi-class classification
		return A2, A1
	# end of forward function

	def predict(self, X):
		"""
		:param X: N x K input matrix
		:return: N x 1 prediction vector
		"""
		pY, _ = self.forward(X)
		pred = np.argmax(pY, axis = 1)
		pred = np.reshape(pred, (pred.shape[0], 1))
		return pred
	# end predict function

	def score(self, X, Y):
		"""
		:param X: training set which is 2d array
		:param Y: vector of target
		:return: accuracy of model
		"""
		prediction = self.predict(X)
		return 1 - error_rate(Y, prediction)
	# end score function

def main():
	print("ANN for facial expression")
	X, Y = getData() 	# get the data for all classes
	model = ANN(200)	# 200 hidden unit
	model.fit(X, Y, reg = 0, show_fig = True)
	print('model accuracy: ', model.score(X, Y))
# end main function

if __name__ == '__main__':
	main()
# end if
# end of class