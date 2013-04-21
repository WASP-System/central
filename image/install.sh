setup_mysql(){
  /usr/bin/mysqladmin -u root password 'waspsystem'
}

install_db(){
  mysql -f -uroot -pwaspsystem < $HOME/wasp/db/InitializeWaspDb.sql
  mysql -f -uroot -pwaspsystem wasp < $HOME/wasp/db/createSpringBatchTables.sql
  mysql -f -uroot -pwaspsystem wasp < $HOME/wasp/db/minimalDataLoad.sql
}

deploy_tomcat(){
  cd $HOME/wasp/wasp-web
  mvn tomcat:redeploy
}


export -f deploy_tomcat
export -f install_db

echo "Begin first time setup"

echo "setting up mysql"
setup_mysql

echo "installing wasp db"
su wasp -c "bash -c install_db"

echo "deploying to tomcat"
su wasp -c "bash -c deploy_tomcat"

echo "registering daemon"
update-rc.d wasp-daemon defaults

echo "starting daemon"
/etc/init.d/wasp-daemon start

echo "unregistering setup task"
sed -i 's|bash /root/install.sh|exit 0|' /etc/rc.local

echo "Finished."
