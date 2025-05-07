/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import autonoma.PulgasLocas.views.VentanaPrincipal; // Para interactuar con la UI
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer; // Para los timers del juego

/**
 * Clase que representa el campo de batalla y la lógica principal del juego.
 * Gestiona pulgas, estado del juego, puntajes, timers y acciones del jugador.
 * @author Juan Jacobo Cañas Henao & AI adaptaciones
 */
public class CampoDeBatalla {
    private List<Pulga> pulgas;
    private int puntaje;
    private int puntajeMaximo;

    // --- TAMAÑO DEL CAMPO DE JUEGO (MODIFICADO) ---
    public static final int ANCHO_CAMPO = 1400; // Nuevo ancho (ejemplo)
    public static final int ALTO_CAMPO = 1200;   // Nuevo alto (ejemplo)
    // ---------------------------------------------

    // Tiempos de generación (en milisegundos)
    private static final int TIEMPO_GENERACION_NORMAL_MS = 5000; 
    private static final int TIEMPO_GENERACION_MUTANTE_MS = 10000; 
    private static final int TIEMPO_SALTO_PULGAS_MS = 3000;
    private static final int DELAY_GAME_LOOP_MS = 50; // Aproximadamente 20 FPS

    // Usamos GeneradorAutomatico (Threads) para añadir pulgas
    private GeneradorAutomatico generadorNormalThread;
    private GeneradorAutomatico generadorMutanteThread;

    // Timers de Swing para saltos y game loop
    private Timer timerSaltoPulgasGlobal;
    private Timer timerGameLoop;

    private Random randomGenerator;
    private VentanaPrincipal ventana; // Referencia a la UI

    /**
     * Constructor de CampoDeBatalla.
     * @param ventana Referencia a la VentanaPrincipal para comunicación UI.
     */
    public CampoDeBatalla(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.pulgas = new ArrayList<>();
        this.randomGenerator = new Random();
        
        GestorDeSprite.precargarSprites(); // Carga las imágenes necesarias
        
        this.puntajeMaximo = GestorDeArchivos.cargarPuntajeMaximo(); // Carga el puntaje máximo guardado
        if (this.ventana != null) {
            this.ventana.actualizarPuntajeMaximo(this.puntajeMaximo); // Actualiza la UI
        }
        
        configurarLogicaDeJuego(); // Configura los timers
    }

