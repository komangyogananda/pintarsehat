import json
import hashlib

f = open('food.json', 'r')
foods = json.load(f)
f.close()

new_foods = {}

for food in foods:
  doc = foods[food]
  food = food.strip()
  food = food.replace('/', ',')
  doc['title'] = food
  key_source = doc['category'] + doc['title']
  key = hashlib.md5(key_source.encode('utf-8')).hexdigest()
  new_foods[key] = doc
  print(food, key)
  f = open('new_foods.json', 'w')
  f.write(json.dumps(new_foods, indent=2, ensure_ascii=False))
  f.close()

