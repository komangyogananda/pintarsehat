from selenium import webdriver
from selenium.webdriver.common.by import By 
from selenium.webdriver.support.ui import WebDriverWait 
from selenium.webdriver.support import expected_conditions as EC 
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.firefox.options import Options
import time
import sys
import json
import html

options = Options()
options.headless = True
driver = webdriver.Firefox(options=options)
num_of_pages = 32
links = "https://foodb.ca/"
counter = 0

f = open("foods2.json", 'r')
foods_obj = json.load(f)
f.close()

filename = sys.argv[1]
start_point = foods_obj[-1]['name']
found = False

for i in range(1, num_of_pages):
  driver.get("https://foodb.ca/foods?page={}".format(i))
  foods = driver.find_elements_by_css_selector("#food_search > table > tbody > tr > td:nth-child(2) > a")
  food_pages = [food.get_attribute('href') for food in foods]
  for food_page in food_pages:
    wait_time = 1
    error_this = 0
    while 1:
      if error_this >= 5:
        break
      try:
        driver.get(food_page)
        time.sleep(wait_time)

        food_name = driver.find_element_by_css_selector("body > main > table > tbody > tr:nth-child(2) > td > strong").text
        scientific_name = driver.find_element_by_xpath("//*[text()='Scientific Name']/following-sibling::td").text
        group = driver.find_element_by_xpath("//*[text()='Group']/following-sibling::td").text
        sub_group = driver.find_element_by_xpath("//*[text()='Sub-Group']/following-sibling::td").text
        picture = driver.find_element_by_xpath("//*[text()='Picture']/following-sibling::td/child::a").get_attribute('href')

        if found == False and food_name != start_point:
          print(food_name, counter)
          counter += 1
          break
        elif food_name == start_point:
          print(food_name, counter)
          counter += 1
          found = True
          break

        # compound_number = driver.find_element_by_id("DataTables_Table_0_info").text.split(" ")[5]
        compounds = []
        # for i in range(int(compound_number) // 10 + 1):
        #   compounds_name = driver.find_elements_by_css_selector("#DataTables_Table_0 > tbody > tr > td:nth-child(1)")
        #   if compounds_name[0].text == 'No data available in table':
        #     break
        #   average = driver.find_elements_by_css_selector("#DataTables_Table_0 > tbody > tr > td:nth-child(4)")
        #   for i, name in enumerate(compounds_name):
        #     compounds.append({
        #       'name': html.unescape(name.text),
        #       'average': html.unescape(average[i].text)
        #     })
        #   next_button = driver.find_element_by_css_selector("#DataTables_Table_0_next > a").click()
        #   time.sleep(2)

        nutrient_number = driver.find_element_by_id("DataTables_Table_1_info").text.split(" ")[5]
        nutrients = []
        for i in range(int(nutrient_number) // 10 + 1):
          nutrients_name = driver.find_elements_by_css_selector("#DataTables_Table_1 > tbody > tr > td:nth-child(1)")
          average = driver.find_elements_by_css_selector("#DataTables_Table_1 > tbody > tr > td:nth-child(3)")
          if nutrients_name[0].text == 'No data available in table':
            break
          for i, name in enumerate(nutrients_name):
            nutrients.append({
              'name': html.unescape(name.text),
              'average': html.unescape(average[i].text)
            })
          next_button = driver.find_element_by_css_selector("#DataTables_Table_1_next > a").click()
          time.sleep(wait_time)

        food_obj = {
          'name': html.unescape(food_name),
          'scientific_name': html.unescape(scientific_name),
          'picture': picture,
          'classification': {
            'group': html.unescape(group),
            'sub_group': html.unescape(sub_group),
          },
          'compounds': compounds,
          'nutrients': nutrients,
        }
        print (food_name, counter)
        counter += 1
        foods_obj.append(food_obj)

        f = open(filename, 'w')
        f.write(json.dumps(foods_obj, indent=2, ensure_ascii=False))
        f.close()
        wait_time = 1
        error_this = 0
        break
      except:
        print("error")
        time.sleep(1)
        wait_time += 1
        error_this += 1
        continue

driver.quit()