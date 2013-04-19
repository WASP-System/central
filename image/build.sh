#!/bin/bash

IMAGE=http://cdimage.debian.org/cdimage/wheezy_di_rc1/i386/iso-cd/debian-wheezy-DI-rc1-i386-netinst.iso
IMAGE_NAME=wheezy-wasp.iso
IMGLOC="$HOME/VirtualBox VMs"
NAME=wheezy-wasp

if [ ! -e $IMAGE_NAME ]; then
	echo "Run break.sh on $IMAGE"
fi

VBoxManage createvm --name $NAME --ostype Debian --register
VBoxManage modifyvm $NAME \
		   --memory 1024 \
		   --vram 1 \
		   --acpi on \
		   --ioapic off \
		   --cpus 1 \
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

VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/ssh/Protocol" TCP
VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/ssh/GuestPort" 22
VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/ssh/HostPort" 2222

VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/http-alt/Protocol" TCP
VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/http-alt/GuestPort" 8080
VBoxManage setextradata $NAME "VBoxInternal/Devices/e1000/0/LUN#0/Config/http-alt/HostPort" 8888
