import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class WebErrorPnael extends JPanel {
	public WebErrorPnael(Exception e) {
		super();
		this.add(new JLabel(e.getMessage()));
	}
}
