from rest_framework.serializers import ModelSerializer

from models import Location

class LocationListSerializer(ModelSerializer):
	class Meta:
		model = Location
		fields = ('id', 'name', 'riddle', 'location')

class LocationResultSerializer(ModelSerializer):
	class Meta:
		model = Location
		fields = ('id', 'name', 'result')