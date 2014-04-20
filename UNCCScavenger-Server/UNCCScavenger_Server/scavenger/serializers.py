from rest_framework.serializers import ModelSerializer

from models import Location

class LocationListSerializer(ModelSerializer):
	class Meta:
		model = Location
		fields = ('id', 'name', 'riddle', 'location_long', 'location_lat',
					'riddle_image_url')

class LocationResultSerializer(ModelSerializer):
	class Meta:
		model = Location
		fields = ('id', 'name', 'result')
