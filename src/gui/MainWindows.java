/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Controllers.Constante;
import Controllers.exceptions.NonexistentEntityException;
import Controllers.XEtudiantJpaController;
import Controllers.XGroupeJpaController;
import Outils.Linker;
import Outils.Printer;
import entities.Etudiant;
import entities.Groupe;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Oracle
 */
public class MainWindows extends javax.swing.JFrame {

    /**
     * Creates new form MainWindows
     */
    // global var
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("StudentGrouperPU");
    XGroupeJpaController groupeJpaController = new XGroupeJpaController(emf);
    XEtudiantJpaController etudiantJpaController = new XEtudiantJpaController(emf);
    CardLayout layout;
    ResourceBundle bundle;
    private Groupe groupe;
    private Etudiant etudiant;

    public MainWindows() {
        initComponents();
        layout = (CardLayout) jPanel_root.getLayout();
        bundle = java.util.ResourceBundle.getBundle("gui/Bundle"); // NOI18N

        this.setTitle(Constante.APP_NAME + " - Accueil");
        decoration();
        init();
        runProgressBar();
    }

    private void decoration() {
        // visibility
        jMenuBar1.setVisible(false);
        jButton_demarrer.setVisible(false);
        jTableEtudiants.setVisible(true);
        jTableEtudiants.setCursor(new Cursor(HAND_CURSOR));
        jTableGroupes.setCursor(new Cursor(HAND_CURSOR));
        jButtonDeleteEtudiants.setVisible(false);
        //jButtonPrintEtudiants.setVisible(false);
        jButtonDeleteGroupe.setVisible(false);
        jButtonUpdateGroupe.setVisible(false);

        //color
        jButtonCreateEtudiant.setForeground(Color.white);
        jButtonPrintEtudiants.setForeground(Color.white);
        jButtonDeleteEtudiants.setForeground(Color.white);
    }

    private void init() {
        // chargement des groupes
        String[] colonnes = new String[]{"N*", "Id", "CODE-LIBELLE"};
        groupes = groupeJpaController.findGroupeEntities();
        // 
        if (groupes.size() > 0) {
            data = new Object[groupes.size()][colonnes.length];
            int i = 0;
            for (Groupe groupe : groupes) {
                data[i][0] = i + 1;
                data[i][1] = groupe.getId();
                data[i][2] = groupe.getCode() + " - " + groupe.getLibelle();

                //System.out.println("code de groupe");
                //System.out.println(groupe.getCode());
                i++;
            }
            jTableGroupes.setModel(new DefaultTableModel(data, colonnes));

            if (jTableGroupes.getRowCount() > 0) {
                jTableGroupes.setRowSelectionInterval(0, 0);
                jButtonDeleteGroupe.setVisible(true);
                jButtonUpdateGroupe.setVisible(true);
            }

            if (jTableGroupes.getColumnModel().getColumnCount() > 0) {
                jTableGroupes.getColumnModel().getColumn(0).setResizable(false);
                jTableGroupes.getColumnModel().getColumn(0).setPreferredWidth(1);
                jTableGroupes.getColumnModel().getColumn(0).setMaxWidth(1);
                jTableGroupes.getColumnModel().getColumn(1).setMinWidth(0);
                jTableGroupes.getColumnModel().getColumn(1).setPreferredWidth(0);
                jTableGroupes.getColumnModel().getColumn(1).setMaxWidth(0);
                jTableGroupes.getColumnModel().getColumn(2).setResizable(false);
            }
            // chargement des etudiants
            //etudiants = etudiantJpaController.findEtudiantEntities();
            jLabel1.setText("Liste de etudiants dans " + groupes.get(0).getCode());
            etudiants = groupes.get(0).getEtudiantList();
            refreshEtudiantsTable();
        }
    }

