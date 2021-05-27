import mysql.connector
from textblob import TextBlob

# establish database connection
connection = mysql.connector.connect(user='root', password='pacutu.98',
                                     host='127.0.0.1', database='clientdb',
                                     use_pure=False,
                                     auth_plugin='mysql_native_password')
mycursor = connection.cursor()

# retrieve feedbacks
mycursor.execute("SELECT description FROM review")
data = mycursor.fetchall()
feedbacks = []
for x in data:
    x = (str(x).strip('(''),'))
    feedbacks.append(x.strip("''"))

# retrieve clients
clients = []
mycursor.execute("SELECT client_id FROM review")
data = mycursor.fetchall()
for x in data:
    x = (str(x).strip('(''),'))
    clients.append(x.strip("''"))

clients_to_keep = []
i = 0

for feedback in feedbacks:
    feedback_polarity = TextBlob(feedback).sentiment.polarity
    if feedback_polarity > 0:
        clients_to_keep.append(clients[i])
        i += 1
        continue

clients_to_keep = list(dict.fromkeys(clients_to_keep))

mycursor.execute("DROP TABLE IF EXISTS clients_retention")
mycursor.execute(
    "CREATE TABLE clients_retention (id INT PRIMARY KEY, client_id INT, foreign key (client_id) references client(id))")

i = 1
for id in clients_to_keep:
    sql = "INSERT INTO clients_retention VALUES (%s, %s);"
    mycursor.execute(sql, (int(i), int(id)))
    i += 1

# print('Positive_feebacks Count : {}'.format(len(positive_feedbacks)))
# print(positive_feedbacks)
connection.commit()
print(clients_to_keep)

connection.close()
