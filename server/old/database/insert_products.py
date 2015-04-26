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

main = list()
for row_idx in range(0, num_rows):
    cell_obj = sheet.cell(row_idx, main_index)
    if not cell_obj.value:
        continue
    elif cell_obj.value in main:
        continue
    else:
        main.append(cell_obj.value)

sub = dict()
m = ""
for row_idx in range(0, num_rows):
    cell_obj = sheet.cell(row_idx, main_index)
    cell_obj2 = sheet.cell(row_idx, sub_index)
    if not cell_obj2.value:
        continue
    if cell_obj.value:
        m = cell_obj.value
        if not m in sub:
            sub[m] = []
    if cell_obj2.value in sub[m]:
        continue
    else:
        sub[m].append(cell_obj2.value)

# ssub = dict()
# s = ""
# for row_idx in range(0, num_rows):
#     cell_obj1 = sheet.cell(row_idx, main_index)
#     cell_obj2 = sheet.cell(row_idx, sub_index)
#     cell_obj3 = sheet.cell(row_idx, ssub_index)
#     if not cell_obj3.value:
#         continue
#     if cell_obj.value:
#         m = cell_obj.value
#         if not m in ssub:
#             ssub[m] = dict()
#     print m, cell_obj2.value, cell_obj3.value
#     if cell_obj2.value:
#         s = cell_obj2.value
#     else:
#         ssub[m][s].append(cell_obj2.value)

# for i in ssub:
#     sys.stdout.write('\n')
#     sys.stdout.write(i)
#     sys.stdout.write(': ')
#     for j in ssub[i]:
#         sys.stdout.write(j)
#         sys.stdout.write(', ')

import MySQLdb as mdb
import sys
print "*** insert into DB ***"
try:
    con = mdb.connect('localhost', 'hyuse', '1234', 'test')
    cur = con.cursor()
    for i in range(len(main)):
        query = "insert into main(name) values('%s')" % main[i].encode('utf-8')
        cur.execute(query)        
    con.commit()
# insert 'sub' table
    for i in sub:
        query = "select id from main where name='%s'" % i.encode('utf-8')
        cur.execute(query)
        result = cur.fetchall()
        main_id = int(result[0][0])
        for j in sub[i]:
            unit = j.encode('utf-8')
            i1 = int(unit.index('('))
            i2 = int(unit.index(')'))
            unit = unit[i1+1:i2]
            sub_value = j.encode('utf-8')
            i1 = 0
            i2 = int(sub_value.index('('))
            sub_value = sub_value[i1:i2]
            query = "insert into sub(name, main_id) values('%s', '%d')" % (sub_value, main_id)
            cur.execute(query)
    con.commit()
#insert 'ssub' table
    for i in ssub:
        query = "select id from sub where name='%s'" % i.encode('utf-8')
        cur.execute(query)
        result = cur.fetchall()
        main_id = int(result[0][0])
        for j in sub[i]:
            unit = j.encode('utf-8')
            i1 = int(unit.index('('))
            i2 = int(unit.index(')'))
            unit = unit[i1+1:i2]
            sub_value = j.encode('utf-8')
            i1 = 0
            i2 = int(sub_value.index('('))
            sub_value = sub_value[i1:i2]
            query = "insert into sub(name, main_id) values('%s', '%d')" % (sub_value, main_id)
            cur.execute(query)
    con.commit()

except Exception, e:
    print e
    sys.exit(1)

finally:
    if con:
        con.close()






        # sub_value = cell_obj2.value.encode('utf-8')
        # i1 = int(sub_value.index('('))
        # i2 = int(sub_value.index(')'))
        # print sub_value[i1:i2]
