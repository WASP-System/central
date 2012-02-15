#!/bin/sh 


echo drop database wasp | mysql -u root --password=2011season
echo create database wasp | mysql -u root --password=2011season

mysql -u wasp --password=waspV2 wasp < create.sql
mysql -u wasp --password=waspV2 wasp < spring_batch_init.sql
mysql -u wasp --password=waspV2 wasp < ../wasp-core/src/main/resources/uifield.update.sql
 

