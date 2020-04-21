/**
 * Thomas Scott - S1824798
 * Description - This is the centre of the application where the menu is created and where
 * depending on which option is selected, fragments are placed into the main content.
 * This is also where the Async tasks are declared and run. These tasks will retrieve the data from the URLs and parse the XML,
 * creating relevant Linked Lists for each type (roadwork, incident and futureroadworks).
 * These Async tasks run in the background which is why sometimes when looking at a page too quickly the data doesn't show.
 */
package com.example.tscott202mpd_cw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Creating the necessary instances to create the side menu and fragment management.
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    // The URL to get the different data
    private String roadworksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String futureRoadworksURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    public static LinkedList<Roadwork> roadworkLinkedList = new LinkedList<>();
    public static LinkedList<FutureRoadwork> futureRoadworkLinkedList = new LinkedList<>();
    public static LinkedList<Incident> incidentLinkedList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GetRoadworksAsyncTask().execute();
        new GetFutureRoadworksAsyncTask().execute();
        new GetIncidentssAsyncTask().execute();

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        // Setting up the navbar menu.
        drawerLayout = findViewById(R.id.drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // This will load the default fragment for the app which is home.
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    // This function will determine the fragment which is loaded based on the users option at the menu.
    // The fragments are replaced each time a new item in the menu is selected.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.nav_home) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
            fragmentTransaction.commit();
        }

        if (item.getItemId() == R.id.nav_travel_data) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new TravelInformationFragment());
            fragmentTransaction.commit();
        }

        if (item.getItemId() == R.id.nav_journey_planner) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new JourneyPlannerFragment());
            fragmentTransaction.commit();
        }

        if (item.getItemId() == R.id.nav_future_roadworks) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new FutureRoadworksFragment());
            fragmentTransaction.commit();
        }

        return true;
    }

    /**
     * Asyn function to get the data and populate the linked list
     * Gets the data then passed the result to the XMLPullParser which
     * then navigates thrugh until it finds the Item tag.
     * Then it gets the subsequent title description and points and stores then in a new instance of the class.
     * Then the Object is added to the linked list.
     * THIS IS THE SAME FOR THE OTHER 2 ASYNC TASKS EXCEPT IT STORES IN RELEVANT LINKEDLISTS AND USES THE RELEVANT OBJECT CLASS.
     */
    private class GetRoadworksAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String url = roadworksURL;
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";

            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();

            } catch (IOException ae) {
                Log.e("Exception: ", ae.getMessage());
            }

            Roadwork roadwork = null;
            String text = null;

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                roadworkLinkedList = new LinkedList<Roadwork>();

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            roadwork = new Roadwork();
                        }

                    }

                    if (eventType == XmlPullParser.TEXT) {

                        text = xpp.getText();

                    }

                    if (eventType == XmlPullParser.END_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            roadworkLinkedList.add(roadwork);
                        } else if (roadwork != null && xpp.getName().equalsIgnoreCase("title")) {
                            roadwork.setTitle(text);
                        } else if (roadwork != null && xpp.getName().equalsIgnoreCase("description")) {
                            roadwork.setDescription(text);
                        } else if (roadwork != null && xpp.getName().equalsIgnoreCase("point")) {
                            roadwork.setCoordinates(text);
                        }

                    }

                    eventType = xpp.next();

                }

            } catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }

            Log.e("SUCCESS", "Roadworks Data Received");
            Log.e("DATA", roadworkLinkedList.toString());

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }

    }

    private class GetFutureRoadworksAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String url = futureRoadworksURL;
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";

            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();

            } catch (IOException ae) {
                Log.e("Exception: ", ae.getMessage());
            }

            FutureRoadwork futureRoadwork = null;
            String text = null;

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                futureRoadworkLinkedList = new LinkedList<FutureRoadwork>();

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            futureRoadwork = new FutureRoadwork();
                        }

                    }

                    if (eventType == XmlPullParser.TEXT) {

                        text = xpp.getText();

                    }

                    if (eventType == XmlPullParser.END_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            futureRoadworkLinkedList.add(futureRoadwork);
                        } else if (futureRoadwork != null && xpp.getName().equalsIgnoreCase("title")) {
                            futureRoadwork.setTitle(text);
                        } else if (futureRoadwork != null && xpp.getName().equalsIgnoreCase("description")) {
                            futureRoadwork.setDescription(text);
                        } else if (futureRoadwork != null && xpp.getName().equalsIgnoreCase("point")) {
                            futureRoadwork.setCoordinates(text);
                        }

                    }

                    eventType = xpp.next();

                }

            } catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }

            Log.e("SUCCESS", "Future Roadworks Data Received");
            Log.e("DATA", futureRoadworkLinkedList.toString());

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


        }

    }

    private class GetIncidentssAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String url = incidentsURL;
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";

            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();

            } catch (IOException ae) {
                Log.e("Exception: ", ae.getMessage());
            }

            Incident incident = null;
            String text = null;

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                incidentLinkedList = new LinkedList<Incident>();

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            incident = new Incident();
                        }

                    }

                    if (eventType == XmlPullParser.TEXT) {

                        text = xpp.getText();

                    }

                    if (eventType == XmlPullParser.END_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            incidentLinkedList.add(incident);
                        } else if (incident != null && xpp.getName().equalsIgnoreCase("title")) {
                            incident.setTitle(text);
                        } else if (incident != null && xpp.getName().equalsIgnoreCase("description")) {
                            incident.setDescription(text);
                        } else if (incident != null && xpp.getName().equalsIgnoreCase("point")) {
                            incident.setCoordinates(text);
                        }

                    }

                    eventType = xpp.next();

                }

            } catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }

            Log.e("SUCCESS", "Incident Data Received");
            Log.e("DATA", incidentLinkedList.toString());

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }

    }


}
