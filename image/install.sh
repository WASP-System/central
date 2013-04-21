setup_mysql(){
  /usr/bin/mysqladmin -u root password 'waspsystem'
}

install_db(){
  mysql -f -uroot -pwaspsystem < $HOME/src/central/db/InitializeWaspDb.sql
  mysql -f -uroot -pwaspsystem wasp < $HOME/src/central/db/createSpringBatchTables.sql
}

deploy_tomcat(){
  cd $HOME/src/central/wasp-web
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

echo "unregistering setup task"
sed -i 's|bash /root/install.sh|exit 0|' /etc/rc.local

echo "Finished."
