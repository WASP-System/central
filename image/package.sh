#!/bin/bash

NAME="wheezy-wasp"
GITHEAD=`git rev-parse --verify HEAD`
OVA="${NAME}-${GITHEAD:0:6}.ova"

VBoxManage modifyhd --compact wasp.vdi

VBoxManage export $NAME \
  --output $OVA \
  --vsys 0 \
  --product "Wasp System Example Image" \
  --vendor "Wasp System" \
  --vendorurl "http://waspsystem.org/" \
  --version "$GITHEAD"
