#! /bin/bash

for r in {0..5}; do
	mkdir data
	seed=2025
	for run in {0..29}; do
		quantum=20
		#echo "$seed"
		for q in {1..19}; do
			#echo "$quantum"
			make run ARGS="30 2 4 $quantum $seed $run 1"
			quantum=$((quantum+10))

		done
		seed=$((seed+1))
	done
	mv data "data_$r"
done
