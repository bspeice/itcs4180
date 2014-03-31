from django.conf.urls import patterns, url
from rest_framework.urlpatterns import format_suffix_patterns
from scavenger import views

urlpatterns = patterns('',
		url(r'^locations/$', views.LocationList.as_view()),
		url(r'^validate/$', views.LocationResult.as_view()),
		)
