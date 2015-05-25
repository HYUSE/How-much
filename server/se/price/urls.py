# 2012036774
# Kim Haryeong
from django.conf.urls import include, url
from . import views

urlpatterns = [
	url(r'^$', views.index, name='index'),
	url(r'^auto_complete/$', views.auto_complete, name='auto_complete'),
	url(r'^result/$', views.result, name='result'),
	url(r'^category/$', views.category, name='category'),
	url(r'^main/$', views.main, name='main'),
	url(r'^sub/$', views.sub, name='sub'),
	url(r'^home/$', views.home, name='home'),

]