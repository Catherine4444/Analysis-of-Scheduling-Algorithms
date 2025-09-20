#! /bin/bash

for r in {0..5}; do
	mkdir data
	seed=2025
	for run in {0..29}; do
		#echo "$seed"
		for algo in {0..2}; do
			#echo "$algo"
			make run ARGS="30 $algo 4 100 $seed $run 0"
		done
		seed=$((seed+1))
	done
	mv data "data_$r"
done
