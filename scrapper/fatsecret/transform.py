import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import sys
import json
import time

cred = credentials.Certificate('pintarsehat_firebase.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

f = open('foods.json', 'r')
foods_collections = json.load(f)
f.close()

foods_collections_firestore = db.collection(u'foods')
failed = []
new_foods = {}

for food in foods_collections:
  try:
    print(food, foods_collections[food]["title"], end=' ')
    new_doc_ref = foods_collections[food]
    # print(foods_collections[food]["portions"])
    for portion_key in new_doc_ref["portions"]:
      portion = new_doc_ref["portions"][portion_key]
      summary = {
        'Kalori': portion["summary"]["Kal"],
        'Lemak': portion["summary"]["Lemak"],
        'Karbohidrat': portion["summary"]["Karb"],
        'Protein': portion["summary"]["Prot"]
      }
      new_doc_ref["portions"][portion_key]["summary"] = summary
    # print(new_doc_ref)
    new_foods[food] = new_doc_ref
    doc_ref_firestore = foods_collections_firestore.document(food).set(new_doc_ref)
    print("success")
  except KeyboardInterrupt:
    break
  except:
    print("failed")
    failed.append(food)

f = open("foods.json", "w")
f.write(json.dumps(new_foods, indent=2, ensure_ascii=True))
f.close()

f = open("failed.json", "w")
f.write(json.dumps(failed))
f.close()
  


