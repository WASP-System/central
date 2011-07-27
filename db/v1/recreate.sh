#!/bin/sh 


echo drop database wasp | /vol0/mysql/bin/mysql -u root --password=5n1ck3l
echo create database wasp | /vol0/mysql/bin/mysql -u root --password=5n1ck3l


/vol0/mysql/bin/mysql -u wasp --password=waspV2 wasp < ~/wasp/db/create.sql
