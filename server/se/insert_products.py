#-*- coding: utf-8 -*-
import sys
import xlrd
from xlrd.sheet import ctype_text
main_index = 1
sub_index = 4
ssub_index = 5

workbook = xlrd.open_workbook("../pytest.xlsx")
sheet = workbook.sheet_by_index(0)
num_cols = sheet.ncols
num_rows = sheet.nrows

l = list()
d = dict()
for row_idx in range(0, num_rows):
    print sheet.cell(row_idx, 1).value
    if sheet.cell(row_idx, 1).value:
        main = sheet.cell(row_idx, 1)
        d = dict()
        d['key'] = main.value
        ll = list()
        d['value'] = ll
        l.append(d)
    if sheet.cell(row_idx, 8).value:
        if sheet.cell(row_idx, 2).value:
            region_do = sheet.cell(row_idx, 2)
        if sheet.cell(row_idx,3).value:
            region_si = sheet.cell(row_idx, 3)
        if sheet.cell(row_idx, 4).value:
            sub = sheet.cell(row_idx, 4).value
            unit = sub.encode('utf-8')
            i1 = int(unit.index('('))
            i2 = int(unit.index(')'))
            unit = unit[i1+1:i2]
            sub = sub.encode('utf-8')
            i1 = 0
            i2 = int(sub.index('('))
            sub = sub[i1:i2]
        ssub = sheet.cell(row_idx, 5)
        year = sheet.cell(row_idx, 6)
        month = sheet.cell(row_idx, 7)
        day = sheet.cell(row_idx, 8)
        price = sheet.cell(row_idx, 9)
        t = dict()
        t['region_do'] = region_do.value
        t['region_si'] = region_si.value
        t['sub'] = sub
        t['ssub'] = ssub.value
        t['year'] = year.value
        t['month'] = month.value
        t['day'] = day.value
        t['price'] = price.value
        t['unit'] = unit
        d['value'].append(t)

print l
from price.models import *
from datetime import datetime

for ll in l:
    main = Main.objects.get(name=ll['key'])
    for value in ll['value']:
        region_do, _ = RegionDo.objects.get_or_create(name=value['region_do'])
        region_si, _ = RegionSi.objects.get_or_create(name=value['region_si'], region_do=region_do)
        sub, _ = Sub.objects.get_or_create(name=value['sub'], main=main)
        ssub, _ = SSub.objects.get_or_create(name=value['ssub'], sub=sub)
        item = Item(price_r=value['price'], unit=value['unit'], price_date=datetime(int(value['year']), int(value['month']), int(value['day'])), region=region_si, category=ssub)
        item.save()

