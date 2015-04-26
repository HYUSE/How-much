# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Category',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=255)),
            ],
        ),
        migrations.CreateModel(
            name='Item',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('price_r', models.IntegerField(null=True, verbose_name=b'Retail Price', blank=True)),
                ('price_w', models.IntegerField(null=True, verbose_name=b'Wholesale Price', blank=True)),
                ('unit', models.CharField(max_length=255)),
                ('price_date', models.DateField(verbose_name=b'Price Date')),
            ],
        ),
        migrations.CreateModel(
            name='Main',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=255)),
                ('category', models.ForeignKey(to='price.Category')),
            ],
        ),
        migrations.CreateModel(
            name='RegionDo',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=255)),
            ],
        ),
        migrations.CreateModel(
            name='RegionSi',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=255)),
                ('region_do', models.ForeignKey(to='price.RegionDo')),
            ],
        ),
        migrations.CreateModel(
            name='SSub',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=255)),
            ],
        ),
        migrations.CreateModel(
            name='Sub',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=255)),
                ('main', models.ForeignKey(to='price.Main')),
            ],
        ),
        migrations.CreateModel(
            name='SUser',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('iemi', models.CharField(max_length=255)),
            ],
        ),
        migrations.AddField(
            model_name='ssub',
            name='sub',
            field=models.ForeignKey(to='price.Sub'),
        ),
        migrations.AddField(
            model_name='item',
            name='category',
            field=models.ForeignKey(to='price.SSub'),
        ),
        migrations.AddField(
            model_name='item',
            name='region',
            field=models.ForeignKey(to='price.RegionSi'),
        ),
    ]
