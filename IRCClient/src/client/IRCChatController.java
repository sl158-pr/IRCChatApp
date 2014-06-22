package client;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.a
 */

/*
 * IRCConnection.java

 */
import networking.IRCSListener;
import networking.IRCCHandler;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.TrayIcon;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 */
public class IRCChatController extends javax.swing.JPanel implements
		IRCSListener {

	private Thread thread;
	private IRCChatWindow mainWindow;
	private JTabbedPane parentPane;
	private JFrame debugFrame;
	private JTextPane debugPane;
	private IRCCHandler ch;
	private String nickname, channel, server;
	private int port;
	private boolean debugEnabled = true;
	private ChatConsole pd;
	private int tabIndex;
	private TrayIcon ti;
	// History
	private ArrayList<String> history;
	private String origString;
	private int historyPos = 0;
	// FONTS
	static SimpleAttributeSet BLACK = new SimpleAttributeSet();
	static SimpleAttributeSet GREEN = new SimpleAttributeSet();
	static SimpleAttributeSet BLUE = new SimpleAttributeSet();

	/** Creates new form IRCConnection */
	public IRCChatController() {
		initComponents();
	}

	public IRCChatController(String server, String port, String nickname,
			String channel, JTabbedPane parentPane, TrayIcon ti,
			IRCChatWindow main) {
		initComponents();
		this.mainWindow = main;
		this.ti = ti;
		this.parentPane = parentPane;
		this.nickname = nickname;
		this.server = server;
		this.channel = channel;

		history = new ArrayList<String>();
		pd = new ChatConsole();
		this.port = Integer.parseInt(port);
		ch = new IRCCHandler(server, this.port);
		ch.addSocketListener(this);

		tabIndex = parentPane.getTabCount();
		parentPane.insertTab(server, null, this, server, tabIndex);
		inputPane.requestFocus();

	}

	private String getIrcString(String input) {
		String i = input.trim();
		String output = "";
		if (i.indexOf("/") == 0) {
			String text = i.substring(1);
			String command = text.split(" ")[0];
			String args = text.substring(text.indexOf(" ") + 1);
			debug("c: " + command);
			debug("a: " + args);
			if (command.equalsIgnoreCase("JOIN")
					|| command.equalsIgnoreCase("J")) {
				output = "PART " + channel + "\r\n" + "JOIN " + args;
				channel = args;
			} else if (command.equalsIgnoreCase("PART")
					|| command.equalsIgnoreCase("P")) {
				output = "PART " + channel;
			} else if (command.equalsIgnoreCase("QUIT")
					|| command.equalsIgnoreCase("Q")) {
				output = "QUIT";
			} else if (command.equalsIgnoreCase("NAMES")) {
				output = "NAMES " + channel;
			} else if (command.equalsIgnoreCase("NICK")) {
				writeOutput(nickname + " is now known as " + args);
				nickname = args;
				output = "NICK " + args + "\r\nNAMES " + channel;
			} else if (command.equalsIgnoreCase("COM")) {
				output = args;
			}
		} else {
			debug("text: " + i);
			output = "PRIVMSG " + channel + " :" + i;
		}
		debug("output: " + output);
		return output;
	}

	private void parseData(String input) {
		String i = input.trim();
		String output = i;

		if (i.startsWith(":" + nickname)) {
			output = "";
		} else if (i.indexOf("PING") != -1) {
			ch.write("PONG " + i.substring(5));
		}

		boolean serverMessage = (i.indexOf("!") == -1 || i.indexOf(" ") < i
				.indexOf("!"));
		if (serverMessage) {
			if (i.split(":")[1].indexOf("353") != -1) {
				try {
					String users[] = i.split(":")[2].split(" ");

					Arrays.sort(users);
					userList.setListData(users);
					String us = "";
					for (String s : users) {
						us += " " + s;
					}
				} catch (Exception e) {
				}
				// writeOutput("- Users on " + channel + ":" + us);
			} else if (i.split(":")[1].indexOf("366") != -1) {
				return;
			} else {
				try {
					if (i.indexOf("PING") != -1)
						writeOutput("* " + i, BLUE);
					else
						writeOutput("* " + i.substring(i.indexOf(":", 1) + 1),
								BLUE);
				} catch (Exception e) {
					writeOutput("* " + i, BLUE);
				}
			}
		} else {
			String nick = i.substring(1, i.indexOf("!"));
			String host = i.substring(i.indexOf("@") + 1, i.indexOf(" "));
			String action = i.split(" ")[1];
			String params = i
					.substring(i.indexOf(action) + action.length() + 1);

			if (action.equalsIgnoreCase("PRIVMSG")) {
				writeOutput("<" + nick + "> "
						+ params.substring(params.indexOf(":") + 1));
				writeNotify("<" + nick + "> "
						+ params.substring(params.indexOf(":") + 1));
			} else if (action.equalsIgnoreCase("JOIN")) {
				writeOutput(nick + " has joined " + channel, GREEN);
				updateUser();
			} else if (action.equalsIgnoreCase("PART")) {
				writeOutput(nick + " has left " + params, GREEN);
				updateUser();
			} else if (action.equalsIgnoreCase("QUIT")) {
				writeOutput(
						nick + " has quit IRC ("
								+ params.substring(params.indexOf(":") + 1)
								+ ")", GREEN);
				updateUser();
			} else if (action.equalsIgnoreCase("MODE")) {
				String channel = params.substring(0, params.indexOf(" "));
				params = params.substring(params.indexOf(channel)
						+ channel.length() + 1);
				String mode = params.substring(0, 2);
				params = params.substring(params.indexOf(mode) + mode.length()
						+ 1);
				String user = params;
				writeOutput(nick + " sets mode: " + mode + " " + user, GREEN);
			}
		}
	}

	private void updateUser() {
		ch.write("NAMES " + channel);
	}

	private void writeOutput(String s) {
		writeOutput(s, BLACK);
	}

	private void writeNotify(String s) {
		if (!mainWindow.isVisible()) {
			ti.displayMessage("Chat", s, TrayIcon.MessageType.INFO);
		}
	}

	private void writeOutput(String s, SimpleAttributeSet set) {
		try {
			outputPane.getDocument().insertString(
					outputPane.getDocument().getLength(), s + "\n", set);
			outputPane
					.setCaretPosition(outputPane.getDocument().getLength() - 1);
		} catch (Exception e) {
			debug("Error appending to output pane");
		}
	}

	private void login() {
		ch.write("NICK " + nickname, false);
		ch.write("USER " + nickname + " 8 * : Java IRC Client");
		if (channel.length() > 0) {
			ch.write("JOIN " + channel);
		}
	}

	private void removeFromParent() {
		shutdown();
		debug(tabIndex);
		parentPane.remove(tabIndex);
		mainWindow.removeServerItem(tabIndex);
	}

	@Override
	public void onConnect() {
		debug("Socket connected");
		pd.close();
		login();
	}

	@Override
	public void onDisconnect() {
		removeFromParent();
		JOptionPane.showMessageDialog(new JFrame(),
				"You have been disconnected from " + server, "Disconnect",
				JOptionPane.ERROR_MESSAGE);

	}

	@Override
	public void onError(Exception e) {
		debug("Error occured: " + e.toString());
		pd.close();
		removeFromParent();
		JOptionPane.showMessageDialog(new JFrame(), e.toString(), "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void dataReceived(String s) {
		debug("Data received: " + s);
		parseData(s);
	}

	public void shutdown() {
		ch.shutdown();
	}

	public void setDebug(Boolean v) {
		debugEnabled = v;
	}

	public void debug(Object o) {
		if (debugEnabled) {
			if (debugFrame == null || debugFrame.isVisible() == false) {
				debugFrame = new JFrame();
				debugPane = new JTextPane();
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(debugPane);
				debugFrame.setTitle("Debug: " + server);
				debugFrame.setSize(320, 240);
				debugPane.setBounds(0, 0, 320, 240);
				debugFrame.add(scrollPane);
				debugFrame.setVisible(true);
			}
			debugPane.setText(debugPane.getText() + o + "\n");
			debugPane.setCaretPosition(debugPane.getDocument().getLength() - 1);
			System.out.println(o);
		}
	}

	public int getIndex() {
		return tabIndex;
	}

	public void setIndex(int index) {
		tabIndex = index;
	}

	public String getServer() {
		return server;
	}

	public JTextPane getOutputPane() {
		return outputPane;
	}

	static {
		StyleConstants.setForeground(BLACK, Color.black);
		StyleConstants.setFontFamily(BLACK, "Tahoma");
		StyleConstants.setFontSize(BLACK, 12);

		StyleConstants.setForeground(GREEN, new Color(72, 142, 72));
		StyleConstants.setFontFamily(GREEN, "Tahoma");
		StyleConstants.setFontSize(GREEN, 12);

		StyleConstants.setForeground(BLUE, new Color(51, 0, 120));
		StyleConstants.setFontFamily(BLUE, "Tahoma");
		StyleConstants.setFontSize(BLUE, 12);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jScrollPane2 = new javax.swing.JScrollPane();
		jTextPane2 = new javax.swing.JTextPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		outputPane = new javax.swing.JTextPane();
		jScrollPane3 = new javax.swing.JScrollPane();
		inputPane = new javax.swing.JTextPane();
		jScrollPane4 = new javax.swing.JScrollPane();
		userList = new javax.swing.JList();

		jScrollPane2.setViewportView(jTextPane2);

		setPreferredSize(new java.awt.Dimension(640, 480));

		outputPane.setFocusable(false);
		jScrollPane1.setViewportView(outputPane);

		inputPane.addKeyListener(new java.awt.event.KeyAdapter() {

			public void keyPressed(java.awt.event.KeyEvent evt) {
				inputPaneKeyPressed(evt);
			}
		});
		jScrollPane3.setViewportView(inputPane);

		jScrollPane4.setViewportView(userList);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addComponent(jScrollPane1,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										480, Short.MAX_VALUE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jScrollPane4,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										80,
										javax.swing.GroupLayout.PREFERRED_SIZE))
				.addComponent(jScrollPane3,
						javax.swing.GroupLayout.DEFAULT_SIZE, 566,
						Short.MAX_VALUE));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jScrollPane4,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														311, Short.MAX_VALUE)
												.addComponent(
														jScrollPane1,
														javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														311, Short.MAX_VALUE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jScrollPane3,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										93, Short.MAX_VALUE)));
	}// </editor-fold>

	private void inputPaneKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			history.add(inputPane.getText());
			historyPos = 0;
			String text = getIrcString(inputPane.getText());
			ch.write(text);
			if (text.indexOf("PRIVMSG") != -1) {
				writeOutput("<" + nickname + "> " + text.split(":")[1]);
			}
			inputPane.setText("");
			evt.consume();
		} else if (evt.getKeyCode() == KeyEvent.VK_UP) {
			if (historyPos == 0)
				origString = inputPane.getText();
			if (history.size() - historyPos > 0) {
				historyPos++;
				inputPane.setText(history.get(history.size() - historyPos));
			}
		} else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
			if (historyPos > 0) {
				historyPos--;
				if (historyPos == 0) {
					inputPane.setText(origString);
				} else {
					inputPane.setText(history.get(history.size() - historyPos));
				}
			}
		}
	}

	// Variables declaration - do not modify
	private javax.swing.JTextPane inputPane;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JTextPane jTextPane2;
	private javax.swing.JTextPane outputPane;
	private javax.swing.JList userList;
	// End of variables declaration
}