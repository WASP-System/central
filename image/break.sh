#!/bin/bash
# modified from http://wiki.debian.org/DebianInstaller/Preseed/EditIso
# run on linux as root

if [ ! -e ./preseed.cfg ]; then
  echo "can not proceed without preseed.cfg";
  exit 1
fi

mkdir -p loopdir
mount -o loop $1 loopdir
rm -rf cd
mkdir cd
rsync -a -H --exclude=TRANS.TBL loopdir/ cd
umount loopdir

mkdir irmod
cd irmod
gzip -d < ../cd/install.386/initrd.gz | \
        cpio --extract --verbose --make-directories --no-absolute-filenames
cp ../preseed.cfg preseed.cfg
find . | cpio -H newc --create --verbose | \
        gzip -9 > ../cd/install.386/initrd.gz
cd ../
rm -fr irmod/

# make media autobooting (don't put this iso in a real machine!)

cd cd
cat >> isolinux/isolinux.cfg << EOF

label default automatic
menu label ^Automated install - headless
kernel /install.386/vmlinuz
append auto=true priority=critical vga=788 initrd=/install.386/initrd.gz -- quiet 
EOF
sed -i 's/timeout 0/timeout 10/g' isolinux/isolinux.cfg

# fix md5s and make a new iso

md5sum `find -follow -type f` > md5sum.txt
cd ..

mkisofs -o wheezy-wasp.iso -r -J -no-emul-boot -boot-load-size 4 \
 -boot-info-table -b isolinux/isolinux.bin -c isolinux/boot.cat ./cd

rm -rf cd loopdir
