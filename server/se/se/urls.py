# 2012036774
# Kim Haryeong
from django.conf.urls import include, url
from django.contrib import admin

urlpatterns = [
    # Examples:
    # url(r'^$', 'se.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),
    url(r'^price/', include('price.urls')),
    url(r'^admin/', include(admin.site.urls)),
]
