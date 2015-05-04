# 2012036774
# Kim Haryeong
from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from .models import Item, Sub, Main, Category, SSub
import json
import dateutil.parser
from datetime import date, timedelta
import time

@csrf_exempt
def index(request):
	request_j = json.loads(request.body)
	if request_j["type"] == "home":
		region_si = request_j["data"]["region_si"]
		response_j = dict(data=list())
		now = date.today()
		now = "%d%02d%02d" % (now.year, now.month, now.day)
		for data in request_j["data"]["sub_id"]:# data : {"sub_id":"91"}
			items = Item.objects.filter(category__sub__id=data, region__name=region_si, price_date=dateutil.parser.parse(now))
			for item in items:
				response_j["data"].append(dict(sub_id=item.category.sub.id, sub_name=item.category.sub.name, price_r=item.price_r, price_w=item.price_w, unit_r=item.unit_r, unit_w=item.unit_w))
		return HttpResponse(json.dumps(response_j), content_type="application/json")

	if request_j["type"] == "result":
		response_j = dict(data=list())
		ssubs = SSub.objects.filter(sub__id=request_j["data"]["sub_id"])
		for ssub in ssubs:
			price = list()
			for i in range(5):
				price = list()
				delta = timedelta(days=-i)
				now = date.today() + delta
				now = "%d%02d%02d" % (now.year, now.month, now.day)
				items = Item.objects.filter(category=ssub, region__name=request_j["data"]["region_si"], price_date=dateutil.parser.parse(now))
				for item in items:
					price_date = str(item.price_date)
					price_date = price_date[0:4]+price_date[5:7]+price_date[8:]
					price.append(dict(price_r=item.price_r, price_w=item.price_w, unit_r=item.unit_r, unit_w=item.unit_w, date=price_date))
				if len(price) != 0:
					response_j["data"].append(dict(grade=ssub.name, price=price))
		return HttpResponse(json.dumps(response_j), content_type="application/json")

	if request_j["type"] == "auto_complete":
		word = request_j["data"]
		mains = Main.objects.filter(name__contains=word)
		subs = Sub.objects.filter(name__contains=word)
		response_j = dict(data=list())
		for main in mains:
			main_subs = Sub.objects.filter(main=main)
			for main_sub in main_subs:
				response_j["data"].append(dict(name=main_sub.main.name+"("+main_sub.name+")", sub_id=main_sub.id))
		return HttpResponse(json.dumps(response_j), content_type="application/json")

	if request_j["type"] == "category":
		response_j = dict(data=list())
		categories = Category.objects.all()
		for category in categories:
			response_j["data"].append(dict(name=category.name, cate_id=category.id))
		return HttpResponse(json.dumps(response_j), content_type="application/json")

	if request_j["type"] == "main":
		response_j = dict(data=list())
		mains = Main.objects.filter(category=Category.objects.get(id=request_j["data"]))
		for main in mains:
			response_j["data"].append(dict(name=main.name, main_id=main.id))
		return HttpResponse(json.dumps(response_j), content_type="application/json")

	if request_j["type"] == "sub":
		response_j = dict(data=list())
		subs = Sub.objects.filter(main=Main.objects.get(id=request_j["data"]))
		for sub in subs:
			response_j["data"].append(dict(name=sub.name, sub_id=sub.id))
		return HttpResponse(json.dumps(response_j), content_type="application/json")	
		
	return HttpResponse("else")
