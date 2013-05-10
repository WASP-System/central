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
  #cd $HOME/wasp/wasp-file
  #mvn tomcat:redeploy
}

setup_gridengine() {
  qconf -au wasp users
  qconf -as localhost
  qconf -Ahgrp /dev/stdin << EOF
group_name @allhosts
hostlist NONE
EOF
  qconf -aattr hostgroup hostlist localhost @allhosts
  qconf -Aq /dev/stdin << EOF
qname                 main.q
hostlist              NONE
seq_no                0
load_thresholds       np_load_avg=1.75
suspend_thresholds    NONE
nsuspend              1
suspend_interval      00:05:00
priority              0
min_cpu_interval      00:05:00
processors            UNDEFINED
qtype                 BATCH INTERACTIVE
ckpt_list             NONE
pe_list               make
rerun                 FALSE
slots                 1
tmpdir                /tmp
shell                 /bin/csh
prolog                NONE
epilog                NONE
shell_start_mode      posix_compliant
starter_method        NONE
suspend_method        NONE
resume_method         NONE
terminate_method      NONE
notify                00:00:60
owner_list            NONE
user_lists            NONE
xuser_lists           NONE
subordinate_list      NONE
complex_values        NONE
projects              NONE
xprojects             NONE
calendar              NONE
initial_state         default
s_rt                  INFINITY
h_rt                  INFINITY
s_cpu                 INFINITY
h_cpu                 INFINITY
s_fsize               INFINITY
h_fsize               INFINITY
s_data                INFINITY
h_data                INFINITY
s_stack               INFINITY
h_stack               INFINITY
s_core                INFINITY
h_core                INFINITY
s_rss                 INFINITY
h_rss                 INFINITY
s_vmem                INFINITY
h_vmem                INFINITY
EOF
  qconf -Arqs /dev/stdin << "EOF"
{
   name         GlobalUserThrottle
   description  Keep to small usage
   enabled      TRUE
   limit        users {*} to slots=1
}
EOF
  qconf -Mc /dev/stdin << "EOF"
  arch                a          RESTRING    ==    YES         NO         NONE     0
  calendar            c          RESTRING    ==    YES         NO         NONE     0
  cpu                 cpu        DOUBLE      >=    YES         NO         0        0
  display_win_gui     dwg        BOOL        ==    YES         NO         0        0
  h_core              h_core     MEMORY      <=    YES         NO         0        0
  h_cpu               h_cpu      TIME        <=    YES         NO         0:0:0    0
  h_data              h_data     MEMORY      <=    YES         NO         0        0
  h_fsize             h_fsize    MEMORY      <=    YES         NO         0        0
  h_rss               h_rss      MEMORY      <=    YES         NO         0        0
  h_rt                h_rt       TIME        <=    YES         NO         0:0:0    0
  h_stack             h_stack    MEMORY      <=    YES         NO         0        0
  h_vmem              h_vmem     MEMORY      <=    YES         YES        1g       0
  hostname            h          HOST        ==    YES         NO         NONE     0
  load_avg            la         DOUBLE      >=    NO          NO         0        0
  load_long           ll         DOUBLE      >=    NO          NO         0        0
  load_medium         lm         DOUBLE      >=    NO          NO         0        0
  load_short          ls         DOUBLE      >=    NO          NO         0        0
  m_core              core       INT         <=    YES         NO         0        0
  m_socket            socket     INT         <=    YES         NO         0        0
  m_topology          topo       RESTRING    ==    YES         NO         NONE     0
  m_topology_inuse    utopo      RESTRING    ==    YES         NO         NONE     0
  mem_free            mf         MEMORY      <=    YES         YES        1g       0
  mem_total           mt         MEMORY      <=    YES         NO         0        0
  mem_used            mu         MEMORY      >=    YES         NO         0        0
  min_cpu_interval    mci        TIME        <=    NO          NO         0:0:0    0
  np_load_avg         nla        DOUBLE      >=    NO          NO         0        0
  np_load_long        nll        DOUBLE      >=    NO          NO         0        0
  np_load_medium      nlm        DOUBLE      >=    NO          NO         0        0
  np_load_short       nls        DOUBLE      >=    NO          NO         0        0
  num_proc            p          INT         <=    YES         YES        1        0
  qname               q          RESTRING    ==    YES         NO         NONE     0
  rerun               re         BOOL        ==    NO          NO         0        0
  s_core              s_core     MEMORY      <=    YES         NO         0        0
  s_cpu               s_cpu      TIME        <=    YES         NO         0:0:0    0
  s_data              s_data     MEMORY      <=    YES         NO         0        0
  s_fsize             s_fsize    MEMORY      <=    YES         NO         0        0
  s_rss               s_rss      MEMORY      <=    YES         NO         0        0
  s_rt                s_rt       TIME        <=    YES         NO         0:0:0    0
  s_stack             s_stack    MEMORY      <=    YES         NO         0        0
  s_vmem              s_vmem     MEMORY      <=    YES         NO         0        0
  seq_no              seq        INT         ==    NO          NO         0        0
  slots               s          INT         <=    YES         YES        1        1000
  swap_free           sf         MEMORY      <=    YES         NO         0        0
  swap_rate           sr         MEMORY      >=    YES         NO         0        0
  swap_rsvd           srsv       MEMORY      >=    YES         NO         0        0
  swap_total          st         MEMORY      <=    YES         NO         0        0
  swap_used           su         MEMORY      >=    YES         NO         0        0
  tmpdir              tmp        RESTRING    ==    NO          NO         NONE     0
  virtual_free        vf         MEMORY      <=    YES         NO         0        0
  virtual_total       vt         MEMORY      <=    YES         NO         0        0
  virtual_used        vu         MEMORY      >=    YES         NO         0        0
EOF
  qconf -aattr queue slots "4, [localhost=1]" main.q

}

export -f setup_ssh
export -f deploy_tomcat
export -f install_db
export -f setup_gridengine

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

echo "configuring SGE"
sudo -u sgeadmin qconf -am wasp
su wasp -c "bash -c setup_gridengine"
echo "-l h_vmem=1g" >> /var/lib/gridengine/default/common/sge_request

echo "unregistering setup task"
sed -i 's:bash /root/install.sh 2>\&1 | tee /home/wasp/install.log:exit 0:' /etc/rc.local

echo "Finished."
sleep 10
