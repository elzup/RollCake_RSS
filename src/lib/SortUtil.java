package lib;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * クラスのソートのためのユーティリティ
 */
public class SortUtil<T> {

	/**
	 * ソートオーダー
	 */
	public enum SortOrder {
		ASCENDING,	// 昇順
		DESCENDING,	// 降順
	}

	// ソートのための比較クラス
	private SortComparator sortComparator = new SortComparator();

	/**
	 * ソートの情報を追加する
	 * @param itemName 項目名
	 * @param order ソートオーダー
	 */
	public void add(String itemName, SortOrder order) {
		sortComparator.add(new SortInfo(itemName, order));
	}

	/**
	 * ソートを行う
	 * @param list ソート対象の List オブジェクト
	 */
	public void sort(List<T> list) {

		Collections.sort(list, sortComparator);
	}

	// ソートの情報
	private class SortInfo {
		// 項目名
		private String itemName;
		public String getItemName() {
			return itemName;
		}

		// ソートオーダー
		private SortOrder order;
		public SortOrder getOrder() {
			return order;
		}

		// コンストラクタ
		public SortInfo(String itemName, SortOrder order) {
			this.itemName = itemName;
			this.order = order;
		}
	}

	// ソートのための比較クラス
	private class SortComparator implements Comparator<T> {

		// ソートの情報を保持する
		private List<SortInfo> sortInfoList = new ArrayList<SortInfo>();

		// ソートの情報を追加する
		public void add(SortInfo sortInfo) {
			sortInfoList.add(sortInfo);
		}

		// 比較を行う(Comparator#compare() の実装)
		@Override
		public int compare(T item1, T item2) {

			try {

				int result = 0;

				// ソートの優先順位の高い項目から順に比較する。
				for (int i = 0; i < sortInfoList.size(); ++i) {

					SortInfo sortInfo = sortInfoList.get(i);

					Object value1 = getFieldValue(item1, sortInfo.getItemName());
					Object value2 = getFieldValue(item2, sortInfo.getItemName());

					result = compareInternal(value1, value2);
					if (result == 0) {
						// 項目値が同じであれば、次に優先順位の高い項目の比較を行う
						continue;
					}

					if (sortInfo.getOrder() == SortOrder.DESCENDING) {
						result *= -1;
					}
					break;
				}

				return result;

			} catch (Exception ex) {
				throw new RuntimeException(ex.toString());
			}
		}

		// 実際の比較処理を行う
		private int compareInternal(Object value1, Object value2) {
			if (value1 == null && value2 == null) {
				return 0;
			} else if (value1 == null && value2 != null) {
				return -1;
			} else if (value1 != null && value2 == null) {
				return 1;
			} else {

				// TODO: とりあえず以下の型のみ。必要に応じ適宜追加してください。
				if (value1 instanceof Integer) {
					return ((Integer) value1).compareTo((Integer) value2);
				} else if (value1 instanceof Long) {
					return ((Long) value1).compareTo((Long) value2);
				} else if (value1 instanceof String) {
					return ((String) value1).compareTo((String) value2);
				} else {
					throw new RuntimeException("対応していない型です: " + value1.getClass().getCanonicalName());
				}
			}
		}

		// フィールドの値を得る
		private Object getFieldValue(Object obj, String fieldName) {
			try {
				Field field = obj.getClass().getDeclaredField(fieldName);

				// NOTE: Field#setAccessible() を使用したくない場合は、この行を削除してください。
				//       なお、削除した場合には、private フィールド、および、別パッケージの protected フィールドは
				//       ソートの対象にできません。
				field.setAccessible(true);

				return field.get(obj);
			} catch (Exception ex) {
				throw new RuntimeException("フィールド値を取得できません: " + ex.toString());
			}
		}
	}
}
