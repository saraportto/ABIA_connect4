import java.util.Arrays;
import java.util.Random;

public class EstrategiaMiniMax extends Estrategia {
    /* Estrategia que implementa una busqueda MINIMAX
     * 
     * Los parametros de la busqueda (funcion de evaluacion + cota máxima)
     * se establecen al crear el objeto o con las funciones
     * "establecerEvaluador()" y "establecerCapaMaxima()"
     */
     
    private Evaluador _evaluador;
    private int _capaMaxima;
    private int _jugadorMAX;
   
    /** Creates a new instance of EstrategiaMiniMax */
    public EstrategiaMiniMax() {
    }
    
    public EstrategiaMiniMax(int capaMaxima, Evaluador evaluador) {
       this.establecerEvaluador(evaluador);
       this.establecerCapaMaxima(capaMaxima);

    }

    public int buscarMovimiento(Tablero tablero, int jugador) {

        boolean movimientosPosibles[] = tablero.columnasLibres();
        Tablero nuevoTablero;
        int col;
        double valorSucesor;
        int[] mejoresPosiciones={-1};  // Movimiento nulo
        double mejorValor=Evaluador.MINIMO; // Minimo  valor posible
        Random rand = new Random();

        _jugadorMAX = jugador; 

        for (col=0; col<Tablero.NCOLUMNAS; col++) {
            if (movimientosPosibles[col]) { //se puede añadir ficha en columna
                // crear nuevo tablero y comprobar ganador
                nuevoTablero = (Tablero) tablero.clone();
                nuevoTablero.anadirFicha(col,jugador);
                nuevoTablero.obtenerGanador();

                valorSucesor = MINIMAX(nuevoTablero, Integer.MIN_VALUE, Integer.MAX_VALUE, Jugador.alternarJugador(jugador),1);                
                nuevoTablero = null;
                
                // tomar mejor valor
                if (valorSucesor > mejorValor) {
                    mejorValor = valorSucesor;
                    mejoresPosiciones = new int[1];
                    mejoresPosiciones[0] = col;
                } else if (valorSucesor == mejorValor) {
                    int[] aux = new int[mejoresPosiciones.length+1];
                    System.arraycopy(mejoresPosiciones, 0, aux, 0, mejoresPosiciones.length);
                    aux[mejoresPosiciones.length] = col;
                    mejoresPosiciones = aux;
                    aux = null;
                }
            }
        }

        // seleccionar uno de los mejores movimientos de forma aleatoria
        if (mejoresPosiciones.length > 1) {
            int indiceAleatorio = rand.nextInt(mejoresPosiciones.length);
            while (mejoresPosiciones[indiceAleatorio] == -1) {
                indiceAleatorio = rand.nextInt(mejoresPosiciones.length);
            }
            return mejoresPosiciones[indiceAleatorio];
        } else {
            return mejoresPosiciones[0];
        }
    }
    
    
    public double MINIMAX(Tablero tablero, double alpha, double beta, int jugador, int capa) {
        // Implementa la propagación de valores MINIMAX propiamente dicha
	// a partir del segundo nivel (capa 1)
       
        // Casos base
        if (tablero.hayEmpate()) {
            return(0);
        }
	// la evaluacion de posiciones finales (caso base recursididad)
	// se hace SIEMPRE desde la prespectiva de MAX
	// -> se usa el identificador del jugador MAX (1 o 2) guardado
	//    en la llamada a buscarMovimiento()
        if (tablero.esGanador(_jugadorMAX)){ // gana MAX
            return(Evaluador.MAXIMO);
        }
        if (tablero.esGanador(Jugador.alternarJugador(_jugadorMAX))){ // gana el otro
            return(Evaluador.MINIMO);
        }
        if (capa == (_capaMaxima)) { // alcanza nivel maximo
            //return(_evaluador.valoracion(tablero, _jugadorMAX));
            double valor = 0.0;
            valor = _evaluador.valoracion(tablero, _jugadorMAX);
            return((double)valor);
        }

       // Recursividad sobre los sucesores
        boolean movimientosPosibles[] = tablero.columnasLibres();
        Tablero nuevoTablero;
        int col;
        double alpha_actual, beta_actual, aux;

        if (esCapaMIN(capa)) {
           beta_actual = beta;
            aux = Integer.MAX_VALUE;
            for (col=0; col<Tablero.NCOLUMNAS; col++) {
                if (movimientosPosibles[col]) { //se puede añadir ficha en columna
                    // crear nuevo tablero y comprobar ganador
                    nuevoTablero = (Tablero) tablero.clone();
                    nuevoTablero.anadirFicha(col,jugador);
                    nuevoTablero.obtenerGanador();

                    if (beta_actual <= alpha) {
                        break;
                    }
                    else {
                        aux = minimo2(aux, MINIMAX(nuevoTablero, alpha, beta_actual, Jugador.alternarJugador(jugador), (capa+1)));
                        beta_actual = minimo2(beta_actual, aux);
                    }
                }
            }
        }
        else {
            alpha_actual = alpha;
            aux = Integer.MIN_VALUE;
            for (col=0; col<Tablero.NCOLUMNAS; col++) {
                if (movimientosPosibles[col]) { //se puede añadir ficha en columna
                    // crear nuevo tablero y comprobar ganador
                    nuevoTablero = (Tablero) tablero.clone();
                    nuevoTablero.anadirFicha(col,jugador);
                    nuevoTablero.obtenerGanador();

                    if (alpha_actual >= beta) {
                        break;
                    }
                    else {
                        aux = maximo2(aux, MINIMAX(nuevoTablero, alpha_actual, beta, Jugador.alternarJugador(jugador), (capa+1)));
                        alpha_actual = maximo2(alpha_actual, aux);
                    }
                }
            }
        }
        return(aux);
    }
    
    public void establecerCapaMaxima(int capaMaxima) {
        _capaMaxima = capaMaxima;
    }
   
    public void establecerEvaluador(Evaluador evaluador) {
        _evaluador = evaluador;
    }

    public Evaluador obtenerEvaluador() {
        return(_evaluador);
    }

    private static final boolean esCapaMIN(int capa) {
        return((capa % 2)==1); // es impar
    }

    private static final double maximo2(double v1, double v2) {
        if (v1 > v2)
            return(v1);
        else
            return(v2);
    }
    
    private static final double minimo2(double v1, double v2) {
        if (v1 < v2)
            return(v1);
        else
            return(v2);    
    }
    
}  // Fin clase EstartegiaMINIMAX
