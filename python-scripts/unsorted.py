import re

#the large file is first divided into 15 equal parts and each part is open in read mode
f1 = open("xaa", 'r')
f2 = open("xab", 'r')
f3 = open("xac", 'r')
f4 = open("xad", 'r')
f5 = open("xae", 'r')
f6 = open("xaf", 'r')
f7 = open("xag", 'r')
f8 = open("xah", 'r')
f9 = open("xai", 'r')
f10 = open("xaj", 'r')
f11 = open("xak", 'r')
f12 = open("xal", 'r')
f13 = open("xam", 'r')
f14 = open("xan", 'r')
f15 = open("xao", 'r')

fread = [f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15] #list of filedescriptors to read
fwrite = [open('unsort'+str(i), 'w') for i in range(1,16)] # list of file descriptors with the name of file as unsort[i] to be written

for i in range(len(fread)):  # for each open file
	for line in fread[i]:	# for each line in open file
		x = line.rsplit(':', 1)		#split the line by ':'
		if x[0].isalpha():			#check if the word contains only alphabets
			fwrite[i].write(line.lower())	#write to file

	print "file " + str(i) + "created"
