#-*- coding: utf-8 -*-
# 2012036774
# Kim Haryeong
import sys
import xlrd
from xlrd.sheet import ctype_text
from price.models import *
from datetime import datetime

def readExcel(current_date, type_rw):
    whole_list = list()
    workbook = xlrd.open_workbook("../data/"+current_date+"_"+type_rw+".xlsx")
    sheet = workbook.sheet_by_index(0) # bring firtst sheet
    num_rows = sheet.nrows # the number of rows
    for row_idx in range(0, num_rows):
        # case of unnecessary row 
        if sheet.cell(row_idx, 1).value == sheet.cell(row_idx, 2).value:
            continue
        # exists main category row
        if sheet.cell(row_idx, 1).value:
            main = sheet.cell(row_idx, 1).value
            dic = dict()
            dic['key'] = main
            value_list = list()
            dic['value'] = value_list
            whole_list.append(dic)
        if sheet.cell(row_idx, 2).value:
            region_do = sheet.cell(row_idx, 2).value
        if sheet.cell(row_idx, 3).value:
            region_si = sheet.cell(row_idx, 3).value
        if sheet.cell(row_idx, 4).value:
            sub = sheet.cell(row_idx, 4).value.encode('utf-8')
            # unit parsing
            unit = sub[int(sub.index('('))+1:int(sub.index(')'))]
            sub = sub[0:int(sub.index('('))]
        ssub = sheet.cell(row_idx, 5).value
        year = sheet.cell(row_idx, 6).value
        month = sheet.cell(row_idx, 7).value
        day = sheet.cell(row_idx, 8).value
        price = sheet.cell(row_idx, 9).value
        dic_value = {'region_do':region_do, 'region_si':region_si, 'sub':sub, 'ssub':ssub, 'year':year, 'month':month, 'day':day, 'price':price, 'unit':unit}
        dic['value'].append(dic_value)
    return whole_list

def insertDB(whole_list, type_rw):
    for ll in whole_list:
        main = Main.objects.get(name=ll['key'])
        for value in ll['value']:
            region_do, _ = RegionDo.objects.get_or_create(name=value['region_do'])
            region_si, _ = RegionSi.objects.get_or_create(name=value['region_si'], region_do=region_do)
            sub, _ = Sub.objects.get_or_create(name=value['sub'], main=main)
            ssub, _ = SSub.objects.get_or_create(name=value['ssub'], sub=sub)
            if type_rw == "r":
                item = Item(price_r=value['price'], unit_r=value['unit'], price_date=datetime(int(value['year']), int(value['month']), int(value['day'])), region=region_si, category=ssub)
            else:
                item, _ = Item.objects.get_or_create(price_date=datetime(int(value['year']), int(value['month']), int(value['day'])), region=region_si, category=ssub)
                item.price_w = value['price']
                item.unit_w = value['unit']
            item.save()

insertDB(readExcel("20150429", "r"), "r")
insertDB(readExcel("20150429", "w"), "w")
