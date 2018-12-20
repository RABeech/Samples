# Author: Richard Beech
# Keras is a higher level TF wrapper for AI deep learning
# Model
from keras.models import Sequential

# Keras layers
from keras.layers import Dense, Activation, Conv2D, MaxPool2D, Flatten, Dropout, BatchNormalization

import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

# Keras helper method
# Requires that T is one hot encoded
def y2indicator(y):
    N = len(y)
    K = len(set(y))
    I = np.zeros((N, K))
    I[np.arange(N),y] = 1
    return I

# Load data
data = pd.read_csv('./large_files/fashion-mnist_train.csv')
data = data.as_matrix()
np.random.shuffle(data)

print('Shape of data: ', data.shape)
print()

# Data need to be shaped by NxHxWxColor
# The image is 28x28 in grayscale
# Normalize the input
# X will be from column 1 onwards
# Y is on column 0
X = data[:,1:].reshape(-1,28,28,1)/255.0
Y = data[:,0].astype(np.int32)

print('Shape of X', X.shape)
print('Shape of Y', Y.shape)
print()

# Get the num of class K
K = len(set(Y))

# By default, Keras wants one hot encoded lables
Y = y2indicator(Y)

model = Sequential()

# Define 3 convolutional layers
# First conv pool layer
model.add(Conv2D(input_shape=(28,28,1),filters=32,kernel_size=(3,3)))
# used to normalize the output fro each layer
model.add(BatchNormalization())
model.add(Activation('relu'))
model.add(MaxPool2D())

# Second conv pool layer
# Define 64 3x3 filters
model.add(Conv2D(filters=64, kernel_size=(3,3)))
model.add(BatchNormalization())
model.add(Activation('relu'))
model.add(MaxPool2D())

# Third conv pool layer
# Define 128 3x3 filters
model.add(Conv2D(filters=128, kernel_size=(3,3)))
model.add(BatchNormalization())
model.add(Activation('relu'))
model.add(MaxPool2D())

# Flatten last conv pool layer
model.add(Flatten())

# Add 1 dense hidden layer
# 300 hidden neurons
model.add(Dense(units=300))
model.add(Activation('relu'))
model.add(Dropout(0.2)) # Rand drop the node for regulartiztion purpose

# Define output layer
model.add(Dense(units=K))
model.add(Activation('softmax'))

model.compile(
    loss='categorical_crossentropy',
    optimizer='adam',
    metrics=['accuracy']
)

print('Summery of Model')
print(model.summary())
print()

r = model.fit(X, Y, validation_split=0.33, epochs=10, batch_size=32)
print('Returned: ', r)
print((r.history.keys()))

# Loss for training and crossvalidation
plt.plot(r.history['loss'], label='loss')
plt.plot(r.history['val_loss'], label='val_loss')
plt.legend()
plt.show()

plt.plot(r.history['acc'], label='acc')
plt.plot(r.history['val_acc'], label='val_acc')
plt.legend()
plt.show()

