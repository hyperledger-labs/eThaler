#!/bin/sh


#Copyright 2020 Swapshub
#Licensed under the Apache License, Version 2.0 (the "License");
#you may not use this file except in compliance with the License.
#You may obtain a copy of the License at
#http://www.apache.org/licenses/LICENSE-2.0


ARG=""

echo 'Starting eThalerClient'
START_DIR=`pwd`

#change directory
cd build/runeThalerClient

if [ $# -eq 1 ]
then
	ARG=$1
	./eThalerClient $START_DIR $ARG
else
     ./eThalerClient $START_DIR 
fi



