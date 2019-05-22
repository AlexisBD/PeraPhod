package com.example.reproductor;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {

    Button btnAtras, btnPlayPause, btnStop, btnSiguiente;
    MediaPlayer arraymp[] = new MediaPlayer[6];
    MediaPlayer mp;
    SeekBar sb;
    TextView txtViewSound, txtViewArtist, txtDuracion, continua;
    ImageView iv;
    Thread actualizarSeekbar;
    String aux = "";
    int posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAtras = findViewById(R.id.btnAtras);
        btnPlayPause = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        txtViewSound = findViewById(R.id.txtViewSound);
        txtViewArtist = findViewById(R.id.txtViewArtist);
        txtDuracion =  findViewById(R.id.txtTiempoFin);
        continua  = findViewById(R.id.txtTiempoInicio);
        iv = findViewById(R.id.imageAlbum);
        sb = findViewById(R.id.seekDuracion);

        music();

        actualizarSeekbar = new Thread() {
            @Override
            public void run() {
                updateDuracion();
            }
        };

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPause();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

    }


    private void music(){
        arraymp[0] =  MediaPlayer.create(this, R.raw.sound);
        arraymp[1] =  MediaPlayer.create(this, R.raw.sound1);
        arraymp[2] =  MediaPlayer.create(this, R.raw.sound2);
        arraymp[3] =  MediaPlayer.create(this, R.raw.sound3);
        arraymp[4] =  MediaPlayer.create(this, R.raw.sound4);
        arraymp[5] =  MediaPlayer.create(this, R.raw.sound5);
    }

    private void images() {
        if (posicion == 0) {
            iv.setImageResource(R.drawable.fondo);
        } else if (posicion == 1) {
            iv.setImageResource(R.drawable.fondo2);
        }  else if (posicion == 2) {
            iv.setImageResource(R.drawable.fondo3);
        } else if (posicion == 3) {
            iv.setImageResource(R.drawable.fondo4);
        }  else if (posicion == 4) {
            iv.setImageResource(R.drawable.fondo5);
        } else if (posicion == 5) {
            iv.setImageResource(R.drawable.fondo6);
        }
    }


    private void nombres() {
        if (posicion == 0) {
            txtViewSound.setText("In-da-club");
            txtViewArtist.setText("50-cent");
        } else if (posicion == 1) {
            txtViewSound.setText("girls-like-you");
            txtViewArtist.setText("maroon-5-ft-cardi-b");
        } else if (posicion == 2) {
            txtViewSound.setText("Eextasis");
            txtViewArtist.setText("cartel-de-santa-wcorona-y-millonario");
        } else if (posicion == 3) {
            txtViewSound.setText("i-think-i-like-it");
            txtViewArtist.setText("fake-blood");
        } else if (posicion == 4) {
            txtViewSound.setText("All-night");
            txtViewArtist.setText("R5");
        } else if (posicion == 5) {
            txtViewSound.setText("See-You-Again");
            txtViewArtist.setText("Wiz khalifa-ft-Chalie Puth");
        }

    }

    private void updateDuracion() {
        int duracion = arraymp[posicion].getDuration();
        sb.setMax(duracion);

        int posicionActual = 0;
        int ejecucion = 0;

        boolean ban = false;
        while (posicionActual < duracion) {
            try {
                sleep(500);
                posicionActual = arraymp[posicion].getCurrentPosition();
                sb.setProgress(posicionActual);
                ejecucion = sb.getProgress();
                aux = getTiempo(ejecucion);
                continua.setText(aux.toString().trim());

            } catch (Exception e) {
                continua.setText(aux);
            }
        }
    }

    private String getTiempo(int milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) (milliseconds / (1000*60) % 60);
        int hours = (int) (milliseconds / (1000*60*60) % 24);

        String aux = "";
        aux = ((hours<10)?"0"+hours:hours) + ":" + ((minutes<10)?"0"+minutes:minutes)+":"+((seconds<10)?"0"+seconds:seconds);
        return aux;
    }

    private  void previous() {
        if (posicion >= 1) {
            if (arraymp[posicion].isPlaying()) {
                arraymp[posicion].stop();
                music();
                posicion--;
                images();
                nombres();
                actualizarSeekbar.start();
                arraymp[posicion].start();
                sb.setMax(0);
                txtDuracion.setText(getTiempo(arraymp[posicion].getDuration()));
                sb.setMax(arraymp[posicion].getDuration());
            } else {
                posicion--;
                images();
                nombres();
            }

        } else {
            Toast.makeText(this, "No hay mas canciones", Toast.LENGTH_SHORT).show();
        }
    }

    private void playPause() {
        if (arraymp[posicion].isPlaying()) {
            arraymp[posicion].pause();
            nombres();
            btnPlayPause.setBackgroundResource(R.drawable.play);
            Toast.makeText(this, "Pausa", Toast.LENGTH_SHORT).show();
        } else {
            arraymp[posicion].start();
            nombres();

            actualizarSeekbar.start();
            sb.setMax(0);
            txtDuracion.setText(getTiempo(arraymp[posicion].getDuration()));
            sb.setMax(arraymp[posicion].getDuration());

            btnPlayPause.setBackgroundResource(R.drawable.pausa);


            Toast.makeText(this, "Play", Toast.LENGTH_SHORT).show();

        }
    }

    private void stop() {
        if (arraymp[posicion] != null) {
            arraymp[posicion].stop();
            music();

            posicion = 0;
            btnPlayPause.setBackgroundResource(R.drawable.play);
            iv.setImageResource(R.drawable.fondo);
            Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay cancion reproduciendo", Toast.LENGTH_SHORT).show();
        }

    }

    private  void next() {
        if (posicion < arraymp.length-1) {
            if (arraymp[posicion].isPlaying()) {
                arraymp[posicion].stop();
                posicion++;
                arraymp[posicion].start();
                images();
                nombres();

                sb.setMax(0);
                txtDuracion.setText(getTiempo(arraymp[posicion].getDuration()));
                sb.setMax(arraymp[posicion].getDuration());

            } else {
                posicion++;
                images();
                nombres();

                sb.setMax(0);
                txtDuracion.setText(getTiempo(arraymp[posicion].getDuration()));
                try {
                    sb.setMax(arraymp[posicion].getDuration());
                } catch (Exception e){
                    Log.d("next", ""+e);
                }
            }

        } else {
            Toast.makeText(this, "No hay mas canciones", Toast.LENGTH_SHORT).show();
        }
    }

}

