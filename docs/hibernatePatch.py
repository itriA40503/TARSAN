#-----------------------------------------------------------------------------
# Name: hibernatePatch.py
# Purpose: Insert auto-increment configuration that Hibernate codegen loses
#
# Author: 990388 <Norman Huang>
#
# Created: 2012/03/26
#
# Usage: python hibernatePatch.py
# Usage of p2exe: python setup.py py2exe
#-----------------------------------------------------------------------------
#
#Modified by Paul Shih (A20365) on 04/14/2015
# 
#<BEFORE MODIFICATION>
#LINE74: fout.write(''.join('\t@SequenceGenerator(name="' + name + '", sequenceName="' + seqname + '")\r'))
#LINE75: fout.write(''.join('\t@GeneratedValue(generator="' + name + '")\r'))
#
#<AFTER MODIFICATION>
#LINE74: fout.write(''.join('\t@SequenceGenerator(name="' + name + '", sequenceName="' + seqname + '", allocationSize=1)\r'))
#LINE75: fout.write(''.join('\t@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="' + name + '")\r'))
#LINE61: fout.write('import javax.persistence.GenerationType;\r')

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
	
	lookup = '@Id'
	lookup2 = '@EmbeddedId'
	with open(filename) as myFile:
		for num, line in enumerate(myFile, 1):
			if lookup in line:
				print filename, " found at #", num
				foundLine = num
			if lookup2 in line:
				print filename, " found another token at #", num
				foundLine = num
	
	if foundLine > 0: 
		for line in fin:
			counter+=1
			if re.search("// Generated", line):
				fout.write(line)
				line = fin.next()
				fout.write('import javax.persistence.GeneratedValue;\r')
				fout.write('import javax.persistence.SequenceGenerator;\r')
				fout.write('import javax.persistence.GenerationType;\r')			
			if re.search("@Entity", line):
				fout.write('@SuppressWarnings("serial")\r')
			if re.search("@Embeddable", line):
				fout.write('@SuppressWarnings("serial")\r')
			if re.search("@Table", line):
				classname = shlex.split(line)[2][:-1]
				classname2 = classname
				if classname == 'analyzed_license' or classname == 'camera_graph':
					classname2 = classname.replace("_", "")
			if (foundLine-2)==counter:
				print "Added @Id in ", filename
				fout.write(line)
				name = ''.join(classname + "_seq")
				seqname = ''.join(classname + "_" + classname2 + "_id_seq")
				fout.write(''.join('\t@SequenceGenerator(name="' + name + '", sequenceName="' + seqname + '", allocationSize=1)\r'))
				fout.write(''.join('\t@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="' + name + '")\r'))
			else: 
				fout.write(line)

	if foundLine == 0:
		for line in fin:
			fout.write(line)
	
	# reset variables
	counter = 0
	foundLine = 0

	# close files
	fin.close()
	fout.close()

	# remove old one and rename the new one
	remove(filename)
	move(foutName, filename)
