import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

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
	public static final Dimension rightbutton_dimension = new Dimension(rightpane_size_width, rightbutton_height);
	public static final Border rightbutton_border = new LineBorder(Color.white);

	// ------------------- layoutButtonBox -------------------//
	public static final int layout_button_box_height = 70;
	public static final Dimension layout_button_box = new Dimension(rightpane_size_width, layout_button_box_height);
	@Deprecated
	public static final Dimension layout_button = new Dimension(100, layout_button_box_height - 10);
	public static final Dimension group_comb = new Dimension(rightpane_size_width, layout_button_box_height);

	public static final int feedlist_size_height = 400;
	public static final Dimension feedlist_size = new Dimension(rightpane_size_width, feedlist_size_height);
	public static final int feedlist_button_size_height = 30;
	public static final Dimension feedlist_button_size = new Dimension(rightpane_size_width, feedlist_button_size_height);

	public static final Insets margin_default = new Insets(5, 5, 5, 5);
	public static final Border margin_border = new EmptyBorder(margin_default);

	// ------------------- itmePane -------------------//
	public static final Dimension itempane_size = new Dimension(200, 310);
	public static final int item_image_width = 200;
	public static final int item_image_height = 124; //(int) (200 / Math.sqrt(2));
	public static final Dimension item_imagepane = new Dimension(item_image_width, item_image_height + 5);
	public static final Dimension item_underpane = new Dimension(item_image_width, 130);
	public static final Dimension item_titlepane = new Dimension(item_image_width, 50);

	public static final Dimension item_brows_button = new Dimension(50, 30);


	// ------------------- setting panel -------------------//
	public static final Dimension settingpanel_size = new Dimension(500, 400);
	public static final Dimension setting_child_size = new Dimension(400, 40);
	public static final Dimension setting_child2_size = new Dimension(300, 20);
	public static final Dimension setting_grouplist_size = new Dimension(300, 100);


	/* --------------------------------------------------------- *
	 *     design
	 * --------------------------------------------------------- */

	// ------------------- homepane -------------------//
	public static final Color home_back_color = Color.white;
	//	public static final Color home_back_color = Color.white;

	// ------------------- item -------------------//
	public static final Color itempane_back_color = Color.white;

	//------------------- table basic -------------------//
	public static final int cell_size_height = 10;
	public static final int cell_size_width = 14;

	//------------------- table hour -------------------//
	public static final int label_table_hour_size_width = 14;
	public static final Dimension label_table_hour = new Dimension(label_table_hour_size_width, cell_size_height);

	//------------------- button -------------------//
	public static final Color button_font_color = new Color(0, 100, 0);
	public static final Color button_back_color = Color.white;
	public static final Border button_border = new LineBorder(Color.black);
	public static final Insets button_insets = new Insets(3, 3, 3, 3);

	public static final Color rightbutton_back_color = new Color(200, 255, 200);
	public static final Color rightbutton_font_color = new Color(0, 100, 0);


	public static final MatteBorder setting_list_group_border = new MatteBorder(new Insets(0, 10, 0, 10), Color.black);
	public static final MatteBorder setting_list_feed_border = new MatteBorder(new Insets(0, 5, 0, 0), Color.gray);

	/* --------------------------------------------------------- *
	 *     main value setting
	 * --------------------------------------------------------- */
	public static final int num_day_recentry = 3;

	//@formatter:off
	public static final String[] ng_names_elem_start = {
						"dc:" ,"rdf:","dcq:",
	};
	public static final String[] ng_title_start = {
						"AD:","PR:",
	};
	public static final String[] ng_names_elem = {
	};
	//@formatter:on

	public static final String no_match_text = "no matched";

	//------------------- table layout -------------------//
	public static final int num_split_column_hour = 4;
	public static final int num_panel_layout_column = 3;

	/* --------------------------------------------------------- *
	 *     Extensive vlaies
	* --------------------------------------------------------- */
	public static final Color no_color = Color.LIGHT_GRAY;


	/* --------------------------------------------------------- *
	 *     File io
	 * --------------------------------------------------------- */

	public static final String savefile_name = "data/save.xml";
	public static final String savefile_encoding = "UTF-8";

	public static final String dir_img = System.getProperty("java.class.path") + "/../img/";

	public static final String default_notfound_img = dir_img + "item.gif";

	public static final String no_image = "<div style=\"width:" + (item_image_width - 10) + "px;height:" + (item_image_height - 10) + "px;\">no image</div>";

	/* --------------------------------------------------------- *
	 *     config methods
	* --------------------------------------------------------- */

	@SuppressWarnings("deprecation")
	public static final String DateToString(java.util.Date date) {
		return String.format("%2d/%2d", date.getMonth(), date.getDate());
	}

	public static final String getDateStringDiff(int diff) {
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

	public static final String toImgTag(String url) {
		return "<div style=\"margin:0;padding:0;float:left;width:" + (item_image_width - 50) + "px;height:" + (item_image_height) + "px;\"><img src=\"" + url + "\" width=\"" + (item_image_width - 2) + "\" height=\"" + (item_image_height - 2) + "\" /></div>";
//		return "<div style=\"margin:0;padding:0;border:1px solid black;width:" + (item_image_width) + "px;height:" + (item_image_height) + "px;\">" + "" +
//				"<img style=\"margin:0;padding:0;\" src=\"" + url + "\" width=\"" + (item_image_width - 2) + "\" height=\"" + (item_image_height - 2) + "\" /></div>";
	}

	public static final boolean isNgTag(String name) {
		for (String str : ng_names_elem_start)
			if (name.startsWith(str))
				return true;
		return Arrays.asList(ng_names_elem).contains(name);
	}

	public static final boolean isNgTitle(String name) {
		for (String str : ng_title_start)
			if (name.startsWith(str))
				return true;
		return false;
	}

}
