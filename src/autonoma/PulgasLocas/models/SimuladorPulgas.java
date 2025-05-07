/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import autonoma.PulgasLocas.views.VentanaPrincipal; // Para actualizar UI
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.Timer;

public class SimuladorPulgas {
    public static final int ANCHO_CAMPO = 800;
    public static final int ALTO_CAMPO = 600;
    private static final int TIEMPO_GENERACION_NORMAL = 5000; // ms (5 segundos)
    private static final int TIEMPO_GENERACION_MUTANTE = 10000; // ms (10 segundos)
    private static final int TIEMPO_SALTO_PULGAS = 3000; // ms (3 segundos)
    private static final int DELAY_GAME_LOOP = 50; // ms (para aprox 20 FPS)

    private ArrayList<Pulga> pulgas;
    private int puntaje;
    private int puntajeMaximo;
    private boolean enJuego;

    private Timer timerGeneracionNormal;
    private Timer timerGeneracionMutante;
    private Timer timerSaltoPulgasGlobal;
    private Timer timerGameLoop;

    private Random randomGenerator;
    private VentanaPrincipal ventana; // Referencia a la ventana para actualizar UI

    // Instancias de armas (para no crearlas en cada uso)
    private PistolaPulguipium pistola;
    private MisilPulgoson misil;

    public SimuladorPulgas(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.randomGenerator = new Random();
        this.pulgas = new ArrayList<>();
        this.pistola = new PistolaPulguipium();
        this.misil = new MisilPulgoson();

        // Precargar sprites
        GestorDeSprite.precargarSprites();
        
        // Cargar puntaje máximo al inicio
        this.puntajeMaximo = GestorDeArchivos.cargarPuntajeMaximo();
        if (this.ventana != null) {
            this.ventana.actualizarPuntajeMaximo(this.puntajeMaximo);
        }

        configurarTimers();
    }

