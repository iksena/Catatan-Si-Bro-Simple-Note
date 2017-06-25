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
 * Created by Sena on 4/19/2017.
 */

public class DialogEditNote extends DialogFragment {
    private Catatan baru = new Catatan();

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
        btnCancel.setText("Hapus");
        if(baru != null) {
            editTitle.setText(baru.getJudul());
            editDesc.setText(baru.getDeskripsi());
            cbIde.setChecked(baru.isIniIde());
            cbPenting.setChecked(baru.isIniPenting());
            cbTodo.setChecked(baru.isIniTodo());
        }

        builder.setView(dialogView).setMessage("Ubah catatanmu");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogKonfirmasi dialog = new DialogKonfirmasi();
                dialog.show(getFragmentManager(),"");
                dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baru.setJudul(editTitle.getText().toString());
                baru.setDeskripsi(editDesc.getText().toString());
                baru.setIniIde(cbIde.isChecked());
                baru.setIniTodo(cbTodo.isChecked());
                baru.setIniPenting(cbPenting.isChecked());

                MainActivity panggilaktivitas = (MainActivity) getActivity();
                if (!baru.getJudul().isEmpty()) {
                    panggilaktivitas.ubahCatatan(baru);
                    dismiss();
                } else {
                    Toast.makeText(panggilaktivitas, "Error: Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }

    public void sendNoteSelected(Catatan noteSelected){
        baru = noteSelected;
    }
}