    public void refreshEtudiantsTable(List<Etudiant> etudiants) {
        //jTableEtudiants.setDragEnabled(true);
        String[] colonnes = new String[]{"N°", "Id", "Nom", "Prenom", "Sexe"};
        data = new Object[etudiants.size()][colonnes.length];
        int i = 0;
        for (Etudiant etudiant : etudiants) {
            data[i][0] = i + 1;
            data[i][1] = etudiant.getId();
            data[i][2] = etudiant.getNom();
            //System.out.println("code de etudiant");            
            //System.out.println(etudiant.getId());
            data[i][3] = etudiant.getPrenom();
            data[i][4] = etudiant.getSexe();

            i++;
        }
        TableModel model = new DefaultTableModel(data, colonnes);
        jTableEtudiants.setModel(model);
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        jTableEtudiants.setRowSorter(sorter);
        if (jTableEtudiants.getColumnModel().getColumnCount() > 0) {
            jTableEtudiants.getColumnModel().getColumn(0).setResizable(true);
            jTableEtudiants.getColumnModel().getColumn(0).setMaxWidth(4);
            jTableEtudiants.getColumnModel().getColumn(1).setMinWidth(0);
            jTableEtudiants.getColumnModel().getColumn(1).setPreferredWidth(0);
            jTableEtudiants.getColumnModel().getColumn(1).setMaxWidth(0);
            /*
             jTableEtudiants.getColumnModel().getColumn(2).setMinWidth(1);
             jTableEtudiants.getColumnModel().getColumn(2).setPreferredWidth(1);
             jTableEtudiants.getColumnModel().getColumn(2).setMaxWidth(1);
             */
            jTableEtudiants.getColumnModel().getColumn(2).setResizable(false);
            jTableEtudiants.getColumnModel().getColumn(3).setResizable(false);
        }

        System.out.println("rafraichissement de la table de etudiants");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog_About = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel_contact = new javax.swing.JLabel();
        jDialog_CreateGroupe = new javax.swing.JDialog();
        jLabel_Code = new javax.swing.JLabel();
        jLabel_Libelle = new javax.swing.JLabel();
        jTextField_Code = new javax.swing.JTextField();
        jTextField_Libelle = new javax.swing.JTextField();
        jButton_ValiderCreateGroupe = new javax.swing.JButton();
        jButton_AnnulerCreateGroupe = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jDialog_UpdateGroupe = new javax.swing.JDialog();
        jLabel_Code1 = new javax.swing.JLabel();
        jLabel_Libelle1 = new javax.swing.JLabel();
        jTextField_Code1 = new javax.swing.JTextField();
        jTextField_Libelle1 = new javax.swing.JTextField();
        jButton_ValiderUpdateGroupe = new javax.swing.JButton();
        jButton_AnnulerUpdateGroupe = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jDialog_CreateEtudiant = new javax.swing.JDialog();
        jLabel_Nom = new javax.swing.JLabel();
        jLabel_Prenom = new javax.swing.JLabel();
        jComboBox_Sexes = new javax.swing.JComboBox();
        jLabel_Sexe = new javax.swing.JLabel();
        jLabel_Groupes = new javax.swing.JLabel();
        jTextField_Nom = new javax.swing.JTextField();
        jTextField_Prenom = new javax.swing.JTextField();
        jComboBox_Groupes = new javax.swing.JComboBox();
        jButton_ValiderCreateEtudiant = new javax.swing.JButton();
        jButton_AnnulerCreateEtudiant = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel_Datenaiss = new javax.swing.JLabel();
        dateChooserCombo = new datechooser.beans.DateChooserCombo();
        jDialog_UpdateEtudiant = new javax.swing.JDialog();
        jLabel_Nom1 = new javax.swing.JLabel();
        jLabel_Prenom1 = new javax.swing.JLabel();
        jComboBox_Sexes1 = new javax.swing.JComboBox();
        jLabel_Sexe1 = new javax.swing.JLabel();
        jLabel_Groupes1 = new javax.swing.JLabel();
        jTextField_Nom1 = new javax.swing.JTextField();
        jTextField_Prenom1 = new javax.swing.JTextField();
        jComboBox_Groupes1 = new javax.swing.JComboBox();
        jButton_ValiderUpdateEtudiant = new javax.swing.JButton();
        jButton_AnnulerUpdateEtudiant = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jButtonDeleteEtudiant = new javax.swing.JButton();
        jLabel_Datenaiss1 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jPanel_root = new javax.swing.JPanel();
        jPanel_splathscreen = new javax.swing.JPanel();
        jLabel_logo = new javax.swing.JLabel();
        jLabel_spinner = new javax.swing.JLabel();
        jPanel_progression = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel_progression = new javax.swing.JLabel();
        jButton_demarrer = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel_header = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButtonCreateGroupe = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButtonCreateEtudiant = new javax.swing.JButton();
        jTextRechercherEtudiant = new javax.swing.JTextField();
        jButtonPrintEtudiants = new javax.swing.JButton();
        jButtonDeleteEtudiants = new javax.swing.JButton();
        jButtonUpdateGroupe = new javax.swing.JButton();
        jButtonDeleteGroupe = new javax.swing.JButton();
        header = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableGroupes = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableEtudiants = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu_Application = new javax.swing.JMenu();
        jMenuItem_refresh = new javax.swing.JMenuItem();
        jMenu_rapport = new javax.swing.JMenu();
        jMenuItem_rapporter_tous_etudiants = new javax.swing.JMenuItem();
        jMenu_parametres = new javax.swing.JMenu();
        jMenu_langages = new javax.swing.JMenu();
        jMenuItem_anglais = new javax.swing.JMenuItem();
        jMenuItem_francais = new javax.swing.JMenuItem();
        jMenuItem_Exit = new javax.swing.JMenuItem();
        jMenu_Aide = new javax.swing.JMenu();
        jMenuItem_documentation = new javax.swing.JMenuItem();
        jMenuItem_apropos = new javax.swing.JMenuItem();

        jDialog_About.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("gui/Bundle"); // NOI18N
        jDialog_About.setTitle(bundle.getString("MainWindows.jDialog_About.title")); // NOI18N
        jDialog_About.setResizable(false);

        jLabel3.setBackground(new java.awt.Color(203, 231, 82));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(bundle.getString("MainWindows.jLabel3.text")); // NOI18N
        jLabel3.setToolTipText(bundle.getString("MainWindows.jLabel3.toolTipText")); // NOI18N
        jLabel3.setOpaque(true);
        jDialog_About.getContentPane().add(jLabel3, java.awt.BorderLayout.CENTER);

        jSeparator1.setBackground(new java.awt.Color(29, 17, 17));
        jSeparator1.setMinimumSize(new java.awt.Dimension(4, 5));
        jSeparator1.setName(""); // NOI18N
        jDialog_About.getContentPane().add(jSeparator1, java.awt.BorderLayout.PAGE_START);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(bundle.getString("MainWindows.jLabel6.text")); // NOI18N
        jPanel4.add(jLabel6);

        jLabel5.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText(bundle.getString("MainWindows.jLabel5.text")); // NOI18N
        jPanel4.add(jLabel5);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText(bundle.getString("MainWindows.jTextArea1.text")); // NOI18N
        jScrollPane3.setViewportView(jTextArea1);

        jPanel4.add(jScrollPane3);

        jDialog_About.getContentPane().add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setBackground(new java.awt.Color(127, 190, 100));
        jLabel7.setFont(new java.awt.Font("Liberation Serif", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(174, 103, 161));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/32x32/copyright.png"))); // NOI18N
        jLabel7.setText(bundle.getString("MainWindows.jLabel7.text")); // NOI18N
        jLabel7.setToolTipText(bundle.getString("MainWindows.jLabel7.toolTipText")); // NOI18N
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, -1, -1));

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(75, 78, 179));
        jLabel8.setText(bundle.getString("MainWindows.jLabel8.text")); // NOI18N
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, 20));

        jLabel_contact.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jLabel_contact.setForeground(new java.awt.Color(65, 74, 14));
        jLabel_contact.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_contact.setText(bundle.getString("MainWindows.jLabel_contact.text")); // NOI18N
        jLabel_contact.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_contactMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel_contactMouseEntered(evt);
            }
        });
        jPanel6.add(jLabel_contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, 0, 480, -1));

        jDialog_About.getContentPane().add(jPanel6, java.awt.BorderLayout.LINE_END);

        jDialog_CreateGroupe.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog_CreateGroupe.setTitle(bundle.getString("MainWindows.jDialog_CreateGroupe.title")); // NOI18N

        jLabel_Code.setText(bundle.getString("MainWindows.jLabel_Code.text")); // NOI18N

        jLabel_Libelle.setText(bundle.getString("MainWindows.jLabel_Libelle.text")); // NOI18N

        jButton_ValiderCreateGroupe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/16x16/accept.png"))); // NOI18N
        jButton_ValiderCreateGroupe.setText(bundle.getString("MainWindows.jButton_ValiderCreateGroupe.text")); // NOI18N
        jButton_ValiderCreateGroupe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ValiderCreateGroupeActionPerformed(evt);
            }
        });

        jButton_AnnulerCreateGroupe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/annuler.gif"))); // NOI18N
        jButton_AnnulerCreateGroupe.setText(bundle.getString("MainWindows.jButton_AnnulerCreateGroupe.text")); // NOI18N
        jButton_AnnulerCreateGroupe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnnulerCreateGroupeActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("aakar", 1, 18)); // NOI18N
        jLabel9.setText(bundle.getString("MainWindows.jLabel9.text")); // NOI18N

        javax.swing.GroupLayout jDialog_CreateGroupeLayout = new javax.swing.GroupLayout(jDialog_CreateGroupe.getContentPane());
        jDialog_CreateGroupe.getContentPane().setLayout(jDialog_CreateGroupeLayout);
        jDialog_CreateGroupeLayout.setHorizontalGroup(
            jDialog_CreateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_CreateGroupeLayout.createSequentialGroup()
                .addGroup(jDialog_CreateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_CreateGroupeLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jDialog_CreateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel_Libelle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_Code, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialog_CreateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField_Code, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .addComponent(jTextField_Libelle)))
                    .addGroup(jDialog_CreateGroupeLayout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(jLabel9)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jDialog_CreateGroupeLayout.createSequentialGroup()
                .addContainerGap(83, Short.MAX_VALUE)
                .addComponent(jButton_AnnulerCreateGroupe, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton_ValiderCreateGroupe, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        jDialog_CreateGroupeLayout.setVerticalGroup(
            jDialog_CreateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_CreateGroupeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_CreateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Code, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jDialog_CreateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Libelle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Libelle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jDialog_CreateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_AnnulerCreateGroupe)
                    .addComponent(jButton_ValiderCreateGroupe))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jDialog_UpdateGroupe.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog_UpdateGroupe.setTitle(bundle.getString("MainWindows.jDialog_UpdateGroupe.title")); // NOI18N

        jLabel_Code1.setText(bundle.getString("MainWindows.jLabel_Code1.text")); // NOI18N

        jLabel_Libelle1.setText(bundle.getString("MainWindows.jLabel_Libelle1.text")); // NOI18N

        jButton_ValiderUpdateGroupe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/16x16/accept.png"))); // NOI18N
        jButton_ValiderUpdateGroupe.setText(bundle.getString("MainWindows.jButton_ValiderUpdateGroupe.text")); // NOI18N
        jButton_ValiderUpdateGroupe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ValiderUpdateGroupeActionPerformed(evt);
            }
        });

        jButton_AnnulerUpdateGroupe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/annuler.gif"))); // NOI18N
        jButton_AnnulerUpdateGroupe.setText(bundle.getString("MainWindows.jButton_AnnulerUpdateGroupe.text")); // NOI18N
        jButton_AnnulerUpdateGroupe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnnulerUpdateGroupeActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("aakar", 1, 18)); // NOI18N
        jLabel10.setText(bundle.getString("MainWindows.jLabel10.text")); // NOI18N

        javax.swing.GroupLayout jDialog_UpdateGroupeLayout = new javax.swing.GroupLayout(jDialog_UpdateGroupe.getContentPane());
        jDialog_UpdateGroupe.getContentPane().setLayout(jDialog_UpdateGroupeLayout);
        jDialog_UpdateGroupeLayout.setHorizontalGroup(
            jDialog_UpdateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_UpdateGroupeLayout.createSequentialGroup()
                .addGroup(jDialog_UpdateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_UpdateGroupeLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jDialog_UpdateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel_Libelle1, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                            .addComponent(jLabel_Code1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialog_UpdateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField_Code1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                            .addComponent(jTextField_Libelle1)))
                    .addGroup(jDialog_UpdateGroupeLayout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(jLabel10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jDialog_UpdateGroupeLayout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addComponent(jButton_AnnulerUpdateGroupe, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton_ValiderUpdateGroupe, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        jDialog_UpdateGroupeLayout.setVerticalGroup(
            jDialog_UpdateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_UpdateGroupeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_UpdateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Code1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Code1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jDialog_UpdateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Libelle1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Libelle1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jDialog_UpdateGroupeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_AnnulerUpdateGroupe)
                    .addComponent(jButton_ValiderUpdateGroupe))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jDialog_CreateEtudiant.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog_CreateEtudiant.setTitle(bundle.getString("MainWindows.jDialog_CreateEtudiant.title")); // NOI18N

        jLabel_Nom.setText(bundle.getString("MainWindows.jLabel_Nom.text")); // NOI18N

        jLabel_Prenom.setText(bundle.getString("MainWindows.jLabel_Prenom.text")); // NOI18N

        jComboBox_Sexes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Homme", "Femme" }));

        jLabel_Sexe.setText(bundle.getString("MainWindows.jLabel_Sexe.text")); // NOI18N

        jLabel_Groupes.setText(bundle.getString("MainWindows.jLabel_Groupes.text")); // NOI18N

        jComboBox_Groupes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        jComboBox_Groupes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_GroupesItemStateChanged(evt);
            }
        });
        jComboBox_Groupes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_GroupesActionPerformed(evt);
            }
        });

        jButton_ValiderCreateEtudiant.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/16x16/accept.png"))); // NOI18N
        jButton_ValiderCreateEtudiant.setText(bundle.getString("MainWindows.jButton_ValiderCreateEtudiant.text")); // NOI18N
        jButton_ValiderCreateEtudiant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ValiderCreateEtudiantActionPerformed(evt);
            }
        });

        jButton_AnnulerCreateEtudiant.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/annuler.gif"))); // NOI18N
        jButton_AnnulerCreateEtudiant.setText(bundle.getString("MainWindows.jButton_AnnulerCreateEtudiant.text")); // NOI18N
        jButton_AnnulerCreateEtudiant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnnulerCreateEtudiantActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("aakar", 1, 18)); // NOI18N
        jLabel11.setText(bundle.getString("MainWindows.jLabel11.text")); // NOI18N

        jLabel_Datenaiss.setText(bundle.getString("MainWindows.jLabel_Datenaiss.text")); // NOI18N

        javax.swing.GroupLayout jDialog_CreateEtudiantLayout = new javax.swing.GroupLayout(jDialog_CreateEtudiant.getContentPane());
        jDialog_CreateEtudiant.getContentPane().setLayout(jDialog_CreateEtudiantLayout);
        jDialog_CreateEtudiantLayout.setHorizontalGroup(
            jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_CreateEtudiantLayout.createSequentialGroup()
                .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_CreateEtudiantLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel_Prenom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_Groupes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_Sexe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_Nom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_Datenaiss, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jDialog_CreateEtudiantLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField_Prenom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                        .addComponent(jComboBox_Sexes, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox_Groupes, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField_Nom))))
                            .addGroup(jDialog_CreateEtudiantLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(dateChooserCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))))
                    .addGroup(jDialog_CreateEtudiantLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jButton_AnnulerCreateEtudiant, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jButton_ValiderCreateEtudiant, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        jDialog_CreateEtudiantLayout.setVerticalGroup(
            jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_CreateEtudiantLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Nom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Nom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Prenom, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Prenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Sexe)
                    .addComponent(jComboBox_Sexes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Groupes, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox_Groupes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateChooserCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Datenaiss, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jDialog_CreateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_AnnulerCreateEtudiant, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton_ValiderCreateEtudiant, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(33, 33, 33))
        );

        jDialog_UpdateEtudiant.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog_UpdateEtudiant.setTitle(bundle.getString("MainWindows.jDialog_UpdateEtudiant.title")); // NOI18N
        jDialog_UpdateEtudiant.setPreferredSize(new java.awt.Dimension(341, 374));

        jLabel_Nom1.setText(bundle.getString("MainWindows.jLabel_Nom1.text")); // NOI18N

        jLabel_Prenom1.setText(bundle.getString("MainWindows.jLabel_Prenom1.text")); // NOI18N

        jComboBox_Sexes1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Homme", "Femme" }));
        jComboBox_Sexes1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_Sexes1ItemStateChanged(evt);
            }
        });

        jLabel_Sexe1.setText(bundle.getString("MainWindows.jLabel_Sexe1.text")); // NOI18N

        jLabel_Groupes1.setText(bundle.getString("MainWindows.jLabel_Groupes1.text")); // NOI18N

        jComboBox_Groupes1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        jComboBox_Groupes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_Groupes1sActionPerformed(evt);
            }
        });

        jButton_ValiderUpdateEtudiant.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/16x16/accept.png"))); // NOI18N
        jButton_ValiderUpdateEtudiant.setText(bundle.getString("MainWindows.jButton_ValiderUpdateEtudiant.text")); // NOI18N
        jButton_ValiderUpdateEtudiant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ValiderUpdateEtudiantActionPerformed(evt);
            }
        });

        jButton_AnnulerUpdateEtudiant.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/annuler.gif"))); // NOI18N
        jButton_AnnulerUpdateEtudiant.setText(bundle.getString("MainWindows.jButton_AnnulerUpdateEtudiant.text")); // NOI18N
        jButton_AnnulerUpdateEtudiant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnnulerUpdateEtudiantActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("aakar", 1, 18)); // NOI18N
        jLabel12.setText(bundle.getString("MainWindows.jLabel12.text")); // NOI18N

        jButtonDeleteEtudiant.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/16x16/delete.png"))); // NOI18N
        jButtonDeleteEtudiant.setText(bundle.getString("MainWindows.jButtonDeleteEtudiant.text")); // NOI18N
        jButtonDeleteEtudiant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteEtudiantActionPerformed(evt);
            }
        });

        jLabel_Datenaiss1.setText(bundle.getString("MainWindows.jLabel_Datenaiss1.text")); // NOI18N

        javax.swing.GroupLayout jDialog_UpdateEtudiantLayout = new javax.swing.GroupLayout(jDialog_UpdateEtudiant.getContentPane());
        jDialog_UpdateEtudiant.getContentPane().setLayout(jDialog_UpdateEtudiantLayout);
        jDialog_UpdateEtudiantLayout.setHorizontalGroup(
            jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_UpdateEtudiantLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_Prenom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_Nom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_Groupes1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_Datenaiss1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_Sexe1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jDialog_UpdateEtudiantLayout.createSequentialGroup()
                        .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField_Nom1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jComboBox_Sexes1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox_Groupes1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField_Prenom1, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 25, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jDialog_UpdateEtudiantLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jButton_AnnulerUpdateEtudiant, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonDeleteEtudiant)
                    .addComponent(jButton_ValiderUpdateEtudiant, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jDialog_UpdateEtudiantLayout.setVerticalGroup(
            jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_UpdateEtudiantLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Nom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField_Nom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Prenom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Prenom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_Sexes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Sexe1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_Groupes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Groupes1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_UpdateEtudiantLayout.createSequentialGroup()
                        .addComponent(jLabel_Datenaiss1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jDialog_UpdateEtudiantLayout.createSequentialGroup()
                        .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)))
                .addGroup(jDialog_UpdateEtudiantLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_ValiderUpdateEtudiant)
                    .addComponent(jButton_AnnulerUpdateEtudiant))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonDeleteEtudiant)
                .addGap(20, 20, 20))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(bundle.getString("MainWindows.title")); // NOI18N

        jPanel_root.setLayout(new java.awt.CardLayout());

        jPanel_splathscreen.setBackground(new java.awt.Color(254, 254, 254));
        jPanel_splathscreen.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel_logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/studentgrouper/logo2.png"))); // NOI18N
        jPanel_splathscreen.add(jLabel_logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 0, 690, 90));

        jLabel_spinner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/giphy.gif"))); // NOI18N
        jLabel_spinner.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel_splathscreen.add(jLabel_spinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 610, 240));

        jPanel_progression.setBackground(new java.awt.Color(255, 255, 255));

        jLabel_progression.setFont(new java.awt.Font("Noto Sans", 0, 10)); // NOI18N
        jLabel_progression.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_progression.setText(bundle.getString("MainWindows.jLabel_progression.text")); // NOI18N

        javax.swing.GroupLayout jPanel_progressionLayout = new javax.swing.GroupLayout(jPanel_progression);
        jPanel_progression.setLayout(jPanel_progressionLayout);
        jPanel_progressionLayout.setHorizontalGroup(
            jPanel_progressionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_progressionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_progressionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel_progressionLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel_progression)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel_progressionLayout.setVerticalGroup(
            jPanel_progressionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_progressionLayout.createSequentialGroup()
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel_progression, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel_splathscreen.add(jPanel_progression, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 360, 180, 40));

        jButton_demarrer.setBackground(new java.awt.Color(245, 154, 29));
        jButton_demarrer.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        jButton_demarrer.setText(bundle.getString("MainWindows.jButton_demarrer.text")); // NOI18N
        jButton_demarrer.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_demarrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_demarrerActionPerformed(evt);
            }
        });
        jPanel_splathscreen.add(jButton_demarrer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 310, 190, 50));

        jPanel_root.add(jPanel_splathscreen, "card3");

        jPanel_header.setBackground(new java.awt.Color(217, 233, 214));
        jPanel_header.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel_header.setMaximumSize(new java.awt.Dimension(100, 59));
        jPanel_header.setMinimumSize(new java.awt.Dimension(100, 59));

        jLabel2.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        jLabel2.setText(bundle.getString("MainWindows.jLabel2.text")); // NOI18N

        jButtonCreateGroupe.setBackground(new java.awt.Color(177, 228, 182));
        jButtonCreateGroupe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/16x16/add.png"))); // NOI18N
        jButtonCreateGroupe.setToolTipText(bundle.getString("MainWindows.jButtonCreateGroupe.toolTipText")); // NOI18N
        jButtonCreateGroupe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateGroupeActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        jLabel1.setText(bundle.getString("MainWindows.jLabel1.text")); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(106, 17));

        jButtonCreateEtudiant.setBackground(new java.awt.Color(37, 182, 82));
        jButtonCreateEtudiant.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/16x16/add.png"))); // NOI18N
        jButtonCreateEtudiant.setText(bundle.getString("MainWindows.jButtonCreateEtudiant.text")); // NOI18N
        jButtonCreateEtudiant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateEtudiantActionPerformed(evt);
            }
        });

        jTextRechercherEtudiant.setBackground(new java.awt.Color(201, 228, 193));
        jTextRechercherEtudiant.setFont(new java.awt.Font("Noto Sans", 0, 10)); // NOI18N
        jTextRechercherEtudiant.setText(bundle.getString("MainWindows.jTextRechercherEtudiant.text")); // NOI18N
        jTextRechercherEtudiant.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jTextRechercherEtudiant.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextRechercherEtudiantFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextRechercherEtudiantFocusLost(evt);
            }
        });
        jTextRechercherEtudiant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextRechercherEtudiantKeyReleased(evt);
            }
        });

        jButtonPrintEtudiants.setBackground(new java.awt.Color(127, 109, 210));
        jButtonPrintEtudiants.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/imprimer.gif"))); // NOI18N
        jButtonPrintEtudiants.setText(bundle.getString("MainWindows.jButtonPrintEtudiants.text")); // NOI18N
        jButtonPrintEtudiants.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintEtudiantsActionPerformed(evt);
            }
        });

        jButtonDeleteEtudiants.setBackground(new java.awt.Color(227, 85, 85));
        jButtonDeleteEtudiants.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/16x16/delete.png"))); // NOI18N
        jButtonDeleteEtudiants.setText(bundle.getString("MainWindows.jButtonDeleteEtudiants.text")); // NOI18N
        jButtonDeleteEtudiants.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteEtudiantsActionPerformed(evt);
            }
        });

        jButtonUpdateGroupe.setBackground(new java.awt.Color(227, 228, 170));
        jButtonUpdateGroupe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/16x16/page_edit.png"))); // NOI18N
        jButtonUpdateGroupe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateGroupeActionPerformed(evt);
            }
        });

        jButtonDeleteGroupe.setBackground(new java.awt.Color(236, 199, 199));
        jButtonDeleteGroupe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/16x16/delete.png"))); // NOI18N
        jButtonDeleteGroupe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteGroupeActionPerformed(evt);
            }
        });

        header.setText(bundle.getString("MainWindows.header.text")); // NOI18N

        javax.swing.GroupLayout jPanel_headerLayout = new javax.swing.GroupLayout(jPanel_header);
        jPanel_header.setLayout(jPanel_headerLayout);
        jPanel_headerLayout.setHorizontalGroup(
            jPanel_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_headerLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_headerLayout.createSequentialGroup()
                        .addComponent(jButtonCreateGroupe)
                        .addGap(6, 6, 6)
                        .addComponent(jButtonUpdateGroupe)
                        .addGap(6, 6, 6)
                        .addComponent(jButtonDeleteGroupe))
                    .addComponent(jLabel2))
                .addGap(91, 91, 91)
                .addGroup(jPanel_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_headerLayout.createSequentialGroup()
                        .addComponent(jButtonCreateEtudiant)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonPrintEtudiants)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDeleteEtudiants))
                    .addGroup(jPanel_headerLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextRechercherEtudiant, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(142, 142, 142)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(header)
                .addGap(93, 93, 93))
        );
        jPanel_headerLayout.setVerticalGroup(
            jPanel_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_headerLayout.createSequentialGroup()
                .addGroup(jPanel_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextRechercherEtudiant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCreateGroupe)
                    .addComponent(jButtonUpdateGroupe)
                    .addComponent(jButtonDeleteGroupe)
                    .addGroup(jPanel_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonPrintEtudiants)
                        .addComponent(jButtonCreateEtudiant)
                        .addComponent(jButtonDeleteEtudiants)
                        .addComponent(header)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel_header);

        jPanel3.setBackground(new java.awt.Color(217, 233, 214));

        jScrollPane1.setBackground(new java.awt.Color(180, 216, 173));

        jTableGroupes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N*", "Id", "CODE-LIBELLE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableGroupes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableGroupesMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTableGroupesMouseReleased(evt);
            }
        });
        jTableGroupes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTableGroupesFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(jTableGroupes);
        if (jTableGroupes.getColumnModel().getColumnCount() > 0) {
            jTableGroupes.getColumnModel().getColumn(0).setResizable(false);
            jTableGroupes.getColumnModel().getColumn(0).setPreferredWidth(2);
            jTableGroupes.getColumnModel().getColumn(1).setMinWidth(0);
            jTableGroupes.getColumnModel().getColumn(1).setPreferredWidth(0);
            jTableGroupes.getColumnModel().getColumn(1).setMaxWidth(0);
            jTableGroupes.getColumnModel().getColumn(2).setResizable(false);
        }

        jTableEtudiants.setBackground(new java.awt.Color(194, 228, 185));
        jTableEtudiants.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N°", "Id", "Nom", "Prenom"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableEtudiants.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableEtudiantsMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTableEtudiantsMouseReleased(evt);
            }
        });
        jTableEtudiants.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTableEtudiantsFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(jTableEtudiants);
        if (jTableEtudiants.getColumnModel().getColumnCount() > 0) {
            jTableEtudiants.getColumnModel().getColumn(1).setMinWidth(0);
            jTableEtudiants.getColumnModel().getColumn(1).setPreferredWidth(0);
            jTableEtudiants.getColumnModel().getColumn(1).setMaxWidth(0);
            jTableEtudiants.getColumnModel().getColumn(2).setResizable(false);
            jTableEtudiants.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jPanel1.add(jPanel3);

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setMinimumSize(new java.awt.Dimension(45, 371));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bg/footer.png"))); // NOI18N
        jPanel5.add(jLabel4);

        jPanel1.add(jPanel5);

        jPanel_root.add(jPanel1, "card2");

        getContentPane().add(jPanel_root, java.awt.BorderLayout.CENTER);

        jMenu_Application.setText(bundle.getString("MainWindows.jMenu_Application.text")); // NOI18N

        jMenuItem_refresh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem_refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/32x32/refresh-delicious_32.png"))); // NOI18N
        jMenuItem_refresh.setText(bundle.getString("MainWindows.jMenuItem_refresh.text")); // NOI18N
        jMenuItem_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_refreshActionPerformed(evt);
            }
        });
        jMenu_Application.add(jMenuItem_refresh);

        jMenu_rapport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/32x32/Graphite Computer On.png"))); // NOI18N
        jMenu_rapport.setText(bundle.getString("MainWindows.jMenu_rapport.text")); // NOI18N

        jMenuItem_rapporter_tous_etudiants.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem_rapporter_tous_etudiants.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/32x32/window.png"))); // NOI18N
        jMenuItem_rapporter_tous_etudiants.setText(bundle.getString("MainWindows.jMenuItem_rapporter_tous_etudiants.text")); // NOI18N
        jMenuItem_rapporter_tous_etudiants.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_rapporter_tous_etudiantsActionPerformed(evt);
            }
        });
        jMenu_rapport.add(jMenuItem_rapporter_tous_etudiants);

        jMenu_Application.add(jMenu_rapport);

        jMenu_parametres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/32x32/GeneralPreferences.png"))); // NOI18N
        jMenu_parametres.setText(bundle.getString("MainWindows.jMenu_parametres.text")); // NOI18N

        jMenu_langages.setText(bundle.getString("MainWindows.jMenu_langages.text")); // NOI18N

        jMenuItem_anglais.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/en.gif"))); // NOI18N
        jMenuItem_anglais.setText(bundle.getString("MainWindows.jMenuItem_anglais.text")); // NOI18N
        jMenuItem_anglais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_anglaisActionPerformed(evt);
            }
        });
        jMenu_langages.add(jMenuItem_anglais);

        jMenuItem_francais.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/fr.gif"))); // NOI18N
        jMenuItem_francais.setText(bundle.getString("MainWindows.jMenuItem_francais.text")); // NOI18N
        jMenuItem_francais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_francaisActionPerformed(evt);
            }
        });
        jMenu_langages.add(jMenuItem_francais);

        jMenu_parametres.add(jMenu_langages);

        jMenu_Application.add(jMenu_parametres);

        jMenuItem_Exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem_Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/32x32/stop.png"))); // NOI18N
        jMenuItem_Exit.setText(bundle.getString("MainWindows.jMenuItem_Exit.text")); // NOI18N
        jMenuItem_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_ExitActionPerformed(evt);
            }
        });
        jMenu_Application.add(jMenuItem_Exit);

        jMenuBar1.add(jMenu_Application);

        jMenu_Aide.setText(bundle.getString("MainWindows.jMenu_Aide.text")); // NOI18N

        jMenuItem_documentation.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem_documentation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/32x32/Graphite Globe.png"))); // NOI18N
        jMenuItem_documentation.setText(bundle.getString("MainWindows.jMenuItem_documentation.text")); // NOI18N
        jMenuItem_documentation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_documentationActionPerformed(evt);
            }
        });
        jMenu_Aide.add(jMenuItem_documentation);

        jMenuItem_apropos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        jMenuItem_apropos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/32x32/info.png"))); // NOI18N
        jMenuItem_apropos.setText(bundle.getString("MainWindows.jMenuItem_apropos.text")); // NOI18N
        jMenuItem_apropos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_aproposActionPerformed(evt);
            }
        });
        jMenu_Aide.add(jMenuItem_apropos);

        jMenuBar1.add(jMenu_Aide);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(848, 467));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_ExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem_ExitActionPerformed

    private void jMenuItem_aproposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_aproposActionPerformed
        /*
         new JD_About(this, rootPaneCheckingEnabled).setVisible(true);
         */
        jDialog_About.setSize(488, 254);
        jDialog_About.setLocationRelativeTo(this);
        //jDialog_about.setModal(true);
        jDialog_About.setVisible(true);
    }//GEN-LAST:event_jMenuItem_aproposActionPerformed

    private void jTableEtudiantsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableEtudiantsFocusLost
        //jButtonPrintEtudiants.setVisible(false);
        //jButtonDeleteEtudiants.setVisible(false);
    }//GEN-LAST:event_jTableEtudiantsFocusLost

    private void jTableEtudiantsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableEtudiantsMouseReleased
        int[] nbRows = jTableEtudiants.getSelectedRows();
        if (nbRows.length > 1) {
            jButtonPrintEtudiants.setVisible(true);
            jButtonDeleteEtudiants.setVisible(true);
        } else {
            jButtonDeleteEtudiants.setVisible(false);
        }
    }//GEN-LAST:event_jTableEtudiantsMouseReleased

    private void jTableEtudiantsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableEtudiantsMouseClicked
        int rowNumero = jTableEtudiants.getSelectedRow();
        int id = (int) jTableEtudiants.getValueAt(rowNumero, 1);
        etudiant = etudiantJpaController.findEtudiant(id);
        jTextField_Nom1.setText(etudiant.getNom());
        jTextField_Prenom1.setText(etudiant.getPrenom());
        jTextField_Nom1.setText(etudiant.getNom());
        //jComboBox_Groupes.setSelectedIndex(grpIndex);
        int sexeIndex = etudiant.getSexe().compareToIgnoreCase("H") == 0 
                || etudiant.getSexe().compareToIgnoreCase("M") == 0 ? 0 : 1;
        jComboBox_Sexes1.setSelectedIndex(sexeIndex);
        // chargement de la comboBox_groupes
        jComboBox_Groupes1.setModel(new DefaultComboBoxModel(groupes.toArray()));
        jComboBox_Groupes1.setSelectedItem((Groupe) etudiant.getGroupe());
        
        jDialog_UpdateEtudiant.setSize(341, 374);
        jDialog_UpdateEtudiant.setLocationRelativeTo(this);
        //jDialog_UpdateEtudiant.setTitle(Constante.APP_NAME + " - Modifier un etudiant");
        jDialog_UpdateEtudiant.setModal(true);
        jDialog_UpdateEtudiant.setVisible(true);
        
    }//GEN-LAST:event_jTableEtudiantsMouseClicked

    private void jTableGroupesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableGroupesFocusLost
        //jButtonDeleteGroupe.setVisible(false);
        //jButtonUpdateGroupe.setVisible(false);
    }//GEN-LAST:event_jTableGroupesFocusLost

    private void jTableGroupesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableGroupesMouseReleased

        System.out.println("groupe selectionné");
        
        int[] nbRows = jTableGroupes.getSelectedRows();
        etudiants.clear();

        if (nbRows.length > 1) {
            jButtonUpdateGroupe.setEnabled(false);
            for (int idRow : nbRows) {
                int id = (int) jTableGroupes.getValueAt(idRow, 1);
                System.out.println("id de groupe" + id);
                Groupe groupe = new Groupe();
                groupe = groupeJpaController.findGroupe(id);
                jLabel1.setText("Liste de etudiants");
                boolean addAll = etudiants.addAll(groupe.getEtudiantList()) /*
                         for (Etudiant e : groupe.getEtudiantList()) {
                         etudiants.add(e);
                         }
                         */;
            }
            refreshEtudiantsTable();
            if (etudiants.size() > 0) {

            } else {
                System.out.println("Aucune etudiant enregistre dans ces groupes selectionnes:");
            }
            jButtonDeleteGroupe.setVisible(true);
        } else {
            jButtonUpdateGroupe.setEnabled(true);
        }
    }//GEN-LAST:event_jTableGroupesMouseReleased

    private void jTableGroupesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableGroupesMouseClicked
        
        int row = jTableGroupes.getSelectedRow();
        int id = (int) jTableGroupes.getValueAt(row, 1);
        groupe = groupeJpaController.findGroupe(id);
        System.out.println("groupe selectionné "+ groupe);
        //Masquer a cause de l internationalisation
        //jLabel1.setText("Liste de etudiants " + groupe.getCode());
        etudiants = groupe.getEtudiantList();
        refreshEtudiantsTable();
        if (etudiants.size() > 0) {
        } else {
            System.out.println("Aucune etudiant enregistré dans ce groupe: " + groupe);
        }
    }//GEN-LAST:event_jTableGroupesMouseClicked

    private void jButtonDeleteGroupeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteGroupeActionPerformed
        int[] rows = jTableGroupes.getSelectedRows();
        for (int row : rows) {
            int id = (int) jTableGroupes.getValueAt(row, 1);
            try {
                groupeJpaController.destroy(id);
                System.out.println("groupe supprimé");
            } catch (NonexistentEntityException ex) {
                System.out.println("Erreur de suppression");
                Logger.getLogger(MainWindows.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        refreshGroupesTable();
    }//GEN-LAST:event_jButtonDeleteGroupeActionPerformed

    private void jButtonUpdateGroupeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateGroupeActionPerformed
        //int row = jTableGroupes.getSelectedRow();
        //int id = (int) jTableGroupes.getValueAt(row, 1);
        //groupe = groupeJpaController.findGroupe(id);
        jTextField_Code1.setText(groupe.getCode());
        jTextField_Libelle1.setText(groupe.getLibelle());
        //jDialog_UpdateGroupe.setTitle(Constante.APP_NAME + " - Modifier un etudiant");
        jDialog_UpdateGroupe.setSize(341, 250);
        jDialog_UpdateGroupe.setModal(true);
        jDialog_UpdateGroupe.setLocationRelativeTo(this);
        jDialog_UpdateGroupe.setVisible(true);
        refreshGroupesTable();
    }//GEN-LAST:event_jButtonUpdateGroupeActionPerformed

    private void jButtonDeleteEtudiantsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteEtudiantsActionPerformed
        int[] nbRows = jTableEtudiants.getSelectedRows();
        if (nbRows.length > 1) {
            for (int i = 0; i < nbRows.length; i++) {
                try {
                    int id = (int) jTableEtudiants.getValueAt(i, 1);
                    etudiantJpaController.destroy(id);
                } catch (NonexistentEntityException ex) {
                    System.out.println("Erreur de suppression");
                    Logger.getLogger(MainWindows.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            refreshEtudiantsTable();
        } else {
            jButtonDeleteEtudiants.setVisible(false);
        }
    }//GEN-LAST:event_jButtonDeleteEtudiantsActionPerformed

    private void jButtonPrintEtudiantsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintEtudiantsActionPerformed
        System.out.println("Impression de la table etudiant");
        int row = jTableGroupes.getSelectedRow();
        int id = (int) jTableGroupes.getValueAt(row, 1);
        new Printer().viewInGroupeEtudiants(id);
    }//GEN-LAST:event_jButtonPrintEtudiantsActionPerformed

    private void jTextRechercherEtudiantKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextRechercherEtudiantKeyReleased
        String text = jTextRechercherEtudiant.getText();
        List<Etudiant> etudiantsTrouves = new ArrayList<Etudiant>();
        //etudiantsTrouves.clear();
        if (text.length() >= 1) {
            String[] tokens = text.trim().split(" ");
            if (tokens.length <= 1) {
                text = tokens[0];
                for (Etudiant e : etudiants) {
                    if (e.getNom().toLowerCase().contains(text.toLowerCase()) || e.getPrenom().toLowerCase().contains(text.toLowerCase())) {
                        etudiantsTrouves.add(e);
                    }
                }
            } else if(tokens.length >= 2) {
                for (Etudiant e : etudiants) {
                    String nom = tokens[0];
                    String prenom = tokens[1];
                    if (e.getNom().toLowerCase().contains(nom.toLowerCase()) && e.getPrenom().toLowerCase().contains(prenom.toLowerCase())) {
                        etudiantsTrouves.add(e);
                    }
                }
            }
            // TODO  implemeter la methode find by name
            refreshEtudiantsTable(etudiantsTrouves);
        } else if (text.length() == 0) {
            refreshEtudiantsTable();
        }
    }//GEN-LAST:event_jTextRechercherEtudiantKeyReleased

    private void jTextRechercherEtudiantFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextRechercherEtudiantFocusLost
        jTextRechercherEtudiant.setText("Rechercher un etudiant");
        jTextRechercherEtudiant.setOpaque(false);
    }//GEN-LAST:event_jTextRechercherEtudiantFocusLost

    private void jTextRechercherEtudiantFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextRechercherEtudiantFocusGained
        jTextRechercherEtudiant.setText("");
        jTextRechercherEtudiant.setOpaque(true);
    }//GEN-LAST:event_jTextRechercherEtudiantFocusGained

    private void jButtonCreateEtudiantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateEtudiantActionPerformed
        
        jComboBox_Groupes.setModel(new DefaultComboBoxModel(groupes.toArray()));
        jComboBox_Groupes.setSelectedItem((Groupe) groupe);
        jDialog_CreateEtudiant.setSize(341, 374);
        jDialog_CreateEtudiant.setLocationRelativeTo(this);
        //jDialog_CreateEtudiant.setTitle(Constante.APP_NAME + " - Ajouter un etudiant");
        jDialog_CreateEtudiant.setModal(true);
        jDialog_CreateEtudiant.setVisible(true);
    }//GEN-LAST:event_jButtonCreateEtudiantActionPerformed

    private void jButtonCreateGroupeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateGroupeActionPerformed

      
        jDialog_CreateGroupe.setVisible(true);
        jDialog_CreateGroupe.setLocationRelativeTo(this);
        jDialog_CreateGroupe.setSize(324, 250);
        jDialog_CreateGroupe.setModal(true);

    }//GEN-LAST:event_jButtonCreateGroupeActionPerformed

    private void jMenuItem_rapporter_tous_etudiantsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_rapporter_tous_etudiantsActionPerformed
        new Printer().view();
    }//GEN-LAST:event_jMenuItem_rapporter_tous_etudiantsActionPerformed

    private void jMenuItem_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_refreshActionPerformed
        init();
    }//GEN-LAST:event_jMenuItem_refreshActionPerformed

    private void jButton_demarrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_demarrerActionPerformed
        layout.next(jPanel_root);
        jMenuBar1.setVisible(true);
    }//GEN-LAST:event_jButton_demarrerActionPerformed

    private void jXHyperlink_followUsOnGoogleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXHyperlink_followUsOnGoogleActionPerformed
        Outils.Linker.openUrlTODefaultBrowser(Controllers.Constante.LINK_GOOGLE);
    }//GEN-LAST:event_jXHyperlink_followUsOnGoogleActionPerformed

    private void jXHyperlink_followUsOnFacebookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXHyperlink_followUsOnFacebookActionPerformed
        Outils.Linker.openUrlTODefaultBrowser(Controllers.Constante.LINK_FACEBOOK);
    }//GEN-LAST:event_jXHyperlink_followUsOnFacebookActionPerformed

    private void jLabel_contactMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_contactMouseClicked
        Outils.Linker.mailTo();

    }//GEN-LAST:event_jLabel_contactMouseClicked

    private void jLabel_contactMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_contactMouseEntered
        jLabel_contact.setCursor(new Cursor(HAND_CURSOR));
    }//GEN-LAST:event_jLabel_contactMouseEntered

    private void jXHyperlink_followUsOnLinkedinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXHyperlink_followUsOnLinkedinActionPerformed
        Outils.Linker.openUrlTODefaultBrowser(Controllers.Constante.LINK_LINKEDIN);
    }//GEN-LAST:event_jXHyperlink_followUsOnLinkedinActionPerformed

    private void jXHyperlink1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXHyperlink1ActionPerformed
        jDialog_About.dispose();
    }//GEN-LAST:event_jXHyperlink1ActionPerformed

    private void jMenuItem_documentationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_documentationActionPerformed
        try {
            Linker.openFile(Constante.USER_GUIDE);
        } catch (IOException ex) {
            Logger.getLogger(MainWindows.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem_documentationActionPerformed

    private void jMenuItem_anglaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_anglaisActionPerformed
        System.out.println("langue anglaise");
        changeLocale("en");
    }//GEN-LAST:event_jMenuItem_anglaisActionPerformed

    private void jMenuItem_francaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_francaisActionPerformed
        System.out.println("langue francaise");
        bundle = ResourceBundle.getBundle("gui/Bundle"); // NOI18N
        reInitComponentsText();
    }//GEN-LAST:event_jMenuItem_francaisActionPerformed

    private void jButton_ValiderCreateGroupeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ValiderCreateGroupeActionPerformed
        // TODO add your handling code here:
        Groupe groupe = new Groupe();
        groupe.setCode(jTextField_Code.getText());
        groupe.setLibelle(jTextField_Libelle.getText());
        if (groupe.getCode().isEmpty()) {
            jTextField_Code.setBackground(Color.orange);
            JOptionPane.showMessageDialog(this, "Le code est requis.", "warning", JOptionPane.WARNING_MESSAGE);
        } else {
            jTextField_Code.setBackground(Color.white);
            // recherche du groupe
            boolean exists = (groupeJpaController.findByCode(groupe.getCode()) != null);
            if (exists) {
                JOptionPane.showMessageDialog(this, "Un groupe est déja enregistré avec ce code", "info", 1);
            } else {
                try {
                    groupeJpaController.create(groupe);
                    refreshGroupesTable();
                    JOptionPane.showMessageDialog(this, "groupe enregistré avec succes", "info", 1);
                    jDialog_CreateGroupe.dispose();
                } catch (Exception ex) {
                    System.out.println("erreur d'enreg du groupe " + groupe);
                }
            }

        }
    }//GEN-LAST:event_jButton_ValiderCreateGroupeActionPerformed

    private void jButton_AnnulerCreateGroupeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnnulerCreateGroupeActionPerformed
        
        jDialog_CreateGroupe.dispose();
        jTextField_Code.setText("");
        jTextField_Libelle.setText("");
    }//GEN-LAST:event_jButton_AnnulerCreateGroupeActionPerformed

    private void jButton_ValiderUpdateGroupeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ValiderUpdateGroupeActionPerformed
        // TODO add your handling code here:
        groupe.setCode(jTextField_Code1.getText());
        groupe.setLibelle(jTextField_Libelle1.getText());
        if (groupe.getCode().isEmpty()) {
            jTextField_Code1.setBackground(Color.orange);
            JOptionPane.showMessageDialog(this, "Le code est requis.", "warning", JOptionPane.WARNING_MESSAGE);
        } else {
            jTextField_Code1.setBackground(Color.white);
            // recherche du groupe
            boolean exists = (groupeJpaController.findByCode(groupe.getCode()) != null);
            if (exists) {
                JOptionPane.showMessageDialog(this, "Un groupe est déja enregistré avec ce code", "info", 1);
            } else {
                try {
                    groupeJpaController.edit(groupe);
                    refreshGroupesTable();
                    JOptionPane.showMessageDialog(this, "groupe modifié avec succes", "info", 1);
                    jDialog_UpdateGroupe.dispose();
                } catch (Exception ex) {
                    System.out.println("erreur de modification du groupe " + groupe);
                }
            }

        }
    }//GEN-LAST:event_jButton_ValiderUpdateGroupeActionPerformed

    private void jButton_AnnulerUpdateGroupeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnnulerUpdateGroupeActionPerformed
        // TODO add your handling code here:
        jDialog_UpdateGroupe.dispose();
        jTextField_Code1.setText("");
        jTextField_Libelle1.setText("");
    }//GEN-LAST:event_jButton_AnnulerUpdateGroupeActionPerformed

    private void jComboBox_GroupesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_GroupesItemStateChanged
        // TODO add your handling code here:
        System.out.println("change");
    }//GEN-LAST:event_jComboBox_GroupesItemStateChanged

    private void jComboBox_GroupesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_GroupesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_GroupesActionPerformed

    private void jButton_ValiderCreateEtudiantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ValiderCreateEtudiantActionPerformed

        Etudiant etudiant = new Etudiant();
        etudiant.setNom(jTextField_Nom.getText());
        etudiant.setPrenom(jTextField_Prenom.getText());
        etudiant.setGroupe((Groupe) jComboBox_Groupes.getSelectedItem());
        Calendar dateNaiss = dateChooserCombo.getSelectedDate();
        etudiant.setDateNaiss(dateNaiss.getTime());
        if (etudiant.getNom().isEmpty()) {
            jTextField_Nom.setBackground(Color.orange);
            JOptionPane.showMessageDialog(this, "Le nom est requis.", "warning", JOptionPane.WARNING_MESSAGE);
        } else {
            jTextField_Nom.setBackground(Color.white);

            // recherche de l'existance
            boolean exists = etudiantJpaController.findByNomAndPrenom(etudiant) != null;
            if (exists) {
                JOptionPane.showMessageDialog(this, "Cet etudiant est déja enregistré", "warning", JOptionPane.WARNING_MESSAGE);
            } else {
                //setSexe
                int choix = jComboBox_Sexes.getSelectedIndex();
                if (choix == -1) {
                } else {
                    if (choix == 0) {
                        etudiant.setSexe("H");
                    } else {
                        etudiant.setSexe("F");
                    }
                }
                try {
                    etudiantJpaController.create(etudiant);
                    refreshEtudiantsTable();
                    JOptionPane.showMessageDialog(this, "etudiant enregistré avec succes", "info", 1);
                    jDialog_CreateEtudiant.dispose();
                } catch (Exception ex) {
                    System.out.println("erreur d'enreg de l'etudiant " + etudiant.getNom());
                }
            }
        }
    }//GEN-LAST:event_jButton_ValiderCreateEtudiantActionPerformed

    private void jButton_AnnulerCreateEtudiantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnnulerCreateEtudiantActionPerformed
        jTextField_Nom.setText("");
                jTextField_Prenom.setText("");
        jDialog_CreateEtudiant.dispose();
    }//GEN-LAST:event_jButton_AnnulerCreateEtudiantActionPerformed

    private void jComboBox_Sexes1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_Sexes1ItemStateChanged
        int choix = jComboBox_Sexes1.getSelectedIndex();
        if (choix == -1) {
        } else {
            if (choix == 0) {
                etudiant.setSexe("H");
            } else {
                etudiant.setSexe("F");
            }
        }
    }//GEN-LAST:event_jComboBox_Sexes1ItemStateChanged

    private void jComboBox_Groupes1sActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_Groupes1sActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_Groupes1sActionPerformed

    private void jButton_ValiderUpdateEtudiantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ValiderUpdateEtudiantActionPerformed

        etudiant.setNom(jTextField_Nom1.getText());
        etudiant.setPrenom(jTextField_Prenom1.getText());
        etudiant.setGroupe((Groupe) jComboBox_Groupes1.getSelectedItem());
        Calendar dateNaiss = dateChooserCombo.getSelectedDate();
        etudiant.setDateNaiss(dateNaiss.getTime());
        //setSexe
        String sexe = jComboBox_Sexes1.getSelectedIndex() == 0 ? "M" : "F";
        etudiant.setSexe(sexe);
        if (etudiant.getNom().isEmpty()) {
            jTextField_Nom1.setBackground(Color.orange);
            JOptionPane.showMessageDialog(this, "Le nom est requis.", "warning", JOptionPane.WARNING_MESSAGE);
        } else {
            jTextField_Nom1.setBackground(Color.white);
            //boolean exists = etudianJpaController.findByName(etudiant.getNom()) != null;
            boolean exists = etudiantJpaController.findByNameAndPrenomAndGroupe(etudiant) != null;
            if (exists) {
                JOptionPane.showMessageDialog(this, "Un etudiant est déja enregistré avec ce nom.", "info", 1);
            } else {
                try {
                    etudiantJpaController.edit(etudiant);
                    refreshEtudiantsTable();
                    JOptionPane.showMessageDialog(this, "Etudiant modifié avec succes", "info", 1);
                    jDialog_UpdateEtudiant.dispose();
                } catch (Exception ex) {
                    System.out.println("erreur de modification");
                }
            }

        }
    }//GEN-LAST:event_jButton_ValiderUpdateEtudiantActionPerformed

    private void jButton_AnnulerUpdateEtudiantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnnulerUpdateEtudiantActionPerformed
        jTextField_Nom1.setText("");
        jTextField_Prenom1.setText("");
        jDialog_UpdateEtudiant.dispose();
    }//GEN-LAST:event_jButton_AnnulerUpdateEtudiantActionPerformed

    private void jButtonDeleteEtudiantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteEtudiantActionPerformed
        try {
            etudiantJpaController.destroy(etudiant.getId());
            System.out.println("etudiant supprime");
        } catch (NonexistentEntityException ex) {
            System.out.println("Erreur de suppression");
        }
        refreshEtudiantsTable();
        jDialog_UpdateEtudiant.dispose();
    }//GEN-LAST:event_jButtonDeleteEtudiantActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindows.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindows.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindows.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindows.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainWindows.class.getName()).log(Level.SEVERE, null, ex);
                }
                new MainWindows().setVisible(true);
            }
        });
    }

    // mes Variables
    private Object[][] data;
    private static List<Groupe> groupes;
    private static List<Etudiant> etudiants;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo dateChooserCombo;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private javax.swing.JLabel header;
    private javax.swing.JButton jButtonCreateEtudiant;
    private javax.swing.JButton jButtonCreateGroupe;
    private javax.swing.JButton jButtonDeleteEtudiant;
    private javax.swing.JButton jButtonDeleteEtudiants;
    private javax.swing.JButton jButtonDeleteGroupe;
    private javax.swing.JButton jButtonPrintEtudiants;
    private javax.swing.JButton jButtonUpdateGroupe;
    private javax.swing.JButton jButton_AnnulerCreateEtudiant;
    private javax.swing.JButton jButton_AnnulerCreateGroupe;
    private javax.swing.JButton jButton_AnnulerUpdateEtudiant;
    private javax.swing.JButton jButton_AnnulerUpdateGroupe;
    private javax.swing.JButton jButton_ValiderCreateEtudiant;
    private javax.swing.JButton jButton_ValiderCreateGroupe;
    private javax.swing.JButton jButton_ValiderUpdateEtudiant;
    private javax.swing.JButton jButton_ValiderUpdateGroupe;
    private javax.swing.JButton jButton_demarrer;
    private javax.swing.JComboBox jComboBox_Groupes;
    private javax.swing.JComboBox jComboBox_Groupes1;
    private javax.swing.JComboBox jComboBox_Sexes;
    private javax.swing.JComboBox jComboBox_Sexes1;
    private javax.swing.JDialog jDialog_About;
    private javax.swing.JDialog jDialog_CreateEtudiant;
    private javax.swing.JDialog jDialog_CreateGroupe;
    private javax.swing.JDialog jDialog_UpdateEtudiant;
    private javax.swing.JDialog jDialog_UpdateGroupe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_Code;
    private javax.swing.JLabel jLabel_Code1;
    private javax.swing.JLabel jLabel_Datenaiss;
    private javax.swing.JLabel jLabel_Datenaiss1;
    private javax.swing.JLabel jLabel_Groupes;
    private javax.swing.JLabel jLabel_Groupes1;
    private javax.swing.JLabel jLabel_Libelle;
    private javax.swing.JLabel jLabel_Libelle1;
    private javax.swing.JLabel jLabel_Nom;
    private javax.swing.JLabel jLabel_Nom1;
    private javax.swing.JLabel jLabel_Prenom;
    private javax.swing.JLabel jLabel_Prenom1;
    private javax.swing.JLabel jLabel_Sexe;
    private javax.swing.JLabel jLabel_Sexe1;
    private javax.swing.JLabel jLabel_contact;
    private javax.swing.JLabel jLabel_logo;
    private javax.swing.JLabel jLabel_progression;
    private javax.swing.JLabel jLabel_spinner;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem_Exit;
    private javax.swing.JMenuItem jMenuItem_anglais;
    private javax.swing.JMenuItem jMenuItem_apropos;
    private javax.swing.JMenuItem jMenuItem_documentation;
    private javax.swing.JMenuItem jMenuItem_francais;
    private javax.swing.JMenuItem jMenuItem_rapporter_tous_etudiants;
    private javax.swing.JMenuItem jMenuItem_refresh;
    private javax.swing.JMenu jMenu_Aide;
    private javax.swing.JMenu jMenu_Application;
    private javax.swing.JMenu jMenu_langages;
    private javax.swing.JMenu jMenu_parametres;
    private javax.swing.JMenu jMenu_rapport;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel_header;
    private javax.swing.JPanel jPanel_progression;
    private javax.swing.JPanel jPanel_root;
    private javax.swing.JPanel jPanel_splathscreen;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTableEtudiants;
    private javax.swing.JTable jTableGroupes;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField_Code;
    private javax.swing.JTextField jTextField_Code1;
    private javax.swing.JTextField jTextField_Libelle;
    private javax.swing.JTextField jTextField_Libelle1;
    private javax.swing.JTextField jTextField_Nom;
    private javax.swing.JTextField jTextField_Nom1;
    private javax.swing.JTextField jTextField_Prenom;
    private javax.swing.JTextField jTextField_Prenom1;
    private javax.swing.JTextField jTextRechercherEtudiant;
    // End of variables declaration//GEN-END:variables

    private void refreshGroupesTable() {
        init();
    }

    public void refreshEtudiantsTable() {
        Groupe groupe = new Groupe();
        int rowNumero = jTableGroupes.getSelectedRow();
        if (rowNumero != -1) { // si un groupe est choisi
            int id = (int) jTableGroupes.getValueAt(rowNumero, 1);
            groupe = groupeJpaController.findGroupe(id);
        } else {  // si aucun groupe n'est choisi comme au lancement de l'application
            jLabel1.setText("Liste de etudiants dans " + groupes.get(0).getCode());
            groupe = groupeJpaController.findGroupe(groupes.get(0).getId());
        }
        refreshEtudiantsTable(groupe.getEtudiantList());
    }
    private Timer timer;

    public void runProgressBar() {
        jProgressBar1.setMaximum(100);
        jProgressBar1.setMinimum(0);
        jProgressBar1.setValue(0);
        //jProgressBar1.setString("Chargement...");
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int val = jProgressBar1.getValue(), avancement = 10;
                if (val % (2 * avancement) == 0) {
                    jLabel_progression.setText("Chargement...");
                } else {
                    jLabel_progression.setText("Loading...");
                }
                if (val < jProgressBar1.getMaximum()) {
                    val += avancement;
                    jProgressBar1.setValue(val);
                } else {
                    timer.stop();
                    jPanel_progression.setVisible(false);
                    jButton_demarrer.setVisible(true);
                }
            }
        };
        timer = new Timer(300, action);
        timer.start();
    }

    private void changeLocale(String language) {
        Locale l = new Locale(language);
        bundle = ResourceBundle.getBundle("gui/Bundle", l);
        reInitComponentsText();
    }

    private void reInitComponentsText() {
        jDialog_About.setTitle(bundle.getString("MainWindows.jDialog_About.title")); // NOI18N

        jLabel3.setText(bundle.getString("MainWindows.jLabel3.text")); // NOI18N

        jLabel3.setToolTipText(bundle.getString("MainWindows.jLabel3.toolTipText")); // NOI18N

        jLabel6.setText(bundle.getString("MainWindows.jLabel6.text")); // NOI18N

        jLabel5.setText(bundle.getString("MainWindows.jLabel5.text")); // NOI18N

        jTextArea1.setText(bundle.getString("MainWindows.jTextArea1.text")); // NOI18N

        jLabel7.setText(bundle.getString("MainWindows.jLabel7.text")); // NOI18N

        jLabel7.setToolTipText(bundle.getString("MainWindows.jLabel7.toolTipText")); // NOI18N

        jLabel8.setText(bundle.getString("MainWindows.jLabel8.text")); // NOI18N

        //jXHyperlink_followUsOnGoogle.setText(bundle.getString("MainWindows.jXHyperlink_followUsOnGoogle.text")); // NOI18N

       // jXHyperlink_followUsOnFacebook.setText(bundle.getString("MainWindows.jXHyperlink_followUsOnFacebook.text")); // NOI18N

        jLabel_contact.setText(bundle.getString("MainWindows.jLabel_contact.text")); // NOI18N

       // jXHyperlink_followUsOnLinkedin.setText(bundle.getString("MainWindows.jXHyperlink_followUsOnLinkedin.text")); // NOI18N

        //jXHyperlink1.setText(bundle.getString("MainWindows.jXHyperlink1.text")); // NOI18N

        jDialog_CreateGroupe.setTitle(bundle.getString("MainWindows.jDialog_CreateGroupe.title")); // NOI18N

        jLabel_Code.setText(bundle.getString("MainWindows.jLabel_Code.text")); // NOI18N

        jLabel_Libelle.setText(bundle.getString("MainWindows.jLabel_Libelle.text")); // NOI18N

        jButton_ValiderCreateGroupe.setText(bundle.getString("MainWindows.jButton_ValiderCreateGroupe.text")); // NOI18N

        jButton_AnnulerCreateGroupe.setText(bundle.getString("MainWindows.jButton_AnnulerCreateGroupe.text")); // NOI18N

        jLabel9.setText(bundle.getString("MainWindows.jLabel9.text")); // NOI18N

        jDialog_UpdateGroupe.setTitle(bundle.getString("MainWindows.jDialog_UpdateGroupe.title")); // NOI18N

        jLabel_Code1.setText(bundle.getString("MainWindows.jLabel_Code1.text")); // NOI18N

        jLabel_Libelle1.setText(bundle.getString("MainWindows.jLabel_Libelle1.text")); // NOI18N

        jButton_ValiderUpdateGroupe.setText(bundle.getString("MainWindows.jButton_ValiderUpdateGroupe.text")); // NOI18N

        jButton_AnnulerUpdateGroupe.setText(bundle.getString("MainWindows.jButton_AnnulerUpdateGroupe.text")); // NOI18N

        jLabel10.setText(bundle.getString("MainWindows.jLabel10.text")); // NOI18N

        jDialog_CreateEtudiant.setTitle(bundle.getString("MainWindows.jDialog_CreateEtudiant.title")); // NOI18N

        jLabel_Nom.setText(bundle.getString("MainWindows.jLabel_Nom.text")); // NOI18N

        jLabel_Prenom.setText(bundle.getString("MainWindows.jLabel_Prenom.text")); // NOI18N

        jLabel_Sexe.setText(bundle.getString("MainWindows.jLabel_Sexe.text")); // NOI18N

        jLabel_Groupes.setText(bundle.getString("MainWindows.jLabel_Groupes.text")); // NOI18N

        jButton_ValiderCreateEtudiant.setText(bundle.getString("MainWindows.jButton_ValiderCreateEtudiant.text")); // NOI18N

        jButton_AnnulerCreateEtudiant.setText(bundle.getString("MainWindows.jButton_AnnulerCreateEtudiant.text")); // NOI18N

        jLabel11.setText(bundle.getString("MainWindows.jLabel11.text")); // NOI18N

    jLabel_Datenaiss.setText(bundle.getString("MainWindows.jLabel_Datenaiss.text")); // NOI18N

    jDialog_UpdateEtudiant.setTitle(bundle.getString("MainWindows.jDialog_UpdateEtudiant.title")); // NOI18N

    jLabel_Nom1.setText(bundle.getString("MainWindows.jLabel_Nom1.text")); // NOI18N

    jLabel_Prenom1.setText(bundle.getString("MainWindows.jLabel_Prenom1.text")); // NOI18N

    jLabel_Sexe1.setText(bundle.getString("MainWindows.jLabel_Sexe1.text")); // NOI18N

    jLabel_Groupes1.setText(bundle.getString("MainWindows.jLabel_Groupes1.text")); // NOI18N

    jButton_ValiderUpdateEtudiant.setText(bundle.getString("MainWindows.jButton_ValiderUpdateEtudiant.text")); // NOI18N

    jButton_AnnulerUpdateEtudiant.setText(bundle.getString("MainWindows.jButton_AnnulerUpdateEtudiant.text")); // NOI18N

    jLabel12.setText(bundle.getString("MainWindows.jLabel12.text")); // NOI18N

    jButtonDeleteEtudiant.setText(bundle.getString("MainWindows.jButtonDeleteEtudiant.text")); // NOI18N

    jLabel_Datenaiss1.setText(bundle.getString("MainWindows.jLabel_Datenaiss1.text")); // NOI18N

    setTitle(bundle.getString("MainWindows.title")); // NOI18N

    jLabel_progression.setText(bundle.getString("MainWindows.jLabel_progression.text")); // NOI18N

    jButton_demarrer.setText(bundle.getString("MainWindows.jButton_demarrer.text")); // NOI18N

    jLabel2.setText(bundle.getString("MainWindows.jLabel2.text")); // NOI18N

    jButtonCreateGroupe.setToolTipText(bundle.getString("MainWindows.jButtonCreateGroupe.toolTipText")); // NOI18N

    jLabel1.setText(bundle.getString("MainWindows.jLabel1.text")); // NOI18N

    jButtonCreateEtudiant.setText(bundle.getString("MainWindows.jButtonCreateEtudiant.text")); // NOI18N

    jTextRechercherEtudiant.setText(bundle.getString("MainWindows.jTextRechercherEtudiant.text")); // NOI18N

    jButtonPrintEtudiants.setText(bundle.getString("MainWindows.jButtonPrintEtudiants.text")); // NOI18N

    jButtonDeleteEtudiants.setText(bundle.getString("MainWindows.jButtonDeleteEtudiants.text")); // NOI18N

    jMenu_Application.setText(bundle.getString("MainWindows.jMenu_Application.text")); // NOI18N

    jMenuItem_refresh.setText(bundle.getString("MainWindows.jMenuItem_refresh.text")); // NOI18N

    jMenu_rapport.setText(bundle.getString("MainWindows.jMenu_rapport.text")); // NOI18N

    jMenuItem_rapporter_tous_etudiants.setText(bundle.getString("MainWindows.jMenuItem_rapporter_tous_etudiants.text")); // NOI18N

    jMenu_parametres.setText(bundle.getString("MainWindows.jMenu_parametres.text")); // NOI18N

    jMenu_langages.setText(bundle.getString("MainWindows.jMenu_langages.text")); // NOI18N

    jMenuItem_anglais.setText(bundle.getString("MainWindows.jMenuItem_anglais.text")); // NOI18N

    jMenuItem_francais.setText(bundle.getString("MainWindows.jMenuItem_francais.text")); // NOI18N

    jMenuItem_Exit.setText(bundle.getString("MainWindows.jMenuItem_Exit.text")); // NOI18N

    jMenu_Aide.setText(bundle.getString("MainWindows.jMenu_Aide.text")); // NOI18N

    jMenuItem_documentation.setText(bundle.getString("MainWindows.jMenuItem_documentation.text")); // NOI18N

    jMenuItem_apropos.setText(bundle.getString("MainWindows.jMenuItem_apropos.text")); // NOI18N

    }

}
