from scavenger.models import Location

from django import forms
from django.core.exceptions import ObjectDoesNotExist

class LocationValidator(forms.ModelForm):
	'''
	Validate that the user actually found a location
	'''

	def is_valid(self):
		valid = super(LocationValidator, self).is_valid()
		if not valid:
			return valid

		# Make sure the key and ID submitted match
		try:
			self._instance = Location.objects.get(id=self.cleaned_data['id'],
					key=self.cleaned_data['key'])
			return True
		except ObjectDoesNotExist, e:
			return False
		
	class Meta:
		model = Location
		fields = ('id', 'key')
