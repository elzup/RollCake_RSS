import java.awt.Color;
import java.awt.Dimension;


public class RCConfig {

	/* --------------------------------------------------------- *
	 *     system config
	 * --------------------------------------------------------- */

	//------------------- window -------------------//
	public static String title_frame = "RollCakeRSS";
	public static int window_size_width  = 900;
	public static int window_size_height = 600;
	public static int window_id_lookandfeel = 3;
	public static Color window_background_color = Color.white;

	//------------------- tablepane -------------------//
	public static int tablepane_size_width = 700;
	public static int tablepane_size_height = 400;
	public static Dimension tablepane_size_dimension = new Dimension(tablepane_size_width, tablepane_size_height);
	public static Color tablepane_background_color = new Color(210, 210, 255);

	//------------------- underPanel -------------------//
	public static int underpane_size_width = tablepane_size_width;
	public static int underpane_size_height = 200;
	public static Dimension underpane_size_dimension = new Dimension(underpane_size_width, underpane_size_height);
	public static Color underpane_background_color = new Color(200, 255, 255);

	//------------------- rightPanel -------------------//
	public static int rightpane_size_width = window_size_width - tablepane_size_width;
	public static int rightpane_size_height = tablepane_size_height;
	public static Dimension rightpane_size_dimension = new Dimension(rightpane_size_width, rightpane_size_height);
	public static Color rightpane_background_color = new Color(200, 240, 240);






	/* --------------------------------------------------------- *
	 *     main value setting
	 * --------------------------------------------------------- */
	public static int num_day_recentry = 3;


	//------------------- table layout -------------------//
	public static int num_split_column_hour = 4;


}
