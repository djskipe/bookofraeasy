import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;

public class BookOfRaSlotGUI extends JFrame {
    private static final String[] SYMBOLS = {"Anubis", "Faraone", "Libro", "Esploratore", "Scarab", "Statua"};
    private static final int RULLI = 3;
    
    private JLabel[] rulliLabels;
    private JTextArea risultatoText;
    private JButton spinButton;
    private JComboBox<Double> puntataComboBox;
    private Random random;
    private Timer[] timers;

    // Immagini simboli (caricamento online tramite URL)
    private final ImageIcon[] simboliIcons = new ImageIcon[SYMBOLS.length];

    public BookOfRaSlotGUI() {
        setTitle("Book of Ra Slot");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        random = new Random();
        timers = new Timer[RULLI];

        // Carica le immagini dei simboli
        caricaImmagini();

        JPanel rulliPanel = new JPanel();
        rulliPanel.setLayout(new GridLayout(1, RULLI));
        rulliLabels = new JLabel[RULLI];
        for (int i = 0; i < RULLI; i++) {
            rulliLabels[i] = new JLabel("", SwingConstants.CENTER);
            rulliLabels[i].setPreferredSize(new Dimension(100, 100)); // Dimensione simboli
            rulliPanel.add(rulliLabels[i]);
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        risultatoText = new JTextArea("Premi il pulsante per girare i rulli!");
        risultatoText.setEditable(false);
        risultatoText.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(risultatoText, BorderLayout.CENTER);

        JPanel puntataPanel = new JPanel();
        puntataPanel.setLayout(new FlowLayout());

        puntataComboBox = new JComboBox<>(new Double[]{0.50, 1.00, 2.00, 5.00});
        puntataComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        puntataPanel.add(new JLabel("Puntata: "));
        puntataPanel.add(puntataComboBox);

        spinButton = new JButton("Gira i Rulli");
        spinButton.setFont(new Font("Arial", Font.PLAIN, 18));
        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                avviaAnimazioneRulli();
            }
        });
        puntataPanel.add(spinButton);

        bottomPanel.add(puntataPanel, BorderLayout.SOUTH);

        add(rulliPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void caricaImmagini() {
        try {
            simboliIcons[0] = new ImageIcon(new URL("https://images.vexels.com/content/149477/preview/anubis-afterlife-god-icon-c1362f.png"));
            simboliIcons[1] = new ImageIcon(new URL("https://e7.pngegg.com/pngimages/419/138/png-clipart-egyptian-pharaoh-egyptian-pharaoh-egypt-masterpieces-thumbnail.png"));
            simboliIcons[2] = new ImageIcon(new URL("https://e7.pngegg.com/pngimages/527/243/png-clipart-book-book.png"));
            simboliIcons[3] = new ImageIcon(new URL("https://e7.pngegg.com/pngimages/488/201/png-clipart-national-archaeological-museum-scape-human-behavior-explorador-hand-toddler-thumbnail.png"));
            simboliIcons[4] = new ImageIcon(new URL("https://img.favpng.com/17/16/24/scarab-ancient-egypt-tattoo-khepri-beetle-png-favpng-3Pq7dAK5xwmCfDnVXCtrK3N5t.jpg"));
            simboliIcons[5] = new ImageIcon(new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS--GthDln5QabfRMp8PDe4bLpAJE0bBphENw&s"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void avviaAnimazioneRulli() {
        risultatoText.setText("Girando...");
        spinButton.setEnabled(false);

        for (int i = 0; i < RULLI; i++) {
            final int rulloIndex = i;
            timers[i] = new Timer(100 + i * 100, new ActionListener() {
                int ticks = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    int indice = random.nextInt(SYMBOLS.length);
                    rulliLabels[rulloIndex].setIcon(simboliIcons[indice]);

                    ticks++;
                    if (ticks >= 10 + rulloIndex * 5) {
                        timers[rulloIndex].stop();
                        if (tuttiRulliFermi()) {
                            mostraRisultatoFinale();
                        }
                    }
                }
            });
            timers[i].start();
        }
    }

    private boolean tuttiRulliFermi() {
        for (Timer timer : timers) {
            if (timer.isRunning()) {
                return false;
            }
        }
        return true;
    }

    private void mostraRisultatoFinale() {
        boolean vittoria = true;
        String firstSymbol = rulliLabels[0].getIcon().toString();
        for (int i = 1; i < rulliLabels.length; i++) {
            if (!rulliLabels[i].getIcon().toString().equals(firstSymbol)) {
                vittoria = false;
                break;
            }
        }

        double puntata = (double) puntataComboBox.getSelectedItem();
        if (vittoria) {
            double vincita = puntata * 10;
            risultatoText.setText("Complimenti! Hai vinto " + vincita + "€!");
        } else {
            risultatoText.setText("Peccato, riprova!");
        }

        spinButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BookOfRaSlotGUI frame = new BookOfRaSlotGUI();
                frame.setVisible(true);
            }
        });
    }
}