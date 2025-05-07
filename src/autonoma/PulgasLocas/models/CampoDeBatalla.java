/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import autonoma.PulgasLocas.views.VentanaPrincipal; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer; // Para los timers del juego

/**
 * Clase que representa el campo de batalla y la lógica principal del juego.
 * Gestiona pulgas, estado del juego, puntajes, timers y acciones del jugador.
 * @author Juan Jacobo Cañas Henao 
 */
public class CampoDeBatalla {
    private List<Pulga> pulgas;
    private int puntaje;
    private int puntajeMaximo;

    public static final int ANCHO_CAMPO = 1400; 
    public static final int ALTO_CAMPO = 1200;   
    // ---------------------------------------------

    // Tiempos de generación (en milisegundos)
    private static final int TIEMPO_GENERACION_NORMAL_MS = 5000; 
    private static final int TIEMPO_GENERACION_MUTANTE_MS = 10000; 
    private static final int TIEMPO_SALTO_PULGAS_MS = 3000;
    private static final int DELAY_GAME_LOOP_MS = 50; 

    // Usamos GeneradorAutomatico (Threads) para añadir pulgas
    private GeneradorAutomatico generadorNormalThread;
    private GeneradorAutomatico generadorMutanteThread;

    // Timers de Swing para saltos y game loop
    private Timer timerSaltoPulgasGlobal;
    private Timer timerGameLoop;

    private Random randomGenerator;
    private VentanaPrincipal ventana; 

    public CampoDeBatalla(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.pulgas = new ArrayList<>();
        this.randomGenerator = new Random();
        
        GestorDeSprite.precargarSprites(); // Carga las imágenes necesarias
        
        this.puntajeMaximo = GestorDeArchivos.cargarPuntajeMaximo(); 
        if (this.ventana != null) {
            this.ventana.actualizarPuntajeMaximo(this.puntajeMaximo); 
        }
        
        configurarLogicaDeJuego(); 
    }

    /**
     * Configura los Timers que controlan los saltos y el bucle principal del juego.
     */
    private void configurarLogicaDeJuego() {
        // Timer para que las pulgas salten periódicamente
        timerSaltoPulgasGlobal = new Timer(TIEMPO_SALTO_PULGAS_MS, e -> {
            if (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.EN_JUEGO) {
                saltarPulgas();
            }
        });
        
        timerGameLoop = new Timer(DELAY_GAME_LOOP_MS, e -> {
             // Solo actualiza y limpia si el juego está activo
            if (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.EN_JUEGO) {
                actualizarElementosDeJuego(DELAY_GAME_LOOP_MS); 
                limpiarPulgasMuertas();                      
            }
            if (ventana != null && (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.EN_JUEGO || EstadoDeJuego.getEstadoActual() == EstadoDeJuego.PAUSA)) {
                 ventana.repaintGamePanel();
            }
        });
    }

    /**
     * Inicia una nueva partida o reinicia la actual.
     */
    public void iniciarJuego() {
        if (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.EN_JUEGO) return;
        
        this.puntaje = 0;   // Resetea puntaje
        this.pulgas.clear(); // Limpia pulgas de la partida anterior
        EstadoDeJuego.cambiarEstado(EstadoDeJuego.EN_JUEGO); // Cambia el estado global

        if (this.ventana != null) {
            this.ventana.actualizarPuntaje(this.puntaje);
        }
        
        detenerGeneradoresDePulgas();
        generadorNormalThread = new GeneradorAutomatico(this, TIEMPO_GENERACION_NORMAL_MS, false);
        generadorNormalThread.start();
        generadorMutanteThread = new GeneradorAutomatico(this, TIEMPO_GENERACION_MUTANTE_MS, true);
        generadorMutanteThread.start();
        
        timerSaltoPulgasGlobal.start();
        timerGameLoop.start();
        System.out.println("Juego iniciado.");
    }

    /**
     * Finaliza la partida actual, detiene los procesos y guarda el puntaje máximo si aplica.
     */
    public void finalizarJuego() {
        if (EstadoDeJuego.getEstadoActual() != EstadoDeJuego.EN_JUEGO && EstadoDeJuego.getEstadoActual() != EstadoDeJuego.PAUSA) return;
        
        detenerGeneradoresDePulgas(); 
        timerSaltoPulgasGlobal.stop(); 
        timerGameLoop.stop();          

        EstadoDeJuego.cambiarEstado(EstadoDeJuego.FIN_JUEGO); 

        // Verifica y guarda puntaje máximo
        if (this.puntaje > this.puntajeMaximo) {
            this.puntajeMaximo = this.puntaje;
            GestorDeArchivos.guardarPuntaje(this.puntajeMaximo);
            if (this.ventana != null) {
                this.ventana.actualizarPuntajeMaximo(this.puntajeMaximo);
            }
        }
        
        System.out.println("Juego finalizado. Puntaje: " + this.puntaje);
        // Notifica a la ventana para mostrar el diálogo de fin de juego
        if (this.ventana != null) {
            this.ventana.mostrarDialogoFinJuego(this.puntaje);
        }
    }

