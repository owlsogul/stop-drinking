
# coding: utf-8

# # StopDrinking 모델 학습
# ## import 와 필요한 정보들
#
# Sqlite 쿼리문

# In[2]:


import tensorflow as tf
import numpy as np
import sys

memberId = "test1"
if len(sys.argv) == 2:
    memberId = sys.argv[1]

selectSql = "SELECT partyTension, partyDrinkingYesterday, partySleepHour, feedbackDrinkness, feedbackAmountDrink "
fromSql = "FROM Party NATURAL JOIN Feedback "
whereSql = "where Party.partyHolder=\"" + memberId + "\""
sql = selectSql + fromSql + whereSql
print(sql)


# ## Sqlite 에서 데이터 로드

# In[3]:


dataset = tf.data.experimental.SqlDataset("sqlite", "./database.sqlite3", sql, (tf.float64, tf.float64, tf.float64, tf.float64, tf.float64))
print(dataset)

iterator = dataset.make_one_shot_iterator()
next_element = iterator.get_next()


# ## 학습

# In[4]:


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

element_amount_x = [ tf.slice(next_element, [0], [n_amount_input]) ]
element_amount_y = tf.slice(next_element, [4], [1])

amount_hypothesis = amount_mlp(amount_x)
amount_cost = tf.reduce_mean(tf.square(amount_hypothesis - amount_y))
amount_optimizer = tf.train.AdamOptimizer(learning_rate=0.01).minimize(amount_cost)

# Prints the rows of the result set of the above query.
with tf.Session() as sess:
    sess.run(tf.global_variables_initializer())
    for step in range(3001):
        while True:
            try:
                x_train, y_train = sess.run([element_amount_x, element_amount_y])
                sess.run([amount_optimizer], feed_dict={amount_x: x_train, amount_y:y_train})
            except tf.errors.OutOfRangeError:
                break
    print("train-done")
    saver = tf.train.Saver()
    save_path = saver.save(sess, "./models/"+memberId+"/model")
    print("save-done")

    import os
    print (os.getcwd())
    print("Model saved in file: ", save_path)
    # test
    x_test = [[3, 10, 2, 5], [10, 2, 4, 5], [4, 6,3, 5], [1, 10, 10, 4]]
    print(sess.run([amount_hypothesis], feed_dict={amount_x: x_test}))
