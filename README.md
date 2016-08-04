# v1 -> This version adds the CustomHH class which is very similar with a genetic algorithm. The Results for the instance sq4 are listed below
	
	
	
# v2 ->
	1 -> Inicializa o mecanismo de memória com backtrack (12 posições)
	2 -> Seleciona uma solução do mecanismo de memória de maneira aleatória
	3 -> Copia ela para a ultima posição do mecanismo (index=11)
	4 -> Executa todas as heuristicas de baixo nível em cima da solução do index=11
	5 -> Equanto o tempo limite não expirar.
		5.1 -> Seleciona uma heuristica de baixo nível com base no mecanismo de seleção que foi provido.
		5.2 -> Verifica se a heuristicas de baixo nível é um crossover, 
				caso sim irá obter uma segunda solução do mecanismo de memória (!= index=11) para fazer parte do crossover
		5.3 -> Aplica a heuristica de baixo nível e copia a solução gerada para a posição (index=10)
		5.4 -> O delta é calculado para verificar se houve melhoria na solução corrente (index=11)
		5.5 -> Caso o delta > zero a solução gerada irá substituir a solução corrente  (index=10 irá substituir a solução do index=11)
		5.6 -> Se delta == 0, a solução corrente sera feita um backup no index = 9 e a solução corrente (index=11) irá ser substituida pela gerada (index=10)
	6 -> Passo 5 irá ser executado até que a condição limite seja atingida
		
	
	  
	  

10 indiviudos
50 geracoes

fitness
1 rodada por instancia, 3 instancias 40 segundos cada


