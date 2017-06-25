package ml.ikomangsena.catatansibro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sena on 3/21/2017.
 */

public class DialogNewNote extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_catatan_baru,null);

        final EditText editTitle = (EditText) dialogView.findViewById(R.id.editTitle);
        final EditText editDesc = (EditText) dialogView.findViewById(R.id.editDescription);
        final CheckBox cbIde = (CheckBox) dialogView.findViewById(R.id.cbIdea);
        final CheckBox cbTodo = (CheckBox) dialogView.findViewById(R.id.cbTodo);
        final CheckBox cbPenting = (CheckBox) dialogView.findViewById(R.id.cbImportant);
        Button btnOK = (Button) dialogView.findViewById(R.id.btnOK);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        builder.setView(dialogView).setMessage("Buat catatan baru");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Catatan baru = new Catatan();
                baru.setJudul(editTitle.getText().toString());
                baru.setDeskripsi(editDesc.getText().toString());
                baru.setIniIde(cbIde.isChecked());
                baru.setIniTodo(cbTodo.isChecked());
                baru.setIniPenting(cbPenting.isChecked());

                MainActivity panggilaktivitas = (MainActivity) getActivity();
                if (!baru.getJudul().isEmpty()) {
                    panggilaktivitas.buatBaruCatatan(baru);
                    dismiss();
                } else {
                    Toast.makeText(panggilaktivitas, "Error: Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }
}
