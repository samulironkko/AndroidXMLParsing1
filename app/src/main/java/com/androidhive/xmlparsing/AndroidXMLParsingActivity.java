package com.androidhive.xmlparsing;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AndroidXMLParsingActivity extends ListActivity {

	// All static variables
	static final String URL = "http://172.20.240.11:7002";
	// XML node keys
	static final String KEY_MATCH_RESULTS = "match_results"; // parent node
	static final String KEY_MATCH = "match";
	static final String KEY_HOME = "home_team";
	static final String KEY_COST = "cost";
	static final String KEY_VISITOR = "visitor_team";
	static final String KEY_HOME_GOALS = "home_goals";
	static final String KEY_VISITOR_GOALS = "visitor_goals";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromUrl(URL); // getting XML
		Document doc = parser.getDomElement(xml); // getting DOM element

		NodeList nl = doc.getElementsByTagName(KEY_MATCH);
		// looping through all item nodes <item>
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			Element e = (Element) nl.item(i);
			// adding each child node to HashMap key => value
			map.put(KEY_MATCH, parser.getValue(e, KEY_MATCH));
			map.put(KEY_HOME, parser.getValue(e, KEY_HOME));
			map.put(KEY_COST, parser.getValue(e, KEY_HOME_GOALS) + " - " + parser.getValue(e, KEY_VISITOR_GOALS));
			map.put(KEY_VISITOR, parser.getValue(e, KEY_VISITOR));

			// adding HashList to ArrayList
			menuItems.add(map);
		}

		// Adding menuItems to ListView
		ListAdapter adapter = new SimpleAdapter(this, menuItems,
				R.layout.list_item,
				new String[] { KEY_HOME, KEY_VISITOR, KEY_COST }, new int[] {
						R.id.name, R.id.desciption, R.id.cost });

		setListAdapter(adapter);

		// selecting single ListView item
		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String home = ((TextView) view.findViewById(R.id.name)).getText().toString();
				String cost = ((TextView) view.findViewById(R.id.cost)).getText().toString();
				String visitor = ((TextView) view.findViewById(R.id.desciption)).getText().toString();
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
				in.putExtra(KEY_HOME, home);
				in.putExtra(KEY_COST, cost);
				in.putExtra(KEY_VISITOR, visitor);
				startActivity(in);

			}
		});
	}
}