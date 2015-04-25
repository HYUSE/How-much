#-*- coding: utf-8 -*-
import sys
import xlrd
from xlrd.sheet import ctype_text

workbook = xlrd.open_workbook("pytest.xlsx")
sheet = workbook.sheet_by_index(0)
row = sheet.row(4)

for idx, cell_obj in enumerate(row):
    cell_type_str = ctype_text.get(cell_obj.ctype, 'unknown type')
    print '(%s) %s %s' % (idx, cell_type_str, cell_obj.value)

data = [0 for i in range(len(row))]
for idx, cell_obj in enumerate(row):
    data[idx] = '%s' % cell_obj.value
