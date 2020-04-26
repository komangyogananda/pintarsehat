import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import sys
import json
import uuid

cred = credentials.Certificate('pintarsehat_firebase.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

f = open('foods.json', 'r')
foods = json.load(f)
f.close()

new_foods = {}

foods_collections = db.collection(u'foodsv2')

start_point = None
found = True
start_point = 0
count = 0

for food in foods:
  doc = foods[food]
  key = str(uuid.uuid4())
  doc["refId"] = key
  doc["defaultPortion"] = doc["default_portion"]
  doc.pop("default_portion")
  doc["clicked"] = 0
  new_foods[key] = doc
  print(doc['title'], food, end=" ")
  count += 1
  if count >= start_point:
    doc_ref = foods_collections.document(key)
    doc_ref.set(new_foods[key])
    print("indexing", end=" ")
  else:
    print("skipping", end=" ")
  print("{}/{}".format(count, len(foods)))

f = open("foodsv2.json", "w")
f.write(json.dumps(new_foods, indent=2, ensure_ascii=False))
f.close()
