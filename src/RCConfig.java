import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Calendar;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

public class RCConfig {

	/* --------------------------------------------------------- *
	 *     system config
	 * --------------------------------------------------------- */

	//------------------- window -------------------//
	public static final String title_frame = "RollCakeRSS";
	public static final int window_size_width = 900;
	public static final int window_size_height = 700;
	public static final int window_id_lookandfeel = 0;
	public static final Color window_background_color = Color.white;

	//------------------- rightPanel -------------------//
	public static final int rightpane_size_width = 200;
	public static final int rightpane_size_height = window_size_height;
	public static final Dimension rightpane_size_dimension = new Dimension(rightpane_size_width, rightpane_size_height);
	public static final Color rightpane_background_color = Color.white;

	public static final String key_delimiter = "-";

	public static final int rightbutton_height = 70;
	public static final Dimension rightbutton_dimension = new Dimension (rightpane_size_width, rightbutton_height);
	public static final Border rightbutton_border = new LineBorder(Color.white);

	// ------------------- layoutButtonBox -------------------//
	public static final int layout_button_box_height = 70;
	public static final Dimension layout_button_box = new Dimension(rightpane_size_width , layout_button_box_height);
	@Deprecated
	public static final Dimension layout_button = new Dimension(100, layout_button_box_height - 10);

	public static final Dimension group_comb = new Dimension(rightpane_size_width, layout_button_box_height);

	public static final int feedlist_size_height = 400;
	public static final Dimension feedlist_size = new Dimension(rightpane_size_width, feedlist_size_height);
	public static final int feedlist_button_size_height = 30;
	public static final Dimension feedlist_button_size = new Dimension(rightpane_size_width, feedlist_button_size_height);

	public static final Insets margin_default = new Insets(5, 5, 5, 5);
	public static final Border margin_border = new EmptyBorder(margin_default);






	/* --------------------------------------------------------- *
	 *     design
	 * --------------------------------------------------------- */

	//------------------- table basic -------------------//
	public static final int cell_size_height = 10;
	public static final int cell_size_width = 14;

	//------------------- table hour -------------------//
	public static final int label_table_hour_size_width = 14;
	public static final Dimension label_table_hour = new Dimension(label_table_hour_size_width, cell_size_height);

	//------------------- button -------------------//
	public static final Color button_font_color = new Color(0, 100, 0);
	public static final Color button_back_color = Color.white;
	public static final Border button_border    = new LineBorder(Color.black);
	public static final Insets button_insets    = new Insets(3, 3, 3, 3);

	public static final Color rightbutton_back_color = new Color(200, 255, 200);
	public static final Color rightbutton_font_color = new Color(0, 100, 0);

	//------------------- text date format -------------------//
	@SuppressWarnings("deprecation")
	public static final String DateToString (java.util.Date date) {
		return String.format("%2d/%2d", date.getMonth(), date.getDate());
	}
	public static final String getDateStringDiff (int diff) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -diff);
		return DateToString(cal.getTime());
	}
	@SuppressWarnings("deprecation")
	public static final int getDateDif(int diff) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -diff);
		return cal.getTime().getDate();
	}


	/* --------------------------------------------------------- *
	 *     main value setting
	 * --------------------------------------------------------- */
	public static final int num_day_recentry = 3;

	//------------------- table layout -------------------//
	public static final int num_split_column_hour = 4;

	/* --------------------------------------------------------- *
	 *     File io
	 * --------------------------------------------------------- */

	public static final String savefile_name = "data/save.xml";
	public static final String savefile_encoding = "UTF-8";

}
