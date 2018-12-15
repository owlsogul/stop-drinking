
# coding: utf-8

# # StopDrinking 모델 사용
# ## import

# In[1]:


import tensorflow as tf
import numpy as np
import sys

memberId = "test"
tension = 1
dinkingYesterday = 1
sleepHour = 1
drinkness = 1
if len(sys.argv) == 6:
    memberId = sys.argv[1]
    tension = sys.argv[2]
    dinkingYesterday = sys.argv[3]
    sleepHour = sys.argv[4]
    drinkness = sys.argv[5]


# ## 예측에 사용할 데이터
# (얼마나 긴장했는지, 전날 얼마나 마셨는지, 얼마나 잤는지, 얼마나 취하고 싶은지(default는 5로 고정))

# In[2]:


test_x = [[ tension, dinkingYesterday, sleepHour, drinkness ]]


# ## 학습

# In[3]:


n_amount_input = 4
n_amount_hidden = [4]

amount_weights = {
    'h1' : tf.Variable(tf.random_normal([n_amount_input, n_amount_hidden[0]], dtype=tf.float64), dtype=tf.float64),
    'out' : tf.Variable(tf.random_normal([n_amount_hidden[0], 1], dtype=tf.float64), dtype=tf.float64)
}
amount_biases = {
    'b1' : tf.Variable(tf.random_normal([n_amount_hidden[0]], dtype=tf.float64), dtype=tf.float64),
    'out' : tf.Variable(tf.random_normal([1], dtype=tf.float64), dtype=tf.float64)
}

def amount_mlp(x):
    L1 = tf.add(tf.matmul(x, amount_weights['h1']), amount_biases['b1'])
    Lout = tf.matmul(L1, amount_weights['out']) +  amount_biases['out']
    return Lout

amount_x = tf.placeholder(dtype=tf.float64, shape=[None, n_amount_input])
amount_y = tf.placeholder(dtype=tf.float64, shape=[1])

amount_hypothesis = amount_mlp(amount_x)
amount_cost = tf.reduce_mean(tf.square(amount_hypothesis - amount_y))
amount_optimizer = tf.train.AdamOptimizer(learning_rate=0.01).minimize(amount_cost)

# Prints the rows of the result set of the above query.
with tf.Session() as sess:
    sess.run(tf.global_variables_initializer())
    # save model
    saver = tf.train.Saver()
    save_path = "./models/"+memberId+"/model"
    saver.restore(sess, save_path)

    # test
    print("result:"+str(sess.run([amount_hypothesis], feed_dict={amount_x: test_x})))
print("jobs-done")


# backup
