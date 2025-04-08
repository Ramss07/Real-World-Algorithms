import java.util.ArrayList;
import java.util.Map;

import bridges.connect.Bridges;
import bridges.connect.DataSource;
import bridges.data_src_dependent.USState;
import bridges.data_src_dependent.USCounty;
import bridges.base.SLelement;
import bridges.base.USMap;
import bridges.base.Color;


// this program illustrates how to access the data of the US state county
// maps and apply attributes to them
public class citiesLinkedList {
	public static void main(String[] args) throws Exception {
		// create bridges object
		Bridges bridges  = new Bridges(4,"RamleyHirn","575356762377");

		// set title
		bridges.setTitle("Linked List of US Cities with a Map Overlay");
		DataSource ds = bridges.getDataSource();

		// get us map data - states and boundaries
		ArrayList<USState> map_data = ds.getUSMapData ();

		for (USState st: map_data) 
			st.setFillColor(new Color("skyblue"));

		// create a USMap object with the map data
		USMap us_maps = new USMap(map_data);
		bridges.setMap (us_maps);
		// TODO: 
    	// Build a linked list connecting all the cities around the border of 
    	// USA. Use at least 15-20 cities
    	// Review the tutorial on single linked list to build the list and
    	// apply attributes like color, size, link thickness, link color
		String[][] cities = {
			{"Charlotte NC","35.2271","-80.8431"},
			{"Wilmington NC", "34.2257", "-77.9447"},
			{"Brownsville TX","25.9017","-97.4975"},
			{"Laredo TX","27.5036","-99.5076"},
			{"El Paso TX","31.7619","-106.4850"},
			{"San Diego CA","32.7157","-117.1611"},
			{"Los Angeles CA","34.0522","-118.2437"},
			{"San Francisco CA","37.7749","-122.4194"},
			{"Portland OR","45.5051","-122.6750"},
			{"Seattle WA","47.6062","-122.3321"},
			{"Blaine WA","48.9939","-122.7476"},
			{"Sweet Grass MT","48.9986","-111.9644"},
			{"Pembina ND","48.9665","-97.2406"},
			{"Duluth MN","46.7867","-92.1005"},
			{"Detroit MI","42.3314","-83.0458"},
			{"Buffalo NY","42.8864","-78.8784"},
			{"Portland ME", "43.6591", "-70.2568"}


		};

		SLelement<String> head = new SLelement<>(cities[0][0], cities[0][0]);
		head.setLocation(Double.parseDouble(cities[0][2]), Double.parseDouble(cities[0][1]));
		head.setSize(15.);
		head.setColor(new Color("yellow"));

		SLelement<String> current = head;

		for (int i = 1; i < cities.length; i++)
		{
			SLelement<String> newNode = new SLelement<>(cities[i][0], cities[i][0]);
			newNode.setLocation(Double.parseDouble(cities[i][2]), Double.parseDouble(cities[i][1]));
			newNode.setSize(15.);
			newNode.setColor(new Color("yellow"));

			current.setNext(newNode);
			current.getLinkVisualizer(newNode).setThickness(2.0);
            current.getLinkVisualizer(newNode).setColor(new Color("yellow"));

			current = newNode;
		}
		current.setNext(head);
		current.getLinkVisualizer(head).setThickness(2.0);
        current.getLinkVisualizer(head).setColor(new Color("yellow"));


	
		// set data structure
		bridges.setDataStructure(head);

		// visualize
		bridges.visualize();
	
	}
};
