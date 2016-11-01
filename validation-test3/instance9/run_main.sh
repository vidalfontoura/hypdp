#/bin/bash

#for i in $(seq 1 11)
#do
	#Seeds 1 to 30	
	for j in $(seq 1 30)
	do
		qsub run_hypdp_cluster.sh $j 9 600000 "( ( ( ( ( Caccept / RC ) * Cr / Caccept ) / RC * Cr ) / Caccept / RC ) * Cr ) / Caccept" "( ( ( ( ( CI / PF ) * Delta / CI ) / PF * Delta ) / CI / PF ) * Delta ) / CI"

	done
#done
