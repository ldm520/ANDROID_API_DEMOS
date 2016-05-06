/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.view;

import com.example.android.apis.R;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 带悬浮提示框的ListView
 * 
 * @description：
 * @author ldm
 * @date 2016-4-21 上午10:55:51
 */
public class List9 extends ListActivity implements ListView.OnScrollListener {

	private final class RemoveWindow implements Runnable {
		public void run() {
			removeWindow();
		}
	}

	private RemoveWindow mRemoveWindow = new RemoveWindow();
	Handler mHandler = new Handler();
	private WindowManager mWindowManager;
	private TextView mDialogText;
	private boolean mShowing;
	private boolean mReady;
	private char mPrevLetter = Character.MIN_VALUE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mStrings));

		getListView().setOnScrollListener(this);

		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mDialogText = (TextView) inflate.inflate(R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);

		mHandler.post(new Runnable() {

			public void run() {
				mReady = true;
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT,
						WindowManager.LayoutParams.TYPE_APPLICATION,
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
								| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
						PixelFormat.TRANSLUCENT);
				mWindowManager.addView(mDialogText, lp);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mReady = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		removeWindow();
		mReady = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWindowManager.removeView(mDialogText);
		mReady = false;
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mReady) {
			char firstLetter = mStrings[firstVisibleItem].charAt(0);

			if (!mShowing && firstLetter != mPrevLetter) {

				mShowing = true;
				mDialogText.setVisibility(View.VISIBLE);
			}
			mDialogText.setText(((Character) firstLetter).toString());
			mHandler.removeCallbacks(mRemoveWindow);
			mHandler.postDelayed(mRemoveWindow, 3000);
			mPrevLetter = firstLetter;
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	private void removeWindow() {
		if (mShowing) {
			mShowing = false;
			mDialogText.setVisibility(View.INVISIBLE);
		}
	}

	private String[] mStrings = new String[] { "Abbaye de Belloc",
			"Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu",
			"Airag", "Airedale", "Aisy Cendre", "Allgauer Emmentaler",
			"Alverca", "Ambert", "American Cheese", "Ami du Chambertin",
			"Beenleigh Blue", "Beer Cheese", "Bel Paese", "Bergader",
			"Bergere Bleue", "Berkswell", "Beyaz Peynir", "Bierkase",
			"Bishop Kennedy", "Blarney", "Bleu d'Auvergne", "Bleu de Gex",
			"Bleu de Laqueuille", "Bleu de Septmoncel", "Bleu Des Causses",
			"Blue", "Blue Castello", "Blue Rathgore", "Blue Vein (Australian)",
			"Blue Vein Cheeses", "Bocconcini", "Bocconcini (Australian)",
			"Boeren Leidenkaas", "Bonchester", "Bosworth", "Bougon",
			"Boule Du Roves", "Boulette d'Avesnes", "Boursault", "Boursin",
			"Bouyssou", "Bra", "Braudostur", "Breakfast Cheese",
			"Brebis du Lavort", "Brebis du Lochois", "Brebis du Puyfaucon",
			"Bresse Bleu", "Brick", "Brie", "Brie de Meaux", "Brie de Melun",
			"Brillat-Savarin", "Brin", "Brin d' Amour", "Brin d'Amour",
			"Brinza (Burduf Brinza)", "Briquette de Brebis",
			"Briquette du Forez", "Broccio", "Broccio Demi-Affine",
			"Brousse du Rove", "Bruder Basil",
			"Brusselae Kaas (Fromage de Bruxelles)", "Bryndza",
			"Buchette d'Anjou", "Buffalo", "Chevrotin des Aravis",
			"Chontaleno", "Civray", "Coeur de Camembert au Calvados",
			"Coeur de Chevre", "Colby", "Cold Pack", "Comte", "Coolea",
			"Cooleney", "Coquetdale", "Corleggy", "Cornish Pepper",
			"Cotherstone", "Cotija", "Cottage Cheese",
			"Cottage Cheese (Australian)", "Cougar Gold", "Coulommiers",
			"Coverdale", "Crayeux de Roncq", "Cream Cheese", "Cream Havarti",
			"Crema Agria", "Crema Mexicana", "Creme Fraiche", "Crescenza",
			"Croghan", "Crottin de Chavignol", "Crottin du Chavignol",
			"Crowdie", "Crowley", "Cuajada", "Curd", "Cure Nantais",
			"Curworthy", "Cwmtawe Pecorino", "Cypress Grove Chevre",
			"Danablu (Danish Blue)", "Danbo", "Danish Fontina",
			"Daralagjazsky", "Dauphin", "Delice des Fiouves",
			"Denhany Dorset Drum", "Derby", "Dessertnyj Belyj", "Devon Blue",
			"Devon Garland", "Dolcelatte", "Doolin", "Doppelrhamstufel",
			"Dorset Blue Vinney", "Double Gloucester", "Double Worcester",
			"Dreux a la Feuille", "Dry Jack", "Garrotxa", "Gastanberra",
			"Geitost", "Gippsland Blue", "Gjetost", "Gloucester",
			"Golden Cross", "Gorgonzola", "Gornyaltajski", "Gospel Green",
			"Gouda", "Goutu", "Gowrie", "Grabetto", "Graddost",
			"Grafton Village Cheddar", "Grana", "Grana Padano", "Grand Vatel",
			"Grataron d' Areches", "Gratte-Paille", "Graviera", "Greuilh",
			"Greve", "Gris de Lille", "Gruyere", "Gubbeen", "Guerbigny",
			"Halloumi", "Halloumy (Australian)", "Haloumi-Style Cheese",
			"Harbourne Blue", "Havarti", "Heidi Gruyere", "Hereford Hop",
			"Herrgardsost", "Herriot Farmhouse", "Herve", "Hipi Iti",
			"Hubbardston Blue Cow", "Hushallsost", "Iberico", "Idaho Goatster",
			"Idiazabal", "Il Boschetto al Tartufo", "Ile d'Yeu",
			"Isle of Mull", "Jarlsberg", "Jermi Tortes", "Jibneh Arabieh",
			"Jindi Brie", "Jubilee Blue", "Juustoleipa", "Kadchgall", "Kaseri",
			"Kashta", "Kefalotyri", "Kenafa", "Kernhem", "Kervella Affine",
			"Kikorangi", "King Island Cape Wickham Brie", "King River Gold",
			"Klosterkaese", "Knockalara", "Kugelkase", "Menallack Farmhouse",
			"Menonita", "Meredith Blue", "Mesost", "Metton (Cancoillotte)",
			"Meyer Vintage Gouda", "Mihalic Peynir", "Milleens", "Mimolette",
			"Mine-Gabhar", "Mini Baby Bells", "Mixte", "Molbo",
			"Monastery Cheeses", "Mondseer", "Mont D'or Lyonnais", "Montasio",
			"Monterey Jack", "Monterey Jack Dry", "Morbier",
			"Morbier Cru de Montagne", "Mothais a la Feuille", "Mozzarella",
			"Mozzarella (Australian)", "Mozzarella di Bufala",
			"Mozzarella Fresh, in water", "Mozzarella Rolls", "Munster",
			"Murol", "Mycella", "Myzithra", "Peekskill Pyramid",
			"Pelardon des Cevennes", "Pelardon des Corbieres", "Penamellera",
			"Penbryn", "Pencarreg", "Perail de Brebis", "Petit Morin",
			"Petit Pardou", "Petit-Suisse", "Picodon de Chevre",
			"Picos de Europa", "Piora", "Pithtviers au Foin",
			"Plateau de Herve", "Plymouth Cheese", "Podhalanski",
			"Poivre d'Ane", "Polkolbin", "Pont l'Eveque", "Port Nicholson",
			"Port-Salut", "Postel", "Pouligny-Saint-Pierre", "Pourly",
			"Prastost", "Pressato", "Prince-Jean", "Processed Cheddar",
			"Provolone", "Provolone (Australian)", "Pyengana Cheddar",
			"Pyramide", "Quark", "Quark (Australian)", "Quartirolo Lombardo",
			"Quatre-Vents", "Quercy Petit", "Queso Blanco",
			"Queso Blanco con Frutas --Pina y Mango", "Queso de Murcia",
			"Queso del Montsec", "Saint-Marcellin", "Saint-Nectaire",
			"Saint-Paulin", "Salers", "Samso", "San Simon", "Sancerre",
			"Sap Sago", "Sardo", "Sardo Egyptian", "Sbrinz", "Scamorza",
			"Schabzieger", "Schloss", "Selles sur Cher", "Selva", "Serat",
			"Seriously Strong Cheddar", "Serra da Estrela", "Sharpam",
			"Shelburne Cheddar", "Shropshire Blue", "Siraz", "Sirene",
			"Smoked Gouda", "Somerset Brie", "Sonoma Jack",
			"Sottocenare al Tartufo", "Soumaintrain", "Sourire Lozerien",
			"Spenwood", "Sraffordshire Organic", "St. Agur Blue Cheese",
			"Stilton", "Stinking Bishop", "String", "Sussex Slipcote",
			"Sveciaost", "Swaledale", "Sweet Style Swiss", "Swiss",
			"Syrian (Armenian String)", "Tala", "Taleggio", "Tamie",
			"Tasmania Highland Chevre Log", "Taupiniere", "Teifi", "Telemea",
			"Testouri", "Tete de Moine", "Tetilla", "Venaco", "Vendomois",
			"Vieux Corse", "Vignotte", "Vulscombe", "Waimata Farmhouse Blue",
			"Washed Rind Cheese (Australian)", "Waterloo", "Weichkaese",
			"Wellington", "Wensleydale", "White Stilton",
			"Zanetti Parmigiano Reggiano" };

}
