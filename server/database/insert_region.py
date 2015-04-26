#-*- coding: utf-8 -*-
import sys
import xlrd
from xlrd.sheet import ctype_text
region_do_index = 2
region_si_index = 3

workbook = xlrd.open_workbook("../pytest.xlsx")
sheet = workbook.sheet_by_index(0)
num_cols = sheet.ncols
num_rows = sheet.nrows

region_do = list()
for row_idx in range(0, num_rows):
    cell_obj = sheet.cell(row_idx, region_do_index)
    if not cell_obj.value:
        continue
    elif cell_obj.value in region_do:
        continue
    else:
        region_do.append(cell_obj.value)

region = dict()
do = ""
for row_idx in range(0, num_rows):
    cell_obj = sheet.cell(row_idx, region_do_index)
    cell_obj2 = sheet.cell(row_idx, region_si_index)
    print "******* %s %s" % (cell_obj.value,cell_obj2.value)
    if not cell_obj2.value:
        continue
    if cell_obj.value:
        do = cell_obj.value
        if not do in region:
            region[do] = []
    if cell_obj2.value in region[do]:
        continue
    else:
        region[do].append(cell_obj2.value)

for i in region:
    sys.stdout.write('\n')
    sys.stdout.write(i)
    sys.stdout.write(': ')
    for j in region[i]:
        sys.stdout.write(j)
        sys.stdout.write(', ')

import MySQLdb as mdb
import sys
print "*** insert into DB ***"
try:
    con = mdb.connect('localhost', 'hyuse', '1234', 'test')
    cur = con.cursor()
    query = "delete from region_do"
##    cur.execute(query)
    for i in range(len(region_do)):
        query = "insert into region_do(name) values('%s')" % region_do[i].encode('utf-8')
##        cur.execute(query)        
# insert 'region_si' table
    query = "delete from region_si"
##    cur.execute(query)
    for i in region:
        query = "select id from region_do where name='%s'" % i.encode('utf-8')
        cur.execute(query)
        result = cur.fetchall()
        do_id = int(result[0][0])
        for j in region[i]:
            print j, do_id
            query = "insert into region_si(name, do_id) values('%s', '%d')" % (j.encode('utf-8'), do_id)
            cur.execute(query)
    con.commit()
except Exception, e:
    print e
    sys.exit(1)

finally:
    if con:
        con.close()


