package hotstu.github.javacaptcha;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class MainForm extends JFrame {

	private JPanel contentPane;
	private JLabel lblCaptcha;
	private JLabel lblResult;
	private IController identy;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm frame = new MainForm();
					frame.setController(new ControllerImpl());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setController(IController contoller) {
		this.identy = contoller;
	}

	/**
	 * Create the frame.
	 */
	public MainForm() {
		setTitle("AutoCaptcha");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 439, 163);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);
		
		lblResult = new JLabel("选择chatcha");
		contentPane.add(lblResult, BorderLayout.WEST);
		
		JPanel btnPanel = new JPanel();
		contentPane.add(btnPanel, BorderLayout.EAST);
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
		
		JButton btnLoad = new JButton("读取");

		btnPanel.add(btnLoad);
		
		JButton btnDownload = new JButton("下载");
		btnPanel.add(btnDownload);
		
		JPanel captchaPanel = new JPanel();
		contentPane.add(captchaPanel, BorderLayout.CENTER);
		captchaPanel.setLayout(new BorderLayout(0, 0));
		
		lblCaptcha = new JLabel("--");
		lblCaptcha.setHorizontalAlignment(SwingConstants.CENTER);
		captchaPanel.add(lblCaptcha);
		//lblCaptcha.setMaximumSize(new Dimension(10, 28));
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser("download2");
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					lblCaptcha.setIcon(new ImageIcon(file.getPath()));
					String reuslt = identy.predict(file);
					lblResult.setText(reuslt);

				}
			}
		});
	}

}
