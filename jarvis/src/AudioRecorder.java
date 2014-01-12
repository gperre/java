

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javaFlacEncoder.FLACFileWriter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.homer.modules.SoundJLayer;
import com.homer.modules.SpeechInput;


public class AudioRecorder implements Runnable {

    private TargetDataLine line;
    private File file;
    private ByteArrayOutputStream outputStream;
    private AudioFormat audioFormat;
    private static final AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;

    public void init() {

       /* audioFormat = new AudioFormat(44100, 16, 2, true, false);
        AudioFormat format = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameBytes, frameRate, bigEndian);
       */ // En plus du format du flux audio d'entrée il est nécessaire de
        // spécifier le type de DataLine qu'on veut
        // ici le DataLine qu'on souhaite est un SourceDataLine qui permet
        // la
        // lecture (targetDataLine permet l'enregistrement).

        Encoding encoding = Encoding.PCM_SIGNED;
        float frameRate = 16000;
        int channels = 1;
        int sampleSizeInBits = 16;
        boolean bigEndian = false;
        int sampleBytes = sampleSizeInBits / 8;
        int frameBytes = sampleBytes * channels;
        float sampleRate = frameRate;
        audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameBytes, frameRate, bigEndian);
        
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                audioFormat);

        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Audio Format specified is not supported");
            return;
        }

        // On récupère le DataLine adéquat
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);

        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        }

    }

    public void run() {

        // on ouvre le Dataline
        try {
            line.open(audioFormat);
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
            return;
        }

        // pour que le flux soit effectivement redirigé sur la carte son il
        // faut
        // demarrer la ligne
        line.start();
        
        AudioInputStream audioInputStream = new AudioInputStream(line);

        try {
        	System.out.println("eh!");
            AudioSystem.write(audioInputStream, FLACFileWriter.FLAC, file);
            System.out.println("oh!");
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            line.close();
            try {
                audioInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        line.stop();
        line.close();

    }
    public void stop(){
    	 line.stop();
         line.close();
    	
    }
    public void setFile(File file) {
        this.file = file;
    }

    public TargetDataLine getLine() {
        return line;
    }

    
    public static void main(String args[]) throws Exception {
    	 Thread audioRecorderThread;	
    	 AudioRecorder recorder = new AudioRecorder();
 		recorder.setFile(new File("d:/toto.flac"));
        recorder.init();
		audioRecorderThread = new Thread(recorder);
		audioRecorderThread.start();
		Thread.sleep(5000);
		audioRecorderThread.stop();
		
		SpeechInput s = new SpeechInput();
		String reponse = s.recognize("d:/toto.flac","fr-FR");
		System.out.println(reponse);
		
		
		try{
            String word=reponse;
            word=java.net.URLEncoder.encode(word, "ISO-8859-1");
            System.out.println("encoding :" + "http://translate.google.com/translate_tts?tl=fr&q="+word);
            URL url = new URL("http://translate.google.com/translate_tts?tl=fr&q="+word);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/4.76");
            urlConn.addRequestProperty("Content-Type", "charset: ISO-8859-1");
            InputStream audioSrc = urlConn.getInputStream();
            DataInputStream read = new DataInputStream(audioSrc);
            OutputStream outstream = new FileOutputStream(new File("d:/reponse.mp3"));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = read.read(buffer)) > 0) {
                    outstream.write(buffer, 0, len);                    
            }
            outstream.close();              
}catch(Exception e){
           System.out.println(e.getMessage());
}
		
	/*	 Thread audioRecorderThread;	
    	 AudioPlayer p = new AudioPlayer();
 		p.setFile(new File("d:/reponse.mp3"));
        p.init();
		audioRecorderThread = new Thread(p);
		audioRecorderThread.start();*/
		
		 SoundJLayer soundToPlay = new SoundJLayer(new File("d:/reponse.mp3").getAbsolutePath());
	        soundToPlay.play();
		
		
		
    }
}
