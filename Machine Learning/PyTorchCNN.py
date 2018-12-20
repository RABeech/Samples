'''
Richard Beech

This program is a convolutional neural network built with the
PyTorch framework.
'''

import numpy as np # Util for matrix multiplication
import pandas as pd # Util for data processing, csv file parsing
import torch # PyTorch framework
import torchvision # Package for PyTorch data sets
import matplotlib.pyplot as plt # Util for constructing visual graphs

from torch import nn # Base class for neural network modules

'''
Dataset - MNIST is a popular Data set that is directly supported by the PyTorch framework
The import allows an entire MNIST data set to be passed as a parameter to a method
Also allows for the use of additional APIs such as 'transform' which is a function call that 
takes in an PIL image and returns a transformed version

DataLoader -  is an iterator which provides batching the data, shuffling the data,
and loading the data in parallel.
'''
from torch.utils.data import Dataset, DataLoader
from torchvision import transforms


# Print out the current installed version on PyTorch
print(torch.__version__)

# Variable created for training data set
data_train = pd.read_csv('./input/fashion-mnist_train.csv')

# Variable created for validation data set
data_test = pd.read_csv('./input/fashion-mnist_test.csv')

# Use a numpy util to ensure it's directed at the beginning of the data set
data_train.head()

BATCH_SIZE = 50 # Batch size of 50 images
LR = 0.003 # Learning rate to control gradient descent
NUM_CLASS = 10 # Number of filters used to for back propagation
IMAGE_SIZE = 28 # Size of the image
CHANNEL = 1 # One channel used for the greyscale images
Train_epoch = 25 # Number of passes through the neural network

# Built in PyTorch util that allows for the use of GPU is available
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

# Labels created to ID data set
CLASS_CLOTHING = {0 :'T-shirt/top',
                  1 :'Pants',
                  2 :'Pullover',
                  3 :'Dress',
                  4 :'Coat',
                  5 :'Sandal',
                  6 :'Shirt',
                  7 :'Sneaker',
                  8 :'Bag',
                  9 :'Boot'}

class MyDataset(Dataset):
    '''
    Constructing the data set

    Load the data from the csv file. Utilize data.values to read it
    as a data frame and converted to a numpy array for indexing
    '''
    def __init__(self, data, transform = None):
        self.fashion_mnist = list(data.values)
        self.transform = transform
        label, img = [],[]

        for one_line in self.fashion_mnist:
            label.append(one_line[0])
            img.append(one_line[1:])

        self.label = np.asarray(label)
        self.img = np.asarray(img).reshape(-1, IMAGE_SIZE, IMAGE_SIZE, CHANNEL).astype('float32')

    '''
    Return a single image from the loader. It takes in an index, 
    returns that item and label with it. If the transform function
    is defined, apply the transformation to the item and return it.
    '''
    def __getitem__(self, item):
        label, img = self.label[item], self.img[item]
        if self.transform is not None:
            img = self.transform(img)

        return label, img

    '''
    Compute the length of a given label
    '''
    def __len__(self):
        return len(self.label)

'''
Converts the original PIL image from the Fashion data set
to a float tensor. Take the default range of [0, 255] and
converts it to [0.0, 1.0] to be fed into the neural network
'''
My_transform = transforms.Compose([transforms.ToTensor(),])

'''
Pass the previously declared training and validation set to the
data set class to construct a usable data set to enter into the
CNN
'''
Train_data = MyDataset(data_train, transform=My_transform)
Test_data = MyDataset(data_test, transform=My_transform)

'''
Create a DataLoader object that holds additional information for
the batch size, converted data models, and avoided shuffle request
to be loaded into the CNN
'''
Train_dataloader = DataLoader(dataset=Train_data,
                              batch_size=BATCH_SIZE,
                              shuffle=False
                              )
Test_dataloader = DataLoader(dataset=Test_data,
                             batch_size=BATCH_SIZE,
                             shuffle=False
                             )
# Iterating over the loader to get the items in batch to process on
data_iter = iter(Train_dataloader)
label , img = next(data_iter)

'''
Use the matplotlib to create a visual grid of the 
MNIST data set
'''
def imshow(img, title):
    img = torchvision.utils.make_grid(img)/255
    img = img.numpy().transpose([1, 2, 0])
    plt.imshow(img)
    if title is not None:
        plt.title(title)

imshow(img, [CLASS_CLOTHING[x] for x in label.numpy().tolist()])
plt.show()

class My_Model(nn.Module):
    '''
    Training Model (CNN)
    Neural Network Graph

    Created for the basic neural network module made available through the
    PyTorch utils.
    '''
    def __init__(self, num_of_class):
        super(My_Model, self).__init__()

        '''
        Sequential allows for a sequence of a layer, normalization, activation and pooling
        Conv2d applies a 2D convolution over an input signal composed of several input planes.
        Stride paces across an image to apply the filter and padding allows for spaces between
        strides
        '''
        self.layer1 = nn.Sequential(
            nn.Conv2d(1, 16, kernel_size=5, stride=1, padding=2),
            nn.BatchNorm2d(16),
            nn.ReLU(),
            nn.MaxPool2d(kernel_size=2, stride=2))
        self.layer2 = nn.Sequential(
            nn.Conv2d(16, 32, kernel_size=5, stride=1, padding=2),
            nn.BatchNorm2d(32),
            nn.ReLU(),
            nn.MaxPool2d(kernel_size=2, stride=2))
        self.fc = nn.Linear(7 * 7 * 32, num_of_class)

    def forward(self, x):
        '''
        called on the CNN for a set of inputs,
        and it passes that input through the different
        layers that have been defined
        '''
        out = self.layer1(x)
        out = self.layer2(out)
        out = out.reshape(out.size(0), -1)
        out = self.fc(out)
        return out

def train():
    # Create a model object to train
    model = My_Model(NUM_CLASS).to(device)

    # Use PyTorch package implementing various optimization algorithms and apply learning rate
    optimizer = torch.optim.Adam(model.parameters(), lr = LR)

    # PyTorch loss function to use in conjunction with ReLu activation
    criterion = nn.CrossEntropyLoss()

    # Training cycle, iterate over a batch of images
    for epoch in range(1, Train_epoch + 1):
        for batch_id, (label, image) in enumerate(Train_dataloader):
            label, image = label.to(device), image.to(device)
            output = model(image)
            loss = criterion(output, label)

            '''
            Zero out the gradients in the optimizer. Because the backward() 
            function accumulates gradients, we don’t want to mix values between 
            mini batches. Feed the data through the convolution net.
            Calculate the loss based on the outputs and actual labels.
            Backpropagate the gradients. Update the parameters based 
            on the back propagated values.
            '''
            optimizer.zero_grad()
            loss.backward()
            optimizer.step()

            if batch_id % 1000 == 0:
                print('Loss :{:.4f} Epoch[{}/{}]'.format(loss.item(), epoch, Train_epoch))
    return model

def test(model):
    '''
    Test the model and calculate the loss for model accuracy
    '''
    with torch.no_grad():
        correct = 0
        total = 0
        for label , image in Test_dataloader:
            image = image.to(device)
            label = label.to(device)
            outputs = model(image)
            predicted = torch.argmax(outputs,dim=1)
            total += label.size(0)
            correct += (predicted == label).sum().item()
        print('Test Accuracy of the model on the test images: {} %'.format(100 * correct / total))

# Execute the model
if __name__ == '__main__':
    model = train()
    test(model)