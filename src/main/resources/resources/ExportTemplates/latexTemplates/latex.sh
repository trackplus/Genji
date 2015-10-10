#!/bin/bash
if [ -e /usr/texbin/xelatex ]
then
   export PATH=/usr/texbin:$PATH
else
   if [ -e /usr/bin/xelatex ]
   then
      export PATH=/usr/texbin:$PATH
   fi
fi

# export PATH=/usr/local/texlive/2014/bin/x86_64-linux:$PATH

for i in $1 $2 $3 $4 $5
do
   case "$i" in
      -output-directory*)
          export WORKDIR=`echo $i | sed -e 's/^[^=]*=//g'`
          ;;
      *)
          ;;
   esac
done

cd $WORKDIR

xelatex $1 $2 $3 $4 $5  > stdout.txt 2> stderr.txt

