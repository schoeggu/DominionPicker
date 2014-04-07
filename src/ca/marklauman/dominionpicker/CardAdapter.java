package ca.marklauman.dominionpicker;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class CardAdapter extends CursorSelAdapter
						 implements OnItemClickListener,
						 			ViewBinder {
	
	public static final String _ID = CardList._ID;
	public static final String _NAME = CardList._NAME;
	public static final String _DESC = CardList._DESC;
	public static final String[] COLS = CardList.COLS;
	
	private static final HashMap<String, Integer> exp_icons;
	
	static {
		exp_icons = new HashMap<String, Integer>();
		exp_icons.put("Base", R.drawable.ic_set_base);
		exp_icons.put("Alchemy", R.drawable.ic_set_alchemy);
		exp_icons.put("Black Market", R.drawable.ic_set_black_market);
		exp_icons.put("Cornucopia", R.drawable.ic_set_cornucopia);
		exp_icons.put("Dark Ages", R.drawable.ic_set_dark_ages);
		exp_icons.put("Envoy", R.drawable.ic_set_envoy);
		exp_icons.put("Governor", R.drawable.ic_set_governor);
		exp_icons.put("Hinterlands", R.drawable.ic_set_hinterlands);
		exp_icons.put("Intrigue", R.drawable.ic_set_intrigue);
		exp_icons.put("Prosperity", R.drawable.ic_set_prosperity);
		exp_icons.put("Seaside", R.drawable.ic_set_seaside);
		exp_icons.put("Stash", R.drawable.ic_set_stash);
		exp_icons.put("Walled Village", R.drawable.ic_set_walled_village);
		
	}
	
	private int col_desc = -1;
	private int col_cost = -1;
	private int col_potion = -1;
	private int col_expansion = -1;
	private int col_gold = -1;
	private int col_vict = -1;
	private int col_buy = -1;
	private int col_card = -1;
	private int col_act = -1;
	
	public CardAdapter(Context context) {
		super(context, R.layout.list_item_card,
			  new String[]{CardList._NAME,
						   CardList._COST,
						   CardList._POTION,
						   CardList._EXP,
						   CardList._CATEGORY,
						   CardList._GOLD,
						   CardList._BUY,
						   CardList._DESC,
						   CardList._VICTORY},
			  new int[]{R.id.card_title,
				        R.id.card_cost,
				        R.id.card_potion,
				        R.id.card_set,
				        R.id.card_cat,
				        R.id.card_res_gold,
				        R.id.card_res,
				        R.id.card_desc,
				        R.id.card_res_victory});
		this.setViewBinder(this);
	}
	
	public void changeCursor(Cursor cursor) {
		super.changeCursor(cursor);
		if(cursor == null) return;
		for(int i = 0; i < cursor.getCount(); i++)
			selectItem(i);
		col_cost = cursor.getColumnIndex(CardList._COST);
		col_potion = cursor.getColumnIndex(CardList._POTION);
		col_expansion = cursor.getColumnIndex(CardList._EXP);
		col_gold = cursor.getColumnIndex(CardList._GOLD);
		col_vict = cursor.getColumnIndex(CardList._VICTORY);
		col_buy = cursor.getColumnIndex(CardList._BUY);
		col_card = cursor.getColumnIndex(CardList._DRAW);
		col_act = cursor.getColumnIndex(CardList._ACTION);
		col_desc = cursor.getColumnIndex(CardList._DESC);
	}
	
	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		if(col_cost == columnIndex
				|| col_gold == columnIndex
				|| col_vict == columnIndex) {
			if("0".equals(cursor.getString(columnIndex)))
				view.setVisibility(View.GONE);
			else view.setVisibility(View.VISIBLE);
			return false;
		}
		if(col_potion == columnIndex) {
			if(1 > cursor.getInt(columnIndex))
				view.setVisibility(View.GONE);
			else view.setVisibility(View.VISIBLE);
			return true;
		}
		if(col_expansion == columnIndex) {
			String val = cursor.getString(col_expansion);
			ImageView v = (ImageView) view;
			v.setContentDescription(val);
			Integer icon_id = exp_icons.get(val);
			if(icon_id == null) icon_id = R.drawable.ic_set_unknown;
			v.setImageResource(icon_id);
			return true;
		}
		if(col_buy == columnIndex) {
			String res = "";
			String val = cursor.getString(col_buy);
			if(!"0".equals(val)) res += ", +" + val + " buy";
			val = cursor.getString(col_card);
			if(!"0".equals(val)) res += ", +" + val + " card";
			val = cursor.getString(col_act);
			if(!"0".equals(val)) res += ", +" + val + " action";
			Log.d("gold=", cursor.getString(col_gold));
			if(!"0".equals(col_gold)
					&& !"0".equals(col_vict)
					&& res.length() > 2)
				res = res.substring(2);
			if("".equals(res))
				view.setVisibility(View.GONE);
			else {
				view.setVisibility(View.VISIBLE);
				TextView v = (TextView) view;
				v.setText(res);
			}
			return true;
		}
		if(col_desc == columnIndex) {
			String desc = cursor.getString(columnIndex);
			if("".equals(desc)) {
				view.setVisibility(View.GONE);
				return true;
			}
			view.setVisibility(View.VISIBLE);
			return false;
		}
		return false;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		toggleItem(position);
	}
	
	public String[] getCard(long id) {
		int position = getPosition(id);
		this.mCursor.moveToPosition(position);
		ArrayList<String> data = new ArrayList<String>(COLS.length);
		for(int i = 0; i < COLS.length; i++) {
			int index = mCursor.getColumnIndex(COLS[i]);
			data.add(mCursor.getString(index));
		}
		String[] res = new String[data.size()];
		data.toArray(res);
		return res;
	}
}