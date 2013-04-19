#!/bin/bash
# modified from http://wiki.debian.org/DebianInstaller/Preseed/EditIso
# run on linux as root
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

cd cd
md5sum `find -follow -type f` > md5sum.txt
cd ..

mkisofs -o wheezy-wasp.iso -r -J -no-emul-boot -boot-load-size 4 \
 -boot-info-table -b isolinux/isolinux.bin -c isolinux/boot.cat ./cd

rm -rf cd loopdir