    /**
     * Configura los Timers que controlan los saltos y el bucle principal del juego.
     */
    private void configurarLogicaDeJuego() {
        // Timer para que las pulgas salten periódicamente
        timerSaltoPulgasGlobal = new Timer(TIEMPO_SALTO_PULGAS_MS, e -> {
            // Solo salta si el juego está activo
            if (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.EN_JUEGO) {
                saltarPulgas();
            }
        });
        
        // Timer principal del Game Loop
        timerGameLoop = new Timer(DELAY_GAME_LOOP_MS, e -> {
             // Solo actualiza y limpia si el juego está activo
            if (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.EN_JUEGO) {
                actualizarElementosDeJuego(DELAY_GAME_LOOP_MS); // Actualiza animaciones (pasa tiempo delta)
                limpiarPulgasMuertas();                       // Remueve pulgas inactivas
            }
            // Siempre repinta si el juego está en pantalla (jugando o pausado)
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

        // Actualiza la UI
        if (this.ventana != null) {
            this.ventana.actualizarPuntaje(this.puntaje);
        }
        
        // Detiene generadores anteriores (si es un reinicio) y crea/inicia los nuevos
        detenerGeneradoresDePulgas();
        generadorNormalThread = new GeneradorAutomatico(this, TIEMPO_GENERACION_NORMAL_MS, false);
        generadorNormalThread.start();
        generadorMutanteThread = new GeneradorAutomatico(this, TIEMPO_GENERACION_MUTANTE_MS, true);
        generadorMutanteThread.start();
        
        // Inicia los timers de Swing
        timerSaltoPulgasGlobal.start();
        timerGameLoop.start();
        System.out.println("Juego iniciado.");
    }

    /**
     * Finaliza la partida actual, detiene los procesos y guarda el puntaje máximo si aplica.
     */
    public void finalizarJuego() {
        if (EstadoDeJuego.getEstadoActual() != EstadoDeJuego.EN_JUEGO && EstadoDeJuego.getEstadoActual() != EstadoDeJuego.PAUSA) return;
        
        detenerGeneradoresDePulgas(); // Detiene los hilos de generación
        timerSaltoPulgasGlobal.stop(); // Detiene el timer de saltos
        timerGameLoop.stop();          // Detiene el game loop

        EstadoDeJuego.cambiarEstado(EstadoDeJuego.FIN_JUEGO); // Cambia estado global

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
        // Esperar un poco a que terminen (opcional)
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

    /**
     * Actualiza el estado de los elementos del juego (llamado por el game loop).
     * @param deltaTime Tiempo transcurrido desde la última actualización (en ms).
     */
    private void actualizarElementosDeJuego(long deltaTime) {
        // Crear copia para evitar problemas si la lista se modifica mientras se itera
        List<Pulga> copiaPulgas = new ArrayList<>(pulgas); 
        for (Pulga p : copiaPulgas) {
            if (p.estaViva() && p.getSprite() != null) {
                // Actualiza la animación del sprite basado en el tiempo
                p.getSprite().actualizarAnimacion(deltaTime); 
                // Llama a la lógica de actualización de la pulga (si tuviera, ej. movimiento)
                p.actualizar(); 
            }
        }
    }

    /**
     * Elimina las pulgas que ya no están activas de la lista principal.
     */
    private void limpiarPulgasMuertas() {
        // Usar removeIf (Java 8+) que maneja correctamente la eliminación durante la iteración
        pulgas.removeIf(p -> !p.estaViva());
    }

    /**
     * Intenta agregar una pulga al campo, buscando una posición sin colisiones.
     * Añadidos mensajes de depuración.
     * @param nuevaPulga La pulga a agregar.
     */
    private void agregarPulgaConColision(Pulga nuevaPulga) {
        if (nuevaPulga.getSprite() == null) {
            System.err.println("Intento de agregar pulga con sprite nulo. Abortando.");
            return;
        }
        
        // --- DEBUG ---
        int anchoInicial = nuevaPulga.getSprite().getAncho();
        int altoInicial = nuevaPulga.getSprite().getAlto();
        System.out.println("-> Intentando agregar " + nuevaPulga.getClass().getSimpleName() + 
                           " en (" + nuevaPulga.getX() + "," + nuevaPulga.getY() + ")" +
                           " con tamaño (" + anchoInicial + "x" + altoInicial + ")");
        if (anchoInicial <= 0 || altoInicial <= 0) {
             System.err.println("   ¡ADVERTENCIA! El tamaño inicial del sprite es 0 o negativo. Verifica la carga de imágenes.");
        }
        // -------------

        int intentosMax = 10; // Máximo de intentos para encontrar lugar
        for (int i = 0; i < intentosMax; i++) {
            boolean colisionDetectada = false;
            // Crear copia de la lista actual para iterar de forma segura
            List<Pulga> copiaExistentes = new ArrayList<>(pulgas);
            for (Pulga existente : copiaExistentes) {
                // Solo chequear colisión con pulgas vivas
                if (existente.estaViva() && nuevaPulga.colisiona(existente)) {
                    colisionDetectada = true;
                    // --- DEBUG ---
                    System.out.println("   Colisión detectada con pulga en (" + existente.getX() + "," + existente.getY() + "). Reintentando posición.");
                    // -------------
                    // Cambiar posición de la nueva pulga e intentar de nuevo
                    nuevaPulga.setX(getPosicionAleatoriaX(anchoInicial)); // Usar ancho/alto inicial
                    nuevaPulga.setY(getPosicionAleatoriaY(altoInicial));
                     // --- DEBUG ---
                    System.out.println("   Nueva posición tentativa: (" + nuevaPulga.getX() + "," + nuevaPulga.getY() + ")");
                     // -------------
                    break; // Salir del for interno para volver a verificar con todas las pulgas
                }
            }
            // Si después de verificar con todas las existentes no hubo colisión, agregarla.
            if (!colisionDetectada) {
                pulgas.add(nuevaPulga);
                 // --- DEBUG ---
                System.out.println("   ¡Pulga agregada exitosamente en (" + nuevaPulga.getX() + "," + nuevaPulga.getY() + ")!");
                 // -------------
                return; // Salir, se agregó exitosamente
            }
        }
         // --- DEBUG ---
        System.out.println("   No se pudo agregar pulga sin colisión después de " + intentosMax + " intentos.");
         // -------------
        // Si después de N intentos no se encuentra lugar, no se agrega.
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
        // System.out.println("Saltando todas las pulgas!"); // Mensaje opcional
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
        
        // Iterar para encontrar la primera pulga impactada
        for (Pulga p : pulgas) { 
            if (p.estaViva() && p.contienePunto(x, y)) {
                boolean estabaVivaAntes = p.estaViva();
                p.recibirImpacto(); // Aplica el impacto
                
                // Si murió en este disparo, sumar punto
                if (estabaVivaAntes && !p.estaViva()) {
                    puntaje++;
                }
                
                // Actualizar la UI del puntaje
                if (ventana != null) ventana.actualizarPuntaje(this.puntaje);
                
                break; // La pistola solo afecta a una
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
            if(pulgaSeleccionada.estaViva()){ // Doble chequeo
                 pulgaSeleccionada.destruir(); // Usar método directo para asegurar destrucción
                 pulgasRealmenteDestruidas++;
            }
        }
        
        if(pulgasRealmenteDestruidas > 0) {
            this.puntaje += pulgasRealmenteDestruidas;
            if (ventana != null) ventana.actualizarPuntaje(this.puntaje);
        }
        // System.out.println("Misil lanzado. Destruidas: " + pulgasRealmenteDestruidas);
    }

    // --- Métodos de posición aleatoria (considerando tamaño del objeto) ---
    private int getPosicionAleatoriaX(int anchoObjeto) {
        // Asegura que anchoObjeto no sea mayor o igual que ANCHO_CAMPO
        anchoObjeto = Math.max(0, anchoObjeto); // No puede ser negativo
        if (ANCHO_CAMPO <= anchoObjeto) return 0; 
        return randomGenerator.nextInt(ANCHO_CAMPO - anchoObjeto);
    }

    private int getPosicionAleatoriaY(int altoObjeto) {
         // Asegura que altoObjeto no sea mayor o igual que ALTO_CAMPO
        altoObjeto = Math.max(0, altoObjeto); // No puede ser negativo
        if (ALTO_CAMPO <= altoObjeto) return 0;
        return randomGenerator.nextInt(ALTO_CAMPO - altoObjeto);
    }

    // --- Getters para que la UI acceda a la información ---
    public List<Pulga> getPulgas() { 
        return new ArrayList<>(pulgas); // Devuelve copia
    } 
    public int getPuntaje() { return puntaje; }
    public int getPuntajeMaximo() { return puntajeMaximo; }
    // ANCHO_CAMPO y ALTO_CAMPO son public static final, se pueden acceder directamente
}