    /**
     * Detiene de forma segura los hilos GeneradorAutomatico.
     */
    private void detenerGeneradoresDePulgas() {
        if (generadorNormalThread != null && generadorNormalThread.isAlive()) {
            generadorNormalThread.detenerGenerador();
        }
        if (generadorMutanteThread != null && generadorMutanteThread.isAlive()) {
            generadorMutanteThread.detenerGenerador();
        }
        try {
            if (generadorNormalThread != null) generadorNormalThread.join(100);
            if (generadorMutanteThread != null) generadorMutanteThread.join(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupción al esperar que terminen los generadores.");
        }
    }
    
    /**
     * Cambia el estado del juego a PAUSA.
     */
    public void pausarJuego() {
        if (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.EN_JUEGO) {
            EstadoDeJuego.cambiarEstado(EstadoDeJuego.PAUSA);
            System.out.println("Juego Pausado.");
        }
    }

    /**
     * Reanuda el juego si estaba en PAUSA.
     */
    public void reanudarJuego() {
        if (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.PAUSA) {
            EstadoDeJuego.cambiarEstado(EstadoDeJuego.EN_JUEGO);
            System.out.println("Juego Reanudado.");
        }
    }

    private void actualizarElementosDeJuego(long deltaTime) {
        List<Pulga> copiaPulgas = new ArrayList<>(pulgas); 
        for (Pulga p : copiaPulgas) {
            if (p.estaViva() && p.getSprite() != null) {
                p.getSprite().actualizarAnimacion(deltaTime); 
                p.actualizar(); 
            }
        }
    }

    /**
     * Elimina las pulgas que ya no están activas de la lista principal.
     */
    private void limpiarPulgasMuertas() {
        pulgas.removeIf(p -> !p.estaViva());
    }

    /**
     * Intenta agregar una pulga al campo, buscando una posición sin colisiones.
     * Añadidos mensajes de depuración.
     */
    private void agregarPulgaConColision(Pulga nuevaPulga) {
        if (nuevaPulga.getSprite() == null) {
            System.err.println("Intento de agregar pulga con sprite nulo. Abortando.");
            return;
        }
        
        int anchoInicial = nuevaPulga.getSprite().getAncho();
        int altoInicial = nuevaPulga.getSprite().getAlto();
        System.out.println("-> Intentando agregar " + nuevaPulga.getClass().getSimpleName() + 
                           " en (" + nuevaPulga.getX() + "," + nuevaPulga.getY() + ")" +
                           " con tamaño (" + anchoInicial + "x" + altoInicial + ")");
        if (anchoInicial <= 0 || altoInicial <= 0) {
             System.err.println("   ¡ADVERTENCIA! El tamaño inicial del sprite es 0 o negativo. Verifica la carga de imágenes.");
        }

        int intentosMax = 10; 
        for (int i = 0; i < intentosMax; i++) {
            boolean colisionDetectada = false;
            List<Pulga> copiaExistentes = new ArrayList<>(pulgas);
            for (Pulga existente : copiaExistentes) {
                if (existente.estaViva() && nuevaPulga.colisiona(existente)) {
                    colisionDetectada = true;
                    System.out.println("   Colisión detectada con pulga en (" + existente.getX() + "," + existente.getY() + "). Reintentando posición.");
                    nuevaPulga.setX(getPosicionAleatoriaX(anchoInicial));
                    nuevaPulga.setY(getPosicionAleatoriaY(altoInicial));
                    System.out.println("   Nueva posición tentativa: (" + nuevaPulga.getX() + "," + nuevaPulga.getY() + ")");
                     // -------------
                    break; 
                }
            }
            // Si después de verificar con todas las existentes no hubo colisión, agregarla.
            if (!colisionDetectada) {
                pulgas.add(nuevaPulga);
                System.out.println("   ¡Pulga agregada exitosamente en (" + nuevaPulga.getX() + "," + nuevaPulga.getY() + ")!");
                return; 
            }
        }
        System.out.println("   No se pudo agregar pulga sin colisión después de " + intentosMax + " intentos."); // Si después de N intentos no se encuentra lugar, no se agrega.
    }

    /**
     * Añade una PulgaNormal al juego (llamado por timer/teclado).
     */
    public void agregarPulgaNormal() {
        Sprite spriteProto = GestorDeSprite.obtenerSprite(GestorDeSprite.PULGA_NORMAL_SPRITE_KEY);
        if (spriteProto == null) {
             System.err.println("No se puede agregar PulgaNormal, sprite no encontrado.");
             return;
        }
        PulgaNormal p = new PulgaNormal(getPosicionAleatoriaX(spriteProto.getAncho()), getPosicionAleatoriaY(spriteProto.getAlto()));
        agregarPulgaConColision(p); 
    }

    /**
     * Añade una PulgaMutante al juego (llamado por timer/teclado).
     */
    public void agregarPulgaMutante() {
        Sprite spriteProto = GestorDeSprite.obtenerSprite(GestorDeSprite.PULGA_MUTANTE_SPRITE_KEY);
         if (spriteProto == null) {
             System.err.println("No se puede agregar PulgaMutante, sprite no encontrado.");
             return;
        }
        PulgaMutante p = new PulgaMutante(getPosicionAleatoriaX(spriteProto.getAncho()), getPosicionAleatoriaY(spriteProto.getAlto()));
        agregarPulgaConColision(p);
    }

    /**
     * Hace que todas las pulgas vivas salten a una nueva posición aleatoria.
     */
    public void saltarPulgas() {
        for (Pulga p : pulgas) {
            if (p.estaViva()) {
                p.saltar(ANCHO_CAMPO, ALTO_CAMPO);
            }
        }
    }

    /**
     * Aplica el efecto de la pistola en las coordenadas dadas.
     * @param x Coordenada X del clic.
     * @param y Coordenada Y del clic.
     */
    public void dispararPistola(int x, int y) {
        if (EstadoDeJuego.getEstadoActual() != EstadoDeJuego.EN_JUEGO) return;

        for (Pulga p : pulgas) { 
            if (p.estaViva() && p.contienePunto(x, y)) {
                boolean estabaVivaAntes = p.estaViva();
                p.recibirImpacto();

                if (estabaVivaAntes && !p.estaViva()) {
                    puntaje++;
                }

                if (ventana != null) ventana.actualizarPuntaje(this.puntaje);
                
                break;
            }
        }
    }

    /**
     * Aplica el efecto del misil (destruye 50% de pulgas vivas aleatoriamente).
     */
    public void lanzarMisil() {
        if (EstadoDeJuego.getEstadoActual() != EstadoDeJuego.EN_JUEGO) return;
        
        List<Pulga> vivas = new ArrayList<>();
        for(Pulga p : pulgas){
            if(p.estaViva()){
                vivas.add(p);
            }
        }
        if (vivas.isEmpty()) return;

        int numeroADestruir = (int) Math.ceil(vivas.size() * 0.5);
        java.util.Collections.shuffle(vivas); 

        int pulgasRealmenteDestruidas = 0;
        for (int i = 0; i < numeroADestruir && i < vivas.size(); i++) {
            Pulga pulgaSeleccionada = vivas.get(i);
            if(pulgaSeleccionada.estaViva()){ 
                 pulgaSeleccionada.destruir();
                 pulgasRealmenteDestruidas++;
            }
        }
        
        if(pulgasRealmenteDestruidas > 0) {
            this.puntaje += pulgasRealmenteDestruidas;
            if (ventana != null) ventana.actualizarPuntaje(this.puntaje);
        }
    }

    // --- Métodos de posición aleatoria (considerando tamaño del objeto) ---
    private int getPosicionAleatoriaX(int anchoObjeto) {
        anchoObjeto = Math.max(0, anchoObjeto); // No puede ser negativo
        if (ANCHO_CAMPO <= anchoObjeto) return 0; 
        return randomGenerator.nextInt(ANCHO_CAMPO - anchoObjeto);
    }

    private int getPosicionAleatoriaY(int altoObjeto) {
        altoObjeto = Math.max(0, altoObjeto); // No puede ser negativo
        if (ALTO_CAMPO <= altoObjeto) return 0;
        return randomGenerator.nextInt(ALTO_CAMPO - altoObjeto);
    }

    public List<Pulga> getPulgas() { 
        return new ArrayList<>(pulgas); 
    } 
    public int getPuntaje() { return puntaje; }
    public int getPuntajeMaximo() { return puntajeMaximo; }
}