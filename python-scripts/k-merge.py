import time
import heapq
from operator import itemgetter

try:
    import Queue as Q  # ver. < 3.0
except ImportError:
    import queue as Q

f1 = open("sort1", 'r')
f2 = open("sort2", 'r')
f3 = open("sort3", 'r')
f4 = open("sort4", 'r')
f5 = open("sort5", 'r')
f6 = open("sort6", 'r')
f7 = open("sort7", 'r')
f8 = open("sort8", 'r')
f9 = open("sort9", 'r')
f10 = open("sort10", 'r')
f11 = open("sort11", 'r')
f12 = open("sort12", 'r')
f13 = open("sort13", 'r')
f14 = open("sort14", 'r')
f15 = open("sort15", 'r')

filelist = [f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15]

f = open('largeindex_sorted', 'w')

flag = 0
lfile = []
count = -1
heap = []
for desc in filelist:		# for each descriptor in file list
	count += 1
	lfile = []
	for line in desc:		#reads one line from each file and pushes into a list
		if line != None:
			lfile = line.split(':')
			for i in range(len(heap)):
				if heap[i][0] == lfile[0]:
					flag = 1
					break
			if flag == 1:
				flag = 0
				continue
			lfile.append(filelist[count])
			#print lfile[0]
			#heapq.heappush(heap, lfile)
			heap.append(lfile)
			break

count = 0

temp = ''
while heap:
	heap = sorted(heap, key = itemgetter(0))      # now we extract the first element from the sorted list and write to file 
	#for i in range(len(heap)):
	#	print heap[i][0] + " ",
	#print heap[0][0]
	print >> f,heap[0][0]+':'+heap[0][1][:-1]
	#break
	try:
		nextline = heap[0][2].next()			# the file from which the line is selected, we read the next line and push in list
	except Stop:
		nextline = heap[1][2].next()
	
	l = nextline.rsplit(':', 1)
	for i in range(len(heap)):
		while heap[i][0] == l[0]:
			#print "duplicate", l[0]
			nextline = heap[0][2].next()
			l = nextline.rsplit(':', 1)
			

	#print l[0]
	l.append(heap[0][2])					
	del heap[0]
	heap.append(l)
	if count % 10000 == 0:
		print count

	count = count + 1  							#continue until list is empty
