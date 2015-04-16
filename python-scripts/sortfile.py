import re

fread = [open('unsort'+str(i), 'r') for i in range(1,16)] # list of file descriptors to be read from
fwrite = [open('sort'+str(i), 'w') for i in range(1,16)]	#list of file descriptors to be written to

for i in range(len(fwrite)):		
	for line in sorted(fread[i]):		#reads the file line by line in sorted order
		x = line.rsplit(':', 1)
		
		if x[0].isalpha():
			line = line.lower()			#converts line to lower case
			fwrite[i].write(line)		

	print "sorting of file sort", i+1, "completed\n"
