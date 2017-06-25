package ml.ikomangsena.catatansibro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteAdapter mNoteAdapter;
    private boolean apaSound;
    private int apaAnimation;
    private SharedPreferences sharedPreferences;
    private int manaEdit;
    Animation kedip, fadeIn;
    SoundPool soundPool;
    int sHapus = -1;
    int sPencet = -1;
    int sPenting = -1;
    int sUbah = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            soundPool = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        try{
            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("delete.ogg");
            sHapus = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("select.ogg");
            sPencet = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("important.ogg");
            sPenting = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("edit.ogg");
            sUbah = soundPool.load(descriptor, 0);
        } catch (IOException e){
            Toast.makeText(this, "Error euy ngapa yak", Toast.LENGTH_SHORT).show();
        }

        //Note adapter
        mNoteAdapter = new NoteAdapter();
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mNoteAdapter);
        listView.setEmptyView(findViewById(android.R.id.empty));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int whichItem, long id) {
                Catatan sementaraCT = (Catatan) mNoteAdapter.getItem(whichItem);
                DialogShowNote dialog = new DialogShowNote();
                dialog.sendNoteSelected(sementaraCT);
                dialog.show(getFragmentManager(), "");
                if (apaSound){soundPool.play(sPencet, 1, 1, 0, 0, 1);}
            }
        });

        //I can't believe I figured these out on my own! lol I'm such a genius!
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int whichItem, long id) {
                Catatan sementaraCT = (Catatan) mNoteAdapter.getItem(whichItem);
                manaEdit = whichItem;
                DialogEditNote dialog = new DialogEditNote();
                dialog.sendNoteSelected(sementaraCT);
                dialog.show(getFragmentManager(), "");
                if (apaSound){soundPool.play(sUbah, 1, 1, 0, 0, 1);}
                return true;
            }
        });
    }

    public void floatAdd(View v){
        DialogNewNote dialogNewNote = new DialogNewNote();
        dialogNewNote.show(getFragmentManager(),"");
        if(apaSound){soundPool.play(sPencet, 1, 1, 0, 0, 1);}
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNoteAdapter.simpanNote();
    }


    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = getSharedPreferences("Catatan Si Bro", MODE_PRIVATE);
        apaSound = sharedPreferences.getBoolean("sound", true);
        apaAnimation = sharedPreferences.getInt("anim option", SettingsActivity.SLOW);
        kedip = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.kedip);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        if (apaAnimation == SettingsActivity.FAST){
            kedip.setDuration(100);
            fadeIn.setDuration(500);
        } else if (apaAnimation == SettingsActivity.SLOW){
            kedip.setDuration(1000);
            fadeIn.setDuration(1000);
        }
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogNewNote dialogNewNote = new DialogNewNote();
        Intent pindahSetting = new Intent(this,SettingsActivity.class);
        Intent pindahAbout = new Intent(this,Tentang.class);
        switch(item.getItemId()){
            case R.id.action_add: dialogNewNote.show(getFragmentManager(),""); if(apaSound){soundPool.play(sPencet, 1, 1, 0, 0, 1);} return true;
            case R.id.menuSetting: startActivity(pindahSetting); if(apaSound){soundPool.play(sPencet, 1, 1, 0, 0, 1);} return true;
            case R.id.menuAbout: startActivity(pindahAbout); if(apaSound){soundPool.play(sPencet, 1, 1, 0, 0, 1);} return true;
            default: return true;
        }
    }

    public void buatBaruCatatan(Catatan baru){
        mNoteAdapter.tambahNote(baru);
    }

    public void ubahCatatan (Catatan note){
        mNoteAdapter.ubahNote(manaEdit, note);
    }

    public void hapusCatatan (){
        mNoteAdapter.hapusNote(manaEdit);
    }

    public class NoteAdapter extends BaseAdapter{
        List<Catatan> noteList = new ArrayList<Catatan>();
        Catatan tempNote = new Catatan();
        LayoutInflater inflater;
        private JSONSerializer mSerializer;

        public NoteAdapter() {
            mSerializer = new JSONSerializer("CatatanBro.json", MainActivity.this.getApplicationContext());
            try{
                noteList = mSerializer.load();
            }catch (Exception e){
                noteList = new ArrayList<Catatan>();
                e.printStackTrace();
                Log.e("Error gansss!", "", e);
            }
        }

        @Override
        public int getCount() {
            return noteList.size();
        }

        @Override
        public Object getItem(int whichItem) {
            return noteList.get(whichItem);
        }

        @Override
        public long getItemId(int whichItem) {
            return whichItem;
        }

        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup){
            if(view==null) {
                inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listitem, viewGroup, false);
            }
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtDesc = (TextView) view.findViewById(R.id.txtDesc);
            ImageView imgTodo = (ImageView) view.findViewById(R.id.imageViewTodo);
            ImageView imgPenting = (ImageView) view.findViewById(R.id.imageViewImportant);
            ImageView imgIde = (ImageView) view.findViewById(R.id.imageViewIdea);
            tempNote = noteList.get(whichItem);
            CharSequence judul = tempNote.getJudul();
            CharSequence desc = tempNote.getDeskripsi();
            txtDesc.setText(desc);
            txtTitle.setText(judul);
            if (!tempNote.isIniPenting()) {imgPenting.setVisibility(View.GONE);}
            if (!tempNote.isIniTodo()) {imgTodo.setVisibility(View.GONE);}
            if (!tempNote.isIniIde()) {imgIde.setVisibility(View.GONE);}

            if (tempNote.isIniPenting()) {
                imgPenting.setVisibility(View.VISIBLE);
                view.setAnimation(kedip);
            } else {
                view.setAnimation(fadeIn);
            }
            if (tempNote.isIniTodo()) {imgTodo.setVisibility(View.VISIBLE);}
            if (tempNote.isIniIde()) {imgIde.setVisibility(View.VISIBLE);}
            if (apaAnimation == SettingsActivity.NONE){view.clearAnimation();}
            return view;
        }

        public void tambahNote(Catatan baru){
            if (baru.isIniPenting()) { //penting
                noteList.add(0, baru);
                if (apaSound) {soundPool.play(sPenting, 1, 1, 0, 0, 1);}
            } else { //biasa
                noteList.add(baru);
                if (apaSound) {soundPool.play(sPencet, 1, 1, 0, 0, 1);}
            }
            notifyDataSetChanged();
        }

        public void simpanNote(){
            try{
                mSerializer.save(noteList);
            } catch (Exception e){
                e.printStackTrace();
                Log.e("Error gansss!", "", e);
            }
        }

        //watchout these are dope codes!
        public void hapusNote(int whichItem){
            noteList.remove(whichItem);
            notifyDataSetChanged();
            if (apaSound){soundPool.play(sHapus, 1, 1, 0, 0, 1);}
        }

        public void ubahNote(int whichItem, Catatan baru){
            //simpan catatan
            if (baru.isIniPenting() && (noteList.indexOf(baru) != 0)) {
                noteList.add(0, baru);
                noteList.remove(whichItem + 1);
            } else {
                noteList.set(whichItem, baru);
            }
            //suara
            if (baru.isIniPenting()) {
                if (apaSound) {soundPool.play(sPenting, 1, 1, 0, 0, 1);}
            } else {
                if (apaSound) {soundPool.play(sPencet, 1, 1, 0, 0, 1);}
            }
            notifyDataSetChanged();
        }


    }
}
