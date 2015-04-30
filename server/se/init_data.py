# 2012036774
# Kim Haryeong
import json
from price.models import *

with open('price/initial_category.json', 'r') as f:
    data = json.loads(f.read())
    for category_data in data['data']:
        category = Category(name=category_data['key'])
        category.save()
        for main_data in category_data['value']:
            Main(name=main_data, category=category).save()


