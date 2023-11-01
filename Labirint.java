import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.*;

class Labirint extends JPanel {
    private static int currentParcurgeriIndex = 0, sursa ;
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static ArrayList<ArrayList<Integer>> matrix, parcurgeri, listaAdiacenta;
    private static ArrayList<Integer> iesiriInaccesibile = new ArrayList<Integer>();
    private static Vector<Nod> noduri = new Vector<Nod>();
    private static Vector<Arc> arce = new Vector<Arc>();
    private Vector<Rectangle> rectangles = new Vector<Rectangle>();
    private static int dim=50;
    private void drawLabirint(Graphics g) {
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        int mazeWidth = matrix.get(0).size() * dim;
        int mazeHeight = matrix.size() * dim;
        int xOffset = (screenWidth - mazeWidth) / 2;
        int yOffset = (screenHeight - mazeHeight) / 2;
        int nodeIndex = 0;

        for(int i=0; i<matrix.size(); i++) {
            for(int j=0; j<matrix.get(i).size(); j++) {
                int x = xOffset + j * dim; // Note the swap of i and j
                int y = yOffset + i * dim; // Note the swap of i and j
                rectangles.add(new Rectangle(x, y, dim, dim));

                if(sursa==nodeIndex) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, dim, dim);
                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, dim, dim);
                }
                else if(iesiriInaccesibile.contains(nodeIndex)){
                    g.setColor(Color.RED);
                    g.fillRect(x, y, dim, dim);
                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, dim, dim);
                }
                else if(matrix.get(i).get(j)==1) {
                    g.setColor(Color.GRAY);
                    g.fillRect(x, y, dim, dim);
                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, dim, dim);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, dim, dim);
                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, dim, dim);
                }
                // Add node labels
                if (nodeIndex < noduri.size()) {
                    g.setColor(Color.YELLOW);
                    g.drawString(Integer.toString(noduri.get(nodeIndex++).getID()), x + dim / 2, y + dim / 2);
                }
            }
        }
    }


    public Labirint() {
        matrix=GenerateGraphFromMatrix.getMatrix();
        sursa=GenerateGraphFromMatrix.pickRandomNode();
        parcurgeri=GenerateGraphFromMatrix.Parcurgeri(sursa);
        listaAdiacenta=GenerateGraphFromMatrix.getListaAdiacenta();
        iesiriInaccesibile=GenerateGraphFromMatrix.getIesiriInaccessible();
        //pick a random node which has at least one neighbor of access type
        initNoduri();
        initArce();
    }
    private void initNoduri() {
        int index=0;
        for (int i = 0; i < matrix.size(); i++) {
            for(int j=0; j<matrix.get(i).size(); j++) {
                    noduri.add(new Nod(++index));
            }
        }
    }
    private void initArce() {
        for (int i=0;i<listaAdiacenta.size();i++) {
            for (int j=0;j<listaAdiacenta.get(i).size();j++) {
                arce.add(new Arc(noduri.get(i), noduri.get(listaAdiacenta.get(i).get(j))));
            }
        }
    }
    private void drawArce(Graphics g) {
        for (Arc arc : arce) {

            g.setColor(Color.RED);
            g.drawLine(rectangles.get(arc.getNod1().getID()-1).x+dim/2, rectangles.get(arc.getNod1().getID()-1).y+dim/2, rectangles.get(arc.getNod2().getID()-1).x+dim/2, rectangles.get(arc.getNod2().getID()-1).y+dim/2);
        }
    }

    private Point getIndex(int nr)
    {
        Point p=new Point();
        p.x=nr/matrix.get(0).size();
        p.y=nr%matrix.get(0).size();
        return p;
    }
    private void drawParcurgeri(Graphics g,int i) {
        /*if(parcurgeri.get(i).size()==1) {
            int x1 = rectangles.get(parcurgeri.get(i).get(0)).x + dim / 2;
            int y1 = rectangles.get(parcurgeri.get(i).get(0)).y + dim / 2;
            int x2 = rectangles.get(parcurgeri.get(i).get(0)).x;
            int y2 = rectangles.get(parcurgeri.get(i).get(0)).y;
            System.out.println("Exit node: " + (parcurgeri.get(i).get(0)+1));
            // Extend the line outside the rectangle
            Point p=getIndex(parcurgeri.get(i).get(0));
            if(p.x-1>=0 && matrix.get(p.x-1).get(p.y)==1) {

                x2 = rectangles.get(parcurgeri.get(i).get(0)).x;
                y2 = rectangles.get(parcurgeri.get(i).get(0)).y-dim/2;
            }
            else if(p.x+1<matrix.size() && matrix.get(p.x+1).get(p.y)==1) {
                x2 = rectangles.get(parcurgeri.get(i).get(0)).x;
                y2 = rectangles.get(parcurgeri.get(i).get(0)).y+dim/2;
            }
            else if(p.y-1>=0 && matrix.get(p.x).get(p.y-1)==1) {
                x2 = rectangles.get(parcurgeri.get(i).get(0)).x-dim/2;
                y2 = rectangles.get(parcurgeri.get(i).get(0)).y;
            }
            else if(p.y+1<matrix.get(0).size() && matrix.get(p.x).get(p.y+1)==1) {
                x2 = rectangles.get(parcurgeri.get(i).get(0)).x+dim/2;
                y2 = rectangles.get(parcurgeri.get(i).get(0)).y;
            }
            else if(p.x-1>=0 && p.y-1>=0 && matrix.get(p.x-1).get(p.y-1)==1) {
                x2 = rectangles.get(parcurgeri.get(i).get(0)).x-dim/2;
                y2 = rectangles.get(parcurgeri.get(i).get(0)).y-dim/2;
            }
            else if(p.x-1>=0 && p.y+1<matrix.get(0).size() && matrix.get(p.x-1).get(p.y+1)==1) {
                x2 = rectangles.get(parcurgeri.get(i).get(0)).x-dim/2;
                y2 = rectangles.get(parcurgeri.get(i).get(0)).y+dim/2;
            }
            else if(p.x+1<matrix.size() && p.y-1>=0 && matrix.get(p.x+1).get(p.y-1)==1) {
                x2 = rectangles.get(parcurgeri.get(i).get(0)).x+dim/2;
                y2 = rectangles.get(parcurgeri.get(i).get(0)).y-dim/2;
            }

            g.setColor(Color.GREEN);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g2.drawLine(x1, y1, x2, y2);
            return;
        }*/
        if(parcurgeri.get(i).size()==1) {
            g.setColor(Color.GREEN);
            g.drawOval(rectangles.get(parcurgeri.get(i).get(0)).x, rectangles.get(parcurgeri.get(i).get(0)).y, dim, dim);
        }
        for (int j=0;j<parcurgeri.get(i).size()-1;j++) {
            g.setColor(Color.GREEN);

            //set line tickness
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));

            int x1 = rectangles.get(parcurgeri.get(i).get(j)).x + dim / 2;
            int y1 = rectangles.get(parcurgeri.get(i).get(j)).y + dim / 2;
            int x2 = rectangles.get(parcurgeri.get(i).get(j+1)).x + dim / 2;
            int y2 = rectangles.get(parcurgeri.get(i).get(j+1)).y + dim / 2;

            /*if(GenerateGraphFromMatrix.isExit(parcurgeri.get(i).get(j+1),matrix)){
                System.out.println("Exit node: " + (parcurgeri.get(i).get(j+1)+1));
                // Extend the line outside the rectangle
                if (x1 > x2) x1 += dim / 2;
                else if (x1 < x2) x1 -= dim / 2;
                else if (y1 > y2) y1 -= dim / 2;
                else if (y1 < y2) y1 -= dim / 2;
                else if (x1 == x2 && y1 == y2) {
                    System.out.println("Error: x1 == x2 && y1 == y2");
                    x1 += dim / 2;
                    y1 += dim / 2;
                }
            } else if(GenerateGraphFromMatrix.isExit(parcurgeri.get(i).get(j), matrix)) {
                System.out.println("Exit node: " + (parcurgeri.get(i).get(j)+1));
                // Extend the line outside the rectangle
                if (x2 > x1) x2 += dim / 2;
                else if (x2 < x1) x2 -= dim / 2;
                else if (y2 > y1) y2 += dim / 2;
                else if (y2 < y1) y2 -= dim / 2;
                else if (x2 == x1 && y2 == y1) {
                    System.out.println("Error: x2 == x1 && y2 == y1");
                    x2 += dim / 2;
                    y2 += dim / 2;
                }
            }*/

            g2.drawLine(x1, y1, x2, y2);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLabirint(g);
        if(parcurgeri.size()>0) {
            drawParcurgeri(g, currentParcurgeriIndex);
        }
    }

    public static void run() {
        JFrame frame = new JFrame("Labirint");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("Sursa: ");
        label.setFont(new Font("Serif", Font.PLAIN, 30));
        label.setBounds(0, 0, 100, 50);


        TextField textField = new TextField();
        textField.setPreferredSize(new Dimension(200, 50));
        textField.setBounds(0, 0, 100, 50);
        textField.setFont(new Font("Serif", Font.PLAIN, 30));
        textField.addActionListener(e -> {
            sursa = Integer.parseInt(textField.getText())-1;
            if(sursa>=matrix.size()*matrix.get(0).size()||sursa<0) {
                JOptionPane.showMessageDialog(null, "Nodul nu exista!");
                return;
            }
            else if (matrix.get(sursa / matrix.get(0).size()).get(sursa % matrix.get(0).size()) == 0) {
                JOptionPane.showMessageDialog(null, "Nodul nu este accesibil!");
                return;
            }
            parcurgeri=GenerateGraphFromMatrix.Parcurgeri(sursa);
            iesiriInaccesibile=GenerateGraphFromMatrix.getIesiriInaccessible();
            currentParcurgeriIndex=0;
            while (currentParcurgeriIndex < parcurgeri.size() - 1) {
                currentParcurgeriIndex++;
                System.out.println("currentParcurgeriIndex: " + currentParcurgeriIndex);
                if (parcurgeri.get(currentParcurgeriIndex).size() > 0) {
                    break;
                }
                if (currentParcurgeriIndex == parcurgeri.size() - 1) {
                    currentParcurgeriIndex = -1;
                }
            }
            frame.repaint();
        });

        JButton button2 = new JButton("Submit");
        button2.setPreferredSize(new Dimension(80, 30));
        button2.setBounds(0, 0, 100, 50);
        button2.addActionListener(e -> {
            sursa = Integer.parseInt(textField.getText())-1;
            if(sursa>=matrix.size()*matrix.get(0).size()||sursa<0) {
                JOptionPane.showMessageDialog(null, "Nodul nu exista!");
                return;
            } else if (matrix.get(sursa / matrix.get(0).size()).get(sursa % matrix.get(0).size()) == 0) {
                JOptionPane.showMessageDialog(null, "Nodul nu este accesibil!");
                return;

            }
            parcurgeri=GenerateGraphFromMatrix.Parcurgeri(sursa);
            iesiriInaccesibile=GenerateGraphFromMatrix.getIesiriInaccessible();
            currentParcurgeriIndex=0;
            while (currentParcurgeriIndex < parcurgeri.size() - 1) {
                currentParcurgeriIndex++;
                System.out.println("currentParcurgeriIndex: " + currentParcurgeriIndex);
                if (parcurgeri.get(currentParcurgeriIndex).size() > 0) {
                    break;
                }
                if (currentParcurgeriIndex == parcurgeri.size() - 1) {
                    currentParcurgeriIndex = -1;
                }
            }
            frame.repaint();
        });

        JButton button = new JButton("Next");
        //set button color
        button.setBackground(Color.GREEN);
        button.setPreferredSize(new Dimension(80, 30));
        button.setBounds(0, 0, 100, 50);
        button.addActionListener(e -> {
            frame.repaint();
            if(currentParcurgeriIndex>=parcurgeri.size()-1) {
                currentParcurgeriIndex=-1;
            }
            while(currentParcurgeriIndex<parcurgeri.size()-1) {
                currentParcurgeriIndex++;
                System.out.println("currentParcurgeriIndex: " + currentParcurgeriIndex);
                if(parcurgeri.get(currentParcurgeriIndex).size()>0) {
                    break;
                }
                if(currentParcurgeriIndex==parcurgeri.size()-1) {
                    currentParcurgeriIndex=-1;
                }
            }

        });
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(label);
        panel.add(textField);
        panel.add(button2);
        panel.add(button);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new Labirint());
        frame.setVisible(true);

        System.out.println(noduri);
        System.out.println(arce);
    }
}
