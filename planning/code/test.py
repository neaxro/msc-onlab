from pymongo import MongoClient
from bson import ObjectId

# Connect to MongoDB
client = MongoClient('mongodb://localhost:27017')
db = client['Proba']

documents = list(db['Gyak'].find({"_id": ObjectId("65d3989697497904b6fe30a8")}))

print(documents)