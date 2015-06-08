# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('price', '0004_auto_20150501_1536'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='item',
            name='unit',
        ),
        migrations.AddField(
            model_name='item',
            name='unit_r',
            field=models.CharField(max_length=255, null=True, verbose_name=b'Retail unit', blank=True),
        ),
        migrations.AddField(
            model_name='item',
            name='unit_w',
            field=models.CharField(max_length=255, null=True, verbose_name=b'Wholesale unit', blank=True),
        ),
    ]
