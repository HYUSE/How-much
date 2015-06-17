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
            # server test data
            now = "20150427"
            items = Item.objects.filter(category=ssub, region__name=request_j["data"]["region_si"], price_date=dateutil.parser.parse(now))
            for item in items:
                price_date = str(item.price_date)
                price_date = price_date[0:4]+price_date[5:7]+price_date[8:]
                price.append(dict(price_r=item.price_r, price_w=item.price_w, unit_r=item.unit_r, unit_w=item.unit_w, date=price_date))
            now = "20150428"
            items = Item.objects.filter(category=ssub, region__name=request_j["data"]["region_si"], price_date=dateutil.parser.parse(now))
            for item in items:
                price_date = str(item.price_date)
                price_date = price_date[0:4]+price_date[5:7]+price_date[8:]
                price.append(dict(price_r=item.price_r, price_w=item.price_w, unit_r=item.unit_r, unit_w=item.unit_w, date=price_date))
            now = "20150429"
            items = Item.objects.filter(category=ssub, region__name=request_j["data"]["region_si"], price_date=dateutil.parser.parse(now))
            for item in items:
                price_date = str(item.price_date)
                price_date = price_date[0:4]+price_date[5:7]+price_date[8:]
                price.append(dict(price_r=item.price_r, price_w=item.price_w, unit_r=item.unit_r, unit_w=item.unit_w, date=price_date))
            now = "20150430"
            items = Item.objects.filter(category=ssub, region__name=request_j["data"]["region_si"], price_date=dateutil.parser.parse(now))
            for item in items:
                price_date = str(item.price_date)
                price_date = price_date[0:4]+price_date[5:7]+price_date[8:]
                price.append(dict(price_r=item.price_r, price_w=item.price_w, unit_r=item.unit_r, unit_w=item.unit_w, date=price_date))
            now = "20150504"
            items = Item.objects.filter(category=ssub, region__name=request_j["data"]["region_si"], price_date=dateutil.parser.parse(now))
            for item in items:
                price_date = str(item.price_date)
                price_date = price_date[0:4]+price_date[5:7]+price_date[8:]
                price.append(dict(price_r=item.price_r, price_w=item.price_w, unit_r=item.unit_r, unit_w=item.unit_w, date=price_date))
            # server test data end
            if len(price) != 0:
                response_j["data"].append(dict(grade=ssub.name, price=price))
            # for i in range(5):
            #     delta = timedelta(days=-(4-i))
            #     now = date.today() + delta
            #     now = "%d%02d%02d" % (now.year, now.month, now.day)
            #     items = Item.objects.filter(category=ssub, region__name=request_j["data"]["region_si"], price_date=dateutil.parser.parse(now))
            #     for item in items:
            #         price_date = str(item.price_date)
            #         price_date = price_date[0:4]+price_date[5:7]+price_date[8:]
            #         price.append(dict(price_r=item.price_r, price_w=item.price_w, unit_r=item.unit_r, unit_w=item.unit_w, date=price_date))
            # if len(price) != 0:
            #     response_j["data"].append(dict(grade=ssub.name, price=price))
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


def auto_complete(request):
	word = request.GET.get('data')
	mains = Main.objects.filter(name__contains=word)
	subs = Sub.objects.filter(name__contains=word)
	response_j = dict(data=list())
	for main in mains:
		main_subs = Sub.objects.filter(main=main)
		for main_sub in main_subs:
			response_j["data"].append(dict(name=main_sub.main.name+"("+main_sub.name+")", sub_id=main_sub.id))
	return HttpResponse(json.dumps(response_j), content_type="application/json")

def result(request):
	response_j = dict(data=list())
	sub_id = request.GET.get('sub_id')
	region_si = request.GET.get('region_si')
	ssubs = SSub.objects.filter(sub__id=sub_id)
	for ssub in ssubs:
		price = list()
    	# server test data
    	dates = Item.objects.filter(category=ssub, region__name=region_si).order_by(Coalesce('price_date', 'price_date').desc())[:5]
        for date in dates:
            d = date.price_date
            items = Item.objects.filter(category=ssub, region__name=region_si, price_date=d)
            for item in items:
                price_date = str(item.price_date)
                price_date = price_date[0:4]+price_date[5:7]+price_date[8:]
                price.append(dict(price_r=item.price_r, price_w=item.price_w, unit_r=item.unit_r, unit_w=item.unit_w, date = price_date))
        name = ssub.sub.main.category.name+' > '+ssub.sub.main.name+' > '+ssub.sub.name+' > '+ssub.name
        if len(price) != 0:
        	response_j["data"].append(dict(grade=name, price=price))
	return HttpResponse(json.dumps(response_j), content_type="application/json")

def category(request):
	response_j = dict(data=list())
	categories = Category.objects.all()
	for category in categories:
		response_j["data"].append(dict(name=category.name, cate_id=category.id))
	return HttpResponse(json.dumps(response_j), content_type="application/json")

def main(request):
	response_j = dict(data=list())
	category_id = request.GET.get('category_id')
	mains = Main.objects.filter(category=Category.objects.get(id=category_id))
	for main in mains:
		response_j["data"].append(dict(name=main.name, main_id=main.id))
	return HttpResponse(json.dumps(response_j), content_type="application/json")

def sub(request):
	response_j = dict(data=list())
	main_id = request.GET.get('main_id')
	subs = Sub.objects.filter(main=Main.objects.get(id=main_id))
	for sub in subs:
		response_j["data"].append(dict(name=sub.name, sub_id=sub.id))
	return HttpResponse(json.dumps(response_j), content_type="application/json")

@csrf_exempt
def home(request):
	request_j = json.loads(request.body)
	region_si = request_j["data"]["region_si"]
	response_j = dict(data=list())
	for data in request_j["data"]["sub_id"]:# data : {"sub_id":"91"}
        date = Item.objects.filter(category__sub__id=data, region__name=region_si).order_by(Coalesce('price_date','price_date').desc())[:1]
        if len(date) == 0:
            continue
        date = date[0].price_date
        price_date = str(date)
        price_date = price_date[0:4]+price_date[5:7]+price_date[8:]
        items = Item.objects.filter(category__sub__id=data, region__name=region_si, price_date=date)        
        item = items[0]
        response_j["data"].append(dict(sub_id=item.category.sub.id, sub_name=item.category.sub.name, price_r=item.price_r, price_w=item.price_w, unit_r=item.unit_r, unit_w=item.unit_w, price_date=price_date))
	return HttpResponse(json.dumps(response_j), content_type="application/json")

