#/bin/bash

#for i in $(seq 1 11)
#do
	#Seeds 1 to 30	
	for j in $(seq 1 30)
	do
		qsub run_hypdp_cluster.sh $j 4 600000 "RC * Ccurrent * Cava - Cr" "( ( TI / Delta ) / ( ( Delta * ( ( TI / Delta ) / CI ) * Delta / Delta * TI ) - CI ) )"

	done
#done
