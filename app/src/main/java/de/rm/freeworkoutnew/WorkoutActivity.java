package de.rm.freeworkoutnew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.ArrayList;

import tools.BaseGameActivity;


public class WorkoutActivity extends  BaseGameActivity {
    private de.rm.freeworkoutnew.workoutPack workoutPack;
    private de.rm.freeworkoutnew.specialPack specialPack;
    private de.rm.freeworkoutnew.exercisePack exercisePack;
    private String TextName;
    private String TextType = "";
    private int type;
    private int quantity;
    private String[] Types = {"Endurance","Standard","Strength"};
    private int counter_rounds=0;
    private ArrayList<ArrayList> roundsList = new ArrayList<ArrayList>();
    private ArrayList[] roundList = new ArrayList[18];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);
        try {
            InputStream source = getAssets().open("workouts.xml");
            Serializer serializer = new Persister();
            workoutPack = serializer.read(de.rm.freeworkoutnew.workoutPack.class, source);
            //Toast.makeText(this, "Wow! Klappt!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //Toast.makeText(this, "Oh oh! workoutPack", Toast.LENGTH_LONG).show();
            Log.e(getClass().getSimpleName(), "loading levels threw exception", e);
        }
        try {
            InputStream source = getAssets().open("exercises.xml");
            Serializer serializer = new Persister();
            exercisePack = serializer.read(de.rm.freeworkoutnew.exercisePack.class, source);
            //Toast.makeText(this, "Wow! Klappt!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Oh oh! exercisePack", Toast.LENGTH_LONG).show();
            Log.e(getClass().getSimpleName(), "loading levels threw exception", e);
        }
        try {
            InputStream source = getAssets().open("specials.xml");
            Serializer serializer = new Persister();
            specialPack = serializer.read(de.rm.freeworkoutnew.specialPack.class, source);
            //Toast.makeText(this, "Wow! Klappt!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //Toast.makeText(this, "Oh oh! specialPack", Toast.LENGTH_LONG).show();
            Log.e(getClass().getSimpleName(), "loading levels threw exception", e);
        }
        Intent empfangenerIntent = this.getIntent();
        if (empfangenerIntent != null && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT)) {
            //Workout/Special,Workout/Exercise-Nummer,Type(Strth,Std,End.),Anzahl
            String s = empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT);
            Integer wore = Integer.valueOf(s.substring(0,1)); //wore = Workout oder Exercise
            String s1=s.substring(2,s.length());
            int n = s1.indexOf(",");
            Integer number = Integer.valueOf(s1.substring(0,n));
            n = s1.lastIndexOf(",");
            type = Integer.valueOf(s1.substring(n-1,n));
            quantity = Integer.valueOf(s1.substring(n+1,s1.length()));


            for (int i = 0; i < 18; i++){
                roundList[i] = new ArrayList();
            }

            if(wore==0){
                Workout w = workoutPack.getWorkouts().get(number);
                TextName = w.getName();
                TextType = Types[type];
                switch (type) {
                    case 0: {
                        Endurance wo = workoutPack.getWorkouts().get(number).getEndurance();
                        int counter_practice=0;
                        for(Round r : wo.getRounds()) { // alle Round-Knoten durchlaufen
                            counter_practice=0;
                            counter_rounds++;

                            for(Practice p : r.getPractice()){  // alle Practice-Knoten durchlaufen
                                String xmeter = " x ";
                                if(p.getName().equals("Sprint")) xmeter=" m ";
                                roundList[counter_rounds].add(p.getQuantity()+xmeter+" "+p.getName());
                                counter_practice++;
                            }
                        }
                        //Toast.makeText(this, "specialPack Anzahl: "+String.valueOf(counter_rounds)+" | "+String.valueOf(counter_practice)+" | "+String.valueOf(wo), Toast.LENGTH_LONG).show();
                        break;
                    }
                    case 1: {
                        Standard wo = workoutPack.getWorkouts().get(number).getStandard();
                        int counter_practice=0;
                        for(Round r : wo.getRounds()) { // alle Round-Knoten durchlaufen
                            counter_practice=0;
                            counter_rounds++;

                            for(Practice p : r.getPractice()){  // alle Practice-Knoten durchlaufen
                                String xmeter = " x ";
                                if(p.getName().equals("Sprint")) xmeter=" m ";
                                roundList[counter_rounds].add(p.getQuantity()+xmeter+" "+p.getName());
                                counter_practice++;
                            }
                        }
                        //Toast.makeText(this, "specialPack Anzahl: "+String.valueOf(counter_rounds)+"|"+String.valueOf(counter_practice)+" | "+String.valueOf(wo), Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
                        Strength wo = workoutPack.getWorkouts().get(number).getStrength();
                        int counter_practice=0;
                        for(Round r : wo.getRounds()) { // alle Round-Knoten durchlaufen
                            counter_practice=0;

                            for(Practice p : r.getPractice()){  // alle Practice-Knoten durchlaufen
                                String xmeter = " x ";
                                String xhalf = "";
                                int q = p.getQuantity();
                                if(p.getName().equals("Sprint")) xmeter=" m ";
                                if(p.getName().equals("Run")) xmeter=" m ";
                                // quantity wird bei Laufstrecken unter 100 m halbiert für neue Darstellung von 40 m auf 2x 20 m
                                if(xmeter.equals(" m ") && q<100) {
                                    q=q/2;
                                    xhalf="2x ";
                                }
                                roundList[counter_rounds].add(xhalf+q+xmeter+" "+p.getName());
                                counter_practice++;
                                Toast.makeText(this, "specialPack Anzahl: "+String.valueOf(counter_rounds)+"|"+String.valueOf(counter_practice), Toast.LENGTH_LONG).show();
                            }
                            counter_rounds++;
                        }
                        //Toast.makeText(this, "specialPack Anzahl: "+String.valueOf(counter_rounds)+"|"+String.valueOf(counter_practice)+" | "+String.valueOf(wo), Toast.LENGTH_LONG).show();
                        break;
                    }

                }

            }
            else{
                Exercise w = exercisePack.getExercises().get(number);
                TextName = w.getName();
            }
            /*for (int i = 0; i < 18; i++){
                roundsList.add(roundList[i]);
            }
          //  ((TextView) findViewById(R.id.text_fragment)).setText(quantity+"x "+TextName+" "+TextType+" | "+s);
            int test=0;
            roundList[test].add("Test1");
            roundList[test].add("Test2");
            roundList[test].add("Test3");*/

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roundList[0]);
            ListView listView1 = (ListView) findViewById(R.id.list_round1);
            listView1.setAdapter(adapter1);
            ListUtils.setDynamicHeight(listView1);
            showView(R.id.text_workout1);
            showView(R.id.list_round1);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roundList[1]);
            showView(R.id.text_workout2);
            showView(R.id.list_round2);


            //Exercise w = exercisePack.getExercises().get(number); // XML für Exercise laden, number = Exercisenummer
            //Toast.makeText(this, "specialPack Anzahl: "+String.valueOf(), Toast.LENGTH_LONG).show();



            /*for (int i = 0; i < 18; i++){
                roundsList.add(roundList[i]);
            }*/
        }
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
