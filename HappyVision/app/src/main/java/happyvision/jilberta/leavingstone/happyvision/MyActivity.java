package happyvision.jilberta.leavingstone.happyvision;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;

import adapter.TestAdapter;
import item.SongItem;
import views.CircleViewTest;
import views.CircularViewModifier;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


//        ((Button)findViewById(R.id.captureVideo)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent cameraActivity = new Intent(MyActivity.this, CameraActivity.class);
//                startActivity(cameraActivity);
//            }
//        });

        ViewGroup layout = (ViewGroup) findViewById(R.id.linearlayout);
        CircleViewTest list = new CircleViewTest(this, null);
        list.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
        TestAdapter adapter = null;
        try {
            adapter = parseXML();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list.setAdapter(adapter);
        list.setViewModifier(new CircularViewModifier());
        layout.addView(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent cameraActivity = new Intent(MyActivity.this, CameraActivity.class);
                cameraActivity.putExtra("Song", (SongItem)parent.getAdapter().getItem(position));
                startActivity(cameraActivity);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private TestAdapter parseXML() throws XmlPullParserException, IOException {
        TestAdapter adapter = new TestAdapter();
        XmlResourceParser xpp = getResources().getXml(R.xml.songs);
        int event = xpp.getEventType();

        String title = "";
        String artist = "";
        String imageName = "";
        String musicName = "";
        int imageId = 0;
        int musicId = 0;

        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.END_TAG && xpp.getName().equals("song")) {
                imageId = getResources().getIdentifier(imageName, "drawable",
                        getApplicationContext().getPackageName());
                musicId = getResources().getIdentifier(musicName, "drawable",
                        getApplicationContext().getPackageName());
                SongItem newSong = new SongItem(title, artist, imageId, musicId);
                adapter.list.add(newSong);
            } else if (event == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("title")) {
                    event = xpp.next();
                    if (event == XmlPullParser.TEXT) {
                        title = xpp.getText();
                    }
                } else if (xpp.getName().equals("artist")) {
                    event = xpp.next();
                    if (event == XmlPullParser.TEXT) {
                        artist = xpp.getText();
                    }
                } else if (xpp.getName().equals("image")) {
                    event = xpp.next();
                    if (event == XmlPullParser.TEXT) {
                        imageName = xpp.getText();
                    }
                } else if (xpp.getName().equals("music")) {
                    event = xpp.next();
                    if (event == XmlPullParser.TEXT) {
                        musicName = xpp.getText();
                    }
                }
            }
            event = xpp.next();
        }
        return adapter;
    }
}
