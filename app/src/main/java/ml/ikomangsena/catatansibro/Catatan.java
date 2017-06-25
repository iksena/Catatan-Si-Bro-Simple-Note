package ml.ikomangsena.catatansibro;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sena on 3/21/2017.
 */

public class Catatan {
    private String judul;
    private String deskripsi;
    private boolean iniIde;
    private boolean iniTodo;
    private boolean iniPenting;

    private static final String JSON_TITLE = "judul";
    private static final String JSON_DESCRIPTION = "deskripsi";
    private static final String JSON_IDEA = "ide";
    private static final String JSON_TODO = "todo";
    private static final String JSON_IMPORTANT = "penting";

    public Catatan(){}

    public Catatan(JSONObject jsonObject) throws JSONException{
        judul = jsonObject.getString(JSON_TITLE);
        deskripsi = jsonObject.getString(JSON_DESCRIPTION);
        iniIde = jsonObject.getBoolean(JSON_IDEA);
        iniTodo = jsonObject.getBoolean(JSON_TODO);
        iniPenting = jsonObject.getBoolean(JSON_IMPORTANT);
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public boolean isIniIde() {
        return iniIde;
    }

    public void setIniIde(boolean iniIde) {
        this.iniIde = iniIde;
    }

    public boolean isIniTodo() {
        return iniTodo;
    }

    public void setIniTodo(boolean iniTodo) {
        this.iniTodo = iniTodo;
    }

    public boolean isIniPenting() {
        return iniPenting;
    }

    public void setIniPenting(boolean iniPenting) {
        this.iniPenting = iniPenting;
    }

    public JSONObject convertToJSON() throws JSONException{
        JSONObject jo = new JSONObject();

        jo.put(JSON_TITLE, judul);
        jo.put(JSON_DESCRIPTION, deskripsi);
        jo.put(JSON_IDEA, iniIde);
        jo.put(JSON_TODO, iniTodo);
        jo.put(JSON_IMPORTANT, iniPenting);

        return jo;
    }
}
