# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('price', '0003_auto_20150430_0059'),
    ]

    operations = [
        migrations.CreateModel(
            name='Item',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('price_r', models.IntegerField(null=True, verbose_name=b'Retail Price', blank=True)),
                ('price_w', models.IntegerField(null=True, verbose_name=b'Wholesale Price', blank=True)),
                ('unit', models.CharField(max_length=255)),
                ('price_date', models.DateField(verbose_name=b'Price Date')),
                ('category', models.ForeignKey(to='price.SSub')),
                ('region', models.ForeignKey(to='price.RegionSi')),
            ],
        ),
        migrations.RemoveField(
            model_name='item_r',
            name='category',
        ),
        migrations.RemoveField(
            model_name='item_r',
            name='region',
        ),
        migrations.RemoveField(
            model_name='item_w',
            name='category',
        ),
        migrations.RemoveField(
            model_name='item_w',
            name='region',
        ),
        migrations.DeleteModel(
            name='Item_r',
        ),
        migrations.DeleteModel(
            name='Item_w',
        ),
    ]
