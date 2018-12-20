# Richard Beech
# 6/20/2018
# Practice TensorFlow basics


import numpy as np
import tensorflow as tf

# In tensorflow, tf.placeHolder is used to feed actual training examples
# tf.Variable is used for trainable variables such as weights and bias

# Create 5x5 matrix
# Required: data type for each element

A = tf.placeholder(tf.float32, shape=(5,5), name='A')

# Shape and name are optional when using placeholder
# Can create a vector w/o giving a shape or name
V = tf.placeholder(tf.float32)

# Matmul is matrix multiplication
# Current defining the computational graph. Currently no data
W = tf.matmul(A,V)

# In TF you do actual work in a session
with tf.Session() as session:
    # figure out what is shape of W
    # feed_dict tells what A is and what V is
    # tf.shape(w) is the output
    shape_w = session.run(tf.shape(W), feed_dict={A: np.random.randn(5,5), V: np.random.randn(5,1)})
    print()
    print("Output type of shape_w: ", type(shape_w))
    print("Shape of W: ", shape_w)
    print()

    # W is computation for this run
    output_w = session.run(W, feed_dict={A: np.random.rand(5,5),
                                         V: np.random.rand(5,1)})

    print()
    print('Output type of output_w: ', type(output_w))
    print('output_w: ', output_w)
    print()

    shape_v = session.run(tf.shape(V), feed_dict={V: np.random.randn(5,2)})
    print('Shape of V: ', shape_v)
    print()

    # tf.Variable is used for trainable variables such as weights and bias for model
    # tf.placeholder is used to feed actual training examples
    #

# tf.Variable can be initialized with a numpy array or a tf array or anything that can be turned into a tf tensor
shape = (2,2)
# Use numpy array to initialize tf.Variable
y = tf.Variable(np.random.randn(2,2))

# Can use tf Array for initialization
x = tf.Variable(tf.random_normal(shape))

# Make your variable a scalar
t = tf.Variable(0)

# Need to initialize a variable first
init = tf.global_variables_initializer()

with tf.Session() as session:

    # and then "run the init operation
    out = session.run(init)

    # You can print out the value of tensor flow variable using eval() func
    result = x.eval()
    print()
    print('Value of X: ', result)
    print('Type of X:', type(result))
    print('Shape of X', result.shape)
    print()
    result = t.eval()
    print('Value of T: ', result)
    print('Type of T: ', type(result))
    print()

# Find the minimum of a simple cost func
u = tf.Variable(20.0)
cost = u*u + u + 1.0 # Cost can be considered as variable

# Can choose the optimizer that implements the algorithm you want
# 0.3 is the learning rate
# for minimize, you tell which expression you want to minimize
# no worry about how to find out the derivative and update u
train_op = tf.train.GradientDescentOptimizer(0.3).minimize(cost)
init = tf.global_variables_initializer()
# Run session again
with tf.Session() as session:
    session.run(init)

    for i in range(12):
        # For each iteration, you do one update
        # Weight update is automated, but loop itself is not
        session.run(train_op)
        print('i = ', i, ' Cost: ', cost.eval(), ' u: ', u.eval())