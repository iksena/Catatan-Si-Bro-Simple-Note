package ml.ikomangsena.catatansibro;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sena on 4/18/2017.
 */

public class JSONSerializer {
    private String namaFile;
    private Context konteks;

    public JSONSerializer(String nf, Context kon) {
        namaFile = nf;
        konteks = kon;
    }

    public void save(List<Catatan> catatan) throws JSONException, IOException{
        JSONArray jsonArray = new JSONArray();

        for (Catatan c : catatan){
            jsonArray.put(c.convertToJSON());
        }

        Writer penulis = null;
        try {
            OutputStream out = konteks.openFileOutput(namaFile, konteks.MODE_PRIVATE);
            penulis = new OutputStreamWriter(out);
            penulis.write(jsonArray.toString());
        } finally {
            if(penulis != null){
                penulis.close();
            }
        }
    }

    public ArrayList<Catatan> load() throws IOException, JSONException{
        ArrayList<Catatan> daftarCatatan = new ArrayList<Catatan>();
        BufferedReader pembaca = null;

        try{
            InputStream in = konteks.openFileInput(namaFile);
            pembaca = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String baris = null;

            while ((baris = pembaca.readLine()) != null){
                jsonString.append(baris);
            }

            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for(int i=0;i<jsonArray.length();i++){
                daftarCatatan.add(new Catatan(jsonArray.getJSONObject(i)));
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if(pembaca!=null){
                pembaca.close();
            }
        }

        return daftarCatatan;
    }
}