    private void configurarTimers() {
        // Timer para generar pulgas normales
        timerGeneracionNormal = new Timer(TIEMPO_GENERACION_NORMAL, e -> agregarPulgaNormal());
        
        // Timer para generar pulgas mutantes
        timerGeneracionMutante = new Timer(TIEMPO_GENERACION_MUTANTE, e -> agregarPulgaMutante());

        // Timer para que todas las pulgas salten
        timerSaltoPulgasGlobal = new Timer(TIEMPO_SALTO_PULGAS, e -> {
            for (Pulga p : pulgas) {
                if (p.estaViva()) {
                    p.saltar(ANCHO_CAMPO, ALTO_CAMPO);
                }
            }
        });

        // Timer principal del bucle de juego
        timerGameLoop = new Timer(DELAY_GAME_LOOP, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enJuego) {
                    actualizarElementosDeJuego();
                    limpiarPulgasMuertas();
                    // verificarCondicionesDeFin(); // Si hubiera condiciones automáticas
                    if (ventana != null) {
                        ventana.repaintGamePanel(); // Solicita repintar el panel de juego
                    }
                }
            }
        });
    }

    public void iniciarJuego() {
        this.puntaje = 0;
        this.pulgas.clear();
        this.enJuego = true;

        if (this.ventana != null) {
            this.ventana.actualizarPuntaje(this.puntaje);
            // El puntaje máximo ya se actualizó al crear el simulador o al finalizar juego anterior
        }
        
        // Iniciar todos los timers
        timerGeneracionNormal.start();
        timerGeneracionMutante.start();
        timerSaltoPulgasGlobal.start();
        timerGameLoop.start();
        System.out.println("Juego iniciado.");
    }

    public void finalizarJuego() {
        if (!enJuego) return; // Evitar múltiples finalizaciones

        this.enJuego = false;
        
        // Detener todos los timers
        timerGeneracionNormal.stop();
        timerGeneracionMutante.stop();
        timerSaltoPulgasGlobal.stop();
        timerGameLoop.stop();

        if (this.puntaje > this.puntajeMaximo) {
            this.puntajeMaximo = this.puntaje;
            GestorDeArchivos.guardarPuntaje(this.puntajeMaximo);
            if (this.ventana != null) {
                this.ventana.actualizarPuntajeMaximo(this.puntajeMaximo);
            }
        }
        
        System.out.println("Juego finalizado. Puntaje: " + this.puntaje);
        if (this.ventana != null) {
            this.ventana.mostrarDialogoFinJuego(this.puntaje);
        }
    }

    private void agregarPulgaConColision(Pulga nuevaPulga) {
        int intentosMax = 10; // Intentar ubicarla N veces sin colisión
        for (int i = 0; i < intentosMax; i++) {
            boolean colisionDetectada = false;
            for (Pulga existente : pulgas) {
                if (existente.estaViva() && nuevaPulga.colisiona(existente)) {
                    colisionDetectada = true;
                    // Si colisiona, intentar generar nuevas coordenadas para nuevaPulga
                    int anchoPulga = (nuevaPulga.sprite != null && nuevaPulga.sprite.getAncho() > 0) ? nuevaPulga.sprite.getAncho() : 30;
                    int altoPulga = (nuevaPulga.sprite != null && nuevaPulga.sprite.getAlto() > 0) ? nuevaPulga.sprite.getAlto() : 30;
                    nuevaPulga.setX(randomGenerator.nextInt(ANCHO_CAMPO - anchoPulga));
                    nuevaPulga.setY(randomGenerator.nextInt(ALTO_CAMPO - altoPulga));
                    break; 
                }
            }
            if (!colisionDetectada) {
                pulgas.add(nuevaPulga);
                System.out.println("Pulga agregada en: " + nuevaPulga.getX() + "," + nuevaPulga.getY());
                return; // Agregada exitosamente
            }
        }
        System.out.println("No se pudo agregar pulga sin colisión después de " + intentosMax + " intentos.");
    }
    
    public void agregarPulgaNormal() {
        if (!enJuego) return;
        int x = randomGenerator.nextInt(ANCHO_CAMPO - 30); // Asumir ancho 30 para evitar borde
        int y = randomGenerator.nextInt(ALTO_CAMPO - 30); // Asumir alto 30 para evitar borde
        agregarPulgaConColision(new PulgaNormal(x, y));
    }

    public void agregarPulgaMutante() {
        if (!enJuego) return;
        int x = randomGenerator.nextInt(ANCHO_CAMPO - 30);
        int y = randomGenerator.nextInt(ALTO_CAMPO - 30);
        agregarPulgaConColision(new PulgaMutante(x, y));
    }

    public void usarPistolaPulguipium(int x, int y) {
        if (!enJuego) return;
        int eliminadas = pistola.usar(pulgas, x, y);
        if (eliminadas > 0) {
            this.puntaje += eliminadas; // Podría ser más de 1 si la pistola se modifica
            if (this.ventana != null) {
                this.ventana.actualizarPuntaje(this.puntaje);
            }
        }
    }

    public void usarMisilPulgoson() { // Ya no necesita x, y
        if (!enJuego) return;
        int eliminadas = misil.usar(pulgas, 0, 0); // x,y son ignorados por el misil global
        if (eliminadas > 0) {
            this.puntaje += eliminadas;
             if (this.ventana != null) {
                this.ventana.actualizarPuntaje(this.puntaje);
            }
        }
    }
    
    private void actualizarElementosDeJuego() {
        for (Pulga p : pulgas) {
            if (p.estaViva()) {
                p.actualizar(); // Actualiza animación de sprite y cualquier otra lógica de la pulga
            }
        }
    }

    private void limpiarPulgasMuertas() {
        Iterator<Pulga> iterador = pulgas.iterator();
        while (iterador.hasNext()) {
            Pulga p = iterador.next();
            if (!p.estaViva()) {
                // Aquí podrías añadir una animación de muerte si el sprite la tiene
                // y removerla solo después de que la animación termine.
                // Por ahora, se remueven inmediatamente.
                iterador.remove();
                System.out.println("Pulga muerta removida.");
            }
        }
    }

    // Getters para la vista
    public ArrayList<Pulga> getPulgas() { return pulgas; }
    public int getPuntaje() { return puntaje; }
    public int getPuntajeMaximo() { return puntajeMaximo; }
    public boolean isEnJuego() { return enJuego; }
}