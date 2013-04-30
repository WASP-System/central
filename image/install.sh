#!/bin/bash

# commands here are run on the first boot of the deployed image

install_db(){
  mysql -f -uroot -pwaspsystem < $HOME/wasp/db/InitializeWaspDb.sql
  mysql -f -uroot -pwaspsystem wasp < $HOME/wasp/db/createSpringBatchTables.sql
  mysql -f -uroot -pwaspsystem wasp < $HOME/wasp/db/minimalDataLoad.sql
}

setup_ssh() {
  ssh -o StrictHostKeyChecking=no localhost exit 0
}

deploy_tomcat(){
  cd $HOME/wasp/wasp-web
  mvn tomcat:redeploy
}

export -f setup_ssh
export -f deploy_tomcat
export -f install_db

echo "Begin first time Wasp System install"
sleep 10

su wasp -c "bash -c setup_ssh"
cp -R /home/wasp/.ssh/ /usr/share/tomcat7

echo "installing wasp db"
su wasp -c "bash -c install_db"

echo "deploying to tomcat"
su wasp -c "bash -c deploy_tomcat"

echo "registering daemon"
update-rc.d wasp-daemon defaults

echo "starting daemon"
/etc/init.d/wasp-daemon start

echo "unregistering setup task"
sed -i 's:bash /root/install.sh 2>\&1 | tee /home/wasp/install.log:exit 0:' /etc/rc.local

echo "Finished."
