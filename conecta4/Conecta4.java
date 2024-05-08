/*
 * Conecta4.java
 *
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author  ribadas
 */
public class Conecta4 {
    
    private Jugador _jugador1;    
    private Jugador _jugador2;
    private Tablero _tablero;
    
    /** Creates a new instance of Conecta4 */
    public Conecta4() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        cargarArgumentos(args);

    // Crear jugadores y establecer estrategias
	// Jugador 1: personalizado

        Scanner scanner = new Scanner(System.in);

        Jugador jugador1 = crearJugador(scanner, 1);
        DEBUG("Jugador 1: maquina actual\n");

        Jugador jugador2 = crearJugador(scanner, 2);
        DEBUG("Jugador 2: maquina nueva\n");

        // Jugar
        Tablero tablero = new Tablero();
        tablero.inicializar();

        long startTime = System.currentTimeMillis();
        jugar(jugador1, jugador2, tablero);
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución: " + (endTime - startTime) + "ms");
                
        // Mostrar resultados
        tablero.mostrar();
        if (tablero.hayEmpate()) {
            System.out.println("RESULTADO: Empate");
        }
        if (tablero.ganaJ1()){
            System.out.println("RESULTADO: Gana jugador 1");
        }
        if (tablero.ganaJ2()){
            System.out.println("RESULTADO: Gana jugador 2");
        }
        System.exit(1);
    }

    private static void cargarArgumentos(String[] args) {
        // procesar parametros de linea de comandos
    }

    private static void jugar(Jugador jugador1, Jugador jugador2, Tablero tablero) {
        ArrayList<Long> movimientosJugador1 = new ArrayList<Long>();
        ArrayList<Long> movimientosJugador2 = new ArrayList<Long>();
        int turno=0;
        Jugador jugadorActual;
        int movimiento;
        boolean posicionesPosibles[];
        
       // comprobar tablero: necesario para establecer si es o no un tablero final
        tablero.obtenerGanador();
        while(!tablero.esFinal()){
            turno++;
            // establecer jugador del turno actual
            if ((turno%2) == 1) { // turno impar -> jugador1
                jugadorActual = jugador1;
            }
            else {// turno par -> jugador2
                jugadorActual = jugador2;
            }
            // obtener movimiento: llama al jugador que tenga el turno,
	    // que, a su vez, llamará a la estrategia que se le asigno al crearlo
            long startTime = System.currentTimeMillis();
            movimiento = jugadorActual.obtenerJugada(tablero);
            long endTime = System.currentTimeMillis();
            if ((turno%2) == 1) { // turno impar -> jugador1
                movimientosJugador1.add(endTime - startTime);
            }
            else {// turno par -> jugador2
                movimientosJugador2.add(endTime - startTime);
            } 
            // comprobar si es correcto
            if ((movimiento>=0) && (movimiento<Tablero.NCOLUMNAS)) {
                posicionesPosibles = tablero.columnasLibres();
                if (posicionesPosibles[movimiento]) {
                    tablero.anadirFicha(movimiento, jugadorActual.getIdentificador());
                    // comprobar ganador
                    tablero.obtenerGanador();
                }
                else {
                    ERROR_FATAL("Columna completa. Juego Abortado.");
                }
            }
            else {
              ERROR_FATAL("Movimiento invalido. Juego Abortado.");
            }    
        }

        long suma = 0;
        for (long valor : movimientosJugador1) {
            suma += valor;
        }
        double media = (double) suma / movimientosJugador1.size();
        System.out.println("Tiempo medio de movimiento para jugador 1: " + media + "ms");
        suma = 0;
        for (long valor : movimientosJugador2) {
            suma += valor;
        }
        media = (double) suma / movimientosJugador2.size();
        System.out.println("Tiempo medio de movimiento para jugador 2: " + media + "ms");
        
    }

    private static Double[] preguntarPesos(Scanner scanner, int jugador) {
        ArrayList<Double> pesos = new ArrayList<Double>();
        String res;
        Double peso;

        System.out.println("Introduzca los pesos para el jugador "+jugador);
        do {
            res = scanner.nextLine();
            peso = Double.parseDouble(res);
            pesos.add(peso);
        } while (pesos.size() < 4);

        return pesos.toArray(new Double[pesos.size()]);
    }

    private static Jugador crearJugador(Scanner scanner, int id) {
        System.out.println("Introduzca el modo de juego(persona/aleatorio/ponderado/noAlfaBeta):");
        String modo = modoDeJuego(scanner);
        while (!modo.equals("persona") && !modo.equals("aleatorio") && !modo.equals("ponderado") && !modo.equals("noAlfaBeta")) {
            System.out.println("Introduzca el modo de juego(persona/aleatorio/ponderado/noAlfaBeta):");
            modo = modoDeJuego(scanner);
        }

        Jugador jugador = new Jugador(id);
        if (modo.equals("persona")) {
            jugador.establecerEstrategia(new EstrategiaHumano());
        } else if (modo.equals("ponderado")) {
            Double[] pesos = preguntarPesos(scanner, id);
            System.out.println("Introduzca la capa maxima:");
            int capaMaxima = scanner.nextInt();
            jugador.establecerEstrategia(new EstrategiaMinMaxAlfaBeta(
                capaMaxima,
                new EvaluadorPonderado(pesos)
            ));
        } else if (modo.equals("aleatorio")){
            System.out.println("Introduzca la capa maxima:");
            int capaMaxima = scanner.nextInt();
            jugador.establecerEstrategia(new EstrategiaMinMaxAlfaBeta(
                capaMaxima,
                new EvaluadorAleatorio()
            ));
        } else {
            System.out.println("Introduzca la capa maxima:");
            int capaMaxima = scanner.nextInt();
            System.out.println("Introduzca el evaluador(aleatorio/ponderado):");
            String evaluador = scanner.nextLine();
            while (!evaluador.equals("aleatorio") && !evaluador.equals("ponderado")){
                System.out.println("Introduzca el evaluador(aleatorio/ponderado):");
                evaluador = scanner.nextLine();
            }

            if (evaluador.equals("ponderado")) {
                Double[] pesos = preguntarPesos(scanner, id);
                jugador.establecerEstrategia(new EstrategiaMiniMax(
                    capaMaxima,
                    new EvaluadorPonderado(pesos)
                ));
            } else {
                jugador.establecerEstrategia(new EstrategiaMiniMax(
                    capaMaxima,
                    new EvaluadorAleatorio()
                ));
            }
        }

        return jugador;
    }

    private static String modoDeJuego(Scanner scanner) {
        return scanner.nextLine();
    }
      
    public static final void ERROR_FATAL(java.lang.String mensaje) {
        System.out.println("ERROR FATAL\n\t"+mensaje);
        System.exit(0); // Finalizar aplicacion
    }
    
    public static final void DEBUG(String str) {
        System.out.print("DBG:"+str);
    }
    
    public static final void ERROR(java.lang.String mensaje) {
        System.out.println("ERROR\n\t"+mensaje);
    }
    
}
