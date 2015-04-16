f1 = open('largeindex_sorted', 'r')
f2 = open('primaryindex', 'w')

byte_count = 0
line_count = 0
count = 0

for line in f1:
	count += 1
	#line_count += 1
	#if count == 1000:
	l = line.rsplit(':', 1)
	print >> f2, l[0] + ' ' + str(byte_count)
	#count = 0
	byte_count += len(line)
	if count % 10000 == 0:
		print count

f1.close()
f2.close()

