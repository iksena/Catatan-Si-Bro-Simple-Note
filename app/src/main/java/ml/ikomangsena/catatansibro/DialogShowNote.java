package ml.ikomangsena.catatansibro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sena on 3/22/2017.
 */

public class DialogShowNote extends DialogFragment {
    private Catatan baruCT;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_buka_catatan,null);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtDesc = (TextView) view.findViewById(R.id.txtDescription);
        txtTitle.setText(baruCT.getJudul());
        txtDesc.setText(baruCT.getDeskripsi());
        ImageView gambarPenting = (ImageView) view.findViewById(R.id.imageViewImportant);
        ImageView gambarIde = (ImageView) view.findViewById(R.id.imageViewIdea);
        ImageView gambarTodo = (ImageView) view.findViewById(R.id.imageViewTodo);
        Button btnOK = (Button) view.findViewById(R.id.btnOK);

        if (!baruCT.isIniPenting()){
            gambarPenting.setVisibility(View.GONE);
        }
        if (!baruCT.isIniIde()){
            gambarIde.setVisibility(View.GONE);
        }
        if (!baruCT.isIniTodo()){
            gambarTodo.setVisibility(View.GONE);
        }

        builder.setView(view).setMessage("Catatanmu");

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }

    public void sendNoteSelected(Catatan noteSelected){
        baruCT = noteSelected;
    }
}
