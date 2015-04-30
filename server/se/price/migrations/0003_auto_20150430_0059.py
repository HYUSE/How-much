# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('price', '0002_auto_20150429_1908'),
    ]

    operations = [
        migrations.CreateModel(
            name='Item_r',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('price', models.IntegerField(null=True, verbose_name=b'Retail Price', blank=True)),
                ('unit', models.CharField(max_length=255)),
                ('price_date', models.DateField(verbose_name=b'Price Date')),
                ('category', models.ForeignKey(to='price.SSub')),
                ('region', models.ForeignKey(to='price.RegionSi')),
            ],
        ),
        migrations.CreateModel(
            name='Item_w',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('price', models.IntegerField(null=True, verbose_name=b'Wholesale Price', blank=True)),
                ('unit', models.CharField(max_length=255)),
                ('price_date', models.DateField(verbose_name=b'Price Date')),
                ('category', models.ForeignKey(to='price.SSub')),
                ('region', models.ForeignKey(to='price.RegionSi')),
            ],
        ),
        migrations.RemoveField(
            model_name='item',
            name='category',
        ),
        migrations.RemoveField(
            model_name='item',
            name='region',
        ),
        migrations.DeleteModel(
            name='Item',
        ),
    ]
