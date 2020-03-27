import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import sys
import json

cred = credentials.Certificate('pintarsehat_firebase.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

f = open('brand.json', 'r')
foods = json.load(f)
f.close()

foods_collections = db.collection(u'foods')

start_point = None
found = True

for food in foods:
  if food == start_point:
    found = True
  if not found:
    continue
  doc = foods[food]
  print(doc['title'], food)
  doc_ref = foods_collections.document(food)
  doc_ref.set(doc)
