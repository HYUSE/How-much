# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('price', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='suser',
            old_name='iemi',
            new_name='device_id',
        ),
    ]
