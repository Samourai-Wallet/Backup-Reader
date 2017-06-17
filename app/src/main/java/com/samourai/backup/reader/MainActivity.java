package com.samourai.backup.reader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.samourai.crypto.AESUtil;
import com.samourai.util.CharSequenceX;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText passphrase = new EditText(MainActivity.this);
        passphrase.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passphrase.setHint(R.string.passphrase);

        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this)
                .setTitle(MainActivity.this.getText(R.string.app_name) + " " + MainActivity.this.getText(R.string.version_name))
                .setView(passphrase)
                .setMessage(R.string.restore_wallet_from_backup)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        final String pw = passphrase.getText().toString();
                        if (pw == null || pw.length() < 1) {
                            Toast.makeText(MainActivity.this, R.string.invalid_passphrase, Toast.LENGTH_SHORT).show();
                        }

                        final EditText edBackup = new EditText(MainActivity.this);
                        edBackup.setSingleLine(false);
                        edBackup.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        edBackup.setLines(10);
                        edBackup.setHint(R.string.encrypted_backup);
                        edBackup.setGravity(Gravity.START);
                        TextWatcher textWatcher = new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                                edBackup.setSelection(0);
                            }
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                ;
                            }
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                ;
                            }
                        };
                        edBackup.addTextChangedListener(textWatcher);

                        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.app_name)
                                .setView(edBackup)
                                .setMessage(R.string.restore_wallet_from_backup)
                                .setCancelable(false)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        String encrypted = edBackup.getText().toString();
                                        if (encrypted == null || encrypted.length() < 1) {
                                            Toast.makeText(MainActivity.this, R.string.decryption_error, Toast.LENGTH_SHORT).show();
                                        }

                                        String decrypted = null;
                                        try {
                                            decrypted = AESUtil.decrypt(encrypted, new CharSequenceX(pw), AESUtil.DefaultPBKDF2Iterations);
                                        } catch (Exception e) {
                                            Toast.makeText(MainActivity.this, R.string.decryption_error, Toast.LENGTH_SHORT).show();
                                        } finally {
                                            if (decrypted == null || decrypted.length() < 1) {
                                                Toast.makeText(MainActivity.this, R.string.decryption_error, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }

                                        final EditText edDecrypted = new EditText(MainActivity.this);
                                        edDecrypted.setSingleLine(false);
                                        edDecrypted.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                        edDecrypted.setLines(10);
                                        edDecrypted.setHint(R.string.encrypted_backup);
                                        edDecrypted.setGravity(Gravity.START);

                                        edDecrypted.setText(decrypted);

                                        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this)
                                                .setTitle(R.string.app_name)
                                                .setView(edDecrypted)
                                                .setMessage(R.string.decrypted_backup)
                                                .setCancelable(false)
                                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        ;
                                                    }
                                                });
                                        if(!isFinishing())    {
                                            dlg.show();
                                        }

                                    }
                                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ;
                                    }
                                });
                        if(!isFinishing())    {
                            dlg.show();
                        }

                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ;
                    }
                });

        if(!isFinishing())    {
            dlg.show();
        }

    }
}
