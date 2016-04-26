#-----------------------------------------------------------------------------
# Name: hibernatePatch.py
# Purpose: Added @JsonIgnore on One to Many or Many to One operation
# which could prevent infinite loop on Jackson JSON generation from DAO.
#
# Author: Gunarto Sindoro Njoo (532711)
#
# Created: 2015/07/28
#
# Usage: python jsonPatch.py
# Initialization: 
# - Set path variable to respective hibernate DAO files
#-----------------------------------------------------------------------------

import re
import os
import glob
import shlex
from shutil import move
from os import rename, remove, close

path = "../src/main/java/org/itri/ccma/tarsan/hibernate"
counter = 0

for filename in glob.glob( os.path.join(path, '*.java') ):

	fin = open(filename)
	foutName=filename+'2'
	fout = open(foutName, 'w+')
	
	lookup = '@JoinColumn'
	lookup2 = '@OneToMany'
	foundLine = []
	with open(filename) as myFile:
		for num, line in enumerate(myFile, 1):
			if lookup in line:
				print filename, " found at #", num
				foundLine.append(num)
			if lookup2 in line:
				print filename, " found another token at #", num
				foundLine.append(num)
	
	if len(foundLine) > 0: 
		for line in fin:
			counter+=1
			if re.search("// Generated", line):
				fout.write(line)
				line = fin.next()
				fout.write('import com.fasterxml.jackson.annotation.JsonIgnore;\r')
			# Put @JsonIgnore below relationship
			fout.write(line)
			for found in foundLine:
				if (found-1)==counter:
					print "Added @JsonIgnore in ", filename
					fout.write('\t@JsonIgnore\r')

	if len(foundLine) == 0:
		for line in fin:
			fout.write(line)
			
	# reset variables
	counter = 0
	foundLine = []

	# close files
	fin.close()
	fout.close()

	# remove old one and rename the new one
	remove(filename)
	move(foutName, filename)
