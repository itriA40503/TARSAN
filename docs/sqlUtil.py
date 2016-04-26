#-----------------------------------------------------------------------------
# Name: sqlUtil.py
# Purpose: Replace "int8 NOT NULL" with "BIGSERIAL" string.
#
# Author: 990388 <Norman Huang>
#
# Created: 2012/05/11
#
# Usage: python sqlUtil.py
# Usage of p2exe: python setup.py py2exe
#
# README: After execute sqlUtil.py, ubike.sql should be deleted, 
#         and ubike_NEW.sql should be renamed as "ubike.sql"
#-----------------------------------------------------------------------------

import re
import sys
from shutil import move
from os import rename, remove, close

input = sys.argv[1]
temp = 'test.sql'
  
fin = open(input)
fout = open(temp, 'w+')

for line in fin:
    if re.search("CREATE TABLE", line):
        fout.write(line)
        line = fin.next()
        line = re.sub('int8.*NOT NULL', "BIGSERIAL", line)
        fout.write(line)
    else: fout.write(line)
    
fin.close()
fout.close()
remove(input)
move(temp, input)