from django.db import models

class Location(models.Model):

	key = models.CharField(max_length=32)
	name = models.CharField(max_length=32)
	riddle = models.CharField(max_length=512)
	location = models.CharField(max_length=16)
	result = models.TextField()
