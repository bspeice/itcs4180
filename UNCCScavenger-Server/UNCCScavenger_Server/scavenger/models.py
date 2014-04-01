from django.db import models

class Location(models.Model):

	key = models.CharField(max_length=32)
	name = models.CharField(max_length=32)
	riddle = models.CharField(max_length=512)
	riddle_image_url = models.URLField()

	location_long = models.DecimalField()
	location_lat = models.DecimalField()
	result = models.TextField()
