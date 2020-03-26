import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import sys
import json

cred = credentials.Certificate('pintarsehat_firebase.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

f = open('brands.json', 'r')
foods = json.load(f)
f.close()

collected_brands = {}

foods_collections = db.collection(u'brands')

start_point = None
found = True

for food in foods:
  brand = foods[food]['category']
  if brand not in collected_brands:
    collected_brands[brand] = {
      'products': []
    }
  else:
    food = food.strip()
    food = food.replace('/', ',')
    collected_brands[brand]['products'].append(food)

for brand in collected_brands:
  print(brand)
  doc_ref = foods_collections.document('brand')
  doc_ref.set(collected_brands[brand])