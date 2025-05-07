
package autonoma.PulgasLocas.views;

import autonoma.PulgasLocas.models.CampoDeBatalla;
import autonoma.PulgasLocas.models.EstadoDeJuego;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VentanaPrincipal extends JFrame { 

    private CampoDeBatalla campoDeBatalla; 
    private PanelJuego panelJuego;         
    private JPanel panelInfo;              
    private JLabel lblPuntaje;
    private JLabel lblPuntajeMaximo;
    private JPanel panelMenu;              

    public VentanaPrincipal() {
        super("Pulgas Locas"); 
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        setLayout(new BorderLayout());
        setResizable(false); 

        EstadoDeJuego.cambiarEstado(EstadoDeJuego.MENU_PRINCIPAL); 

        lblPuntaje = new JLabel("Puntaje: 0", JLabel.LEFT);
        lblPuntajeMaximo = new JLabel("Max Puntaje: 0", JLabel.RIGHT); 

        campoDeBatalla = new CampoDeBatalla(this); 
        
        panelJuego = new PanelJuego(campoDeBatalla);
        inicializarComponentesUI(); 
        configurarTeclado();        
        configurarCierreVentana();  
        
        actualizarVistaSegunEstado(); 

        int extraHeight = 80; 
        int extraWidth = 20;  
        setSize(CampoDeBatalla.ANCHO_CAMPO + extraWidth, 
                CampoDeBatalla.ALTO_CAMPO + extraHeight);

        setLocationRelativeTo(null); 
    }

    private void inicializarComponentesUI() {
        panelInfo = new JPanel(new GridLayout(1, 2));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
        lblPuntajeMaximo.setText("Max Puntaje: " + campoDeBatalla.getPuntajeMaximo());
        panelInfo.add(lblPuntaje);
        panelInfo.add(lblPuntajeMaximo);

        panelMenu = new JPanel(new BorderLayout(10, 10));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); 
        JPanel panelBotonesMenu = new JPanel(new GridLayout(2,1, 10, 10)); 
        JButton btnJugar = new JButton("Jugar");
        btnJugar.setFont(new Font("Arial", Font.BOLD, 18));
        btnJugar.setFocusable(false); 
        btnJugar.addActionListener(e -> iniciarElJuegoDesdeMenu());
        
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 18));
        btnSalir.setFocusable(false);
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotonesMenu.add(btnJugar);
        panelBotonesMenu.add(btnSalir);
        
        JPanel wrapperBotones = new JPanel(); 
        wrapperBotones.add(panelBotonesMenu);
        
        JLabel lblTituloMenu = new JLabel("PULGAS LOCAS", JLabel.CENTER);
        lblTituloMenu.setFont(new Font("Arial", Font.BOLD, 36));

        panelMenu.add(lblTituloMenu, BorderLayout.CENTER);
        panelMenu.add(wrapperBotones, BorderLayout.SOUTH);
    }
    
    private void actualizarVistaSegunEstado() {
        getContentPane().removeAll(); 
        int estado = EstadoDeJuego.getEstadoActual();
        
        if (estado == EstadoDeJuego.MENU_PRINCIPAL) {
            add(panelMenu, BorderLayout.CENTER);
             setTitle("Pulgas Locas - Menú Principal");
        } else if (estado == EstadoDeJuego.EN_JUEGO || estado == EstadoDeJuego.PAUSA) {
            add(panelInfo, BorderLayout.NORTH); 
            add(panelJuego, BorderLayout.CENTER); 
            setTitle("Pulgas Locas - ¡En Juego!");
             SwingUtilities.invokeLater(() -> requestFocusInWindow()); 
        } else if (estado == EstadoDeJuego.FIN_JUEGO) {
             add(panelMenu, BorderLayout.CENTER); 
             setTitle("Pulgas Locas - Fin del Juego");
        }
        
        revalidate(); 
        repaint();    
    }

    public void iniciarElJuegoDesdeMenu() {
        campoDeBatalla.iniciarJuego(); 
        actualizarVistaSegunEstado();
    }

    private void configurarTeclado() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int estadoActual = EstadoDeJuego.getEstadoActual();
                
                if (estadoActual == EstadoDeJuego.EN_JUEGO) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_P: 
                            campoDeBatalla.agregarPulgaNormal();
                            break;
                        case KeyEvent.VK_M: 
                            campoDeBatalla.agregarPulgaMutante();
                            break;
                        case KeyEvent.VK_S: 
                            campoDeBatalla.saltarPulgas();
                            break;
                        case KeyEvent.VK_Q: 
                            campoDeBatalla.finalizarJuego();
                            break;
                        case KeyEvent.VK_SPACE: 
                            campoDeBatalla.lanzarMisil();
                            break;
                        case KeyEvent.VK_ESCAPE: 
                             campoDeBatalla.pausarJuego();
                             break;
                    }
                } 
                else if (estadoActual == EstadoDeJuego.PAUSA) {
                     if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { 
                         campoDeBatalla.reanudarJuego();
                     }
                }
            }
        });
        setFocusable(true); 
    }

    private void configurarCierreVentana() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int estado = EstadoDeJuego.getEstadoActual();
                 if (estado == EstadoDeJuego.EN_JUEGO || estado == EstadoDeJuego.PAUSA) {
                    campoDeBatalla.finalizarJuego(); 
                 } else { 
                    System.exit(0);
                 }
            }
        });
    }

    public void actualizarPuntaje(int nuevoPuntaje) {
        SwingUtilities.invokeLater(() -> {
            if (lblPuntaje != null) {
                 lblPuntaje.setText("Puntaje: " + nuevoPuntaje);
            }
        });
    }

    public void actualizarPuntajeMaximo(int nuevoPuntajeMaximo) {
        SwingUtilities.invokeLater(() -> {
            if (this.lblPuntajeMaximo != null) {
               this.lblPuntajeMaximo.setText("Max Puntaje: " + nuevoPuntajeMaximo);
            }
        });
    }

    public void mostrarDialogoFinJuego(int puntajeFinal) {
        SwingUtilities.invokeLater(() -> {
            String mensaje = "¡Juego Terminado!\nPuntaje Final: " + puntajeFinal;
            JOptionPane.showMessageDialog(this, mensaje, "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
            EstadoDeJuego.cambiarEstado(EstadoDeJuego.MENU_PRINCIPAL); 
            actualizarVistaSegunEstado(); 
        });
    }
    
    public void repaintGamePanel(){
        if(panelJuego != null){
            panelJuego.repaint();
        }
    }

} 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 599, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
