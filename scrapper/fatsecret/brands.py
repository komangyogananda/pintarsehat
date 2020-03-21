import requests
from bs4 import BeautifulSoup
import json
import time
import sys

root_link = 'https://www.fatsecret.co.id'
link = 'https://www.fatsecret.co.id/Default.aspx?pa=brands&t={}&f={}&pg={}'
session = requests.Session()
session.headers.update({'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36'})

filename = sys.argv[1]
food_documents = {}
name_start = None
found = True
counter = 0

food_documents = {}

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
  

def scrape_per_page(link):
  page = session.get(link)
  soup = BeautifulSoup(page.text, 'html.parser')
  default_portion = scrape_per_portion(soup)[0]
  # porsi_table = soup.find(text="Ukuran porsi umum:").findNext("table").findChildren('a')
  # porsi_table = set([root_link + porsi['href'] for porsi in porsi_table])
  portions = []
  portion, nutrient, summary = scrape_per_portion(soup)
  portions.append({
    'title': portion,
    'nutrient': nutrient,
    'summary': summary
  })
  return default_portion, portions

def scrape_per_brand(link):
  page = session.get(link)
  soup = BeautifulSoup(page.text, 'html.parser')
  brand = soup.select_one('.caption > b').text
  print('====BrandName===', brand)
  see_more = root_link + soup.select_one('.smallText > b > a')['href']
  page = session.get(see_more)
  soup = BeautifulSoup(page.text, 'html.parser')
  total_search = soup.select_one('.searchResultSummary').text.split(" ")[-1]
  num_of_pagination = int(total_search) / 10 + 1
  for pagination in range(0, int(num_of_pagination)):
    link = see_more + '&pg={}'.format(pagination)
    page = session.get(link)
    soup = BeautifulSoup(page.text, 'html.parser')
    products = soup.select('.prominent')
    for product in products:
      global counter
      food_title = product.text
      print(food_title, counter)
      counter += 1
      link = root_link + product['href']
      default_portion, portions = scrape_per_page(link)
      food_document = {
        'title': food_title,
        'category': brand,
        'default_portion': default_portion,
        'portions': portions
      }
      food_documents[food_title] = food_document
      f = open(filename, 'w')
      f.write(json.dumps(food_documents, indent=2, ensure_ascii=False))
      f.close()
    

abjad = [chr(x) for x in range(97, 97 + 26)]
abjad.append('*')

for t in range(1, 6):
  for f in abjad:
    print('Brand Abjad', f)
    pg = 0
    while 1:
      # print(pg)
      page = session.get(link.format(t, f, pg))
      soup = BeautifulSoup(page.text, 'html.parser')
      not_found = soup.select_one('.resultMetrics')
      if not_found:
        break
      brands = soup.find_all('h2', attrs={'style': "text-transform:capitalize;margin:0"})
      brands = [root_link + brand.findChildren("a")[0]['href'] for brand in brands]
      for brand in brands:
        scrape_per_brand(brand)
      pg += 1
