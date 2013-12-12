import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Calendar;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

public class RCConfig {

	/* --------------------------------------------------------- *
	 *     system config
	 * --------------------------------------------------------- */

	//------------------- window -------------------//
	public static String title_frame = "RollCakeRSS";
	public static int window_size_width = 900;
	public static int window_size_height = 700;
	public static int window_id_lookandfeel = 0;
	public static Color window_background_color = Color.white;

	//------------------- tablepane -------------------//
	public static int tablepane_size_width = 700;
	public static int tablepane_size_height = 400;
	public static Dimension tablepane_size_dimension = new Dimension(tablepane_size_width, tablepane_size_height);
	public static Color tablepane_background_color = Color.white;

	//------------------- underPanel -------------------//
	public static int underpane_size_width = tablepane_size_width;
	public static int underpane_size_height = window_size_height - tablepane_size_height - 100;
	public static Dimension underpane_size_dimension = new Dimension(underpane_size_width, underpane_size_height);
	public static Color underpane_background_color = new Color(200, 255, 255);

	//------------------- rightPanel -------------------//
	public static int rightpane_size_width = window_size_width - tablepane_size_width - 15;
	public static int rightpane_size_height = tablepane_size_height;
	public static Dimension rightpane_size_dimension = new Dimension(rightpane_size_width, rightpane_size_height);
	public static Color rightpane_background_color = Color.white;

	public static String key_delimiter = "-";

	public static int rightbutton_height = 70;
	public static Dimension rightbutton_dimension = new Dimension (rightpane_size_width, rightbutton_height);
	public static Border rightbutton_border = new LineBorder(Color.white);



	/* --------------------------------------------------------- *
	 *     design
	 * --------------------------------------------------------- */

	//------------------- item_info -------------------//
	public static Dimension item_info_line = new Dimension(underpane_size_width, 40);
	public static Dimension item_info_description = new Dimension(underpane_size_width, 100);

	public static Border item_info_box_border = new TitledBorder("RSS");

	//------------------- item_info title -------------------//
	public static Border item_info_title_border = new TitledBorder("TITLE");

	//------------------- item_info date -------------------//
	public static Border item_info_date_border = new TitledBorder("DATE");

	//------------------- item_info discription -------------------//
	public static Border item_info_description_border = new MatteBorder(new Insets(4, 4, 4, 4),
			new Color(220, 255, 255));

	//------------------- item_info url -------------------//
	public static Border item_info_url_border = new EtchedBorder();

	//------------------- table basic -------------------//
	public static int cell_size_height = 10;
	public static int cell_size_width = 14;

	//------------------- table hour -------------------//
	public static int label_table_hour_size_width = 14;
	public static Dimension label_table_hour = new Dimension(label_table_hour_size_width, cell_size_height);


	//------------------- button -------------------//
	public static Color button_font_color = new Color(0, 100, 0);
	public static Color button_back_color = new Color(200, 255, 200);
	public static Border button_border = new LineBorder(Color.white);

	public static Color rightbutton_back_color = new Color(200, 255, 200);
	public static Color rightbutton_font_color = new Color(0, 100, 0);


	//------------------- text date format -------------------//
	@SuppressWarnings("deprecation")
	public static String DateToString (java.util.Date date) {
		return String.format("%2d/%2d", date.getMonth(), date.getDate());
	}
	public static String getDateStringDiff (int diff) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -diff);
		return DateToString(cal.getTime());
	}
	@SuppressWarnings("deprecation")
	public static int getDateDif(int diff) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -diff);
		return cal.getTime().getDate();
	}


	/* --------------------------------------------------------- *
	 *     main value setting
	 * --------------------------------------------------------- */
	public static int num_day_recentry = 3;

	//------------------- table layout -------------------//
	public static int num_split_column_hour = 4;

	/* --------------------------------------------------------- *
	 *     File io
	 * --------------------------------------------------------- */

	public static String savefile_name = "data/save.xml";
	public static String savefile_encoding = "UTF-8";

}
