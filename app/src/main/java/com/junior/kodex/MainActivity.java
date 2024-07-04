package com.junior.kodex;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
	private static final int REQUEST_PERMISSION_WRITE = 100;
	private EditText editText;

	private void toast(CharSequence chars) {
		Toast.makeText(getApplicationContext(), chars, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editText = (EditText) findViewById(R.id.mainEd);

		// Request permissions
		if (!(ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
			ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
					REQUEST_PERMISSION_WRITE);
		} else {
			createFile();
		}

		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				highlightKeyWords(s);
			}
		});

	}

	public void highlightKeyWords(Editable s) {

		String keyWords = new String(
				"\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|null|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while)\\b");

		Pattern pattern = Pattern.compile(keyWords);

		Matcher matcher = pattern.matcher(s.toString());

		while (matcher.find()) {
			s.setSpan(new ForegroundColorSpan(Color.BLUE), matcher.start(), matcher.end(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_WRITE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				createFile();
			} else {
				toast("Permission denied to write to external storage");
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// createFile() will be called in onCreate after permissions are granted
	}

	private void createFile() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				File file = new File(
						Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Kodex");
				file.mkdir();
				if (new File(file + "/kodex.txt").createNewFile()) {
					toast("File has been created, Path:" + file.getAbsolutePath());
				} else {
					toast("File already exists, Path:" + file.getAbsolutePath());
					try {
						FileWriter f = new FileWriter(file + "/kodex.txt");
						f.write("Hello world!");
						f.close();
					} catch (Exception e) {

					}
				}
			} else {
				toast("External storage is not available or not writable");
			}
		} catch (

		Exception e) {
			toast("Error creating file: " + e.getMessage());
			e.printStackTrace();
		}
	}
}