#-*- coding: utf-8 -*-
import sys
#sys.path.append("/usr/local/lib/python2.7/dist-packages")
import xlrd
from xlrd.sheet import ctype_text
#import MySQLdb

# Open the workbook and define the worksheet
workbook = xlrd.open_workbook("pytest.xlsx")
sheet_name = workbook.sheet_names()[0]
sheet = workbook.sheet_by_name(sheet_name)
#print workbook.sheet_by_index(0)
row = sheet.row(4)

for idx, cell_obj in enumerate(row):
    cell_type_str = ctype_text.get(cell_obj.ctype, 'unknown type')
    print '(%s) %s %s' % (idx, cell_type_str, cell_obj.value)

data = [0 for i in range(len(row))]
for idx, cell_obj in enumerate(row):
    data[idx] = '%s' % cell_obj.value

#for i in range(len(row)) :
#    row[i] = row[i]

# Establish a MySQL connection
#database = MySQLdb.connect (host="localhost", user = "hyuse", passwd = "1234", db = "test")

# Get the cursor, which is used to traverse the database, line by line
#cursor = database.cursor()

# Create the INSERT INTO sql query
#query = """INSERT INTO orders (product, customer_type, rep, date, actual, expected, open_opportunities, closed_opportunities, city, state, zip, population, region) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"""

query = """insert into region_do(name) values (%s)"""

# Create a For loop to iterate through each row in the XLS file, starting at row 2 to skip the headers

#for c in range(1, sheet.columns):
#      print sheet.cell(2, c).value
# for r in range(1, sheet.nrows):
#       product      = sheet.cell(r,).value
#       customer = sheet.cell(r,1).value
#       rep          = sheet.cell(r,2).value
#       date     = sheet.cell(r,3).value
#       actual       = sheet.cell(r,4).value
#       expected = sheet.cell(r,5).value
#       open        = sheet.cell(r,6).value
#       closed       = sheet.cell(r,7).value
#       city     = sheet.cell(r,8).value
#       state        = sheet.cell(r,9).value
#       zip         = sheet.cell(r,10).value
#       pop          = sheet.cell(r,11).value
#       region   = sheet.cell(r,12).value

      # Assign values from each row
#      values = (product, customer, rep, date, actual, expected, open, closed, city, state, zip, pop, region)

      # Execute sql Query
 #     cursor.execute(query, values)

# Close the cursor
#cursor.close()

# Commit the transaction
#database.commit()

# Close the database connection
#database.close()

# Print results
# columns = str(sheet.ncols)
# rows = str(sheet.nrows)
# print "I just imported " %2B columns %2B " columns and " %2B rows %2B " rows to MySQL!"

# Hope this is useful. More to come!
