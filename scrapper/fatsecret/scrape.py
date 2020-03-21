import requests
from bs4 import BeautifulSoup
import json
import time
import sys

root_link = 'https://www.fatsecret.co.id'
link = 'https://www.fatsecret.co.id/kalori-gizi/grup/biji-bijian'
session = requests.Session()
session.headers.update({'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36'})

filename = sys.argv[1]
food_documents = {}
name_start = None
found = True

def scrape_per_portion(soup):
  facts = soup.select('.fact')
  summary = {}
  nutrient = {}
  portion = soup.select_one('.serving_size_value').text
  for fact in facts:
    fact_title = fact.select_one('.factTitle').text
    fact_val = fact.select_one('.factValue').text
    summary[fact_title] = fact_val

  nutrients_title = soup.select('.nutrient.left')
  nutrients_value = soup.select('.nutrient.right')
  last_main = 0
  for i, nutrient_title in enumerate(nutrients_title):
    class_name = nutrient_title['class']
    title = nutrient_title.text
    if title == '':
      continue
    if 'sub' in class_name:
      nutrient[nutrients_title[last_main].text]['sub'][title] = nutrients_value[i + 1].text
    else:
      last_main = i
      nutrient[title] = {
        'val': nutrients_value[i + 1].text,
        'sub': {}
      }
  
  return portion, nutrient, summary
  

def scrape_per_page(link, session):
  page = session.get(link)
  soup = BeautifulSoup(page.text, 'html.parser')
  default_portion = scrape_per_portion(soup)[0]
  porsi_table = soup.find(text="Ukuran porsi umum:").findNext("table").findChildren('a')
  porsi_table = set([root_link + porsi['href'] for porsi in porsi_table])
  portions = []
  for porsi in porsi_table:
    page = session.get(porsi)
    soup = BeautifulSoup(page.text, 'html.parser')
    portion, nutrient, summary = scrape_per_portion(soup)
    portions.append({
      'title': portion,
      'nutrient': nutrient,
      'summary': summary
    })
  return default_portion, portions

page = session.get(link)
soup = BeautifulSoup(page.text, 'html.parser')
categories = soup.select('table.common > tr > td > a')
categories = [category['href'] for category in categories]

counter = 0

for category in categories:
  link_target = root_link + category
  page = session.get(link_target)
  soup = BeautifulSoup(page.text, 'html.parser')
  category_title = soup.select_one(".title").text
  food_links = soup.select(".food_links > a")
  food_links = [{
    'title': food_link.text,
    'link': root_link + food_link['href']
  } for food_link in food_links]
  for food_link in food_links:
    food_title = food_link['title']
    counter += 1
    print(food_title, counter)
    if food_title == name_start:
      found = True
      continue
    elif found == True:
      default_portion, portions = scrape_per_page(food_link['link'], session)
      food_document = {
        'title': food_title,
        'category': category_title,
        'default_portion': default_portion,
        'portions': portions
      }
      # print(food_document)
      food_documents[food_title] = food_document
      f = open(filename, 'w')
      f.write(json.dumps(food_documents, indent=2, ensure_ascii=False))
      f.close()
