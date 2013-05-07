#!/bin/bash

IMAGE=http://cdimage.debian.org/cdimage/wheezy_di_rc2/i386/iso-cd/debian-wheezy-DI-rc2-i386-netinst.iso
IMAGE_NAME=wheezy-wasp.iso
IMGLOC="$HOME/VirtualBox VMs"
NAME="wheezy-wasp"
DATE=`date +%s`
OVA="${NAME}-${DATE}.ova"

if [ ! -e $IMAGE_NAME ]; then
	echo "Run break.sh on $IMAGE"
fi

# Configure and install empty VirtualBox image

VBoxManage createvm --name $NAME --ostype Debian --register
VBoxManage modifyvm $NAME \
		   --memory 1536 \
		   --vram 1 \
		   --acpi on \
		   --ioapic off \
		   --cpus 2 \
		   --rtcuseutc on \
		   --cpuhotplug off \
		   --pae off \
		   --hwvirtex on \
		   --hwvirtexexcl on \
		   --nestedpaging on \
		   --largepages off \
		   --accelerate3d off \
		   --nic1 nat \
		   --nictype1 82540EM \
		   --natpf1 "Web interface,tcp,127.0.0.1,8001,,8001" \
		   --audio none \
		   --clipboard disabled \
		   --usb off \
		   --usbehci off \
		   --mouse ps2 \
		   --keyboard ps2 \
		   --biosbootmenu menuonly

VBoxManage storagectl $NAME --name "SATA Controller" --add sata
VBoxManage createhd --filename "${IMGLOC}/${NAME}/wasp.vdi" --size 60000 
VBoxManage storageattach $NAME \
		   --storagectl "SATA Controller" \
		   --port 0 --device 0 --type hdd \
		   --medium "${IMGLOC}/${NAME}/wasp.vdi"

VBoxManage storagectl $NAME --name "IDE Controller" --add ide
VBoxManage storageattach $NAME --storagectl "IDE Controller" --port 0 --device 0 --type dvddrive --medium $IMAGE_NAME

# Configure ports to be visible to the host

VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/ssh/Protocol" TCP
VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/ssh/GuestPort" 22
VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/ssh/HostPort" 2222

VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/http-alt/Protocol" TCP
VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/http-alt/GuestPort" 8080
VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/http-alt/HostPort" 8080

VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/wasp/Protocol" TCP
VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/wasp/GuestPort" 23532
VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/wasp/HostPort" 23532

echo begin preseed: `date`
VBoxManage startvm $NAME --type headless

i=1
while [ `ps auxww | grep VBoxHeadless | grep ${NAME} | wc -l` -gt 0 ]; do
  sleep 60
  if [ $[i % 10] -eq 0 ]; then
    echo -n ${i}
  else
    echo -n "."
  fi
  i=$[i+1]
done
echo ""
echo preseed completed: `date`

# "eject" install media

VBoxManage storageattach $NAME --storagectl "IDE Controller" --port 0 --device 0 --type dvddrive --medium emptydrive

# compress image

echo compress disk image

VBoxManage modifyhd --compact "${IMGLOC}/${NAME}/wasp.vdi"

echo compress completed: `date`

# export to "ova" format

echo export image

VBoxManage export $NAME \
  --output $OVA \
  --vsys 0 \
  --product "Wasp System Example Image" \
  --vendor "Wasp System" \
  --vendorurl "http://waspsystem.org/" \
  --version "${DATE}"

echo export completed: `date`

