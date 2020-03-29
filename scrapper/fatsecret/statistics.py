import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import sys
import json

cred = credentials.Certificate('pintarsehat_firebase.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

foods_collections = db.collection(u'foods').limit(2).get()
statistics = db.collection(u'statistics')

for food in foods_collections:
  title = food.get("title")
  key = food.id
  portions = food.get("portions")
  category = food.get("category")
  for portion in portions:
    doc_ref = {
      'title':title,
      'category': category,
      'portion': portion['title'],
      'summary': portion['summary']
    }
    datastore = statistics.document(key)
    datastore.set(doc_ref)