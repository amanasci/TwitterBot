package developers.amanasci.twitterbot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText nameE=findViewById(R.id.name);
        final EditText linkE=findViewById(R.id.link);
        final EditText tagsE=findViewById(R.id.tags);
        final TextView nname=findViewById(R.id.novel_name);
        final TextView tweetnum=findViewById(R.id.tweetnum);
        final TextView nlink =findViewById(R.id.novel_link);
        final TextView ntags=findViewById(R.id.novel_tags);
        Button save=findViewById(R.id.save);




        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);// 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        if(pref.getInt("Tweetno",0)!=1){
            editor.putInt("Tweetno", Integer.parseInt("1"));
           editor.apply();
        }


        nname.setText(pref.getString("Novel_Name","Throne of World"));
        nlink.setText(pref.getString("Novel_Link","http://bit.ly/2PhCBuU"));
        ntags.setText(pref.getString("Tags","#WritingCommunity #writers #Read #fantasy #fiction\n" +
                "#reading #readingcommunity "));
        tweetnum.setText(String.valueOf(pref.getInt("Tweetno",0)));




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable name= nameE.getText();
                Editable link=linkE.getText();
                Editable tags=tagsE.getText();

                if(String.valueOf(name).equals("") || String.valueOf(link).equals("") || String.valueOf(tags).equals("")){
                    Toast.makeText(MainActivity.this, "one of the field is empty ",Toast.LENGTH_LONG).show();
                }
                else{

                    //updating data in shared preferences
                    editor.putString("Novel_Name", String.valueOf(name));
                    editor.putString("Novel_Link", String.valueOf(link));
                    editor.putString("Tags", String.valueOf(tags));
                    editor.commit();


                    // updating views
                    nname.setText(pref.getString("Novel_Name","random data"));
                    nlink.setText(pref.getString("Novel_Link","random data"));
                    ntags.setText(pref.getString("Tags","random data"));
                    tweetnum.setText(String.valueOf(pref.getInt("Tweetno",0)));
                }


            }
        });





    }



}
