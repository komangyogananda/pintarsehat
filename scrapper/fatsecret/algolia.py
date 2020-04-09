import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import sys
import json
import time

f = open('foods.json', 'r')
foods_collections = json.load(f)
f.close()

algolia = []

for key in foods_collections:
  food = foods_collections[key]
  food_index = {
    "title": food["title"],
    "category": food["category"],
    "summary": food["portions"][food["default_portion"]]["summary"],
    "defaultPortion": food["default_portion"],
    "portions": len(food["portions"]) - 1,
    "objectID": key,
    'clicked': 0,
  }
  algolia.append(food_index)
  print (key)

print(len(algolia))
f = open('algolia.json', 'w')
f.write(json.dumps(algolia, indent=2, ensure_ascii=True))
f.close()