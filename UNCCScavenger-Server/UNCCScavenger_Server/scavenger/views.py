# Create your views here.
from scavenger.models import Location
from scavenger.serializers import LocationListSerializer
from scavenger.forms import LocationValidator
from rest_framework.views import APIView
from rest_framework.response import Response

class LocationList(APIView):
	'''
	List all locations available for the scavenger hunt.
	'''
	def get(self, request, format=None):
		locations = Location.objects.all()
		locations_serializer = LocationListSerializer(locations, many=True)
		return Response(serializer.data)

class LocationResult(APIView):
	'''
	Show the result for a specific location
	'''
	def get(self, request, format=None):
		l_form = LocationValidator(data=request.data)
		if l_form.is_valid():
			location = l_form._instance
			return Response(location.result)
		else:
			return Response("You found the wrong code!", status=400)
