#!/usr/bin/python
# -*- coding: UTF-8 -*-
a = 21
b = 10
c = 0

c = a + b
print "a + b =", c

c = a - b
print "a - b =", c

c = a * b
print "a * b =", c

c = a / b
print "a / b =", c

c = a % b
print "a % b =", c

c = a ** b
print "a ** b =", c

count = 0
while (count < 9):
    print 'The count is:',count
    count = count + 1
print "Good bye!"

for letter in "Python":
    print '当前字母:', letter