import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import sys
import json

cred = credentials.Certificate('pintarsehat_firebase.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

f = open('brand.json', 'r')
foods_collections = json.load(f)
f.close()

f = open('new_foods.json', 'r')
foods_collections.update(json.load(f))
f.close()


statistics = db.collection(u'search')

for food in foods_collections:
  food_data = foods_collections[food]
  title = food_data.get("title")
  key = food
  portions = food_data.get("portions")
  category = food_data.get("category")
  for portion in portions:
    doc_ref = {
      'title':title,
      'category': category,
      'portion': portion['title'],
      'summary': portion['summary'],
      'statistics': {
        'clicked': 0
      }
    }
    print(key, doc_ref)
    statistics.document(key).set(doc_ref)