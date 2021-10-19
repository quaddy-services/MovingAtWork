package de.quaddyservices.movingatwork;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class MovingAtWork {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new MovingAtWork().start();
				} catch (AWTException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	private TrayIcon trayIcon;
	private Timer timer;
	private int currentMove = 1;

	public void start() throws AWTException {
		SystemTray tempSystemTray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/tray.gif"));
		PopupMenu popup = new PopupMenu();
		MenuItem defaultItem = new MenuItem("Exit");

		ActionListener exitListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		};
		defaultItem.addActionListener(exitListener);
		popup.add(defaultItem);

		trayIcon = new TrayIcon(image, "Moving@Work", popup);

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayMessage();
			}
		};
		timer = new Timer(30 * 60 * 1000, actionListener);
		timer.setRepeats(true);

		timer.start();
		trayIcon.setImageAutoSize(true);

		trayIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent aE) {
				System.out.println(aE);
				showMove();
			}
		});

		tempSystemTray.add(trayIcon);

	}

	protected void showMove() {
		final JFrame tempFrame = new JFrame("Moving@Work");
		tempFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tempFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/tray.gif")));
		Container tempContentPane = tempFrame.getContentPane();
		tempContentPane.setLayout(new BorderLayout());
		ImageIcon tempImageIcon = new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/move" + currentMove + ".gif")));
		currentMove++;
		if (currentMove > 2) {
			currentMove = 1;
		}
		JLabel tempImageLabel = new JLabel(tempImageIcon) {
			@Override
			public boolean imageUpdate(Image aImg, int aInfoflags, int aX, int aY, int aW, int aH) {
				// Sleep in the daemon thread for a while
				// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6453582
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return super.imageUpdate(aImg, aInfoflags, aX, aY, aW, aH);
			}
		};
		tempContentPane.add(tempImageLabel, BorderLayout.CENTER);
		Dimension tempSize = tempContentPane.getPreferredSize();
		tempSize.width += 30;
		tempSize.height += 50;
		tempFrame.setSize(tempSize);
		Dimension tempScreen = Toolkit.getDefaultToolkit().getScreenSize();
		tempFrame.setLocation((tempScreen.width - tempSize.width) / 2, (tempScreen.height - tempSize.height) / 2);
		tempFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent aE) {
				tempFrame.dispose();
			}
		});
		tempFrame.setVisible(true);
		tempFrame.setAlwaysOnTop(true);
	}

	public void stop() {
		System.out.println("Exiting...");
		System.exit(0);
	}

	private void displayMessage() {
		trayIcon.displayMessage("Moving@Work", "It's time to move!\nKlick here for a move.", TrayIcon.MessageType.INFO);
	}
}
