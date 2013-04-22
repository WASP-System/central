#!/bin/bash

NAME="wheezy-wasp"
GITHEAD=`git rev-parse --verify HEAD`
DATE=`date +%s`
#OVA="${NAME}-${GITHEAD:0:6}.ova"
OVA="${NAME}-${DATE}.ova"
IMGLOC="$HOME/VirtualBox VMs"

VBoxManage modifyhd --compact "${IMGLOC}/${NAME}/wasp.vdi"

VBoxManage export $NAME \
  --output $OVA \
  --vsys 0 \
  --product "Wasp System Example Image" \
  --vendor "Wasp System" \
  --vendorurl "http://waspsystem.org/" \
  --version "${DATE}"
