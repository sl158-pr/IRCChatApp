package client;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * IRCWindow.java
 *
 */
import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import networking.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author 
 */
public class IRCChatWindow extends javax.swing.JFrame {

    // App icons
    Image appImage;
    // Tray
    TrayIcon trayIcon;
    PopupMenu popup, serverPopup;
    // Sound
    private static URL soundResource;

    /** Creates new form IRCWindow */
    public IRCChatWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IRCChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(IRCChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(IRCChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(IRCChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setVisible(true);
        initComponents();
        initIcon();
        initTray();
        initAbout();
    }

    private void initIcon() {
        try {
            URL url = getClass().getResource("/resources/appicon.png");
            System.out.println(url);
            Toolkit kit = Toolkit.getDefaultToolkit();
            appImage = kit.createImage(url);
            this.setIconImage(appImage);
        } catch (Exception e) {
        }
    }

    private void initTray() {
        if (SystemTray.isSupported()) {

            SystemTray tray = SystemTray.getSystemTray();
            ActionListener exitListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            };

            ActionListener aboutListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    aboutDialog.setVisible(true);
                }
            };

            popup = new PopupMenu();
            serverPopup = new PopupMenu();
            serverPopup.setLabel("Servers");
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(exitListener);

            MenuItem aboutItem = new MenuItem("About");
            aboutItem.addActionListener(aboutListener);

            popup.add(serverPopup);
            popup.add(aboutItem);
            popup.add(exitItem);

            trayIcon = new TrayIcon(appImage, "Java IRC Client", popup);

            ActionListener actionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
//                    trayIcon.displayMessage("Action Event",
//                            "An Action Event Has Been Performed!",
//                            TrayIcon.MessageType.INFO);
                    getFrame().setVisible(true);
                }
            };

            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }

        } else {
            //  System Tray is not supported
        }
    }

    public void updateServerItems() {
        IRCChatController con;

        for (int i = 0, max = serverPopup.getItemCount(); i < max; i++) {
            serverPopup.remove(i);
        }

        for (int i = 0, max = jTabbedPane1.getTabCount(); i < max; i++) {
            con = (IRCChatController) jTabbedPane1.getComponentAt(i);
            con.setIndex(i);
            final int index = con.getIndex();
            ActionListener actionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setActiveTab(index);
                }
            };

            MenuItem menuItem = new MenuItem(con.getServer());
            menuItem.addActionListener(actionListener);
            serverPopup.insert(menuItem, con.getIndex());
        }
    }

    public void removeServerItem(int index) {
        serverPopup.remove(index);
    }

    private void initAbout() {

//             JOptionPane.showMessageDialog(new JFrame(), "You have been disconnected from " + resource, "t", JOptionPane.ERROR_MESSAGE);

        JLabel picLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/about.png")));
        picLabel.setBounds(0, 0, 130, 177);
        imagePanel.add(picLabel);

    }

    private JFrame getFrame() {
        return this;
    }

    private void setActiveTab(int index) {
        this.setVisible(true);
        jTabbedPane1.setSelectedIndex(index);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        connectionDialog = new javax.swing.JDialog();
        serverField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        nicknameField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        channelField = new javax.swing.JTextField();
        aboutDialog = new javax.swing.JDialog();
        javax.swing.JLabel appTitleLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel versionLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel appVersionLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel vendorLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel appVendorLabel1 = new javax.swing.JLabel();
        homepageLabel1 = new javax.swing.JLabel();
        appHomepageLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel appDescLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel1 = new javax.swing.JLabel();
        imagePanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        soundToggle = new javax.swing.JCheckBoxMenuItem();
        debugToggle = new javax.swing.JCheckBoxMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        connectionDialog.setLocationRelativeTo(null);
        connectionDialog.setTitle("Connect to server");
        connectionDialog.setAlwaysOnTop(true);
        connectionDialog.setMinimumSize(new java.awt.Dimension(300, 180));
        connectionDialog.setResizable(false);

        serverField.setText("localhost");

        jLabel1.setText("Server:");

        jLabel2.setText("Nickname:");

        jLabel3.setText("Port:");

        portField.setText("9990");

        nicknameField.setText("nick");
        nicknameField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nicknameFieldActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Channel:");

        channelField.setText("#advSE");
        channelField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channelFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout connectionDialogLayout = new javax.swing.GroupLayout(connectionDialog.getContentPane());
        connectionDialog.getContentPane().setLayout(connectionDialogLayout);
        connectionDialogLayout.setHorizontalGroup(
                connectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, connectionDialogLayout.createSequentialGroup().addContainerGap(130, Short.MAX_VALUE).addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addContainerGap()).addGroup(connectionDialogLayout.createSequentialGroup().addGap(37, 37, 37).addGroup(connectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel4).addComponent(jLabel3).addComponent(jLabel1).addComponent(jLabel2)).addGap(18, 18, 18).addGroup(connectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(channelField).addComponent(nicknameField).addComponent(serverField, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE).addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(61, Short.MAX_VALUE)));
        connectionDialogLayout.setVerticalGroup(
                connectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(connectionDialogLayout.createSequentialGroup().addContainerGap().addGroup(connectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(serverField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(connectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)).addGap(18, 18, 18).addGroup(connectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nicknameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(connectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(channelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(connectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelButton).addComponent(okButton)).addContainerGap()));

        aboutDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        aboutDialog.setTitle("About");
        aboutDialog.setModal(true);
        aboutDialog.setResizable(false);
        aboutDialog.setSize(480, 200);

        appTitleLabel1.setFont(appTitleLabel1.getFont().deriveFont(appTitleLabel1.getFont().getStyle() | java.awt.Font.BOLD, appTitleLabel1.getFont().getSize() + 4));
        appTitleLabel1.setText("Java IRC Client");

        versionLabel1.setFont(versionLabel1.getFont().deriveFont(versionLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        versionLabel1.setText("Version:");

        appVersionLabel1.setText("0.1");

        vendorLabel1.setFont(vendorLabel1.getFont().deriveFont(vendorLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        vendorLabel1.setText("Authors:");
        
        appVendorLabel1.setText("Sumanth Lakshminarayana");

        appDescLabel1.setText("<html>A simple Java-based IRC client");

        imagePanel.setPreferredSize(new java.awt.Dimension(130, 177));

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
                imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 130, Short.MAX_VALUE));
        imagePanelLayout.setVerticalGroup(
                imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 189, Short.MAX_VALUE));

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout aboutDialogLayout = new javax.swing.GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
                aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(aboutDialogLayout.createSequentialGroup().addComponent(imageLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, aboutDialogLayout.createSequentialGroup().addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(versionLabel1).addComponent(vendorLabel1).addComponent(homepageLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(appVersionLabel1).addComponent(appVendorLabel1).addComponent(appHomepageLabel1))).addComponent(appTitleLabel1, javax.swing.GroupLayout.Alignment.LEADING).addComponent(appDescLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE).addComponent(closeButton)).addContainerGap()));
        aboutDialogLayout.setVerticalGroup(
                aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(imageLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE).addGroup(aboutDialogLayout.createSequentialGroup().addContainerGap().addComponent(appTitleLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(appDescLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(versionLabel1).addComponent(appVersionLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(vendorLabel1).addComponent(appVendorLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(homepageLabel1).addComponent(appHomepageLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(closeButton).addContainerGap(56, Short.MAX_VALUE)).addGroup(aboutDialogLayout.createSequentialGroup().addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE).addContainerGap()));

        setTitle("IRC Client");

        jMenu1.setText("Command");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Connect");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Disconnect");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator1);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("About");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Save chat");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE));

        pack();
    }// </editor-fold>

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        nicknameField.setText("nick");
        connectionDialog.setVisible(true);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        connectionDialog.setVisible(false);
    }

    private void nicknameFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void soundToggleActionPerformed(java.awt.event.ActionEvent evt) {
        soundEnabled = soundToggle.getState();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        connectionDialog.setVisible(false);
        IRCChatController conn = new IRCChatController(serverField.getText(), portField.getText(), nicknameField.getText(), channelField.getText(), jTabbedPane1, trayIcon, this);
        conn.setDebug(debugToggle.getState());
        updateServerItems();
    }

    private void channelFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTabbedPane1.getTabCount() > 0) {
            IRCChatController con = (IRCChatController) jTabbedPane1.getSelectedComponent();
            serverPopup.remove(con.getIndex());
            con.shutdown();
            jTabbedPane1.remove(jTabbedPane1.getSelectedIndex());
            updateServerItems();
        }
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
        aboutDialog.setVisible(true);
    }

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        aboutDialog.setVisible(false);
    }

    private void debugToggleActionPerformed(java.awt.event.ActionEvent evt) {
        for (int i = 0, max = jTabbedPane1.getTabCount(); i < max; i++) {
            IRCChatController con = (IRCChatController) jTabbedPane1.getComponentAt(i);
            con.setDebug(debugToggle.getState());
        }
        debugEnabled = debugToggle.getState();
    }

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTabbedPane1.getTabCount() > 0) {
            IRCChatController con = (IRCChatController) jTabbedPane1.getSelectedComponent();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            class CustomFileFilter extends javax.swing.filechooser.FileFilter {

                @Override
                public boolean accept(File file) {
                    // Allow only directories, or files with ".txt" extension
                    return file.isDirectory() || file.getAbsolutePath().endsWith(".txt");
                }

                @Override
                public String getDescription() {
                    // This description will be displayed in the dialog,
                    // hard-coded = ugly, should be done via I18N
                    return "Text documents (*.txt)";
                }
            }
            fileChooser.setFileFilter(new CustomFileFilter());


            int returnVal = fileChooser.showSaveDialog(null);
            Writer writer = null;

            try {
                File file = new File(fileChooser.getSelectedFile() + ".txt");
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(con.getOutputPane().getText());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new IRCChatWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify
    private javax.swing.JDialog aboutDialog;
    private javax.swing.JLabel appHomepageLabel1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField channelField;
    private javax.swing.JButton closeButton;
    private javax.swing.JDialog connectionDialog;
    private javax.swing.JCheckBoxMenuItem debugToggle;
    private javax.swing.JLabel homepageLabel1;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField nicknameField;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField portField;
    private javax.swing.JTextField serverField;
    private javax.swing.JCheckBoxMenuItem soundToggle;
    // End of variables declaration
    private static boolean soundEnabled = true;
    private static boolean debugEnabled = true;
}